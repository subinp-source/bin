/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.impl;

import de.hybris.platform.commerceservices.strategies.GenerateMerchantTransactionCodeStrategy;
import de.hybris.platform.core.model.order.CartModel;

import java.util.UUID;


public class DefaultGenerateMerchantTransactionCodeStrategy implements GenerateMerchantTransactionCodeStrategy
{
	@Override
	public String generateCode(final CartModel cartModel)
	{
		return cartModel.getUser().getUid() + "-" + UUID.randomUUID();
	}

}
