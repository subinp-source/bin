/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.search.facetdata;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.search.SearchStateWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Breadcrumb
 */
@ApiModel(value="Breadcrumb", description="Representation of a Breadcrumb")
public  class BreadcrumbWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Code of the facet<br/><br/><i>Generated property</i> for <code>BreadcrumbWsDTO.facetCode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="facetCode", value="Code of the facet") 	
	private String facetCode;

	/** Name of the facet<br/><br/><i>Generated property</i> for <code>BreadcrumbWsDTO.facetName</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="facetName", value="Name of the facet") 	
	private String facetName;

	/** Value code of the facet<br/><br/><i>Generated property</i> for <code>BreadcrumbWsDTO.facetValueCode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="facetValueCode", value="Value code of the facet") 	
	private String facetValueCode;

	/** Value name of the facet<br/><br/><i>Generated property</i> for <code>BreadcrumbWsDTO.facetValueName</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="facetValueName", value="Value name of the facet") 	
	private String facetValueName;

	/** Remove query<br/><br/><i>Generated property</i> for <code>BreadcrumbWsDTO.removeQuery</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="removeQuery", value="Remove query") 	
	private SearchStateWsDTO removeQuery;

	/** Truncate query<br/><br/><i>Generated property</i> for <code>BreadcrumbWsDTO.truncateQuery</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="truncateQuery", value="Truncate query") 	
	private SearchStateWsDTO truncateQuery;
	
	public BreadcrumbWsDTO()
	{
		// default constructor
	}
	
	public void setFacetCode(final String facetCode)
	{
		this.facetCode = facetCode;
	}

	public String getFacetCode() 
	{
		return facetCode;
	}
	
	public void setFacetName(final String facetName)
	{
		this.facetName = facetName;
	}

	public String getFacetName() 
	{
		return facetName;
	}
	
	public void setFacetValueCode(final String facetValueCode)
	{
		this.facetValueCode = facetValueCode;
	}

	public String getFacetValueCode() 
	{
		return facetValueCode;
	}
	
	public void setFacetValueName(final String facetValueName)
	{
		this.facetValueName = facetValueName;
	}

	public String getFacetValueName() 
	{
		return facetValueName;
	}
	
	public void setRemoveQuery(final SearchStateWsDTO removeQuery)
	{
		this.removeQuery = removeQuery;
	}

	public SearchStateWsDTO getRemoveQuery() 
	{
		return removeQuery;
	}
	
	public void setTruncateQuery(final SearchStateWsDTO truncateQuery)
	{
		this.truncateQuery = truncateQuery;
	}

	public SearchStateWsDTO getTruncateQuery() 
	{
		return truncateQuery;
	}
	

}