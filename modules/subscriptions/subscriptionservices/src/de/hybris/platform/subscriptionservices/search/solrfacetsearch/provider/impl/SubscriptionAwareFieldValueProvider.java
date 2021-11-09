/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.subscriptionservices.subscription.SubscriptionProductService;
import org.springframework.beans.factory.annotation.Required;


/**
 * Extension of {@link AbstractPropertyFieldValueProvider} with {@link SubscriptionProductService}.
 */
public abstract class SubscriptionAwareFieldValueProvider extends AbstractPropertyFieldValueProvider
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
