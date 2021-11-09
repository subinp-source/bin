/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.order.dao.impl.DefaultCartEntryDao;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.marketplaceservices.dao.MarketplaceCartEntryDao;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class DefaultMarketplaceCartEntryDao extends DefaultCartEntryDao implements MarketplaceCartEntryDao
{

	@Override
	public List<CartEntryModel> findUnSaleableCartEntries(final CartModel cart)
	{
		validateParameterNotNull(cart, "Cart must not be null");
		final List<CartEntryModel> unSaleableCartEntryList = new ArrayList<>();
		cart.getEntries().forEach(e -> {
			if(e.getProduct().getSaleable().equals(Boolean.FALSE))
			{
				unSaleableCartEntryList.add((CartEntryModel) e);
			}
		});

		return unSaleableCartEntryList;
	}
}
