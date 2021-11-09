/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.interceptors;

import de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel;
import de.hybris.platform.kymaintegrationservices.event.InvalidateCertificateCredentialsCacheEvent;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;


/**
 * ConsumedCertificateCredentialValidateInterceptor publishes {@link InvalidateCertificateCredentialsCacheEvent}
 * when updating the existing {@link ConsumedCertificateCredentialModel}
 */
public class ConsumedCertificateCredentialValidateInterceptor implements ValidateInterceptor<ConsumedCertificateCredentialModel>
{
	private ModelService modelService;
	private EventService eventService;

	@Override
	public void onValidate(final ConsumedCertificateCredentialModel consumedCertificateCredential,
			final InterceptorContext interceptorContext) throws InterceptorException
	{
		if (!getModelService().isNew(consumedCertificateCredential))
		{
			final String originalConsumedCertificateCredentialId = consumedCertificateCredential.getItemModelContext().getOriginalValue(ConsumedCertificateCredentialModel.ID);
			getEventService().publishEvent(new InvalidateCertificateCredentialsCacheEvent(originalConsumedCertificateCredentialId));
		}
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
