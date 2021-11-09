/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.dao;

import de.hybris.platform.commerceservices.order.dao.CartEntryDao;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;

import java.util.List;


/**
 * Dao to find order related data
 */
public interface MarketplaceCartEntryDao extends CartEntryDao
{
	/**
	 * Find entries in given cart that include unsaleable product
	 *
	 * @param cart
	 *           CartModel
	 * @return list of entries in given cart
	 */
	List<CartEntryModel> findUnSaleableCartEntries(CartModel cart);
}
