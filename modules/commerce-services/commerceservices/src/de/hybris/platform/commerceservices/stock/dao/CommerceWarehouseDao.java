/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.stock.dao;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;


public interface CommerceWarehouseDao extends Dao
{
	/**
	 * 
	 * @param baseStore
	 * @return List of WarehouseModels
	 */
	List<WarehouseModel> getDefaultWarehousesForBaseStore(BaseStoreModel baseStore);
}
