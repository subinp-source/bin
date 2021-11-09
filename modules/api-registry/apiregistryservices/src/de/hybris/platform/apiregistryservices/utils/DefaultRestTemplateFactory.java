/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;


/**
 * Default implementation to generate the new instance of  {@link RestTemplate} depending on the destination and it's credentials.
 * Please do not use this class in your developments as this class will be removed soon.
 */
public class DefaultRestTemplateFactory
{

	public RestTemplate getRestTemplate(final AbstractDestinationModel destinationModel) throws CredentialException
	{
		if(destinationModel.getCredential() ==  null)
		{
			return new RestTemplate();
		}
		if (!getRestTemplateProviders().containsKey(destinationModel.getCredential().getClass()))
		{
			throw new IllegalArgumentException(
					String.format("RestTemplateProvider for Credential class %s is not registered", destinationModel.getCredential().getClass()));
		}
		return getRestTemplateProviders().get(destinationModel.getCredential().getClass())
				.getRestTemplate(destinationModel.getCredential());
	}

	protected Map<Class<? extends AbstractCredentialModel>, RestTemplateProvider> getRestTemplateProviders()
	{
		final HashMap<Class<? extends AbstractCredentialModel>, RestTemplateProvider> restTemplateProviderMap = new HashMap<>();
		restTemplateProviderMap.put(de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel.class, new DefaultCertificateCredentialsRestTemplateProvider());
		restTemplateProviderMap.put(de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel.class, new DefaultOAuthCredentialsRestTemplateProvider());
		restTemplateProviderMap.put(de.hybris.platform.apiregistryservices.model.BasicCredentialModel.class, new DefaultBasicCredentialsRestTemplateProvider());
		restTemplateProviderMap.put(de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel.class, new DefaultOAuthCredentialsRestTemplateProvider());
		return restTemplateProviderMap;
	}

}
