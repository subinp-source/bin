/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.strategies;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;

/**
 * Strategy for finding the consumed destination
 */
public interface ConsumedDestinationLocatorStrategy
{
	/**
	 * Lookup the consumed destination for the given client type
	 *
	 * @param clientTypeName
	 *           the name of client type
	 * @return the consumed destination for the given client type
	 * @throws CredentialException
	 *            in case when failed to find the expected credential
	 */
	ConsumedDestinationModel lookup(String clientTypeName);
}
