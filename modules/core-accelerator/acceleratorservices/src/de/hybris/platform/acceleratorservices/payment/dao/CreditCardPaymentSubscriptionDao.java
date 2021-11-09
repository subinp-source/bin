/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.dao;


import de.hybris.platform.acceleratorservices.model.payment.CCPaySubValidationModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

public interface CreditCardPaymentSubscriptionDao extends Dao
{
	CCPaySubValidationModel findSubscriptionValidationBySubscription(String subscriptionId);

	CreditCardPaymentInfoModel findCreditCartPaymentBySubscription(String subscriptionId);
}
