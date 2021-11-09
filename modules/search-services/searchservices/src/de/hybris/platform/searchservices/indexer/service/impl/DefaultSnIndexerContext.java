/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.enums.SnIndexerOperationType;
import de.hybris.platform.searchservices.index.service.impl.DefaultSnIndexContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation;
import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest;
import de.hybris.platform.searchservices.indexer.service.SnIndexerResponse;

import java.util.List;


/**
 * Default implementation for {@link SnIndexerContext}.
 */
public class DefaultSnIndexerContext extends DefaultSnIndexContext implements SnIndexerContext
{
	private SnIndexerRequest indexerRequest;
	private SnIndexerResponse indexerResponse;

	private List<SnIndexerItemSourceOperation> indexerItemSourceOperations;
	private String indexerOperationId;

	@Override
	public SnIndexerRequest getIndexerRequest()
	{
		return indexerRequest;
	}

	public void setIndexerRequest(final SnIndexerRequest indexerRequest)
	{
		this.indexerRequest = indexerRequest;
	}

	@Override
	public SnIndexerResponse getIndexerResponse()
	{
		return indexerResponse;
	}

	@Override
	public void setIndexerResponse(final SnIndexerResponse indexerResponse)
	{
		this.indexerResponse = indexerResponse;
	}

	@Override
	public SnIndexerOperationType getIndexerOperationType()
	{
		return indexerRequest.getIndexerOperationType();
	}

	@Override
	public List<SnIndexerItemSourceOperation> getIndexerItemSourceOperations()
	{
		return indexerItemSourceOperations;
	}

	public void setIndexerItemSourceOperations(final List<SnIndexerItemSourceOperation> indexerItemSourceOperations)
	{
		this.indexerItemSourceOperations = indexerItemSourceOperations;
	}

	@Override
	public String getIndexerOperationId()
	{
		return indexerOperationId;
	}

	@Override
	public void setIndexerOperationId(final String indexerOperationId)
	{
		this.indexerOperationId = indexerOperationId;
	}
}
