/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.event;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.PublishEventContext;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


/**
 * Event for Credentials invalidation in @{@link de.hybris.platform.kymaintegrationservices.utils.RestTemplateWrapper}
 */
public class InvalidateCertificateCredentialsCacheEvent extends AbstractEvent implements ClusterAwareEvent
{
    private String consumedCertificateCredentialId;

    public InvalidateCertificateCredentialsCacheEvent(final String certificateCredentialModelId)
    {
        this.consumedCertificateCredentialId = certificateCredentialModelId;
    }

    @Override
    public boolean canPublish(final PublishEventContext publishEventContext)
    {
        return true;
    }

    public String getConsumedCertificateCredentialId()
    {
        return consumedCertificateCredentialId;
    }

    public void setConsumedCertificateCredentialId(String consumedCertificateCredentialId)
    {
        this.consumedCertificateCredentialId = consumedCertificateCredentialId;
    }
}
