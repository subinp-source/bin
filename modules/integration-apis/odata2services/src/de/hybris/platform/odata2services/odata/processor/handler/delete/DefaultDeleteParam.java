/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.delete;

import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.info.DeleteUriInfo;

/**
 * Default implementation of the {@link DeleteParam}
 */
public class DefaultDeleteParam implements DeleteParam
{
	private DeleteUriInfo uriInfo;
	private ODataContext context;

	public static Builder deleteParam()
	{
		return new Builder();
	}

	@Override
	public DeleteUriInfo getUriInfo()
	{
		return uriInfo;
	}

	@Override
	public ODataContext getContext()
	{
		return context;
	}

	public static class Builder
	{
		private final DefaultDeleteParam param = new DefaultDeleteParam();

		public Builder withUriInfo(final DeleteUriInfo uriInfo)
		{
			param.uriInfo = uriInfo;
			return this;
		}

		public Builder withContext(final ODataContext context)
		{
			param.context = context;
			return this;
		}

		public DefaultDeleteParam build()
		{
			return param;
		}
	}
}
