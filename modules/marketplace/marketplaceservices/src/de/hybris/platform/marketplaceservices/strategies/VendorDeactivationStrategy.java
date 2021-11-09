/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies;

import de.hybris.platform.ordersplitting.model.VendorModel;


/**
 * A strategy for deactivating a vendor.
 */
public interface VendorDeactivationStrategy
{

	/**
	 * Deactivate the specific vendor.
	 *
	 * @param vendor
	 *           the specific vendor.
	 */
	void deactivateVendor(VendorModel vendor);
}
