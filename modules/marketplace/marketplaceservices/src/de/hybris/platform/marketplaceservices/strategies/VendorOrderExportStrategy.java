/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies;

/**
 * A strategy to export csv file for pending vendor orders
 */
public interface VendorOrderExportStrategy
{
	/**
	 * check if the vendor's orders are ready to be exported
	 *
	 * @param vendorCode
	 *           the vendor's code
	 * @return true if the vendor's orders are ready to be exported and false otherwise
	 */
	boolean readyToExportOrdersForVendor(String vendorCode);

	/**
	 * export the vendor's orders
	 *
	 * @param vendorCode
	 *           the vendor's code
	 */
	void exportOrdersForVendor(String vendorCode);
}
