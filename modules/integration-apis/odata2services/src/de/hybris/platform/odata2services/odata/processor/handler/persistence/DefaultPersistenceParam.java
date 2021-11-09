/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence;

import de.hybris.platform.integrationservices.util.Log;

import java.io.InputStream;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.UriInfo;
import org.slf4j.Logger;

/**
 * Default implementation of the {@link PersistenceParam}
 */
public class DefaultPersistenceParam implements PersistenceParam
{
	private static final Logger LOG = Log.getLogger(DefaultPersistenceParam.class);

	private UriInfo uriInfo;
	private InputStream content;
	private String requestContentType;
	private String responseContentType;
	private ODataContext context;

	public static Builder persistenceParam()
	{
		return new Builder();
	}


	@Override
	public UriInfo getUriInfo()
	{
		return uriInfo;
	}

	@Override
	public EdmEntitySet getEntitySet()
	{
		return getUriInfo() != null ? getUriInfo().getStartEntitySet() : null;
	}

	@Override
	public EdmEntityType getEntityType()
	{
		try
		{
			if (getEntitySet() != null)
			{
				return getEntitySet().getEntityType();
			}
		}
		catch (final EdmException e)
		{
			LOG.error("An exception occurred while geting the entity type from the entity set: ", e);
		}
		return null;
	}

	@Override
	public InputStream getContent()
	{
		return content;
	}

	@Override
	public String getRequestContentType()
	{
		return requestContentType;
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
		private DefaultPersistenceParam param = new DefaultPersistenceParam();

		public Builder withUriInfo(final UriInfo uriInfo)
		{
			param.uriInfo = uriInfo;
			return this;
		}

		public Builder withContent(final InputStream content)
		{
			param.content = content;
			return this;
		}

		public Builder withRequestContentType(final String requestContentType)
		{
			param.requestContentType = requestContentType;
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

		public DefaultPersistenceParam build()
		{
			return param;
		}
	}
}
