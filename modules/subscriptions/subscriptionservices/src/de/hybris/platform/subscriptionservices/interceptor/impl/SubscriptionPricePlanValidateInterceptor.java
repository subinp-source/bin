/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.interceptor.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import javax.annotation.Nonnull;


/**
 * Interceptor to validate SubscriptionPricePlanModel.
 * <ul>
 * <li>the {@link SubscriptionPricePlanModel}'s parent objects are marked as modified
 * </ul>
 */
public class SubscriptionPricePlanValidateInterceptor extends AbstractParentChildValidateInterceptor
{

	@Override
	public void doValidate(@Nonnull final Object model, @Nonnull final InterceptorContext ctx) throws InterceptorException
	{
		// nothing is validated
	}

}
