/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies;

import de.hybris.platform.ordersplitting.model.VendorModel;


/**
 * A strategy for activating a vendor.
 */
public interface VendorActivationStrategy
{

	/**
	 * Activate the specific vendor.
	 *
	 * @param vendor
	 *           the specific vendor.
	 */
	void activateVendor(VendorModel vendor);
}
