/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.event.listeners;

import de.hybris.platform.kymaintegrationservices.event.InvalidateCertificateCredentialsCacheEvent;
import de.hybris.platform.kymaintegrationservices.utils.RestTemplateWrapper;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Listener, which runs restTemplate connection factory cache invalidation
 */
public class InvalidateCertificateCredentialsCacheEventListener extends AbstractEventListener<InvalidateCertificateCredentialsCacheEvent>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidateCertificateCredentialsCacheEventListener.class);

    private RestTemplateWrapper kymaDestinationRestTemplateWrapper;
    private RestTemplateWrapper kymaEventRestTemplateWrapper;

    @Override
    protected void onEvent(final InvalidateCertificateCredentialsCacheEvent event)
    {
        if (LOGGER.isDebugEnabled())
        {
			LOGGER.debug("InvalidateCertificateCredentialsCacheEvent received, object: {}", event);
        }
        final String consumedCertificateCredentialId = event.getConsumedCertificateCredentialId();
        getKymaDestinationRestTemplateWrapper().invalidateTheCache(consumedCertificateCredentialId);
        getKymaEventRestTemplateWrapper().invalidateTheCache(consumedCertificateCredentialId);
    }

    protected RestTemplateWrapper getKymaEventRestTemplateWrapper()
    {
        return kymaEventRestTemplateWrapper;
    }

    @Required
    public void setKymaEventRestTemplateWrapper(
          final RestTemplateWrapper kymaEventRestTemplateWrapper)
    {
        this.kymaEventRestTemplateWrapper = kymaEventRestTemplateWrapper;
    }

    protected RestTemplateWrapper getKymaDestinationRestTemplateWrapper()
    {
        return kymaDestinationRestTemplateWrapper;
    }

    @Required
    public void setKymaDestinationRestTemplateWrapper(
          final RestTemplateWrapper kymaDestinationRestTemplateWrapper)
    {
        this.kymaDestinationRestTemplateWrapper = kymaDestinationRestTemplateWrapper;
    }
}
