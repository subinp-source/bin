/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;


import de.hybris.platform.core.model.order.CartModel;

import java.math.BigDecimal;

/**
 * Strategy to abstract the estimation of taxes on a cart.
 */
public interface CommerceCartEstimateTaxesStrategy
{
	/**
	 * Estimate taxes for the cartModel and using the deliveryZipCode as the delivery zip code.
	 *
	 * @param cartModel              cart to estimate taxes for
	 * @param deliveryZipCode        zip code to use as the delivery address
	 * @param deliveryCountryIsocode country to use for the delivery address
	 * @return total of the estimated taxes
	 */
	BigDecimal estimateTaxes(CartModel cartModel, String deliveryZipCode, String deliveryCountryIsocode);
}
