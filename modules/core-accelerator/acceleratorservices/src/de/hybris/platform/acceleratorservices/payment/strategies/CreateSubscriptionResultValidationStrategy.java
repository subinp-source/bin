/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.strategies;

import de.hybris.platform.acceleratorservices.payment.data.CreateSubscriptionResult;
import de.hybris.platform.acceleratorservices.payment.data.PaymentErrorField;

import java.util.Map;

/**
 *
 *
 */
public interface CreateSubscriptionResultValidationStrategy
{
	Map<String, PaymentErrorField> validateCreateSubscriptionResult(Map<String, PaymentErrorField> errors,
	                                                                CreateSubscriptionResult response);
}
