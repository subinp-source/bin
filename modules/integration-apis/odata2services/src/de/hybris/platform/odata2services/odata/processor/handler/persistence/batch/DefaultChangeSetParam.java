/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence.batch;

import java.util.List;

import org.apache.olingo.odata2.api.batch.BatchHandler;
import org.apache.olingo.odata2.api.processor.ODataRequest;

/**
 * Default implementation of the {@link ChangeSetParam}
 */
public class DefaultChangeSetParam implements ChangeSetParam
{
	private BatchHandler batchHandler;
	private List<ODataRequest> requests;

	public static Builder changeSetParam()
	{
		return new Builder();
	}

	@Override
	public BatchHandler getBatchHandler()
	{
		return batchHandler;
	}

	@Override
	public List<ODataRequest> getRequests()
	{
		return requests;
	}

	public static class Builder
	{
		private DefaultChangeSetParam param = new DefaultChangeSetParam();

		public Builder withBatchHandler(final BatchHandler batchHandler)
		{
			param.batchHandler = batchHandler;
			return this;
		}

		public Builder withRequests(final List<ODataRequest> requests)
		{
			param.requests = requests;
			return this;
		}

		public DefaultChangeSetParam build()
		{
			return param;
		}
	}
}
