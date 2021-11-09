/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.model;


import de.hybris.platform.core.model.product.ProductModel;

/**
 *
 * AttributeHandler for dynamic attribute UsageCharge.billingFrequency.
 *
 */
public class BillingTimeUsageChargeAttribute extends SubscriptionAwareAttributeHandler<BillingTimeModel, UsageChargeModel>
{
	@Override
	public BillingTimeModel get(final UsageChargeModel model)
	{
		final SubscriptionPricePlanModel subscriptionPricePlanModel = model.getSubscriptionPricePlanUsage();

		if (subscriptionPricePlanModel != null &&
				getSubscriptionProductService().isSubscription(subscriptionPricePlanModel.getProduct()))
		{
			final ProductModel subscriptionProduct = subscriptionPricePlanModel.getProduct();

			if (subscriptionProduct.getSubscriptionTerm() != null
					&& subscriptionProduct.getSubscriptionTerm().getBillingPlan() != null)
			{
				return subscriptionProduct.getSubscriptionTerm().getBillingPlan().getBillingFrequency();
			}
		}

		return null;
	}

	@Override
	public void set(final UsageChargeModel model, final BillingTimeModel value)
	{
		super.set(model, value);
	}

}
