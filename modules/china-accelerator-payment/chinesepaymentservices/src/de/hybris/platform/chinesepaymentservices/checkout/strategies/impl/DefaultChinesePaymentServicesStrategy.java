/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentservices.checkout.strategies.impl;

import de.hybris.platform.chinesepaymentservices.checkout.strategies.ChinesePaymentServicesStrategy;
import de.hybris.platform.chinesepaymentservices.exceptions.InvalidPspException;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Required;


public class DefaultChinesePaymentServicesStrategy implements ChinesePaymentServicesStrategy
{

	private Map<String, ChinesePaymentService> chinesePaymentServicesMap;

	@Override
	public ChinesePaymentService getPaymentService(final String paymentService)
	{
		if (Objects.nonNull(getChinesePaymentServicesMap().get(paymentService)))
		{
			return getChinesePaymentServicesMap().get(paymentService);
		}
		else
		{
			throw new InvalidPspException(paymentService);
		}
	}


	protected Map<String, ChinesePaymentService> getChinesePaymentServicesMap()
	{
		return chinesePaymentServicesMap;
	}

	@Required
	public void setChinesePaymentServicesMap(final Map<String, ChinesePaymentService> chinesePaymentServicesMap)
	{
		this.chinesePaymentServicesMap = chinesePaymentServicesMap;
	}

}
