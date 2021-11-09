/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.interceptor.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.subscriptionservices.model.OverageUsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;
import de.hybris.platform.util.localization.Localization;

import javax.annotation.Nonnull;
import java.util.Locale;


/**
 * Interceptor to validate that there is only one {@link OverageUsageChargeEntryModel} assigned to a
 * {@link UsageChargeModel}.
 */
public class OverageUsageChargeEntryValidateInterceptor extends AbstractParentChildValidateInterceptor
{

	@Override
	public void doValidate(@Nonnull final Object model, @Nonnull final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof OverageUsageChargeEntryModel)
		{

			final OverageUsageChargeEntryModel overageUsageChargeEntry = (OverageUsageChargeEntryModel) model;
			final UsageChargeModel usageCharge = overageUsageChargeEntry.getUsageCharge();

			if (usageCharge == null)
			{
				return;
			}

			for (final UsageChargeEntryModel entry : usageCharge.getUsageChargeEntries())
			{
				if (entry == null || entry.equals(overageUsageChargeEntry))
				{
					continue;
				}
				if (entry instanceof OverageUsageChargeEntryModel)
				{
					final Object[] args = {usageCharge.getName(Locale.ENGLISH)};

					throw new InterceptorException(
							Localization.getLocalizedString("subscriptionservices.customvalidation.chargeentry.overageusage", args)
					);
				}
			}
		}
	}

}
