/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.data.impl;

import de.hybris.platform.cms2.servicelayer.data.impl.DefaultRestrictionData;
import de.hybris.platform.marketplaceservices.data.MarketplaceRestrictionData;
import de.hybris.platform.ordersplitting.model.VendorModel;

public class MarketplaceRestrictionDataImpl extends DefaultRestrictionData implements MarketplaceRestrictionData
{
	private static final long serialVersionUID = -2190700602976596400L;
	private VendorModel vendor;

	@Override
	public void setVendor(VendorModel vendor)
	{
		this.vendor = vendor;
	}

	@Override
	public VendorModel getVendor()
	{
		return this.vendor;
	}

}
