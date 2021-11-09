/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;


import de.hybris.platform.commerceservices.order.CommerceCartEstimateTaxesStrategy;
import de.hybris.platform.core.model.order.CartModel;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

public class DefaultCommerceCartEstimateTaxesStrategy implements CommerceCartEstimateTaxesStrategy
{
	private final static Logger LOG = Logger.getLogger(DefaultCommerceCartEstimateTaxesStrategy.class);

	@Override
	public BigDecimal estimateTaxes(final CartModel cartModel, final String deliveryZipCode, final String deliveryCountryIsocode)
	{
		LOG.warn("Default external taxes estimation being called... This will always return 0.");
		return BigDecimal.ZERO;
	}
}
