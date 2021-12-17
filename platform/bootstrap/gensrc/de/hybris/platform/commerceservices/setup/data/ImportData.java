/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.setup.data;

import java.io.Serializable;
import java.util.List;


import java.util.Objects;
public  class ImportData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ImportData.productCatalogName</code> property defined at extension <code>commerceservices</code>. */
		
	private String productCatalogName;

	/** <i>Generated property</i> for <code>ImportData.contentCatalogNames</code> property defined at extension <code>commerceservices</code>. */
		
	private List<String> contentCatalogNames;

	/** <i>Generated property</i> for <code>ImportData.storeNames</code> property defined at extension <code>commerceservices</code>. */
		
	private List<String> storeNames;
	
	public ImportData()
	{
		// default constructor
	}
	
	public void setProductCatalogName(final String productCatalogName)
	{
		this.productCatalogName = productCatalogName;
	}

	public String getProductCatalogName() 
	{
		return productCatalogName;
	}
	
	public void setContentCatalogNames(final List<String> contentCatalogNames)
	{
		this.contentCatalogNames = contentCatalogNames;
	}

	public List<String> getContentCatalogNames() 
	{
		return contentCatalogNames;
	}
	
	public void setStoreNames(final List<String> storeNames)
	{
		this.storeNames = storeNames;
	}

	public List<String> getStoreNames() 
	{
		return storeNames;
	}
	

}