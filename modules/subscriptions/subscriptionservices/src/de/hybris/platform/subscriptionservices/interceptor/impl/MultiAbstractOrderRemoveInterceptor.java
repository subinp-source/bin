/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.interceptor.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

import javax.annotation.Nonnull;

import org.apache.commons.collections.CollectionUtils;


/**
 * If a master {@link AbstractOrderModel} is deleted all dependent child {@link AbstractOrderModel}s are also removed.
 */
public class MultiAbstractOrderRemoveInterceptor implements RemoveInterceptor
{

	@Override
	public void onRemove(@Nonnull final Object model, @Nonnull final InterceptorContext ctx)
			throws InterceptorException
	{
		if (model instanceof AbstractOrderModel)
		{
			final AbstractOrderModel order = (AbstractOrderModel) model;

			if (CollectionUtils.isNotEmpty(order.getChildren()))
			{
				for (final AbstractOrderModel childOrder : order.getChildren())
				{
					ctx.getModelService().remove(childOrder);
				}
			}
		}
	}

}
