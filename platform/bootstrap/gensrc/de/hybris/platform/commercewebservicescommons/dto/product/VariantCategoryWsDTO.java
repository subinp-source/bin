/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Variant Category
 */
@ApiModel(value="VariantCategory", description="Representation of a Variant Category")
public  class VariantCategoryWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Variant category name<br/><br/><i>Generated property</i> for <code>VariantCategoryWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Variant category name") 	
	private String name;

	/** Flag if varian category has image assigned<br/><br/><i>Generated property</i> for <code>VariantCategoryWsDTO.hasImage</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="hasImage", value="Flag if varian category has image assigned") 	
	private Boolean hasImage;

	/** Priority number of variant category<br/><br/><i>Generated property</i> for <code>VariantCategoryWsDTO.priority</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="priority", value="Priority number of variant category") 	
	private Integer priority;
	
	public VariantCategoryWsDTO()
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
	
	public void setHasImage(final Boolean hasImage)
	{
		this.hasImage = hasImage;
	}

	public Boolean getHasImage() 
	{
		return hasImage;
	}
	
	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}

	public Integer getPriority() 
	{
		return priority;
	}
	

}