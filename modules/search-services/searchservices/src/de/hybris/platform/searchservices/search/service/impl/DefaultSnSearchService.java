/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service.impl;

import de.hybris.platform.searchservices.search.SnSearchException;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.service.SnSearchRequest;
import de.hybris.platform.searchservices.search.service.SnSearchResponse;
import de.hybris.platform.searchservices.search.service.SnSearchService;
import de.hybris.platform.searchservices.search.service.SnSearchStrategy;
import de.hybris.platform.searchservices.search.service.SnSearchStrategyFactory;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link SnSearchService}.
 */
public class DefaultSnSearchService implements SnSearchService
{
	private SnSearchStrategyFactory snSearchStrategyFactory;

	@Override
	public SnSearchRequest createSearchRequest(final String indexTypeId, final SnSearchQuery searchQuery)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("indexTypeId", indexTypeId);
		ServicesUtil.validateParameterNotNullStandardMessage("searchQuery", searchQuery);

		return new DefaultSnSearchRequest(indexTypeId, searchQuery);
	}

	@Override
	public SnSearchResponse search(final SnSearchRequest searchRequest) throws SnSearchException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("searchRequest", searchRequest);

		final SnSearchStrategy searchStrategy = snSearchStrategyFactory.getSearchStrategy(searchRequest);
		return searchStrategy.execute(searchRequest);
	}

	public SnSearchStrategyFactory getSnSearchStrategyFactory()
	{
		return snSearchStrategyFactory;
	}

	@Required
	public void setSnSearchStrategyFactory(final SnSearchStrategyFactory snSearchStrategyFactory)
	{
		this.snSearchStrategyFactory = snSearchStrategyFactory;
	}
}
