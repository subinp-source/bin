/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchStrategy;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchStrategyFactory;
import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest;
import de.hybris.platform.searchservices.indexer.service.SnIndexerStrategyFactory;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link SnIndexerStrategyFactory}.
 */
public class DefaultSnIndexerBatchStrategyFactory implements SnIndexerBatchStrategyFactory
{
	private SnIndexerBatchStrategy defaultIndexerBatchStrategy;

	@Override
	public SnIndexerBatchStrategy getIndexerBatchStrategy(final SnIndexerRequest indexerRequest)
	{
		return defaultIndexerBatchStrategy;
	}

	public SnIndexerBatchStrategy getDefaultIndexerBatchStrategy()
	{
		return defaultIndexerBatchStrategy;
	}

	@Required
	public void setDefaultIndexerBatchStrategy(final SnIndexerBatchStrategy defaultIndexerBatchStrategy)
	{
		this.defaultIndexerBatchStrategy = defaultIndexerBatchStrategy;
	}
}
