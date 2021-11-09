/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundservices.client.impl;

import de.hybris.platform.apiregistryservices.model.BasicCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.outboundservices.cache.DestinationRestTemplateId;
import de.hybris.platform.outboundservices.cache.impl.DefaultDestinationRestTemplateId;

import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

/**
 * The default implementation for OAuth2RestTemplate creator.
 */
public class DefaultIntegrationBasicRestTemplateCreator extends AbstractRestTemplateCreator
{
	@Override
	public boolean isApplicable(final ConsumedDestinationModel destination)
	{
		return destination.getCredential() instanceof BasicCredentialModel;
	}

	@Override
	protected RestTemplate createRestTemplate(final ConsumedDestinationModel destination)
	{
		final RestTemplate restTemplate;
		restTemplate = new RestTemplate(getClientHttpRequestFactory());

		final BasicCredentialModel credential = (BasicCredentialModel) destination.getCredential();
		final BasicAuthenticationInterceptor basicAuthenticationInterceptor =
				new BasicAuthenticationInterceptor(credential.getUsername(), credential.getPassword());
		addInterceptors(restTemplate, basicAuthenticationInterceptor);
		addMessageConverters(restTemplate);
		return restTemplate;
	}

	@Override
	protected DestinationRestTemplateId getDestinationRestTemplateId(final ConsumedDestinationModel destinationModel)
	{
		return DefaultDestinationRestTemplateId.from(destinationModel);
	}
}
