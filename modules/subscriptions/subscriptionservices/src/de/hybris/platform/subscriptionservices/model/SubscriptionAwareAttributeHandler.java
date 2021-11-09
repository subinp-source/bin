/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.model;

import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import de.hybris.platform.subscriptionservices.subscription.SubscriptionProductService;
import org.springframework.beans.factory.annotation.Required;


/**
 * Specification of {@link AbstractDynamicAttributeHandler}, providing {@link SubscriptionProductService}.
 * @param <VALUE> value type
 * @param <MODEL> model type
 */
public abstract class SubscriptionAwareAttributeHandler<VALUE, MODEL extends AbstractItemModel>
		extends AbstractDynamicAttributeHandler<VALUE, MODEL>
{
	private SubscriptionProductService subscriptionProductService;

	/**
	 * @return subscription product service
	 */
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
