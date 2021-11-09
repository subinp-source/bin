/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.externaltax;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.externaltax.ExternalTaxDocument;


/**
 * Abstraction for strategy to make the call to a 3rd party tax calculation service.
 */
public interface CalculateExternalTaxesStrategy
{
	/**
	 * Calculate external taxes for the order.
	 *
	 * @param abstractOrder order to calculcate the taxes for
	 * @return a tax document holding the calculated tax values
	 */
	ExternalTaxDocument calculateExternalTaxes(AbstractOrderModel abstractOrder);

}
