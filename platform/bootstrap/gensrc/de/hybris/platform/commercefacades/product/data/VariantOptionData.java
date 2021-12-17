/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import java.util.Collection;


import java.util.Objects;
public  class VariantOptionData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>VariantOptionData.code</code> property defined at extension <code>commercefacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>VariantOptionData.stock</code> property defined at extension <code>commercefacades</code>. */
		
	private StockData stock;

	/** <i>Generated property</i> for <code>VariantOptionData.url</code> property defined at extension <code>commercefacades</code>. */
		
	private String url;

	/** <i>Generated property</i> for <code>VariantOptionData.priceData</code> property defined at extension <code>commercefacades</code>. */
		
	private PriceData priceData;

	/** <i>Generated property</i> for <code>VariantOptionData.variantOptionQualifiers</code> property defined at extension <code>commercefacades</code>. */
		
	private Collection<VariantOptionQualifierData> variantOptionQualifiers;
	
	public VariantOptionData()
	{
		// default constructor
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setStock(final StockData stock)
	{
		this.stock = stock;
	}

	public StockData getStock() 
	{
		return stock;
	}
	
	public void setUrl(final String url)
	{
		this.url = url;
	}

	public String getUrl() 
	{
		return url;
	}
	
	public void setPriceData(final PriceData priceData)
	{
		this.priceData = priceData;
	}

	public PriceData getPriceData() 
	{
		return priceData;
	}
	
	public void setVariantOptionQualifiers(final Collection<VariantOptionQualifierData> variantOptionQualifiers)
	{
		this.variantOptionQualifiers = variantOptionQualifiers;
	}

	public Collection<VariantOptionQualifierData> getVariantOptionQualifiers() 
	{
		return variantOptionQualifiers;
	}
	

}