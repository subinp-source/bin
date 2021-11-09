/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.model;


import de.hybris.platform.core.model.product.ProductModel;

/**
 *
 * AttributeHandler for dynamic attribute ChargeEntry.billingTime.
 *
 */
public class BillingTimeChargeEntryAttribute extends SubscriptionAwareAttributeHandler<BillingTimeModel, ChargeEntryModel>
{
	@Override
	public BillingTimeModel get(final ChargeEntryModel model)
	{
		SubscriptionPricePlanModel subscriptionPricePlanModel = null;
		if (model instanceof OneTimeChargeEntryModel)
		{
			subscriptionPricePlanModel = ((OneTimeChargeEntryModel) model).getSubscriptionPricePlanOneTime();
		}
		else if (model instanceof RecurringChargeEntryModel)
		{
			subscriptionPricePlanModel = ((RecurringChargeEntryModel) model).getSubscriptionPricePlanRecurring();
		}
		else if (model instanceof UsageChargeEntryModel)
		{
			final UsageChargeModel usageChargeModel = ((UsageChargeEntryModel) model).getUsageCharge();
			if (usageChargeModel != null)
			{
				subscriptionPricePlanModel = usageChargeModel.getSubscriptionPricePlanUsage();
			}
		}

		if (subscriptionPricePlanModel != null
				&& getSubscriptionProductService().isSubscription(subscriptionPricePlanModel.getProduct()))
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
	public void set(final ChargeEntryModel model, final BillingTimeModel value)
	{
		super.set(model, value);
	}

}
