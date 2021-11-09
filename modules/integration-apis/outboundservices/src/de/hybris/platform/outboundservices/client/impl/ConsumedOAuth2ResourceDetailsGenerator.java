/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.client.impl;

import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel;

import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

/**
 * Implementation of {@link OAuth2ResourceDetailsGenerator} that generates the resource details from a
 * {@link ConsumedOAuthCredentialModel}
 */
public class ConsumedOAuth2ResourceDetailsGenerator implements OAuth2ResourceDetailsGenerator
{
	@Override
	public boolean isApplicable(final AbstractCredentialModel credentialModel)
	{
		return credentialModel instanceof ConsumedOAuthCredentialModel;
	}

	@Override
	public OAuth2ProtectedResourceDetails createResourceDetails(final ConsumedDestinationModel destination)
	{
		final ConsumedOAuthCredentialModel credential = (ConsumedOAuthCredentialModel) destination.getCredential();
		final ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		resource.setAccessTokenUri(credential.getOAuthUrl());
		resource.setClientId(credential.getClientId());
		resource.setClientSecret(credential.getClientSecret());
		return resource;
	}
}