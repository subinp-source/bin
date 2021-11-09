/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.process.strategies.impl;

import java.util.Optional;

import de.hybris.platform.acceleratorservices.process.strategies.impl.AbstractOrderProcessContextStrategy;
import de.hybris.platform.b2bacceleratorservices.model.process.ReplenishmentProcessModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;


/**
 * Strategy to impersonate site and initialize session context from an instance of ReplenishmentProcessModel.
 */
public class B2BAcceleratorProcessContextStrategy extends AbstractOrderProcessContextStrategy
{
	@Override
	protected Optional<CartModel> getOrderModel(final BusinessProcessModel businessProcessModel)
	{
		return Optional.of(businessProcessModel).filter(businessProcess -> businessProcess instanceof ReplenishmentProcessModel)
				.map(businessProcess -> ((ReplenishmentProcessModel) businessProcess).getCartToOrderCronJob())
				.map(CartToOrderCronJobModel::getCart);
	}
}
