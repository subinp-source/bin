/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.externaltax;

import de.hybris.platform.core.model.order.AbstractOrderModel;


/**
 * Abstraction for service to decide whether the external taxes of an order need recalculation
 * 
 */
public interface RecalculateExternalTaxesStrategy
{
	String SESSION_ATTIR_ORDER_RECALCULATION_HASH = "orderRecalculationHash";

	boolean recalculate(AbstractOrderModel abstractOrderModel);
}
