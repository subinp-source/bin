/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;


/**
 * Implementation of {@link SnIndexerItemSource} that collects all items of a specific type.
 */
public class TypeSnIndexerItemSource implements SnIndexerItemSource
{
	private static final Logger LOG = LoggerFactory.getLogger(TypeSnIndexerItemSource.class);

	@Override
	public List<PK> getPks(final SnIndexerContext indexerContext) throws SnIndexerException
	{
		final SnIndexType indexType = indexerContext.getIndexType();
		final String query = "SELECT {pk} FROM {" + indexType.getItemComposedType() + "}";

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Query: {}", query);
		}

		final StopWatch timer = new StopWatch();
		timer.start();

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
		flexibleSearchQuery.setResultClassList(Arrays.asList(PK.class));
		final SearchResult<PK> flexibleSearchResult = getFlexibleSearchService().search(flexibleSearchQuery);

		timer.stop();

		if (LOG.isDebugEnabled())
		{
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
