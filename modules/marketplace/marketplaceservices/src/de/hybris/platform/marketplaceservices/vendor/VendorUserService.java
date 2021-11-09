/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.vendor;

import de.hybris.platform.marketplaceservices.model.VendorUserModel;


/**
 * Service with VendorUser related methods
 */
public interface VendorUserService
{
	/**
	 * Deactivate a specific vendorUser
	 *
	 * @param vendorUser
	 *           the specific vendorUser
	 */
	void deactivateUser(VendorUserModel vendorUser);

	/**
	 * Activate a specific vendorUser
	 *
	 * @param vendorUser
	 *           the specific vendorUser
	 */
	void activateUser(VendorUserModel vendorUser);
}
