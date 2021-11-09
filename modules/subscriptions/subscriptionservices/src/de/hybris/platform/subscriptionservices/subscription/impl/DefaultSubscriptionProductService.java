/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.subscription.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.subscriptionservices.subscription.SubscriptionProductService;

import javax.annotation.Nonnull;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link SubscriptionProductService}.
 */
public class DefaultSubscriptionProductService implements SubscriptionProductService
{
	@Override
	public boolean isSubscription(@Nonnull final ProductModel product)
	{
		validateParameterNotNull(product, "Product must not be null");
		return product.getSubscriptionTerm() != null;
	}
}
