/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.externaltax;

import de.hybris.platform.core.model.order.AbstractOrderModel;


/**
 * Abstraction for service to calculate 3rd party taxes
 */
public interface ExternalTaxesService
{
	/**
	 * Calculate the taxes for order via an external service
	 * @param abstractOrder A Hybris cart or order
	 * @return True if calculation was successful and false otherwise
	 */
	boolean calculateExternalTaxes(final AbstractOrderModel abstractOrder);

	/**
	 * Removes tax document from session if present
	 */
	void clearSessionTaxDocument();
}
