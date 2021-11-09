/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.client.impl;

import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;

import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * Factory that determines what implementation of the {@link OAuth2ProtectedResourceDetails} needs to be implemented based on the
 * type of the
 */
public interface OAuth2ResourceDetailsGeneratorFactory
{
	/**
	 * Determines if the factory is applicable and a generator needs to be instantiated based on the supported credential model
	 * types
	 *
	 * @param credentialModel used for creating the generator
	 * @return true if the credential model is supported, false if it's not
	 */
	boolean isApplicable(final AbstractCredentialModel credentialModel);

	/**
	 * Factory method that instantiates the correct implementation based on the specific type of the credential model received
	 * as a parameter
	 *
	 * @param credentialModel credential used in the consumed destination
	 * @return The implementation of the generator that corresponds to the specific credential model used.
	 */
	OAuth2ResourceDetailsGenerator getGenerator(final AbstractCredentialModel credentialModel);
}