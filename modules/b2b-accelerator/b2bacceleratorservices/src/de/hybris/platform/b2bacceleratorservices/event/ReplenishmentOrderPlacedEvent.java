/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;


public class ReplenishmentOrderPlacedEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{
	private final CartToOrderCronJobModel cartToOrder;

	public ReplenishmentOrderPlacedEvent(final CartToOrderCronJobModel cartToOrder)
	{
		this.cartToOrder = cartToOrder;
	}

	public CartToOrderCronJobModel getCartToOrderCronJob()
	{
		return cartToOrder;
	}
}
