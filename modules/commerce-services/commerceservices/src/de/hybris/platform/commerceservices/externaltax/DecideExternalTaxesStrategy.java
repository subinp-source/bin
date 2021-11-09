/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.externaltax;

import de.hybris.platform.core.model.order.AbstractOrderModel;


/**
 * Abstraction for strategy determining whether or not to make the call to an external tax service.
 * 
 */
public interface DecideExternalTaxesStrategy
{
	boolean shouldCalculateExternalTaxes(AbstractOrderModel abstractOrder);
}
