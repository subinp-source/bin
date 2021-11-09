/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service.impl;

import de.hybris.platform.searchservices.search.service.SnSearchRequest;
import de.hybris.platform.searchservices.search.service.SnSearchStrategy;
import de.hybris.platform.searchservices.search.service.SnSearchStrategyFactory;


/**
 * Default implementation for {@link SnSearchStrategyFactory}.
 */
public class DefaultSnSearchStrategyFactory implements SnSearchStrategyFactory
{
	private SnSearchStrategy defaultSearchStrategy;

	@Override
	public SnSearchStrategy getSearchStrategy(final SnSearchRequest searchRequest)
	{
		return defaultSearchStrategy;
	}

	public SnSearchStrategy getDefaultSearchStrategy()
	{
		return defaultSearchStrategy;
	}

	public void setDefaultSearchStrategy(final SnSearchStrategy defaultSearchStrategy)
	{
		this.defaultSearchStrategy = defaultSearchStrategy;
	}
}
