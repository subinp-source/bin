/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;

import de.hybris.platform.core.model.order.CartModel;



/**
 * A strategy for clearing unwanted saved data from the cart.
 */
public interface CartCleanStrategy
{

	void cleanCart(CartModel cartModel);

}
