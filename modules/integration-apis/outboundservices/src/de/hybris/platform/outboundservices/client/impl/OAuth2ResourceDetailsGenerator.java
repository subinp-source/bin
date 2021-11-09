/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.client.impl;

import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;

import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * Defines the interface for generating an {@link OAuth2ProtectedResourceDetails}
 */
public interface OAuth2ResourceDetailsGenerator
{
	/**
	 * Determines whether the generator is applicable or not based on the type of the concrete type of the
	 * {@link AbstractCredentialModel}
	 *
	 * @param credentialModel of a specific type
	 * @return true if the credential model is supported, false if it's not
	 */
	boolean isApplicable(final AbstractCredentialModel credentialModel);

	/**
	 * Creates and returns the {@link OAuth2ProtectedResourceDetails} based on the credentials found in the
	 * {@link ConsumedDestinationModel}
	 * received as a parameter
	 *
	 * @param destination {@link ConsumedDestinationModel} with the necessary details to create the resource details
	 * @return {@link OAuth2ProtectedResourceDetails} generated
	 */
	OAuth2ProtectedResourceDetails createResourceDetails(ConsumedDestinationModel destination);
}
