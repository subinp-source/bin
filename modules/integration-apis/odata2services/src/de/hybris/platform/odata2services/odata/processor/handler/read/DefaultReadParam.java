/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.read;

import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.UriInfo;

/**
 * Default implementation of the {@link ReadParam}
 */
public class DefaultReadParam implements ReadParam
{
	private UriInfo uriInfo;
	private String responseContentType;
	private ODataContext context;

	public static Builder readParam()
	{
		return new Builder();
	}

	@Override
	public UriInfo getUriInfo()
	{
		return uriInfo;
	}

	@Override
	public String getResponseContentType()
	{
		return responseContentType;
	}

	@Override
	public ODataContext getContext()
	{
		return context;
	}

	public static class Builder
	{
		private DefaultReadParam param = new DefaultReadParam();

		public Builder withUriInfo(final UriInfo uriInfo)
		{
			param.uriInfo = uriInfo;
			return this;
		}

		public Builder withResponseContentType(final String responseContentType)
		{
			param.responseContentType = responseContentType;
			return this;
		}

		public Builder withContext(final ODataContext context)
		{
			param.context = context;
			return this;
		}

		public DefaultReadParam build()
		{
			return param;
		}
	}
}
