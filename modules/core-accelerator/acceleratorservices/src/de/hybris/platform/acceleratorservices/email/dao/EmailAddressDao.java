/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.email.dao;

import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;


/**
 * Data Access for looking up email addresses.
 */
public interface EmailAddressDao extends Dao
{
	/**
	 * Retrieves EmailAddress given email address and display name.
	 * 
	 * @param email
	 *           the email address
	 * @param displayName
	 *           the display name
	 * @return The EmailAddress or null if not found
	 */
	EmailAddressModel findEmailAddressByEmailAndDisplayName(String email, String displayName);
}
