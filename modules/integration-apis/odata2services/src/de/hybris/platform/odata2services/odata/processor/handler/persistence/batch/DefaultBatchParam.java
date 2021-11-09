/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence.batch;

import de.hybris.platform.odata2services.odata.processor.NewLineSanitizerInputStream;
import de.hybris.platform.odata2services.odata.processor.ODataPayloadProcessingException;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.olingo.odata2.api.batch.BatchHandler;
import org.apache.olingo.odata2.api.batch.BatchRequestPart;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderBatchProperties;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.PathInfo;
import org.assertj.core.util.Lists;

/**
 * Default implementation of the {@link BatchParam}
 */
public class DefaultBatchParam implements BatchParam
{
	private BatchHandler batchHandler;
	private List<BatchRequestPart> batchRequestParts = Lists.emptyList();

	public static Builder batchParam()
	{
		return new Builder();
	}

	@Override
	public BatchHandler getBatchHandler()
	{
		return batchHandler;
	}

	@Override
	public List<BatchRequestPart> getBatchRequestParts()
	{
		return Collections.unmodifiableList(batchRequestParts);
	}

	@Override
	public int getBatchRequestPartSize()
	{
		final List<BatchRequestPart> requestParts = getBatchRequestParts();
		return requestParts != null ? requestParts.size() : 0;
	}

	public static class Builder
	{
		private final DefaultBatchParam param = new DefaultBatchParam();
		private String responseContentType;
		private InputStream content;
		private ODataContext context;

		public Builder withBatchHandler(final BatchHandler batchHandler)
		{
			param.batchHandler = batchHandler;
			return this;
		}

		public Builder withResponseContentType(final String responseContentType)
		{
			this.responseContentType = responseContentType;
			return this;
		}

		public Builder withContent(final InputStream content)
		{
			this.content = content;
			return this;
		}

		public Builder withContext(final ODataContext context)
		{
			this.context = context;
			return this;
		}

		public DefaultBatchParam build()
		{
			param.batchRequestParts = buildBatchRequestParts();
			return param;
		}

		private List<BatchRequestPart> buildBatchRequestParts()
		{
			try
			{
				if (isContentParsable())
				{
					return EntityProvider.parseBatchRequest(responseContentType, new NewLineSanitizerInputStream(content), getBatchProperties());
				}
			}
			catch (final ODataException | RuntimeException e)
			{
				throw new ODataPayloadProcessingException(e);
			}
			return Lists.emptyList();
		}

		private boolean isContentParsable() throws ODataException
		{
			return context != null && context.getPathInfo() != null && content != null && StringUtils.isNotBlank(responseContentType);
		}

		private EntityProviderBatchProperties getBatchProperties() throws ODataException
		{
			final PathInfo pathInfo = context.getPathInfo();
			return EntityProviderBatchProperties.init()
					.pathInfo(pathInfo)
					.setStrict(true)
					.build();
		}
	}
}
