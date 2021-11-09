/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services;

import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;


/**
 * Service layer interface for Destination Credentials.
 */
public interface DestinationCredentialService
{
	/**
	 * Method that :
	 * <ul>
	 * <li><i>validate ExposedDestination does not contain ConsumedOAuthCredential</i></li>
	 * <li><i>validate ConsumedDestination does not contain ExposedOAuthCredential</i></li>
	 * </ul>
	 *
	 * @param destination
	 * 	         a destination to be checked
	 * @return true if the credential type is allowed with the destination type
	 */
	boolean isValidDestinationCredential(AbstractDestinationModel destination);
}
