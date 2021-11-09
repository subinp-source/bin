/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.interceptor;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.marketplaceservices.strategies.VendorOrderTotalPriceCalculationStrategy;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;


/**
 * Assign total price to consignment
 */

public class ConsignmentPrepareInterceptor implements PrepareInterceptor<ConsignmentModel>
{
	private VendorOrderTotalPriceCalculationStrategy vendorOrderTotalPriceCalculationStrategy;

	@Override
	public void onPrepare(final ConsignmentModel consignment, final InterceptorContext paramInterceptorContext)
			throws InterceptorException
	{
		consignment.setTotalPrice(getVendorOrderTotalPriceCalculationStrategy().calculateTotalPrice(consignment));
	}

	protected VendorOrderTotalPriceCalculationStrategy getVendorOrderTotalPriceCalculationStrategy()
	{
		return vendorOrderTotalPriceCalculationStrategy;
	}

	@Required
	public void setVendorOrderTotalPriceCalculationStrategy(
			final VendorOrderTotalPriceCalculationStrategy vendorOrderTotalPriceCalculationStrategy)
	{
		this.vendorOrderTotalPriceCalculationStrategy = vendorOrderTotalPriceCalculationStrategy;
	}
}
