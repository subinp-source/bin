/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.strategies.impl;


import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.commerceservices.strategies.impl.DefaultCartValidationStrategy;
import de.hybris.platform.core.model.order.CartModel;


public class DefaultB2BCartValidationStrategy extends DefaultCartValidationStrategy
{
	@Override
	protected void validateDelivery(final CartModel cartModel)
	{
		final CheckoutPaymentType paymentType = cartModel.getPaymentType();

		if (paymentType == null || CheckoutPaymentType.CARD.equals(paymentType))
		{
			super.validateDelivery(cartModel);
		}
	}
}
