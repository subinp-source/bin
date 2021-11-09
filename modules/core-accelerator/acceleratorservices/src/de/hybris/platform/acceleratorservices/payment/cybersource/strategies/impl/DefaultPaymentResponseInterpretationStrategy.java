/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.cybersource.strategies.impl;

import de.hybris.platform.acceleratorservices.payment.data.CreateSubscriptionResult;
import de.hybris.platform.acceleratorservices.payment.data.PaymentErrorField;
import de.hybris.platform.acceleratorservices.payment.strategies.impl.AbstractPaymentResponseInterpretationStrategy;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;


/**
 * Default impl of PaymentResponseInterpretationStrategy.
 */
public class DefaultPaymentResponseInterpretationStrategy extends AbstractPaymentResponseInterpretationStrategy
{
	private Converter<Map<String, String>, CreateSubscriptionResult> createSubscriptionResultConverter;

	@Override
	public CreateSubscriptionResult interpretResponse(final Map<String, String> params, final String clientRef,
			final Map<String, PaymentErrorField> errors)
	{
		parseMissingFields(params, errors);
		return getCreateSubscriptionResultConverter().convert(params);
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
