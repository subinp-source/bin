/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.facades.PriceDataPair;
import java.util.List;
import java.util.Map;


import java.util.Objects;
public  class PriceValueUpdateData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PriceValueUpdateData.csticUiKey</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String csticUiKey;

	/** <i>Generated property</i> for <code>PriceValueUpdateData.selectedValues</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<String> selectedValues;

	/** <i>Generated property</i> for <code>PriceValueUpdateData.prices</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private Map<String,PriceDataPair> prices;

	/** <i>Generated property</i> for <code>PriceValueUpdateData.showDeltaPrices</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean showDeltaPrices;
	
	public PriceValueUpdateData()
	{
		// default constructor
	}
	
	public void setCsticUiKey(final String csticUiKey)
	{
		this.csticUiKey = csticUiKey;
	}

	public String getCsticUiKey() 
	{
		return csticUiKey;
	}
	
	public void setSelectedValues(final List<String> selectedValues)
	{
		this.selectedValues = selectedValues;
	}

	public List<String> getSelectedValues() 
	{
		return selectedValues;
	}
	
	public void setPrices(final Map<String,PriceDataPair> prices)
	{
		this.prices = prices;
	}

	public Map<String,PriceDataPair> getPrices() 
	{
		return prices;
	}
	
	public void setShowDeltaPrices(final boolean showDeltaPrices)
	{
		this.showDeltaPrices = showDeltaPrices;
	}

	public boolean isShowDeltaPrices() 
	{
		return showDeltaPrices;
	}
	

}