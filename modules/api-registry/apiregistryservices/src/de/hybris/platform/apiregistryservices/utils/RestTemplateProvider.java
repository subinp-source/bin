/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.utils;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;

import org.springframework.web.client.RestTemplate;

/**
 * The RestTemplateProvider provides a new instances of {@link org.springframework.web.client.RestTemplate} for valid instances of
 * {@link de.hybris.platform.apiregistryservices.model.AbstractDestinationModel} sub types.
 * Please do not use this class in your developments as this class will be removed soon.
 */
public abstract class RestTemplateProvider
{
	abstract RestTemplate getRestTemplate(AbstractCredentialModel abstractCredential) throws CredentialException;
}
