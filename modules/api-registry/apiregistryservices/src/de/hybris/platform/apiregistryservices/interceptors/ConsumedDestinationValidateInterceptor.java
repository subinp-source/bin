/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.interceptors;

import de.hybris.platform.apiregistryservices.event.InvalidateCharonCacheEvent;
import de.hybris.platform.apiregistryservices.factory.ClientFactory;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.services.DestinationCredentialService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;


public class ConsumedDestinationValidateInterceptor implements ValidateInterceptor<ConsumedDestinationModel>
{

	private ModelService modelService;

	private EventService eventService;

	private ClientFactory clientFactory;

	private DestinationCredentialService destinationCredentialService;


	@Override
	public void onValidate(final ConsumedDestinationModel consumedDestinationModel, final InterceptorContext interceptorContext)
			throws InterceptorException
	{
		if(!getDestinationCredentialService().isValidDestinationCredential(consumedDestinationModel))
		{
			throw new InterceptorException("Invalid credential type. Consumed credential cannot be assigned to exposed destination!");
		}

		if (!getModelService().isNew(consumedDestinationModel))
		{
			eventService.publishEvent(new InvalidateCharonCacheEvent(getClientFactory().buildCacheKey(consumedDestinationModel)));
		}
	}

	protected DestinationCredentialService getDestinationCredentialService()
	{
		return destinationCredentialService;
	}

	public void setDestinationCredentialService(
			final DestinationCredentialService destinationCredentialService)
	{
		this.destinationCredentialService = destinationCredentialService;
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

	protected ClientFactory getClientFactory()
	{
		return clientFactory;
	}

	@Required
	public void setClientFactory(final ClientFactory clientFactory)
	{
		this.clientFactory = clientFactory;
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}
}
