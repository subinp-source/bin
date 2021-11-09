/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorwebservicesaddon.payment.service.impl;


import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.acceleratorwebservicesaddon.payment.dao.PaymentSubscriptionResultDao;
import de.hybris.platform.acceleratorwebservicesaddon.payment.service.PaymentSubscriptionResultService;
import de.hybris.platform.commercewebservicescommons.model.payment.PaymentSubscriptionResultModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link PaymentSubscriptionResultService}.
 */
public class DefaultPaymentSubscriptionResultService extends AbstractBusinessService implements PaymentSubscriptionResultService
{
	private transient PaymentSubscriptionResultDao paymentSubscriptionResultDao;

	@Override
	public PaymentSubscriptionResultModel findPaymentSubscriptionResultByCart(final String cartId)
	{
		validateParameterNotNull(cartId, "Parameter 'cartId' must not be null!");
		try
		{
			return paymentSubscriptionResultDao.findPaymentSubscriptionResultByCart(cartId);
		}
		catch (final ModelNotFoundException e)
		{
			throw new UnknownIdentifierException(e);
		}
	}

	@Override
	public void removePaymentSubscriptionResultForCart(final String cartId)
	{
		validateParameterNotNull(cartId, "Parameter 'cartId' must not be null!");
		try
		{
			final PaymentSubscriptionResultModel paymentSubscriptionResultModel = paymentSubscriptionResultDao
					.findPaymentSubscriptionResultByCart(cartId);
			getModelService().remove(paymentSubscriptionResultModel);
		}
		catch (final ModelNotFoundException e)
		{
			throw new UnknownIdentifierException(e);
		}
	}

	@Override
	public void removePaymentSubscriptionResultForCart(final String cartCode, final String cartGuid)
	{
		validateParameterNotNull(cartCode, "Parameter 'cartCode' must not be null!");
		validateParameterNotNull(cartGuid, "Parameter 'cartGuid' must not be null!");
		final List<PaymentSubscriptionResultModel> resultList = paymentSubscriptionResultDao
				.findPaymentSubscriptionResultByCart(cartCode, cartGuid);
		for (final PaymentSubscriptionResultModel result : resultList)
		{
			getModelService().remove(result);
		}
	}

	@Override
	public void savePaymentSubscriptionResult(final PaymentSubscriptionResultModel paymentSubscriptionResultModel)
	{
		validateParameterNotNull(paymentSubscriptionResultModel, "Parameter 'paymentSubscriptionResultModel' must not be null!");
		getModelService().save(paymentSubscriptionResultModel);
	}

	public PaymentSubscriptionResultDao getPaymentSubscriptionResultDao()
	{
		return paymentSubscriptionResultDao;
	}

	@Required
	public void setPaymentSubscriptionResultDao(final PaymentSubscriptionResultDao paymentSubscriptionResultDao)
	{
		this.paymentSubscriptionResultDao = paymentSubscriptionResultDao;
	}
}
