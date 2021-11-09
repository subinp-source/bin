/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.stock.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.stock.dao.CommerceWarehouseDao;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class DefaultWarehouseSelectionStrategy implements WarehouseSelectionStrategy
{
	private CommerceWarehouseDao commerceWarehouseDao;

	protected CommerceWarehouseDao getCommerceWarehouseDao()
	{
		return commerceWarehouseDao;
	}

	@Required
	public void setCommerceWarehouseDao(final CommerceWarehouseDao commerceWarehouseDao)
	{
		this.commerceWarehouseDao = commerceWarehouseDao;
	}

	@Override
	public List<WarehouseModel> getWarehousesForBaseStore(final BaseStoreModel baseStore)
	{
		validateParameterNotNull(baseStore, "baseStore must not be null");

		return getCommerceWarehouseDao().getDefaultWarehousesForBaseStore(baseStore);
	}
}
