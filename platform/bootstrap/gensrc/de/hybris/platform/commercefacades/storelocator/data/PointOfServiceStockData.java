/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storelocator.data;

import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;


import java.util.Objects;
public  class PointOfServiceStockData extends PointOfServiceData 
{

 

	/** <i>Generated property</i> for <code>PointOfServiceStockData.stockData</code> property defined at extension <code>commercefacades</code>. */
		
	private StockData stockData;
	
	public PointOfServiceStockData()
	{
		// default constructor
	}
	
	public void setStockData(final StockData stockData)
	{
		this.stockData = stockData;
	}

	public StockData getStockData() 
	{
		return stockData;
	}
	

}