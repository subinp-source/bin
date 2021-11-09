/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.enums.SnIndexerOperationType;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchRequest;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation;

import java.util.List;


/**
 * Default implementation for {@link SnIndexerBatchRequest}.
 */
public class DefaultSnIndexerBatchRequest extends DefaultSnIndexerRequest implements SnIndexerBatchRequest
{
	private final String indexId;
	private final String indexerOperationId;
	private final String indexerBatchId;

	public DefaultSnIndexerBatchRequest(final String indexTypeId, final String indexId,
			final SnIndexerOperationType indexerOperationType, final List<SnIndexerItemSourceOperation> indexerItemSourceOperations,
			final String indexerOperationId, final String indexerBatchId)
	{
		super(indexTypeId, indexerOperationType, indexerItemSourceOperations);
		this.indexId = indexId;
		this.indexerOperationId = indexerOperationId;
		this.indexerBatchId = indexerBatchId;
	}

	@Override
	public String getIndexId()
	{
		return indexId;
	}

	@Override
	public String getIndexerOperationId()
	{
		return indexerOperationId;
	}

	@Override
	public String getIndexerBatchId()
	{
		return indexerBatchId;
	}
}
