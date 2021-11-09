/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionfacades.order.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Nonnull;


/**
 * Subscription order populator.
 */
public class SubscriptionOrderPopulator extends AbstractSubscriptionOrderPopulator<OrderModel, OrderData>
{
	// Concrete implementation of the SubscriptionOrderPopulator that should be used for further customizations

	@Override
	public void populate(@Nonnull final OrderModel source,
	                     @Nonnull final OrderData target) throws ConversionException
	{

		validateParameterNotNullStandardMessage("source", source);

		if (source.getBillingTime() == null)
		{
			// compatibility mode: do not perform the subscription specific populator tasks
			return;
		}

		super.populate(source, target);
	}

}
