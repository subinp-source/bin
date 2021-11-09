/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.dao;

import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;


public interface CartEntryDao extends Dao
{
	List<CartEntryModel> findEntriesByProductAndPointOfService(CartModel cart, ProductModel product,
	                                                           PointOfServiceModel pointOfService);
}
