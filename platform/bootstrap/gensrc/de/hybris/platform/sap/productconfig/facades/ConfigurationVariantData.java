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
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;


import java.util.Objects;
public  class ConfigurationVariantData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ConfigurationVariantData.productCode</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String productCode;

	/** <i>Generated property</i> for <code>ConfigurationVariantData.name</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>ConfigurationVariantData.price</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private PriceData price;

	/** <i>Generated property</i> for <code>ConfigurationVariantData.imageData</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private ImageData imageData;

	/** <i>Generated property</i> for <code>ConfigurationVariantData.cartEntryNumber</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private Integer cartEntryNumber;
	
	public ConfigurationVariantData()
	{
		// default constructor
	}
	
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}

	public String getProductCode() 
	{
		return productCode;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setPrice(final PriceData price)
	{
		this.price = price;
	}

	public PriceData getPrice() 
	{
		return price;
	}
	
	public void setImageData(final ImageData imageData)
	{
		this.imageData = imageData;
	}

	public ImageData getImageData() 
	{
		return imageData;
	}
	
	public void setCartEntryNumber(final Integer cartEntryNumber)
	{
		this.cartEntryNumber = cartEntryNumber;
	}

	public Integer getCartEntryNumber() 
	{
		return cartEntryNumber;
	}
	

}