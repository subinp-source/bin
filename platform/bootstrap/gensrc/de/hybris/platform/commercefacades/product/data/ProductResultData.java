/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.ProductData;
import java.util.List;


import java.util.Objects;
public  class ProductResultData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductResultData.products</code> property defined at extension <code>commercefacades</code>. */
		
	private List<ProductData> products;

	/** <i>Generated property</i> for <code>ProductResultData.totalCount</code> property defined at extension <code>commercefacades</code>. */
		
	private int totalCount;

	/** <i>Generated property</i> for <code>ProductResultData.count</code> property defined at extension <code>commercefacades</code>. */
		
	private int count;

	/** <i>Generated property</i> for <code>ProductResultData.requestedCount</code> property defined at extension <code>commercefacades</code>. */
		
	private int requestedCount;

	/** <i>Generated property</i> for <code>ProductResultData.requestedStart</code> property defined at extension <code>commercefacades</code>. */
		
	private int requestedStart;
	
	public ProductResultData()
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
	
	public void setTotalCount(final int totalCount)
	{
		this.totalCount = totalCount;
	}

	public int getTotalCount() 
	{
		return totalCount;
	}
	
	public void setCount(final int count)
	{
		this.count = count;
	}

	public int getCount() 
	{
		return count;
	}
	
	public void setRequestedCount(final int requestedCount)
	{
		this.requestedCount = requestedCount;
	}

	public int getRequestedCount() 
	{
		return requestedCount;
	}
	
	public void setRequestedStart(final int requestedStart)
	{
		this.requestedStart = requestedStart;
	}

	public int getRequestedStart() 
	{
		return requestedStart;
	}
	

}