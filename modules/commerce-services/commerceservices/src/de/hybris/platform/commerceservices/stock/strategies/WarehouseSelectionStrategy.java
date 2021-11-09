/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.stock.strategies;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;


/**
 * This strategy abstracts the relationship between BaseStore and Warehouse allowing the logic to be easily changed. The
 * Warehouses returned are the "Web" or "Online" warehouses which are typically used for home delivery.
 */
public interface WarehouseSelectionStrategy
{
	List<WarehouseModel> getWarehousesForBaseStore(BaseStoreModel baseStore);
}
