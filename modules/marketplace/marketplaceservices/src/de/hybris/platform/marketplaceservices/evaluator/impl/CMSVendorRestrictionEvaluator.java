/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.evaluator.impl;

import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.marketplaceservices.data.MarketplaceRestrictionData;
import de.hybris.platform.marketplaceservices.model.restrictions.CMSVendorRestrictionModel;


public class CMSVendorRestrictionEvaluator implements CMSRestrictionEvaluator<CMSVendorRestrictionModel>
{

	@Override
	public boolean evaluate(final CMSVendorRestrictionModel vendorRestrictionModel, final RestrictionData restrictionData)
	{
		if (!(restrictionData instanceof MarketplaceRestrictionData))
		{
			return true;
		}
		final MarketplaceRestrictionData marketplaceRestrictionData = (MarketplaceRestrictionData) restrictionData;
		return vendorRestrictionModel.getVendor().getCode().equals(marketplaceRestrictionData.getVendor().getCode());
	}
}
