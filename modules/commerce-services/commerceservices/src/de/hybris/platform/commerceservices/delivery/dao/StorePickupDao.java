/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.delivery.dao;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;
import java.util.Map;



public interface StorePickupDao extends Dao
{
	/**
	 * Checks if a product can be picked up
	 * 
	 * @param productCode
	 * @param baseStoreModel
	 */
	Boolean checkProductForPickup(String productCode, BaseStoreModel baseStoreModel);

	/**
	 * Get stock levels for given given product and base store.
	 * 
	 * @param productCode
	 * @param baseStoreModel
	 * @return Map of {@link PointOfServiceModel} and {@link de.hybris.platform.basecommerce.enums.StockLevelStatus}
	 *         information
	 */
	Map<PointOfServiceModel, List<StockLevelModel>> getLocalStockLevelsForProductAndBaseStore(String productCode,
			BaseStoreModel baseStoreModel);
}
