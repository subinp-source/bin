/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.order;

import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.InvoicePaymentInfoModel;


/**
 * Interface to handle specific B2B Commerce cart services.
 */
public interface B2BCommerceCartService extends CommerceCartService
{
	/**
	 * Forcefully re-calulcate the order total after applying the promotions for payment type
	 * 
	 * @param cartModel
	 *           the cart whose total has to be re-calculated
	 */
	void calculateCartForPaymentTypeChange(CartModel cartModel);

	/**
	 * Creates an invoice payment info.
	 *
	 * @param cartModel
	 *           the cart whose payment info is applied to
	 *
	 * @return the invoice payment info created
	 */
	InvoicePaymentInfoModel createInvoicePaymentInfo(CartModel cartModel);
}
