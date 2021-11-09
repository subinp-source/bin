/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplacefacades.vendor.converters.populator;

import de.hybris.platform.commercefacades.product.data.VendorReviewData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.marketplaceservices.model.CustomerVendorReviewModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;


/**
 *
 */
public class CustomerVendorReviewReversePopulator implements Populator<VendorReviewData, CustomerVendorReviewModel>
{
	@Override
	public void populate(final VendorReviewData source, final CustomerVendorReviewModel target)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("source", source);
		ServicesUtil.validateParameterNotNullStandardMessage("target", target);
		target.setComment(source.getComment());
		target.setCommunication(source.getCommunication());
		target.setSatisfaction(source.getSatisfaction());
		target.setDelivery(source.getDelivery());
	}
}
