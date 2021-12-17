/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.product.data;

import java.io.Serializable;


import java.util.Objects;
public  class SolrFirstVariantCategoryEntryData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SolrFirstVariantCategoryEntryData.categoryName</code> property defined at extension <code>commerceservices</code>. */
		
	private String categoryName;

	/** <i>Generated property</i> for <code>SolrFirstVariantCategoryEntryData.variantCode</code> property defined at extension <code>commerceservices</code>. */
		
	private String variantCode;
	
	public SolrFirstVariantCategoryEntryData()
	{
		// default constructor
	}
	
	public void setCategoryName(final String categoryName)
	{
		this.categoryName = categoryName;
	}

	public String getCategoryName() 
	{
		return categoryName;
	}
	
	public void setVariantCode(final String variantCode)
	{
		this.variantCode = variantCode;
	}

	public String getVariantCode() 
	{
		return variantCode;
	}
	

}