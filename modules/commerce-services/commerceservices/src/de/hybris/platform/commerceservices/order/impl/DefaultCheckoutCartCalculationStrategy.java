/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;


/**
 * A default strategy for commerce cart calculation with external tax calculation.
 *
 * @deprecated Since 6.0. Use DefaultCommerceCartCalculationStrategy with calculateExternalTax to true instead
 */
@Deprecated(since = "6.0", forRemoval = true)
public class DefaultCheckoutCartCalculationStrategy extends DefaultCommerceCartCalculationStrategy
{

	@Override
	public boolean calculateCart(final CommerceCartParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();
		final boolean calculated = super.calculateCart(parameter);
		getExternalTaxesService().calculateExternalTaxes(cartModel);
		return calculated;
	}



}
