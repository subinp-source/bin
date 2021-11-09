/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service.impl;

import de.hybris.platform.searchservices.suggest.service.SnSuggestRequest;
import de.hybris.platform.searchservices.suggest.service.SnSuggestStrategy;
import de.hybris.platform.searchservices.suggest.service.SnSuggestStrategyFactory;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link SnSuggestStrategyFactory}.
 */
public class DefaultSnSuggestStrategyFactory implements SnSuggestStrategyFactory
{
	private SnSuggestStrategy defaultSuggestStrategy;

	@Override
	public SnSuggestStrategy getSuggestStrategy(final SnSuggestRequest suggestRequest)
	{
		return defaultSuggestStrategy;
	}

	public SnSuggestStrategy getDefaultSuggestStrategy()
	{
		return defaultSuggestStrategy;
	}

	@Required
	public void setDefaultSuggestStrategy(final SnSuggestStrategy defaultSuggestStrategy)
	{
		this.defaultSuggestStrategy = defaultSuggestStrategy;
	}
}
