/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence;


import de.hybris.platform.integrationservices.item.IntegrationItem;

import java.net.URI;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

/**
 * Request for a CRUD operation.
 */
public abstract class CrudRequest extends AbstractRequest
{
	private String integrationKey;
	private ODataEntry oDataEntry;
	private URI serviceRoot;
	private String contentType;
	private URI requestUri;
	private IntegrationItem integrationItem;

	public String getIntegrationKey()
	{
		return integrationKey == null && getIntegrationItem() != null
				? getIntegrationItem().getIntegrationKey()
				: integrationKey;
	}

	protected void setIntegrationKey(final String integrationKey)
	{
		this.integrationKey = integrationKey;
	}

	/**
	 * Retrieves an OData entry associated with this request.
	 * @return the entry being handled by this request
	 * @deprecated Since 1905. Use {@link #getIntegrationItem()} instead
	 */
	@Deprecated(since = "1905", forRemoval= true )
	public ODataEntry getODataEntry()
	{
		return oDataEntry;
	}

	/**
	 * Sets the context OData entry for this request.
	 * @param oDataEntry an entry to associate with this request
	 * @deprecated Since 1905. Use {@link #setIntegrationItem(IntegrationItem)} instead
	 */
	@Deprecated(since = "1905", forRemoval= true )
	protected void setODataEntry(final ODataEntry oDataEntry)
	{
		this.oDataEntry = oDataEntry;
	}

	public URI getServiceRoot()
	{
		return serviceRoot;
	}

	protected void setServiceRoot(final URI serviceRoot)
	{
		this.serviceRoot = serviceRoot;
	}

	public String getContentType()
	{
		return contentType;
	}

	protected void setContentType(final String contentType)
	{
		this.contentType = contentType;
	}

	public URI getRequestUri()
	{
		return requestUri;
	}

	protected void setRequestUri(final URI requestUri)
	{
		this.requestUri = requestUri;
	}

	public IntegrationItem getIntegrationItem()
	{
		return integrationItem;
	}

	protected void setIntegrationItem(final IntegrationItem item)
	{
		integrationItem = item;
	}

	public static class DataRequestBuilder<T extends CrudRequest.DataRequestBuilder, R extends CrudRequest> extends AbstractRequestBuilder<T, R>
	{
		protected DataRequestBuilder(final R request)
		{
			super(request);
		}

		/**
		 * Specifies integration key for the request to build
		 * @param integrationKey integration key value
		 * @return a builder with the integration key specified
		 * @deprecated the method has no effect because the only way to specify an integration key is to specify
		 * the integration item, i.e. {@link #withIntegrationItem(IntegrationItem)}
		 */
		@Deprecated(since = "2105", forRemoval = true)
		public T withIntegrationKey(final String integrationKey)
		{
			request().setIntegrationKey(integrationKey);
			return myself();
		}

		/**
		 * Specifies OData entry associated with the request
		 * @param oDataEntry an OData entry to be handled by the request
		 * @return a builder with the OData entry specified
		 * @deprecated since 1905. Use {@link #withIntegrationItem(IntegrationItem)} instead
		 */
		@Deprecated(since = "1905", forRemoval= true )
		public T withODataEntry(final ODataEntry oDataEntry)
		{
			request().setODataEntry(oDataEntry);
			return myself();
		}

		public T withServiceRoot(final URI serviceRoot)
		{
			request().setServiceRoot(serviceRoot);
			return myself();
		}

		public T withContentType(final String contentType)
		{
			request().setContentType(contentType);
			return myself();
		}

		public T withRequestUri(final URI currentRequestUri)
		{
			request().setRequestUri(currentRequestUri);
			return myself();
		}

		public T withIntegrationItem(final IntegrationItem item)
		{
			request().setIntegrationItem(item);
			return myself();
		}

		@Override
		public T from(final R request)
		{
			withODataEntry(request.getODataEntry())
					.withIntegrationItem(request.getIntegrationItem());
			return super.from(request);
		}
	}
}
