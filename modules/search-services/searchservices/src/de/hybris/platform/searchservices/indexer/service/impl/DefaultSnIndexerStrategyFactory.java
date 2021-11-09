/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest;
import de.hybris.platform.searchservices.indexer.service.SnIndexerStrategy;
import de.hybris.platform.searchservices.indexer.service.SnIndexerStrategyFactory;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link SnIndexerStrategyFactory}.
 */
public class DefaultSnIndexerStrategyFactory implements SnIndexerStrategyFactory
{
	private SnIndexerStrategy defaultIndexerStrategy;

	@Override
	public SnIndexerStrategy getIndexerStrategy(final SnIndexerRequest indexerRequest)
	{
		return defaultIndexerStrategy;
	}

	public SnIndexerStrategy getDefaultIndexerStrategy()
	{
		return defaultIndexerStrategy;
	}

	@Required
	public void setDefaultIndexerStrategy(final SnIndexerStrategy defaultIndexerStrategy)
	{
		this.defaultIndexerStrategy = defaultIndexerStrategy;
	}
}
