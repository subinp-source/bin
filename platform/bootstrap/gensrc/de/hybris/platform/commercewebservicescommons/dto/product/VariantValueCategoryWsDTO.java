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
import de.hybris.platform.commercewebservicescommons.dto.product.VariantCategoryWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collection;


import java.util.Objects;
/**
 * Representation of a Variant Value Category
 */
@ApiModel(value="VariantValueCategory", description="Representation of a Variant Value Category")
public  class VariantValueCategoryWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Name of the variant value category<br/><br/><i>Generated property</i> for <code>VariantValueCategoryWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the variant value category") 	
	private String name;

	/** Sequence number of variant value category<br/><br/><i>Generated property</i> for <code>VariantValueCategoryWsDTO.sequence</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="sequence", value="Sequence number of variant value category") 	
	private Integer sequence;

	/** Parent category of variant value category<br/><br/><i>Generated property</i> for <code>VariantValueCategoryWsDTO.superCategories</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="superCategories", value="Parent category of variant value category") 	
	private Collection<VariantCategoryWsDTO> superCategories;
	
	public VariantValueCategoryWsDTO()
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
	
	public void setSequence(final Integer sequence)
	{
		this.sequence = sequence;
	}

	public Integer getSequence() 
	{
		return sequence;
	}
	
	public void setSuperCategories(final Collection<VariantCategoryWsDTO> superCategories)
	{
		this.superCategories = superCategories;
	}

	public Collection<VariantCategoryWsDTO> getSuperCategories() 
	{
		return superCategories;
	}
	

}