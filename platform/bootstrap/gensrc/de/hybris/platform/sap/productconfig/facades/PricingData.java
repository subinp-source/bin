/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.PriceData;


import java.util.Objects;
public  class PricingData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PricingData.basePrice</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private PriceData basePrice;

	/** <i>Generated property</i> for <code>PricingData.selectedOptions</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private PriceData selectedOptions;

	/** <i>Generated property</i> for <code>PricingData.currentTotalSavings</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private PriceData currentTotalSavings;

	/** <i>Generated property</i> for <code>PricingData.currentTotal</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private PriceData currentTotal;
	
	public PricingData()
	{
		// default constructor
	}
	
	public void setBasePrice(final PriceData basePrice)
	{
		this.basePrice = basePrice;
	}

	public PriceData getBasePrice() 
	{
		return basePrice;
	}
	
	public void setSelectedOptions(final PriceData selectedOptions)
	{
		this.selectedOptions = selectedOptions;
	}

	public PriceData getSelectedOptions() 
	{
		return selectedOptions;
	}
	
	public void setCurrentTotalSavings(final PriceData currentTotalSavings)
	{
		this.currentTotalSavings = currentTotalSavings;
	}

	public PriceData getCurrentTotalSavings() 
	{
		return currentTotalSavings;
	}
	
	public void setCurrentTotal(final PriceData currentTotal)
	{
		this.currentTotal = currentTotal;
	}

	public PriceData getCurrentTotal() 
	{
		return currentTotal;
	}
	

}