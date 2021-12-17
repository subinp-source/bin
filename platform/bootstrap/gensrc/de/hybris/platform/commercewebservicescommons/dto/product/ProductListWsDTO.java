/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Product List
 */
@ApiModel(value="ProductList", description="Representation of a Product List")
public  class ProductListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** List of products<br/><br/><i>Generated property</i> for <code>ProductListWsDTO.products</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="products", value="List of products") 	
	private List<ProductWsDTO> products;

	/** Catalog of product list<br/><br/><i>Generated property</i> for <code>ProductListWsDTO.catalog</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="catalog", value="Catalog of product list") 	
	private String catalog;

	/** Version of product list<br/><br/><i>Generated property</i> for <code>ProductListWsDTO.version</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="version", value="Version of product list") 	
	private String version;

	/** Total product count<br/><br/><i>Generated property</i> for <code>ProductListWsDTO.totalProductCount</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="totalProductCount", value="Total product count") 	
	private Integer totalProductCount;

	/** Total page count<br/><br/><i>Generated property</i> for <code>ProductListWsDTO.totalPageCount</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="totalPageCount", value="Total page count") 	
	private Integer totalPageCount;

	/** Number of current page<br/><br/><i>Generated property</i> for <code>ProductListWsDTO.currentPage</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="currentPage", value="Number of current page") 	
	private Integer currentPage;
	
	public ProductListWsDTO()
	{
		// default constructor
	}
	
	public void setProducts(final List<ProductWsDTO> products)
	{
		this.products = products;
	}

	public List<ProductWsDTO> getProducts() 
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
	
	public void setTotalProductCount(final Integer totalProductCount)
	{
		this.totalProductCount = totalProductCount;
	}

	public Integer getTotalProductCount() 
	{
		return totalProductCount;
	}
	
	public void setTotalPageCount(final Integer totalPageCount)
	{
		this.totalPageCount = totalPageCount;
	}

	public Integer getTotalPageCount() 
	{
		return totalPageCount;
	}
	
	public void setCurrentPage(final Integer currentPage)
	{
		this.currentPage = currentPage;
	}

	public Integer getCurrentPage() 
	{
		return currentPage;
	}
	

}