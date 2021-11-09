/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.strategies.impl;

import de.hybris.platform.acceleratorservices.payment.strategies.PaymentResponseInterpretationStrategy;
import de.hybris.platform.acceleratorservices.payment.data.PaymentErrorField;

import java.util.Map;

/**
 *
 */
public abstract class AbstractPaymentResponseInterpretationStrategy implements PaymentResponseInterpretationStrategy
{
	protected void parseMissingFields(final Map<String, String> parameters, final Map<String, PaymentErrorField> errors)
	{
		for (final Map.Entry<String, String> paramEntry : parameters.entrySet())
		{
			if (paramEntry.getKey().startsWith("MissingField"))
			{
				getOrCreatePaymentErrorField(errors, paramEntry.getValue()).setMissing(true);
			}
			if (paramEntry.getKey().startsWith("InvalidField"))
			{
				getOrCreatePaymentErrorField(errors, paramEntry.getValue()).setInvalid(true);
			}
		}
	}

	protected PaymentErrorField getOrCreatePaymentErrorField(final Map<String, PaymentErrorField> errors, final String fieldName)
	{
		if (errors.containsKey(fieldName))
		{
			return errors.get(fieldName);
		}
		final PaymentErrorField result = new PaymentErrorField();
		result.setName(fieldName);
		errors.put(fieldName, result);
		return result;
	}
}
