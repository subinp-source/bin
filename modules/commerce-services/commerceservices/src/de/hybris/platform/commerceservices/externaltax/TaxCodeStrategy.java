/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.externaltax;


import de.hybris.platform.core.model.order.AbstractOrderModel;


public interface TaxCodeStrategy
{
	String getTaxCodeForCodeAndOrder(String code, AbstractOrderModel order);
}
