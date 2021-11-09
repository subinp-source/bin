/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.cache;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;

import org.springframework.web.client.RestTemplate;

/**
 * Defines the identifier that uses destination and rest template class
 */
public interface DestinationRestTemplateId
{
	ConsumedDestinationModel getDestination();

	Class<? extends RestTemplate> getRestTemplateType();
}
