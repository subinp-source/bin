/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.search.facetdata;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.SearchStateWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.BreadcrumbWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.FacetWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.SpellingSuggestionWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Product Search Page
 */
@ApiModel(value="ProductSearchPage", description="Representation of a Product Search Page")
public  class ProductSearchPageWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Free text search<br/><br/><i>Generated property</i> for <code>ProductSearchPageWsDTO.freeTextSearch</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="freeTextSearch", value="Free text search") 	
	private String freeTextSearch;

	/** Code of category<br/><br/><i>Generated property</i> for <code>ProductSearchPageWsDTO.categoryCode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="categoryCode", value="Code of category") 	
	private String categoryCode;

	/** Redirect url address keyword<br/><br/><i>Generated property</i> for <code>ProductSearchPageWsDTO.keywordRedirectUrl</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="keywordRedirectUrl", value="Redirect url address keyword") 	
	private String keywordRedirectUrl;

	/** Spelling suggestion<br/><br/><i>Generated property</i> for <code>ProductSearchPageWsDTO.spellingSuggestion</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="spellingSuggestion", value="Spelling suggestion") 	
	private SpellingSuggestionWsDTO spellingSuggestion;

	/** List of products<br/><br/><i>Generated property</i> for <code>ProductSearchPageWsDTO.products</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="products", value="List of products") 	
	private List<ProductWsDTO> products;

	/** List of sorts<br/><br/><i>Generated property</i> for <code>ProductSearchPageWsDTO.sorts</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="sorts", value="List of sorts") 	
	private List<SortWsDTO> sorts;

	/** Pagination number<br/><br/><i>Generated property</i> for <code>ProductSearchPageWsDTO.pagination</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="pagination", value="Pagination number") 	
	private PaginationWsDTO pagination;

	/** Current query<br/><br/><i>Generated property</i> for <code>ProductSearchPageWsDTO.currentQuery</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="currentQuery", value="Current query") 	
	private SearchStateWsDTO currentQuery;

	/** List of breadcrumbs info<br/><br/><i>Generated property</i> for <code>ProductSearchPageWsDTO.breadcrumbs</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="breadcrumbs", value="List of breadcrumbs info") 	
	private List<BreadcrumbWsDTO> breadcrumbs;

	/** List of facets<br/><br/><i>Generated property</i> for <code>ProductSearchPageWsDTO.facets</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="facets", value="List of facets") 	
	private List<FacetWsDTO> facets;
	
	public ProductSearchPageWsDTO()
	{
		// default constructor
	}
	
	public void setFreeTextSearch(final String freeTextSearch)
	{
		this.freeTextSearch = freeTextSearch;
	}

	public String getFreeTextSearch() 
	{
		return freeTextSearch;
	}
	
	public void setCategoryCode(final String categoryCode)
	{
		this.categoryCode = categoryCode;
	}

	public String getCategoryCode() 
	{
		return categoryCode;
	}
	
	public void setKeywordRedirectUrl(final String keywordRedirectUrl)
	{
		this.keywordRedirectUrl = keywordRedirectUrl;
	}

	public String getKeywordRedirectUrl() 
	{
		return keywordRedirectUrl;
	}
	
	public void setSpellingSuggestion(final SpellingSuggestionWsDTO spellingSuggestion)
	{
		this.spellingSuggestion = spellingSuggestion;
	}

	public SpellingSuggestionWsDTO getSpellingSuggestion() 
	{
		return spellingSuggestion;
	}
	
	public void setProducts(final List<ProductWsDTO> products)
	{
		this.products = products;
	}

	public List<ProductWsDTO> getProducts() 
	{
		return products;
	}
	
	public void setSorts(final List<SortWsDTO> sorts)
	{
		this.sorts = sorts;
	}

	public List<SortWsDTO> getSorts() 
	{
		return sorts;
	}
	
	public void setPagination(final PaginationWsDTO pagination)
	{
		this.pagination = pagination;
	}

	public PaginationWsDTO getPagination() 
	{
		return pagination;
	}
	
	public void setCurrentQuery(final SearchStateWsDTO currentQuery)
	{
		this.currentQuery = currentQuery;
	}

	public SearchStateWsDTO getCurrentQuery() 
	{
		return currentQuery;
	}
	
	public void setBreadcrumbs(final List<BreadcrumbWsDTO> breadcrumbs)
	{
		this.breadcrumbs = breadcrumbs;
	}

	public List<BreadcrumbWsDTO> getBreadcrumbs() 
	{
		return breadcrumbs;
	}
	
	public void setFacets(final List<FacetWsDTO> facets)
	{
		this.facets = facets;
	}

	public List<FacetWsDTO> getFacets() 
	{
		return facets;
	}
	

}