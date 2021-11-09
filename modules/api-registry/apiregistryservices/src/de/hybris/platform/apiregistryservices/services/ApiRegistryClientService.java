/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services;

import java.util.Map;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;


/**
 * Service layer implementation for client operations
 */
public interface ApiRegistryClientService
{
	/**
	 * Generates a client proxy for a given client type
	 *
	 * @param clientType
	 *           the type of the client
	 * @param <T>
	 *           the parametrised type for the client type
	 * @return the client proxy
	 * @throws CredentialException
	 *            in case when failed to find the expected credential
	 */
	<T> T lookupClient(final Class<T> clientType) throws CredentialException;

	/**
	 * Method to build client configuration as map for the given client client proxy type
	 *
	 * @param clientType
	 *           the type of the client
	 * @param destination
	 *           the consumed destination
	 * @return map of client configurations
	 * @throws CredentialException
	 *            in case when responsible strategy fails
	 * 
	 */
	Map<String, String> buildClientConfig(final Class clientType, final ConsumedDestinationModel destination)
			throws CredentialException;

}
