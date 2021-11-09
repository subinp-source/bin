/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.externaltax;

import de.hybris.platform.core.model.order.AbstractOrderModel;


/**
 * A strategy to look up a tax area for an order
 * 
 */
public interface TaxAreaLookupStrategy
{
	String getTaxAreaForOrder(final AbstractOrderModel orderModel);
}
