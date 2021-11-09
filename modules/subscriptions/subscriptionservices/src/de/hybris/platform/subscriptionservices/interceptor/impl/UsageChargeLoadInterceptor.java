/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.interceptor.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.LoadInterceptor;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;
import de.hybris.platform.subscriptionservices.model.impl.UsageChargeEntryModelSortService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Nonnull;


/**
 * {@link LoadInterceptor} implementation for instances of {@link UsageChargeModel}. Ensures that:<br>
 * <ul>
 * <li>tiered usage charge entries are sorted by tier start (ascending)</li>
 * <li>the overage usage charge entry is always the last entry</li>
 * </ul>
 */
public class UsageChargeLoadInterceptor implements LoadInterceptor
{

	@Autowired
	@Qualifier("usageChargeEntryModelSortService")
	private UsageChargeEntryModelSortService sortService;

	@Override
	public void onLoad(@Nonnull final Object model, @Nonnull final InterceptorContext ctx)
			throws InterceptorException
	{
		if (model instanceof UsageChargeModel)
		{
			final UsageChargeModel usageCharge = (UsageChargeModel) model;

			usageCharge.setUsageChargeEntries(sortService.sort(usageCharge.getUsageChargeEntries()));
		}
	}


}
