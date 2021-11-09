/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.model;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;


/**
 * 
 * AttributeHandler for dynamic attribute UsageCharge.currency.
 * 
 */
public class CurrencyUsageChargeAttribute extends AbstractDynamicAttributeHandler<CurrencyModel, UsageChargeModel>
{
	@Override
	public CurrencyModel get(final UsageChargeModel model)
	{
		final SubscriptionPricePlanModel subscriptionPriceRowModel = model.getSubscriptionPricePlanUsage();

		if (subscriptionPriceRowModel != null)
		{
			return subscriptionPriceRowModel.getCurrency();
		}

		return null;
	}

	@Override
	public void set(final UsageChargeModel model, final CurrencyModel value)
	{
		super.set(model, value);
	}

}
