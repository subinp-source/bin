/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.data.impl;

import de.hybris.platform.cms2.servicelayer.data.impl.DefaultCMSDataFactory;
import de.hybris.platform.marketplaceservices.data.MarketplaceCMSDataFactory;
import de.hybris.platform.marketplaceservices.data.MarketplaceRestrictionData;
import de.hybris.platform.ordersplitting.model.VendorModel;

public class MarketplaceCMSDataFactoryImpl extends DefaultCMSDataFactory implements MarketplaceCMSDataFactory
{

	@Override
	public MarketplaceRestrictionData createRestrictionData(final VendorModel vendor)
	{
		final MarketplaceRestrictionDataImpl data = new MarketplaceRestrictionDataImpl();
		data.setVendor(vendor);
		return data;
	}

}
