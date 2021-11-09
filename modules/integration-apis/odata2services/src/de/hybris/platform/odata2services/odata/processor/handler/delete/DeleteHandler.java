/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.delete;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequestFactory;
import de.hybris.platform.odata2services.odata.persistence.PersistenceService;
import de.hybris.platform.odata2services.odata.processor.handler.ODataProcessorHandler;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * ODataProcessor handler that deletes an item
 */
public class DeleteHandler implements ODataProcessorHandler<DeleteParam, ODataResponse>
{
	private static final Logger LOG = Log.getLogger(DeleteHandler.class);

	private ItemLookupRequestFactory itemLookupRequestFactory;
	private PersistenceService persistenceService;

	@Override
	public ODataResponse handle(final DeleteParam param)
	{
		final ItemLookupRequest itemLookupRequest = getItemLookupRequest(param);

		try
		{
			getPersistenceService().deleteItem(itemLookupRequest);
		}
		catch (final EdmException e)
		{
			LOG.error("A problem occurred while attempting to delete an item", e);
			return ODataResponse.newBuilder().entity("").status(HttpStatusCodes.INTERNAL_SERVER_ERROR).build();
		}

		return ODataResponse.newBuilder().entity("").status(HttpStatusCodes.OK).build();
	}

	private ItemLookupRequest getItemLookupRequest(final DeleteParam param)
	{
		return getItemLookupRequestFactory().create(param.getUriInfo(), param.getContext());
	}

	protected PersistenceService getPersistenceService()
	{
		return persistenceService;
	}

	@Required
	public void setPersistenceService(final PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
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
}
