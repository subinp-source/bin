/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.bundle.impl;

import de.hybris.platform.configurablebundleservices.bundle.RemoveableChecker;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.subscriptionservices.subscription.SubscriptionCommerceCartService;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Default Implementation for {@link RemoveableChecker}
 */
public class BundleOrderEntryRemoveableChecker extends BundleCommerceOrderEntryRemoveableChecker
{
	private SubscriptionCommerceCartService subscriptionCommerceCartService;

	@Override
	public boolean canRemove(@Nonnull final CartEntryModel cartEntry)
	{
		validateParameterNotNullStandardMessage("cartEntry", cartEntry);

		final CartModel masterCart = getSubscriptionCommerceCartService().getMasterCartForCartEntry(cartEntry);
		return cartEntry.getOrder().equals(masterCart) && super.canRemove(cartEntry);
	}

	protected SubscriptionCommerceCartService getSubscriptionCommerceCartService()
	{
		return subscriptionCommerceCartService;
	}

	@Required
	public void setSubscriptionCommerceCartService(final SubscriptionCommerceCartService subscriptionCommerceCartService)
	{
		this.subscriptionCommerceCartService = subscriptionCommerceCartService;
	}

}
