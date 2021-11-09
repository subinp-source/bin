/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence.batch;

import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException;
import de.hybris.platform.odata2services.odata.processor.handler.ODataProcessorHandler;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;

import java.util.Collections;
import java.util.List;

import org.apache.olingo.odata2.api.batch.BatchHandler;
import org.apache.olingo.odata2.api.batch.BatchResponsePart;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataRequest;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;

/**
 * ODataProcessor handler that persists a change set request
 */
public class ChangeSetPersistenceHandler implements ODataProcessorHandler<ChangeSetParam, BatchResponsePart>
{
	private ModelService modelService;

	@Override
	public BatchResponsePart handle(final ChangeSetParam param)
	{
		beginTransaction();
		final List<ODataResponse> responses = handleRequestsInTransaction(param);
		endTransaction(responses);
		return createBatchResponsePart(responses);
	}

	protected Transaction getCurrentTransaction()
	{
		return Transaction.current();
	}

	/**
	 * Handle the requests in a transaction
	 * @param param Parameters from the request
	 * @return List of {@link ODataResponse}s
	 */
	@SuppressWarnings("unchecked")
	protected List<ODataResponse> handleRequestsInTransaction(final ChangeSetParam param)
	{
		try
		{
			return (List<ODataResponse>) getCurrentTransaction().execute(new TransactionBody()
			{
				@Override
				public List<ODataResponse> execute() throws ODataException
				{
					return handleChangeSetInTransaction(param);
				}
			});
		}
		catch (final Exception e)
		{
			throw new InternalProcessingException(e);
		}
	}

	private List<ODataResponse> handleChangeSetInTransaction(final ChangeSetParam param) throws ODataException
	{
		final List<ODataResponse> responses = Lists.newArrayList();
		final BatchHandler batchHandler = param.getBatchHandler();
		for (final ODataRequest request : param.getRequests())
		{
			final ODataResponse response = batchHandler.handleRequest(request);
			if (response.getStatus().getStatusCode() >= HttpStatusCodes.BAD_REQUEST.getStatusCode())
			{
				return Collections.singletonList(response);
			}
			responses.add(response);
		}
		return responses;
	}

	/**
	 * Commits the changes in the transaction
	 */
	protected void commitTransaction()
	{
		getCurrentTransaction().commit();
	}

	/**
	 * Rolls back the changes in the transaction
	 */
	protected void rollbackTransaction()
	{
		getCurrentTransaction().rollback();
	}

	/**
	 * Marks the beginning of the transaction
	 */
	protected void beginTransaction()
	{
		getCurrentTransaction().begin();
	}

	/**
	 * Marks the end of the transaction
	 * @param responses The responses from the persistence
	 */
	protected void endTransaction(final List<ODataResponse> responses)
	{
		if (isFailure(responses))
		{
			rollbackTransaction();
			getModelService().detachAll();
		}
		else
		{
			commitTransaction();
		}
	}

	private boolean isFailure(final List<ODataResponse> responses)
	{
		return !responses.isEmpty() &&
				responses.get(0).getStatus().getStatusCode() >= HttpStatusCodes.BAD_REQUEST.getStatusCode();
	}

	/**
	 * Creates a {@link BatchResponsePart} from the persistence responses
	 * @param responses List of persistence responses
	 * @return The BatchResponsePart
	 */
	protected BatchResponsePart createBatchResponsePart(final List<ODataResponse> responses)
	{
		return BatchResponsePart.responses(responses)
				.changeSet(true)
				.build();
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
