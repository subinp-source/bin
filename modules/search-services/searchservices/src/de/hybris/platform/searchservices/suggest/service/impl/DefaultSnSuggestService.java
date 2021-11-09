/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service.impl;

import de.hybris.platform.searchservices.search.service.SnSearchStrategyFactory;
import de.hybris.platform.searchservices.suggest.SnSuggestException;
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery;
import de.hybris.platform.searchservices.suggest.service.SnSuggestRequest;
import de.hybris.platform.searchservices.suggest.service.SnSuggestResponse;
import de.hybris.platform.searchservices.suggest.service.SnSuggestService;
import de.hybris.platform.searchservices.suggest.service.SnSuggestStrategy;
import de.hybris.platform.searchservices.suggest.service.SnSuggestStrategyFactory;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link SnSuggestService}.
 */
public class DefaultSnSuggestService implements SnSuggestService
{
	private SnSearchStrategyFactory snSearchStrategyFactory;
	private SnSuggestStrategyFactory snSuggestStrategyFactory;

	@Override
	public SnSuggestRequest createSuggestRequest(final String indexTypeId, final SnSuggestQuery suggestQuery)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("indexTypeId", indexTypeId);
		ServicesUtil.validateParameterNotNullStandardMessage("suggestQuery", suggestQuery);

		return new DefaultSnSuggestRequest(indexTypeId, suggestQuery);
	}

	@Override
	public SnSuggestResponse suggest(final SnSuggestRequest suggestRequest) throws SnSuggestException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("suggestRequest", suggestRequest);

		final SnSuggestStrategy suggestStrategy = snSuggestStrategyFactory.getSuggestStrategy(suggestRequest);
		return suggestStrategy.execute(suggestRequest);
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

	public SnSuggestStrategyFactory getSnSuggestStrategyFactory()
	{
		return snSuggestStrategyFactory;
	}

	@Required
	public void setSnSuggestStrategyFactory(final SnSuggestStrategyFactory snSuggestStrategyFactory)
	{
		this.snSuggestStrategyFactory = snSuggestStrategyFactory;
	}
}
