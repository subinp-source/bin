/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.utils;

import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import de.hybris.platform.util.Config;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel;

/**
 * Default implementation of {@link RestTemplateProvider} for a REST Web Service interface with OAuth2 authorization.
 * Please do not use this class in your developments as this class will be removed soon.
 */
public class DefaultOAuthCredentialsRestTemplateProvider extends RestTemplateProvider
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultOAuthCredentialsRestTemplateProvider.class);
	private static final String DEPLOYMENT_API_ENDPOINT_PROP = "{ccv2.services.api.url.0}";
	private static final String DEPLOYMENT_API_ENDPOINT_PARAM = "ccv2.services.api.url.0";
	private static final String GRANT_TYPE = "client_credentials";

	@Override
	public RestTemplate getRestTemplate(final AbstractCredentialModel abstractCredential) throws CredentialException
	{
		validateCredential(abstractCredential);

		if(abstractCredential instanceof ConsumedOAuthCredentialModel)
		{
			final ConsumedOAuthCredentialModel credential = (ConsumedOAuthCredentialModel) abstractCredential;
			return new OAuth2RestTemplate(setResourceDetails(credential.getOAuthUrl(),
					credential.getClientId(), credential.getClientSecret()));
		}

		if(abstractCredential instanceof ExposedOAuthCredentialModel)
		{
			final ExposedOAuthCredentialModel credential = (ExposedOAuthCredentialModel) abstractCredential;
			return new OAuth2RestTemplate(setResourceDetails(validateOAuthUrl(credential.getOAuthClientDetails().getOAuthUrl()),
					credential.getOAuthClientDetails().getClientId(), credential.getPassword()));
		}

		return null;
	}

	protected BaseOAuth2ProtectedResourceDetails setResourceDetails(final String OAuthUrl, final String clientId, final String clientSecret)
	{
		final BaseOAuth2ProtectedResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
		resourceDetails.setAccessTokenUri(OAuthUrl);
		resourceDetails.setClientId(clientId);
		resourceDetails.setClientSecret(clientSecret);
		resourceDetails.setGrantType(GRANT_TYPE);
		return resourceDetails;
	}

	protected void validateCredential(final AbstractCredentialModel abstractCredential) throws CredentialException
	{
		if (!(abstractCredential instanceof ConsumedOAuthCredentialModel || abstractCredential instanceof ExposedOAuthCredentialModel))
		{
			final String errorMessage = "Missing Consumed or Exposed OAuth2 Credential type.";
			LOG.error(errorMessage);
			throw new CredentialException(errorMessage);
		}

		if(abstractCredential instanceof ConsumedOAuthCredentialModel)
		{
			final ConsumedOAuthCredentialModel consumedCredential = (ConsumedOAuthCredentialModel) abstractCredential;
			validateCredentialAttributes(consumedCredential.getClientId(), consumedCredential.getClientSecret(),
					consumedCredential.getOAuthUrl(), consumedCredential.getId());
		}

		if(abstractCredential instanceof ExposedOAuthCredentialModel)
		{
			final ExposedOAuthCredentialModel exposedCredential = (ExposedOAuthCredentialModel) abstractCredential;
			validateCredentialAttributes(exposedCredential.getOAuthClientDetails().getClientId(), exposedCredential.getOAuthClientDetails().getClientSecret(),
					exposedCredential.getOAuthClientDetails().getOAuthUrl(), exposedCredential.getId());
		}
	}

	protected String validateOAuthUrl(final String oAuthUrl)
	{
		return oAuthUrl.contains(DEPLOYMENT_API_ENDPOINT_PROP) ?
				oAuthUrl.replace(DEPLOYMENT_API_ENDPOINT_PROP, Config.getParameter(DEPLOYMENT_API_ENDPOINT_PARAM)) : oAuthUrl;
	}

	protected void validateCredentialAttributes(final String clientId, final String clientSecret, final String OAuthUrl, final String Id) throws CredentialException
	{
		if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientSecret)
				|| StringUtils.isEmpty(OAuthUrl))
		{
			final String errorMessage = String.format("Invalid Consumed or Exposed OAuth2 Credential with id: [{%s}]", Id);
			LOG.error(errorMessage);
			throw new CredentialException(errorMessage);
		}
	}
}
