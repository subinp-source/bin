/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.read;

import de.hybris.platform.integrationservices.exception.IntegrationAttributeException;
import de.hybris.platform.integrationservices.exception.TypeAttributeDescriptorNotFoundException;
import de.hybris.platform.integrationservices.search.ItemSearchException;
import de.hybris.platform.integrationservices.security.TypeAccessPermissionException;
import de.hybris.platform.odata2services.odata.OData2ServicesException;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequestFactory;
import de.hybris.platform.odata2services.odata.persistence.exception.ItemNotFoundException;
import de.hybris.platform.odata2services.odata.persistence.exception.PropertyNotFoundException;
import de.hybris.platform.odata2services.odata.processor.RetrievalErrorRuntimeException;
import de.hybris.platform.odata2services.odata.processor.handler.ODataProcessorHandler;
import de.hybris.platform.odata2services.odata.processor.reader.EntityReader;
import de.hybris.platform.odata2services.odata.processor.reader.EntityReaderRegistry;

import org.apache.http.HttpHeaders;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.springframework.beans.factory.annotation.Required;

/**
 * ODataProcessor handler that a read request
 */
public class ReadHandler implements ODataProcessorHandler<ReadParam, ODataResponse>
{
	private ItemLookupRequestFactory itemLookupRequestFactory;
	private EntityReaderRegistry entityReaderRegistry;

	@Override
	public ODataResponse handle(final ReadParam param) throws ODataException
	{
		try
		{
			final EntityReader entityReader = getReader(param);
			final ItemLookupRequest itemLookupRequest = getItemLookupRequest(param);
			final ODataResponse readerResponse = entityReader.read(itemLookupRequest);
			return augmentResponse(itemLookupRequest, readerResponse);
		}
		catch (final PropertyNotFoundException | ItemNotFoundException | OData2ServicesException | IntegrationAttributeException
				| TypeAttributeDescriptorNotFoundException | ItemSearchException | TypeAccessPermissionException ex)
		{
			throw ex;
		}
		catch (final RuntimeException ex)
		{
			throw new RetrievalErrorRuntimeException(param.getUriInfo().getStartEntitySet().getEntityType().getName(), ex);
		}
	}

	private EntityReader getReader(final ReadParam param)
	{
		return getEntityReaderRegistry().getReader(param.getUriInfo());
	}

	private ItemLookupRequest getItemLookupRequest(final ReadParam param)
	{
		return getItemLookupRequestFactory().create(param.getUriInfo(), param.getContext(), param.getResponseContentType());
	}

	/**
	 * Augment the given {@link ODataResponse}
	 *
	 * @param itemLookupRequest Item lookup request contains the information on how to create the response
	 * @param readerResponse    Response to augment
	 * @return The {@link ODataResponse}
	 */
	protected ODataResponse augmentResponse(final ItemLookupRequest itemLookupRequest, final ODataResponse readerResponse)
	{
		return getODataResponseBuilder(readerResponse)
				.header(HttpHeaders.CONTENT_LANGUAGE, itemLookupRequest.getAcceptLocale().getLanguage())
				.build();
	}

	protected ODataResponse.ODataResponseBuilder getODataResponseBuilder(final ODataResponse readerResponse)
	{
		return ODataResponse.fromResponse(readerResponse);
	}

	protected ItemLookupRequestFactory getItemLookupRequestFactory()
	{
		return itemLookupRequestFactory;
	}

	@Required
	public void setItemLookupRequestFactory(final ItemLookupRequestFactory itemLookupRequestFactory)
	{
		this.itemLookupRequestFactory = itemLookupRequestFactory;
	}

	protected EntityReaderRegistry getEntityReaderRegistry()
	{
		return entityReaderRegistry;
	}

	@Required
	public void setEntityReaderRegistry(final EntityReaderRegistry entityReaderRegistry)
	{
		this.entityReaderRegistry = entityReaderRegistry;
	}
}