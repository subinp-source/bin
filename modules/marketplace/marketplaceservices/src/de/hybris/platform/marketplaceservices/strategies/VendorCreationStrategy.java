/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies;

import de.hybris.platform.ordersplitting.model.VendorModel;


/**
 * Strategy for creating a vendor in backoffice.
 */
public interface VendorCreationStrategy
{

	/**
	 * populate the specific vendor model and save it.
	 *
	 * @param vendor
	 *           the vendor to save
	 * @param useCustomPage
	 *           if true will assign the vendor a landing page
	 */
	void createVendor(VendorModel vendor, boolean useCustomPage);
}
