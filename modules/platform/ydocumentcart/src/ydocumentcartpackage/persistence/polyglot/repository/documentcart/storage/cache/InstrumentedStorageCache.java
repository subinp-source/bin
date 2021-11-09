/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.cache;

import de.hybris.platform.persistence.polyglot.model.Identity;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Query;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.QueryResult;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Storage;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.util.MetricUtils;

public class InstrumentedStorageCache extends BaseStorageCache
{
	private final BaseStorageCache targetCache;
	private final MetricRegistry metricRegistry;
	private final String repositoryName;
	private Timer cacheHitTimer;
	private Timer cacheMissTimer;
	private Timer flushTimer;
	private Timer noCacheTimer;

	public InstrumentedStorageCache(final BaseStorageCache targetCache, final MetricRegistry metricRegistry,
	                                final String repositoryName)
	{
		this.targetCache = Objects.requireNonNull(targetCache);

		this.metricRegistry = Objects.requireNonNull(metricRegistry);
		this.repositoryName = Objects.requireNonNull(repositoryName);
	}

	@PostConstruct
	public void postConstruct()
	{
		cacheHitTimer = metricRegistry.timer(metricName(repositoryName, "storage.cache.hit"));
		cacheMissTimer = metricRegistry.timer(metricName(repositoryName, "storage.cache.miss"));
		flushTimer = metricRegistry.timer(metricName(repositoryName, "storage.flush"));
		noCacheTimer = metricRegistry.timer(metricName(repositoryName, "storage.nocache"));
	}

	private String metricName(final String repositoryName, final String timerName)
	{
		return MetricUtils.metricName(repositoryName, timerName, "storage");
	}

	@Override
	protected void cacheDocuments(final QueryResult queryResult)
	{
		targetCache.cacheDocuments(queryResult);
	}

	@Override
	protected boolean isCacheActive()
	{
		return targetCache.isCacheActive();
	}

	@Override
	protected Optional<QueryResult> findInCache(final Query query)
	{

		final Timer.Context hitTime = cacheHitTimer.time();
		final Timer.Context missTime = cacheMissTimer.time();

		final Optional<QueryResult> cachedResult = targetCache.findInCache(query);

		if (cachedResult.isPresent())
		{
			hitTime.stop();
		}
		else
		{
			missTime.stop();
		}

		return cachedResult;
	}

	@Override
	protected QueryResult findInStorage(final Query baseQuery, final LoadFromStorageStrategy targetFunction)
	{
		try (final Timer.Context ignored = noCacheTimer.time())
		{
			return targetCache.findInStorage(baseQuery, targetFunction);
		}
	}

	@Override
	public boolean remove(final Document document)
	{
		return targetCache.remove(document);
	}

	@Override
	public boolean save(final Document document)
	{
		return targetCache.save(document);
	}

	@Override
	protected CacheFlushAction getFlushAction()
	{
		return this::flushAction;
	}

	private void flushAction(final Storage storage, final Identity identity, final DocumentCacheEntry entry)
	{
		flushTimer.time(() -> targetCache.getFlushAction().flushCacheEntry(storage, identity, entry));
	}

	@Override
	protected CacheContext createCacheContext(final Storage storage,
	                                          final CacheFlushAction flushAction)
	{
		return targetCache.createCacheContext(storage, flushAction);
	}


}
