/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.strategies;

import de.hybris.platform.acceleratorservices.payment.data.OrderInfoData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

/**
 *
 *
 */
public interface PaymentTransactionStrategy
{
	PaymentTransactionEntryModel savePaymentTransactionEntry(CustomerModel customerModel, String requestId,
	                                                         OrderInfoData orderInfoData);

	void setPaymentTransactionReviewResult(PaymentTransactionEntryModel reviewDecisionEntry, String guid);
}
