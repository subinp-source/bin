/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.externaltax.impl;

import de.hybris.platform.commerceservices.externaltax.CalculateExternalTaxesStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.externaltax.ExternalTaxDocument;

/**
 * An default implementation for fallback strategy to CalculateExternalTaxesStrategy
 */
public class DefaultCalculateExternalTaxesFallbackStrategy implements CalculateExternalTaxesStrategy
{

	@Override
	public ExternalTaxDocument calculateExternalTaxes(final AbstractOrderModel abstractOrder)
	{
		return new ExternalTaxDocument();
	}
}
