/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.hook;

import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.order.InvalidCartException;


/**
 * Hook interface for PlaceOrder
 */
public interface CommercePlaceOrderMethodHook
{
	/**
	 * Executed after the place order
	 * 
	 * @param parameter
	 *           object containing all the information for checkout
	 * @param orderModel
	 *           object containing the order model
	 */
	void afterPlaceOrder(CommerceCheckoutParameter parameter, CommerceOrderResult orderModel) throws InvalidCartException;

	/**
	 * Executed before the place order
	 * 
	 * @param parameter
	 *           object containing all the information for checkout
	 */
	void beforePlaceOrder(CommerceCheckoutParameter parameter) throws InvalidCartException;

	/**
	 * Executed before the submit order
	 * 
	 * @param parameter
	 *           object containing all the information for checkout
	 * @param result
	 *           object containing the order model
	 */
	void beforeSubmitOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException;
}
