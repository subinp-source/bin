/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentservices.stocklevel;

import de.hybris.platform.chinesepaymentservices.model.StockLevelReservationHistoryEntryModel;

import java.util.List;


/**
 * The service of StockLevelReservationHistoryEntry
 * 
 * @deprecated since 1905
 */
@Deprecated(since = "1905", forRemoval= true )
public interface StockLevelReservationHistoryEntryService
{
	/**
	 * Getting the StockLevelReservationHistoryEntry by the code of the order
	 *
	 * @param orderCode
	 *           The code of the order
	 * @return list of StockLevelReservationHistoryEntryModel
	 * 
	 */
	List<StockLevelReservationHistoryEntryModel> getStockLevelReservationHistoryEntryByOrderCode(String orderCode);
}
