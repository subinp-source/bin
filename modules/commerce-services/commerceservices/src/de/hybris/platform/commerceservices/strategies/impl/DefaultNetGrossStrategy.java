/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.impl;

import de.hybris.platform.commerceservices.strategies.NetGrossStrategy;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.PriceFactory;


/**
 * Default implementation of {@link NetGrossStrategy}
 */
public class DefaultNetGrossStrategy implements NetGrossStrategy
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commerceservices.strategies.NetGrossStrategy#isNet()
	 */
	@Override
	public boolean isNet()
	{
		// compare the behavior in {@link DefaultPriceService}
		final PriceFactory priceFactory = OrderManager.getInstance().getPriceFactory();
		return priceFactory.isNetUser(JaloSession.getCurrentSession().getUser());
	}

}
