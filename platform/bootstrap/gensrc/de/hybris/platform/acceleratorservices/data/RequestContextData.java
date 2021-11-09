/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.data;

import java.io.Serializable;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;


import java.util.Objects;
/**
 * Holds context data for rendering the current request
 */
public  class RequestContextData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>RequestContextData.product</code> property defined at extension <code>acceleratorcms</code>. */
		
	private ProductModel product;

	/** <i>Generated property</i> for <code>RequestContextData.category</code> property defined at extension <code>acceleratorcms</code>. */
		
	private CategoryModel category;

	/** <i>Generated property</i> for <code>RequestContextData.search</code> property defined at extension <code>acceleratorcms</code>. */
		
	private SearchPageData search;
	
	public RequestContextData()
	{
		// default constructor
	}
	
	public void setProduct(final ProductModel product)
	{
		this.product = product;
	}

	public ProductModel getProduct() 
	{
		return product;
	}
	
	public void setCategory(final CategoryModel category)
	{
		this.category = category;
	}

	public CategoryModel getCategory() 
	{
		return category;
	}
	
	public void setSearch(final SearchPageData search)
	{
		this.search = search;
	}

	public SearchPageData getSearch() 
	{
		return search;
	}
	

}