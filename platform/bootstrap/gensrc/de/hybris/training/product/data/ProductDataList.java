/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.training.product.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.ProductData;
import java.util.List;


import java.util.Objects;
public  class ProductDataList  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductDataList.products</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private List<ProductData> products;

	/** <i>Generated property</i> for <code>ProductDataList.catalog</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private String catalog;

	/** <i>Generated property</i> for <code>ProductDataList.version</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private String version;

	/** <i>Generated property</i> for <code>ProductDataList.totalProductCount</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private int totalProductCount;

	/** <i>Generated property</i> for <code>ProductDataList.totalPageCount</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private int totalPageCount;

	/** <i>Generated property</i> for <code>ProductDataList.currentPage</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private int currentPage;
	
	public ProductDataList()
	{
		// default constructor
	}
	
	public void setProducts(final List<ProductData> products)
	{
		this.products = products;
	}

	public List<ProductData> getProducts() 
	{
		return products;
	}
	
	public void setCatalog(final String catalog)
	{
		this.catalog = catalog;
	}

	public String getCatalog() 
	{
		return catalog;
	}
	
	public void setVersion(final String version)
	{
		this.version = version;
	}

	public String getVersion() 
	{
		return version;
	}
	
	public void setTotalProductCount(final int totalProductCount)
	{
		this.totalProductCount = totalProductCount;
	}

	public int getTotalProductCount() 
	{
		return totalProductCount;
	}
	
	public void setTotalPageCount(final int totalPageCount)
	{
		this.totalPageCount = totalPageCount;
	}

	public int getTotalPageCount() 
	{
		return totalPageCount;
	}
	
	public void setCurrentPage(final int currentPage)
	{
		this.currentPage = currentPage;
	}

	public int getCurrentPage() 
	{
		return currentPage;
	}
	

}