/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.externaltax.impl;

import de.hybris.platform.commerceservices.externaltax.DecideExternalTaxesStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;



/**
 * Base {@link DecideExternalTaxesStrategy} implementation, gives decision to call external taxes functionality.
 */
public class DefaultDecideExternalTaxesStrategy implements DecideExternalTaxesStrategy
{

	/**
	 * Return false to be overridden in specific application
	 */
	@Override
	public boolean shouldCalculateExternalTaxes(final AbstractOrderModel abstractOrder)
	{
		if (abstractOrder == null)
		{
			throw new IllegalStateException("Order is null. Cannot apply external tax to it.");
		}

		return false;
	}
}
