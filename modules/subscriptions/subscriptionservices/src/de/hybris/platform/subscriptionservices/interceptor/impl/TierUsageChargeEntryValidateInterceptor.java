/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.interceptor.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.subscriptionservices.model.TierUsageChargeEntryModel;
import de.hybris.platform.util.localization.Localization;

import javax.annotation.Nonnull;


/**
 * Interceptor to validate that the tier start of a {@link TierUsageChargeEntryModel} instance is less that or equal to
 * its tier end.
 */
public class TierUsageChargeEntryValidateInterceptor implements ValidateInterceptor
{

	@Override
	public void onValidate(@Nonnull final Object model, @Nonnull final InterceptorContext ctx)
			throws InterceptorException
	{
		if (model instanceof TierUsageChargeEntryModel)
		{
			final TierUsageChargeEntryModel tierUsageChargeEntry = (TierUsageChargeEntryModel) model;

			if (tierUsageChargeEntry.getTierStart() > tierUsageChargeEntry.getTierEnd())
			{
				throw new InterceptorException(Localization.getLocalizedString("subscriptionservices.customvalidation.tierusagechargeentry.period"));
			}
		}
	}
}
