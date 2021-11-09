/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.dao.impl;

import de.hybris.platform.searchservices.indexer.dao.SnIndexerCronJobDao;
import de.hybris.platform.searchservices.model.FullSnIndexerCronJobModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Default implementation of {@link SnIndexerCronJobDao}.
 */
public class DefaultSnIndexerCronJobDao implements SnIndexerCronJobDao
{

	private static final String MAX_LAST_SUCCESSFUL_START_TIME_FULL = "SELECT MAX({lastSuccessfulStartTime}) " //
			+ "FROM {" + FullSnIndexerCronJobModel._TYPECODE + " as c } " //
			+ "WHERE {indexType}  = ?indexType";

	private FlexibleSearchService flexibleSearchService;

	@Override
	public Optional<Date> getMaxFullLastSuccessfulStartTime(final SnIndexTypeModel indexType)
	{
		final Map<String, Object> queryParams = Map.of("indexType", indexType);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(MAX_LAST_SUCCESSFUL_START_TIME_FULL);
		flexibleSearchQuery.addQueryParameters(queryParams);
		flexibleSearchQuery.setResultClassList(Collections.singletonList(Date.class));

		final SearchResult<Date> result = flexibleSearchService.search(flexibleSearchQuery);

		return result.getResult().stream().filter(Objects::nonNull).findAny();
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
