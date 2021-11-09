/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators;

import de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators.response.AbstractResultPopulator;
import de.hybris.platform.acceleratorservices.payment.data.CreateSubscriptionRequest;
import de.hybris.platform.acceleratorservices.payment.data.PaymentData;

import org.springframework.util.Assert;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


public class PaymentDataPopulator extends AbstractResultPopulator<CreateSubscriptionRequest, PaymentData>
{
	@Override
	public void populate(final CreateSubscriptionRequest source, final PaymentData target)
	{
		//Validate parameters and related data
		validateParameterNotNull(source, "Parameter source (CreateSubscriptionRequest) cannot be null");
		validateParameterNotNull(target, "Parameter target (PaymentData) cannot be null");
		Assert.isInstanceOf(CreateSubscriptionRequest.class, source);
		Assert.notNull(source.getCustomerBillToData(), "customerBillToData cannot be null");
		Assert.notNull(source.getCustomerShipToData(), "customerShipToData cannot be null");
		Assert.notNull(source.getOrderInfoData(), "orderInfoData cannot be null");
		Assert.notNull(source.getPaymentInfoData(), "paymentInfoData cannot be null");
		Assert.notNull(source.getSignatureData(), "signatureData cannot be null");
		Assert.notNull(source.getSubscriptionSignatureData(), "subscriptionSignatureData cannot be null");

		target.setPostUrl(source.getRequestUrl());
	}
}
