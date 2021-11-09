/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon.facades;

import de.hybris.platform.secureportaladdon.data.B2BRegistrationData;
import de.hybris.platform.secureportaladdon.exceptions.CustomerAlreadyExistsException;


/**
 * Facade responsible for everything related to B2B registration
 */
public interface B2BRegistrationFacade
{

	/**
	 * Initiates the registration process for B2B. This method will first validate the submitted data, check if a user or
	 * a company to the given name already exists, persist the registration request (as a model) and initiate the
	 * workflow so that the registration request either gets approved OR rejected.
	 * 
	 * @param data
	 *           The registration data
	 */
	public void register(B2BRegistrationData data) throws CustomerAlreadyExistsException;

}
