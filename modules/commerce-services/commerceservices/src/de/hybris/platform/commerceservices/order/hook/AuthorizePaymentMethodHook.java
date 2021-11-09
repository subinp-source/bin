/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.hook;

import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

/**
 * Hook interface for Payment Authroization
 */
public interface AuthorizePaymentMethodHook
{
	/**
	 * Executed before authorizing payment
	 *
	 * @param  parameter object containing all the information for checkout
	 */
	void beforeAuthorizePaymentAmount(CommerceCheckoutParameter parameter);

	/**
	 * Executed after authorizing payment
	 *
	 * @param parameter object containing all the information for checkout
	 * @param paymentTransactionEntryModel object containing all information for payment transaction
	 */
	void afterAuthorizePaymentAmount(CommerceCheckoutParameter parameter, PaymentTransactionEntryModel paymentTransactionEntryModel);
}
