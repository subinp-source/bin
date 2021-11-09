/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentservices.stocklevel.dao;

import de.hybris.platform.chinesepaymentservices.model.StockLevelReservationHistoryEntryModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;


/**
 * The Dao of StockLevelReservationHistoryEntry
 * 
 * @deprecated since 1905
 */
@Deprecated(since = "1905", forRemoval= true )
public interface StockLevelReservationHistoryEntryDao extends Dao
{
	/**
	 * Search the StockLevelReservationHistoryEntry by ordercode in the database
	 *
	 * @param orderCode
	 *           The code of the order
	 * @return List<StockLevelReservationHistoryEntryModel>
	 */
	List<StockLevelReservationHistoryEntryModel> getStockLevelReservationHistoryEntryByOrderCode(String orderCode);
}
