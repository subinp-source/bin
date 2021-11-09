/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;


/**
 * Implementation of {@link SnIndexerItemSource} that uses flexible search queries.
 */
public class FlexibleSearchSnIndexerItemSource implements SnIndexerItemSource
{
	private static final Logger LOG = LoggerFactory.getLogger(FlexibleSearchSnIndexerItemSource.class);

	private final String query;
	private final Map<String, Object> queryParameters;

	public FlexibleSearchSnIndexerItemSource(final String query, final Map<String, Object> queryParameters)
	{
		this.query = query;
		this.queryParameters = queryParameters;
	}

	@Override
	public List<PK> getPks(final SnIndexerContext indexerContext) throws SnIndexerException
	{
		final StopWatch timer = new StopWatch();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Query: {}", query);
			LOG.debug("Query parameters: {}", queryParameters);

			timer.start();
		}

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
		flexibleSearchQuery.addQueryParameters(queryParameters);
		flexibleSearchQuery.setResultClassList(Arrays.asList(PK.class));
		final SearchResult<PK> flexibleSearchResult = getFlexibleSearchService().search(flexibleSearchQuery);

		if (LOG.isDebugEnabled())
		{
			timer.stop();

			LOG.debug("Query execution time: {}s, result size: {}", timer.getTotalTimeSeconds(),
					flexibleSearchResult.getTotalCount());
		}

		return flexibleSearchResult.getResult();
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return Registry.getApplicationContext().getBean("flexibleSearchService", FlexibleSearchService.class);
	}
}
