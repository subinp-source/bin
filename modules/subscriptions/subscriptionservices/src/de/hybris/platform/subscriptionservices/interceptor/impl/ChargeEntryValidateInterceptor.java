/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.interceptor.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.subscriptionservices.model.ChargeEntryModel;
import de.hybris.platform.util.localization.Localization;

import javax.annotation.Nonnull;


/**
 * Interceptor to validate ChargeEntryModel.
 * <ul>
 * <li>the {@link ChargeEntryModel}'s parent objects are marked as modified
 * <li>the price of the {@link ChargeEntryModel} is not negative
 * </ul>
 */
public class ChargeEntryValidateInterceptor extends AbstractParentChildValidateInterceptor
{
	@Override
	protected void doValidate(@Nonnull final Object model, @Nonnull final InterceptorContext ctx)
			throws InterceptorException
	{
		if (model instanceof ChargeEntryModel)
		{
			final ChargeEntryModel chargeEntry = (ChargeEntryModel) model;
			if (chargeEntry.getPrice() < 0.0D)
			{
				throw new InterceptorException(Localization
						.getLocalizedString("subscriptionservices.customvalidation.chargeentry.nonnegativeprice"));
			}
		}
	}

}
