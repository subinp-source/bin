/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.client.impl;

import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;

import org.slf4j.Logger;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

/**
 * Implementation of {@link OAuth2ResourceDetailsGenerator} that generates the resource details from a
 * {@link ExposedOAuthCredentialModel}
 */
public class ExposedOAuth2ResourceDetailsGenerator implements OAuth2ResourceDetailsGenerator
{
	private static final Logger LOG = Log.getLogger(ExposedOAuth2ResourceDetailsGenerator.class);

	@Override
	public boolean isApplicable(final AbstractCredentialModel credentialModel)
	{
		return credentialModel instanceof ExposedOAuthCredentialModel;
	}

	@Override
	public OAuth2ProtectedResourceDetails createResourceDetails(final ConsumedDestinationModel destination)
	{
		LOG.warn("ConsumedOAuthCredential is recommended over ExposedOAuthCredential for the credentials of the " +
				"ConsumedDestination. The use of ExposedOAuthCredential will eventually be deprecated for ConsumedDestination in" +
				"Outbound Synchronization.");

		final ExposedOAuthCredentialModel credential = (ExposedOAuthCredentialModel) destination.getCredential();
		final OAuthClientDetailsModel details = credential.getOAuthClientDetails();
		final ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		resource.setAccessTokenUri(details.getOAuthUrl());
		resource.setClientId(details.getClientId());
		resource.setClientSecret(credential.getPassword());
		return resource;
	}
}