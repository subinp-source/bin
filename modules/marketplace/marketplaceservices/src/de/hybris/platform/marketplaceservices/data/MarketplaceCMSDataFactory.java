/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.data;

import de.hybris.platform.cms2.servicelayer.data.CMSDataFactory;
import de.hybris.platform.ordersplitting.model.VendorModel;

public interface MarketplaceCMSDataFactory extends CMSDataFactory
{
	MarketplaceRestrictionData createRestrictionData(VendorModel vendorModel);
}
