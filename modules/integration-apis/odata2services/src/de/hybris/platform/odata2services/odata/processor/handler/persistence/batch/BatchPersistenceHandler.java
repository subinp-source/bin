/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence.batch;

import de.hybris.platform.odata2services.config.ODataServicesConfiguration;
import de.hybris.platform.odata2services.odata.processor.BatchLimitExceededException;
import de.hybris.platform.odata2services.odata.processor.handler.ODataProcessorHandler;

import java.util.List;

import org.apache.olingo.odata2.api.batch.BatchException;
import org.apache.olingo.odata2.api.batch.BatchRequestPart;
import org.apache.olingo.odata2.api.batch.BatchResponsePart;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;

/**
 * ODataProcessor handler that persists a batch request
 */
public class BatchPersistenceHandler implements ODataProcessorHandler<BatchParam, ODataResponse>
{
	private ODataServicesConfiguration oDataServicesConfiguration;

	@Override
	public ODataResponse handle(final BatchParam param) throws ODataException
	{
		verifyNumberOfBatchesIsWithinBatchLimit(param);
		final List<BatchResponsePart> responseParts = createResponses(param);
		return writeBatchResponse(responseParts);
	}

	private void verifyNumberOfBatchesIsWithinBatchLimit(final BatchParam param)
	{
		final int batchLimit = getODataServicesConfiguration().getBatchLimit();
		if (param.getBatchRequestPartSize() > batchLimit)
		{
			throw new BatchLimitExceededException(batchLimit);
		}
	}

	private List<BatchResponsePart> createResponses(final BatchParam param) throws ODataException
	{
		final List<BatchResponsePart> responseParts = Lists.newArrayList();
		final List<BatchRequestPart> batchParts = param.getBatchRequestParts();
		for (final BatchRequestPart batchPart : batchParts)
		{
			// we are returning n responses in batch (aka bulk)
			responseParts.add(param.getBatchHandler().handleBatchPart(batchPart));
		}
		return responseParts;
	}

	/**
	 * Writes the list {@link BatchResponsePart} in a {@link ODataResponse}
	 *
	 * @param responseParts Responses
	 * @return an ODataResponse
	 * @throws BatchException is thrown if writing has a failure
	 */
	protected ODataResponse writeBatchResponse(final List<BatchResponsePart> responseParts) throws BatchException
	{
		return EntityProvider.writeBatchResponse(responseParts);
	}

	protected ODataServicesConfiguration getODataServicesConfiguration()
	{
		return oDataServicesConfiguration;
	}

	@Required
	public void setODataServicesConfiguration(final ODataServicesConfiguration oDataServicesConfiguration)
	{
		this.oDataServicesConfiguration = oDataServicesConfiguration;
	}
}
