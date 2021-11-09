/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.order.InvalidCartException;


public interface CommercePlaceOrderStrategy
{

	/**
	 * @param parameter
	 * @return the order that has been created
	 * @throws InvalidCartException
	 */
	CommerceOrderResult placeOrder(CommerceCheckoutParameter parameter) throws InvalidCartException;
}
