/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.interceptor.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.subscriptionservices.model.SubscriptionModel;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Nonnull;


public class SubscriptionIdPrepareInterceptor implements PrepareInterceptor
{

	private KeyGenerator subscriptionIDGenerator;

	@Override
	public void onPrepare(@Nonnull final Object model, @Nonnull final InterceptorContext ctx)
			throws InterceptorException
	{

		if (model instanceof SubscriptionModel)
		{
			final SubscriptionModel subscriptionModel = (SubscriptionModel) model;
			final String id = subscriptionModel.getId();
			if (StringUtils.isEmpty(id))
			{
				subscriptionModel.setId(this.subscriptionIDGenerator.generate().toString());
			}
		}
	}

	@Required
	public void setSubscriptionIDGenerator(final KeyGenerator subscriptionIDGenerator)
	{
		this.subscriptionIDGenerator = subscriptionIDGenerator;
	}
}
