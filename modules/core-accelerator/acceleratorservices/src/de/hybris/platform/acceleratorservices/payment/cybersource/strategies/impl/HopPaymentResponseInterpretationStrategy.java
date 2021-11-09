/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.cybersource.strategies.impl;

import de.hybris.platform.acceleratorservices.payment.data.CreateSubscriptionResult;
import de.hybris.platform.acceleratorservices.payment.data.PaymentErrorField;
import de.hybris.platform.acceleratorservices.payment.strategies.impl.AbstractPaymentResponseInterpretationStrategy;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

/**
 * Hosted order page impl of payment response.
 */

public class HopPaymentResponseInterpretationStrategy extends AbstractPaymentResponseInterpretationStrategy
{
	private Converter<Map<String, String>, CreateSubscriptionResult> createSubscriptionResultConverter;

	@Override
	public CreateSubscriptionResult interpretResponse(final Map<String, String> responseParams, final String clientRef, final Map<String, PaymentErrorField> errors)
	{
		parseMissingFields(responseParams, errors);
		return getCreateSubscriptionResultConverter().convert(responseParams);
	}

	protected Converter<Map<String, String>, CreateSubscriptionResult> getCreateSubscriptionResultConverter()
	{
		return createSubscriptionResultConverter;
	}

	@Required
	public void setCreateSubscriptionResultConverter(
			final Converter<Map<String, String>, CreateSubscriptionResult> createSubscriptionResultConverter)
	{
		this.createSubscriptionResultConverter = createSubscriptionResultConverter;
	}
}
