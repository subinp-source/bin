/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.enums.SnDocumentOperationType;
import de.hybris.platform.searchservices.enums.SnIndexerOperationType;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation;
import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest;
import de.hybris.platform.searchservices.indexer.service.SnIndexerResponse;
import de.hybris.platform.searchservices.indexer.service.SnIndexerService;
import de.hybris.platform.searchservices.indexer.service.SnIndexerStrategy;
import de.hybris.platform.searchservices.indexer.service.SnIndexerStrategyFactory;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link SnIndexerService}.
 */
public class DefaultSnIndexerService implements SnIndexerService
{
	private static final String INDEX_TYPE_ID = "indexTypeId";
	private SnIndexerStrategyFactory snIndexerStrategyFactory;

	@Override
	public SnIndexerRequest createFullIndexerRequest(final String indexTypeId, final SnIndexerItemSource indexerItemSource)
			throws SnIndexerException
	{
		ServicesUtil.validateParameterNotNullStandardMessage(INDEX_TYPE_ID, indexTypeId);
		ServicesUtil.validateParameterNotNullStandardMessage("indexerItemSource", indexerItemSource);

		final List<SnIndexerItemSourceOperation> indexerItemSourceOperations = List
				.of(new DefaultSnIndexerItemSourceOperation(SnDocumentOperationType.CREATE, indexerItemSource));

		return new DefaultSnIndexerRequest(indexTypeId, SnIndexerOperationType.FULL, indexerItemSourceOperations);
	}

	@Override
	public SnIndexerRequest createIncrementalIndexerRequest(final String indexTypeId,
			final List<SnIndexerItemSourceOperation> indexerItemSourceOperations) throws SnIndexerException
	{
		ServicesUtil.validateParameterNotNullStandardMessage(INDEX_TYPE_ID, indexTypeId);
		ServicesUtil.validateParameterNotNullStandardMessage("indexerItemSourceOperations", indexerItemSourceOperations);

		return new DefaultSnIndexerRequest(indexTypeId, SnIndexerOperationType.INCREMENTAL, indexerItemSourceOperations);
	}

	@Override
	public SnIndexerResponse index(final SnIndexerRequest indexerRequest) throws SnIndexerException
	{
		ServicesUtil.validateParameterNotNullStandardMessage(INDEX_TYPE_ID, indexerRequest);

		final SnIndexerStrategy indexerStrategy = snIndexerStrategyFactory.getIndexerStrategy(indexerRequest);
		return indexerStrategy.execute(indexerRequest);
	}

	public SnIndexerStrategyFactory getSnIndexerStrategyFactory()
	{
		return snIndexerStrategyFactory;
	}

	@Required
	public void setSnIndexerStrategyFactory(final SnIndexerStrategyFactory snIndexerStrategyFactory)
	{
		this.snIndexerStrategyFactory = snIndexerStrategyFactory;
	}
}
