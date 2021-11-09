/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.cache;

import de.hybris.platform.persistence.polyglot.model.Identity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Query;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.QueryResult;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Storage;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.BaseQuery;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.EntityCondition;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.util.StorageUtils;

public class ThreadLocalDocumentCache extends BaseStorageCache
{
	private static final ThreadLocal<CacheHolder> CACHE_TL = ThreadLocal.withInitial(CacheHolder::new);

	@Override
	protected Optional<QueryResult> findInCache(final Query query)
	{
		final BaseQuery baseQuery = StorageUtils.requireBaseQuery(query);

		final CacheHolder cacheHolder = getCacheHolder();
		final Map<Identity, DocumentCacheEntry> cache = cacheHolder.getCache();

		return findInCache(cache, baseQuery);
	}

	private Optional<QueryResult> findInCache(final Map<Identity, DocumentCacheEntry> cache, final BaseQuery baseQuery)
	{
		if (baseQuery.getRootId().isPresent())
		{
			final DocumentCacheEntry cart = cache.get(baseQuery.getRootId().get());
			if (cart == null)
			{
				return Optional.empty();
			}

			return Optional.of(cart.isRemoved() ? QueryResult.empty() : QueryResult.from(cart.getDocument()));
		}
		if (baseQuery.getEntityId().isPresent())
		{
			final Identity id = baseQuery.getEntityId().get();
			final DocumentCacheEntry cart = cache.get(id);
			if (cart != null)
			{
				return cart.isRemoved() ? Optional.empty() : Optional.of(QueryResult.from(cart.getDocument()));
			}
			return lookupCacheForResult(cache, c -> c.getDocument().containsEntity(id));

		}
		if (baseQuery.getRootCondition().isPresent())
		{
			final EntityCondition condition = (baseQuery.getRootCondition().get());

			return lookupCacheForResult(cache, c -> condition.getPredicate()
			                                                 .test(c.getDocument().getRootEntity()));
		}

		return Optional.empty();
	}

	private Optional<QueryResult> lookupCacheForResult(final Map<Identity, DocumentCacheEntry> cache,
	                                                   final Predicate<DocumentCacheEntry> cacheEntryPredicate)
	{
		final Map<Boolean, List<Document>> collect = cache.values()
		                                                  .stream()
		                                                  .filter(cacheEntryPredicate)
		                                                  .collect(Collectors.groupingBy(DocumentCacheEntry::isRemoved, Collectors
				                                                  .mapping(DocumentCacheEntry::getDocument, Collectors
						                                                  .toList())));

		if (collect.isEmpty())
		{
			return Optional.empty();
		}

		if (CollectionUtils.isEmpty(collect.get(false)))
		{
			return Optional.of(QueryResult.empty());
		}

		return Optional.of(QueryResult.from(collect.get(false)));
	}

	@Override
	protected QueryResult findInStorage(final Query baseQuery, final LoadFromStorageStrategy loadFromStorageStrategy)
	{
		return loadFromStorageStrategy.loadFromStorage(baseQuery);
	}

	@Override
	public boolean save(final Document document)
	{

		final CacheHolder cacheHolder = getCacheHolder();
		if (cacheHolder.isActive())
		{
			final Map<Identity, DocumentCacheEntry> cache = cacheHolder.getCache();
			final DocumentCacheEntry current = cache.get(document.getRootId());
			if (current != null && current.isRemoved())
			{
				throw new IllegalStateException("Already removed.");
			}
			cache.put(document.getRootId(), new DocumentCacheEntry(document, true));
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean remove(final Document document)
	{
		final CacheHolder cacheHolder = getCacheHolder();
		if (cacheHolder.isActive())
		{

			final Map<Identity, DocumentCacheEntry> cache = cacheHolder.getCache();
			final DocumentCacheEntry current = cache.computeIfAbsent(document.getRootId(),
					identity -> new DocumentCacheEntry(document));

			if (current.isRemoved())
			{
				throw new IllegalStateException("Already removed.");
			}

			current.markAsRemoved();
			return true;
		}
		else
		{
			return false;
		}

	}

	@Override
	public void cacheDocuments(final QueryResult queryResult)
	{
		final CacheHolder cacheHolder = getCacheHolder();
		if (cacheHolder.isActive())
		{
			queryResult.stream().forEach(c -> cacheHolder.getCache().put(c.getRootId(), new DocumentCacheEntry(c)));
		}
	}

	@Override
	protected boolean isCacheActive()
	{
		return getCacheHolder().isActive();
	}

	private CacheHolder getCacheHolder()
	{
		return CACHE_TL.get();
	}

	@Override
	protected CacheFlushAction getFlushAction()
	{
		return (storage, identity, entry) -> flushAction(storage, entry);
	}

	private void flushAction(final Storage storage, final DocumentCacheEntry entry)
	{
		if (entry.isRemoved())
		{
			storage.remove(entry.getDocument());
		}
		else
		{
			storage.save(entry.getDocument());
		}
	}

	@Override
	protected ThreadLocalCacheContext createCacheContext(final Storage storage, final CacheFlushAction flushAction)
	{
		return new ThreadLocalCacheContext(CACHE_TL.get(), flushAction, storage);
	}


	public static class ThreadLocalCacheContext implements CacheContext
	{
		private final CacheHolder holder;
		private final CacheFlushAction flushAction;
		private final Storage storage;
		private boolean success = false;

		private ThreadLocalCacheContext(final CacheHolder holder, final CacheFlushAction flushAction, final Storage storage)
		{
			this.holder = Objects.requireNonNull(holder);
			this.flushAction = Objects.requireNonNull(flushAction);
			this.storage = Objects.requireNonNull(storage);

			holder.activateCache();
		}

		@Override
		public void markAsSuccess()
		{
			success = true;
		}

		@Override
		public void close()
		{
			if (holder.deactivate())
			{
				if (success)
				{
					holder.flush(flushAction, storage);
				}

				holder.reset();
			}
		}
	}

	public static class CacheHolder
	{
		private final Map<Identity, DocumentCacheEntry> cache = new HashMap<>();
		private final MutableInt contextCounter = new MutableInt();

		private boolean active = false;

		void activateCache()
		{
			final int i = contextCounter.getAndIncrement();
			if (i <= 0)
			{
				if (active)
				{
					reset();
					throw new IllegalStateException("cache has already been activated");
				}
				active = true;
			}
		}

		boolean deactivate()
		{
			return contextCounter.decrementAndGet() == 0;
		}

		Map<Identity, DocumentCacheEntry> getCache()
		{
			return cache;
		}


		void flush(final CacheFlushAction flushAction, final Storage storage)
		{
			if (!active)
			{
				throw new IllegalStateException("cache is not activated");
			}

			try
			{
				cache.entrySet().stream().filter(e -> e.getValue().isDirty()).forEach(
						(e) -> flushAction.flushCacheEntry(storage, e.getKey(), e.getValue()));
			}
			finally
			{
				reset();
			}
		}

		void reset()
		{
			cache.clear();
			contextCounter.setValue(0);
			active = false;
		}

		boolean isActive()
		{
			return active;
		}
	}
}
