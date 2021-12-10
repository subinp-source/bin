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
import de.hybris.platform.commercewebservicescommons.dto.product.VariantOptionWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Base Ooption
 */
@ApiModel(value="BaseOption", description="Representation of a Base Ooption")
public  class BaseOptionWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Variant type of base option<br/><br/><i>Generated property</i> for <code>BaseOptionWsDTO.variantType</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="variantType", value="Variant type of base option") 	
	private String variantType;

	/** List of all variant options<br/><br/><i>Generated property</i> for <code>BaseOptionWsDTO.options</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="options", value="List of all variant options") 	
	private List<VariantOptionWsDTO> options;

	/** Variant option selected<br/><br/><i>Generated property</i> for <code>BaseOptionWsDTO.selected</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="selected", value="Variant option selected") 	
	private VariantOptionWsDTO selected;
	
	public BaseOptionWsDTO()
	{
		// default constructor
	}
	
	public void setVariantType(final String variantType)
	{
		this.variantType = variantType;
	}

	public String getVariantType() 
	{
		return variantType;
	}
	
	public void setOptions(final List<VariantOptionWsDTO> options)
	{
		this.options = options;
	}

	public List<VariantOptionWsDTO> getOptions() 
	{
		return options;
	}
	
	public void setSelected(final VariantOptionWsDTO selected)
	{
		this.selected = selected;
	}

	public VariantOptionWsDTO getSelected() 
	{
		return selected;
	}
	

}