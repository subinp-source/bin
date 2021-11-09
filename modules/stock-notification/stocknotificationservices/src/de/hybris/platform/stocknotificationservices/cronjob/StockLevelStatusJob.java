/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationservices.cronjob;

import de.hybris.platform.core.model.ItemModel;

import java.util.Map;


/**
 * Default implementation to send BACK_IN_STOCK notification to customer
 */
public class StockLevelStatusJob extends AbstractStockLevelStatusJob
{
	@Override
	protected StockNotificationTask createTask(final Map<String, ItemModel> data)
	{
		return new StockNotificationTask(getNotificationService(), modelService, data);
	}
}
