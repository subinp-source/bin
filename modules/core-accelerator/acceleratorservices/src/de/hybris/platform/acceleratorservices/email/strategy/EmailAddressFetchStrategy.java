/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.email.strategy;

import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;


/**
 * Interface representing strategy for fetching EmailAddressModel.
 */
public interface EmailAddressFetchStrategy
{

	/**
	 * @param emailAddress
	 *           email for which the email address object should be fetched
	 * @param displayName
	 *           display name for which the email address object should be fetched
	 * @return email address instance
	 */
	EmailAddressModel fetch(String emailAddress, String displayName);

}
