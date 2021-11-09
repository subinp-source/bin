/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators.request;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.acceleratorservices.payment.data.CreateSubscriptionRequest;
import de.hybris.platform.acceleratorservices.payment.data.OrderPageAppearanceData;
import de.hybris.platform.acceleratorservices.payment.data.PaymentData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * 
 */
public class OrderPageAppearanceRequestPopulator extends AbstractRequestPopulator<CreateSubscriptionRequest, PaymentData>
{
	@Override
	public void populate(final CreateSubscriptionRequest source, final PaymentData target) throws ConversionException
	{
		validateParameterNotNull(source, "Parameter [CreateSubscriptionRequest] source cannot be null");
		validateParameterNotNull(target, "Parameter [PaymentData] target cannot be null");

		final OrderPageAppearanceData appearanceData = source.getOrderPageAppearanceData();

		//This is optional data so just return.
		if (appearanceData == null)
		{
			return;
		}

		addRequestQueryParam(target, "orderPage_backgroundImageURL", appearanceData.getOrderPageBackgroundImageURL());
		addRequestQueryParam(target, "orderPage_barColor", appearanceData.getOrderPageBarColor());
		addRequestQueryParam(target, "orderPage_barTextColor", appearanceData.getOrderPageBarTextColor());
		addRequestQueryParam(target, "orderPage_buyButtonText", appearanceData.getOrderPageBuyButtonText());
		addRequestQueryParam(target, "orderPage_colorScheme", appearanceData.getOrderPageColorScheme());
		addRequestQueryParam(target, "orderPage_merchantDisplayName", appearanceData.getOrderPageMerchantDisplayName());
		addRequestQueryParam(target, "orderPage_messageBoxBackgroundColor", appearanceData.getOrderPageMessageBoxBackgroundColor());
		addRequestQueryParam(target, "orderPage_requiredFieldColor", appearanceData.getOrderPageRequiredFieldColor());

	}
}
