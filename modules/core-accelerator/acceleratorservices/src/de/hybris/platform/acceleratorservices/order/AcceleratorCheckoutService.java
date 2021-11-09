/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.order;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;

/**
 * Defines checkout related accelerator services
 */
public interface AcceleratorCheckoutService
{
	/**
	 * Gets the points of service for item pickup for a cart
	 * 
	 * @param cartModel the cart
	 * @return a {@link List} of {@link PointOfServiceDistanceData}
	 */
	List<PointOfServiceDistanceData> getConsolidatedPickupOptions(CartModel cartModel);

	/**
	 * Runs calculation again and updates cart
	 * 
	 * @param cartModel the cart
	 * @param consolidatedPickupPointModel the point of service
	 * @return any unsuccessful modifications that made to the cart (i.e. due to very, very recent stock changes)
	 * @throws CommerceCartModificationException when the cart could not be modified
	 */
	List<CommerceCartModification> consolidateCheckoutCart(CartModel cartModel, PointOfServiceModel consolidatedPickupPointModel)
			throws CommerceCartModificationException;
}
