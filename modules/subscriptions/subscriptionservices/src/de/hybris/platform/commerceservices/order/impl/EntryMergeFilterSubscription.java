/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.EntryMergeFilter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.subscriptionservices.subscription.SubscriptionProductService;

import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Nonnull;


/**
 * Denies merging of entries with subscription products.
 * <p>Quantity of subscription entries should always be 1, so any merge between subscription entries should be disabled.</p>
 */
public class EntryMergeFilterSubscription implements EntryMergeFilter
{
	private SubscriptionProductService subscriptionProductService;

	@Override
	public Boolean apply(@Nonnull final AbstractOrderEntryModel candidate, @Nonnull final AbstractOrderEntryModel target)
	{
		return Boolean.valueOf(
				!getSubscriptionProductService().isSubscription(candidate.getProduct())
				&& !getSubscriptionProductService().isSubscription(target.getProduct())
		);
	}

	protected SubscriptionProductService getSubscriptionProductService()
	{
		return subscriptionProductService;
	}

	@Required
	public void setSubscriptionProductService(final SubscriptionProductService subscriptionProductService)
	{
		this.subscriptionProductService = subscriptionProductService;
	}
}
