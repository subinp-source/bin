/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.cronjob;

import de.hybris.platform.marketplaceservices.strategies.VendorOrderExportStrategy;


/**
 * A task to export vendor orders
 */
public class VendorOrderExportTask implements Runnable
{
	private final VendorOrderExportStrategy vendorOrderExportStrategy;
	private final String vendorCode;

	public VendorOrderExportTask(final VendorOrderExportStrategy vendorOrderexportStrategy, final String vendorCode)
	{
		this.vendorOrderExportStrategy = vendorOrderexportStrategy;
		this.vendorCode = vendorCode;
	}

	@Override
	public void run()
	{
		vendorOrderExportStrategy.exportOrdersForVendor(vendorCode);
	}

}
