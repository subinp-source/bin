/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentservices.checkout.strategies;

import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;


/**
 * The Strategy of ChinesePaymentServices
 */
public interface ChinesePaymentServicesStrategy
{
	/**
	 * Getting the PaymentService
	 *
	 * @param paymentService
	 *           the id of the paymentService
	 * @return ChinesePaymentService
	 */
	ChinesePaymentService getPaymentService(String paymentService);
}
