/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
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
 * Representation of a Facet Value
 */
@ApiModel(value="FacetValue", description="Representation of a Facet Value")
public  class FacetValueWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Name of the facet value<br/><br/><i>Generated property</i> for <code>FacetValueWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the facet value") 	
	private String name;

	/** Count of the facet value<br/><br/><i>Generated property</i> for <code>FacetValueWsDTO.count</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="count", value="Count of the facet value") 	
	private Long count;

	/** Query of the facet value<br/><br/><i>Generated property</i> for <code>FacetValueWsDTO.query</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="query", value="Query of the facet value") 	
	private SearchStateWsDTO query;

	/** Flag stating if facet value is selected<br/><br/><i>Generated property</i> for <code>FacetValueWsDTO.selected</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="selected", value="Flag stating if facet value is selected") 	
	private Boolean selected;
	
	public FacetValueWsDTO()
	{
		// default constructor
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setCount(final Long count)
	{
		this.count = count;
	}

	public Long getCount() 
	{
		return count;
	}
	
	public void setQuery(final SearchStateWsDTO query)
	{
		this.query = query;
	}

	public SearchStateWsDTO getQuery() 
	{
		return query;
	}
	
	public void setSelected(final Boolean selected)
	{
		this.selected = selected;
	}

	public Boolean getSelected() 
	{
		return selected;
	}
	

}