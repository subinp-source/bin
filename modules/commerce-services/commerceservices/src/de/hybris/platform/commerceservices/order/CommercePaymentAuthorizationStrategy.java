/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;


public interface CommercePaymentAuthorizationStrategy
{
	PaymentTransactionEntryModel authorizePaymentAmount(CommerceCheckoutParameter parameter);
}
