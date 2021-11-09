/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.utils;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel;

import org.apache.commons.lang.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.web.client.RestTemplate;

/**
 * Default implementation of {@link RestTemplateProvider} for a REST Web Service interface with Basic authentication.
 * Please do not use this class in your developments as this class will be removed soon.
 */
public class DefaultBasicCredentialsRestTemplateProvider extends RestTemplateProvider
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultBasicCredentialsRestTemplateProvider.class);

	@Override
	RestTemplate getRestTemplate(final AbstractCredentialModel abstractCredential) throws CredentialException
	{
		validateCredential(abstractCredential);

		final BasicCredentialModel basicCredential = (BasicCredentialModel) abstractCredential;

		return new RestTemplate(getClientHttpRequestFactory(basicCredential));

	}

	protected void validateCredential(final AbstractCredentialModel abstractCredential) throws CredentialException
	{
		if (!(abstractCredential instanceof BasicCredentialModel))
		{
			final String errorMessage = "Missing Basic Credential.";
			LOG.error(errorMessage);
			throw new CredentialException(errorMessage);
		}

		final BasicCredentialModel credential = (BasicCredentialModel) abstractCredential;

		if (StringUtils.isEmpty(credential.getUsername()))
		{
			final String errorMessage = String.format("Invalid Basic Credential with id: [{%s}]", credential.getId());
			LOG.error(errorMessage);
			throw new CredentialException(errorMessage);
		}
	}

	protected HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(final BasicCredentialModel basicCredential)
	{
		final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
				= new HttpComponentsClientHttpRequestFactory();

		clientHttpRequestFactory.setHttpClient(httpClient(basicCredential));

		return clientHttpRequestFactory;
	}

	protected HttpClient httpClient(final BasicCredentialModel basicCredential)
	{
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

		credentialsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(basicCredential.getUsername(), basicCredential.getPassword()));

		return HttpClientBuilder
				.create()
				.setDefaultCredentialsProvider(credentialsProvider)
				.build();

	}

}
