/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.order.CommerceOrderService;
import de.hybris.platform.commerceservices.order.dao.CommerceOrderDao;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.QuoteModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link CommerceOrderService}.
 */
public class DefaultCommerceOrderService implements CommerceOrderService
{

	private CommerceOrderDao commerceOrderDao;

	@Override
	public OrderModel getOrderForQuote(final QuoteModel quoteModel)
	{
		validateParameterNotNullStandardMessage("QuoteModel", quoteModel);
		return getCommerceOrderDao().findOrderByQuote(quoteModel);
	}

	protected CommerceOrderDao getCommerceOrderDao()
	{
		return commerceOrderDao;
	}

	@Required
	public void setCommerceOrderDao(final CommerceOrderDao commerceOrderDao)
	{
		this.commerceOrderDao = commerceOrderDao;
	}
}
