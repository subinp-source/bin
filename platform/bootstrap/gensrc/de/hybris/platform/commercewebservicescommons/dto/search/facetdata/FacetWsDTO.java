/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.search.facetdata;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.FacetValueWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Facet
 */
@ApiModel(value="Facet", description="Representation of a Facet")
public  class FacetWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Name of the facet<br/><br/><i>Generated property</i> for <code>FacetWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the facet") 	
	private String name;

	/** Priority value of the facet<br/><br/><i>Generated property</i> for <code>FacetWsDTO.priority</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="priority", value="Priority value of the facet") 	
	private Integer priority;

	/** Flag stating if facet is category facet<br/><br/><i>Generated property</i> for <code>FacetWsDTO.category</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="category", value="Flag stating if facet is category facet") 	
	private Boolean category;

	/** Flag stating if facet is multiSelect<br/><br/><i>Generated property</i> for <code>FacetWsDTO.multiSelect</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="multiSelect", value="Flag stating if facet is multiSelect") 	
	private Boolean multiSelect;

	/** Flag stating if facet is visible<br/><br/><i>Generated property</i> for <code>FacetWsDTO.visible</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="visible", value="Flag stating if facet is visible") 	
	private Boolean visible;

	/** List of top facet values<br/><br/><i>Generated property</i> for <code>FacetWsDTO.topValues</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="topValues", value="List of top facet values") 	
	private List<FacetValueWsDTO> topValues;

	/** List of all facet values<br/><br/><i>Generated property</i> for <code>FacetWsDTO.values</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="values", value="List of all facet values") 	
	private List<FacetValueWsDTO> values;
	
	public FacetWsDTO()
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
	
	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}

	public Integer getPriority() 
	{
		return priority;
	}
	
	public void setCategory(final Boolean category)
	{
		this.category = category;
	}

	public Boolean getCategory() 
	{
		return category;
	}
	
	public void setMultiSelect(final Boolean multiSelect)
	{
		this.multiSelect = multiSelect;
	}

	public Boolean getMultiSelect() 
	{
		return multiSelect;
	}
	
	public void setVisible(final Boolean visible)
	{
		this.visible = visible;
	}

	public Boolean getVisible() 
	{
		return visible;
	}
	
	public void setTopValues(final List<FacetValueWsDTO> topValues)
	{
		this.topValues = topValues;
	}

	public List<FacetValueWsDTO> getTopValues() 
	{
		return topValues;
	}
	
	public void setValues(final List<FacetValueWsDTO> values)
	{
		this.values = values;
	}

	public List<FacetValueWsDTO> getValues() 
	{
		return values;
	}
	

}