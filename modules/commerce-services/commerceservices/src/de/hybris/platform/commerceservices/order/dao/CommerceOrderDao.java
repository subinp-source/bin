/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.dao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;


/**
 * Commerce DAO interface for handling {@link OrderModel}
 */
public interface CommerceOrderDao extends GenericDao<OrderModel>
{

	/**
	 * Retrieves an order associated to a quote if any, else returns null
	 *
	 * @param quote
	 *           quote model
	 * @return order
	 */
	OrderModel findOrderByQuote(QuoteModel quote);
}
