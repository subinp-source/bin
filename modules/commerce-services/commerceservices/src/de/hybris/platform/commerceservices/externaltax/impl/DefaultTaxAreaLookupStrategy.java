/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.externaltax.impl;


import de.hybris.platform.commerceservices.externaltax.TaxAreaLookupStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;


public class DefaultTaxAreaLookupStrategy implements TaxAreaLookupStrategy
{
	@Override
	public String getTaxAreaForOrder(final AbstractOrderModel orderModel)
	{
		if (orderModel == null || orderModel.getDeliveryAddress() == null)
		{
			throw new IllegalArgumentException("Can not determine taxArea for order without delivery address");
		}
		return orderModel.getDeliveryAddress().getCountry().getIsocode();
	}
}
