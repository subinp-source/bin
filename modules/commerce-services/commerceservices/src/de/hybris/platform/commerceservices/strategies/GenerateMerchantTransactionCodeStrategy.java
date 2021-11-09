/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;

import de.hybris.platform.core.model.order.CartModel;


public interface GenerateMerchantTransactionCodeStrategy
{
	/**
	 * Generates a unique id for a {@link de.hybris.platform.payment.model.PaymentTransactionModel}
	 * @param cartModel A cart
	 * @return A unique identifier
	 */
	String generateCode(CartModel cartModel);
}
