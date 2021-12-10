/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.runtime.interf.impl;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.ProductConfigurationDiscount;
import de.hybris.platform.sap.productconfig.runtime.interf.services.impl.ProductConfigurationRelatedObjectType;
import java.util.Date;
import java.util.List;


import java.util.Objects;
public  class ConfigurationRetrievalOptions  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ConfigurationRetrievalOptions.discountList</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private List<ProductConfigurationDiscount> discountList;

	/** <i>Generated property</i> for <code>ConfigurationRetrievalOptions.pricingDate</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private Date pricingDate;

	/** <i>Generated property</i> for <code>ConfigurationRetrievalOptions.pricingProduct</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private String pricingProduct;

	/** <i>Generated property</i> for <code>ConfigurationRetrievalOptions.relatedObjectType</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private ProductConfigurationRelatedObjectType relatedObjectType;
	
	public ConfigurationRetrievalOptions()
	{
		// default constructor
	}
	
	public void setDiscountList(final List<ProductConfigurationDiscount> discountList)
	{
		this.discountList = discountList;
	}

	public List<ProductConfigurationDiscount> getDiscountList() 
	{
		return discountList;
	}
	
	public void setPricingDate(final Date pricingDate)
	{
		this.pricingDate = pricingDate;
	}

	public Date getPricingDate() 
	{
		return pricingDate;
	}
	
	public void setPricingProduct(final String pricingProduct)
	{
		this.pricingProduct = pricingProduct;
	}

	public String getPricingProduct() 
	{
		return pricingProduct;
	}
	
	public void setRelatedObjectType(final ProductConfigurationRelatedObjectType relatedObjectType)
	{
		this.relatedObjectType = relatedObjectType;
	}

	public ProductConfigurationRelatedObjectType getRelatedObjectType() 
	{
		return relatedObjectType;
	}
	

}