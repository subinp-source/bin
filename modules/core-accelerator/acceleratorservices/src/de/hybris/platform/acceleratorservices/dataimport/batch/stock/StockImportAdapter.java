/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.stock;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.stock.StockService;


/**
 * Adapter to translate a stock import row into a service call to {@link StockService}.
 */
public interface StockImportAdapter
{
	/**
	 * Import a stock value by calling the {@link StockService}.
	 * 
	 * @param cellValue
	 * @param product
	 * @throws IllegalArgumentException
	 *            if the cellValue is empty, null or invalid or the product is null
	 */
	void performImport(String cellValue, Item product);

}
