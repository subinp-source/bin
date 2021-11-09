/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.client.impl;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.outboundservices.cache.DestinationRestTemplateId;
import de.hybris.platform.outboundservices.cache.impl.DefaultDestinationRestTemplateId;

import org.springframework.web.client.RestTemplate;

/**
 * The default implementation for RestTemplate creator without authentication credentials.
 */
public class DefaultIntegrationNoCredentialRestTemplateCreator extends AbstractRestTemplateCreator
{
	@Override
	public boolean isApplicable(final ConsumedDestinationModel destination)
	{
		return null == destination.getCredential();
	}

	@Override
	protected RestTemplate createRestTemplate(final ConsumedDestinationModel destination)
	{
		final RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		addMessageConverters(restTemplate);
		addInterceptors(restTemplate);

		return restTemplate;
	}

	@Override
	protected DestinationRestTemplateId getDestinationRestTemplateId(final ConsumedDestinationModel destinationModel)
	{
		return DefaultDestinationRestTemplateId.from(destinationModel);
	}
}

