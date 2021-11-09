/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.interceptor.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;
import de.hybris.platform.util.localization.Localization;

import javax.annotation.Nonnull;
import javax.annotation.Resource;


public class RecurringChargeEntryValidateInterceptor implements ValidateInterceptor
{

	@Resource
	ModelService modelService;

	@Override
	public synchronized void onValidate(@Nonnull final Object model, @Nonnull final InterceptorContext ctx)
			throws InterceptorException
	{
		if (model instanceof RecurringChargeEntryModel)
		{
			final RecurringChargeEntryModel toValidate = (RecurringChargeEntryModel) model;

			final int cycleStart = toValidate.getCycleStart() == null ? Integer.MAX_VALUE : toValidate.getCycleStart();
			final int cycleEnd = (toValidate.getCycleEnd() == null || toValidate.getCycleEnd() == -1) ? Integer.MAX_VALUE
					: toValidate.getCycleEnd();

			if (cycleStart > cycleEnd)
			{
				throw new InterceptorException(Localization.getLocalizedString("subscriptionservices.customvalidation.reccuringchargeentry.period"), this);
			}
		}
	}

}
