/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.cache.impl;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.client.RestTemplate;

public class DestinationOauthRestTemplateId extends DefaultDestinationRestTemplateId
{
	protected DestinationOauthRestTemplateId(final ConsumedDestinationModel destination)
	{
		super(destination);
	}

	public static DestinationOauthRestTemplateId from(final ConsumedDestinationModel destination)
	{
		return new DestinationOauthRestTemplateId(destination);
	}

	@Override
	public Class<? extends RestTemplate> getRestTemplateType()
	{
		return OAuth2RestTemplate.class;
	}
}
