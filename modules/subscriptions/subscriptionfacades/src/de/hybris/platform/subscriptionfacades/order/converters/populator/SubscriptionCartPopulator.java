/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionfacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Nonnull;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Subscription cart populator.
 */
public class SubscriptionCartPopulator extends AbstractSubscriptionOrderPopulator<CartModel, CartData>
{
	// Concrete implementation of the SubscriptionCartPopulator that should be used for further customizations

	@Override
	public void populate(@Nonnull final CartModel source,
	                     @Nonnull final CartData target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", source);

		if (source.getBillingTime() == null)
		{
			// compatibility mode: do not perform the subscription specific populator tasks
			return;
		}

		super.populate(source, target);
		target.setTotalUnitCount(calcTotalUnitCount(source));
	}

}
