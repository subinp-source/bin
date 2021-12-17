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
import de.hybris.platform.commercewebservicescommons.dto.product.VariantMatrixElementWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.VariantOptionWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.VariantValueCategoryWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Variant Matrix Element
 */
@ApiModel(value="VariantMatrixElement", description="Representation of a Variant Matrix Element")
public  class VariantMatrixElementWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Variant value category for variant matrix element<br/><br/><i>Generated property</i> for <code>VariantMatrixElementWsDTO.variantValueCategory</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="variantValueCategory", value="Variant value category for variant matrix element") 	
	private VariantValueCategoryWsDTO variantValueCategory;

	/** Parent variant category for variant matrix element<br/><br/><i>Generated property</i> for <code>VariantMatrixElementWsDTO.parentVariantCategory</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="parentVariantCategory", value="Parent variant category for variant matrix element") 	
	private VariantCategoryWsDTO parentVariantCategory;

	/** Variant option for variant matrix element<br/><br/><i>Generated property</i> for <code>VariantMatrixElementWsDTO.variantOption</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="variantOption", value="Variant option for variant matrix element") 	
	private VariantOptionWsDTO variantOption;

	/** List of elements with the type of variant matrix element<br/><br/><i>Generated property</i> for <code>VariantMatrixElementWsDTO.elements</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="elements", value="List of elements with the type of variant matrix element") 	
	private List<VariantMatrixElementWsDTO> elements;

	/** <i>Generated property</i> for <code>VariantMatrixElementWsDTO.isLeaf</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="isLeaf") 	
	private Boolean isLeaf;
	
	public VariantMatrixElementWsDTO()
	{
		// default constructor
	}
	
	public void setVariantValueCategory(final VariantValueCategoryWsDTO variantValueCategory)
	{
		this.variantValueCategory = variantValueCategory;
	}

	public VariantValueCategoryWsDTO getVariantValueCategory() 
	{
		return variantValueCategory;
	}
	
	public void setParentVariantCategory(final VariantCategoryWsDTO parentVariantCategory)
	{
		this.parentVariantCategory = parentVariantCategory;
	}

	public VariantCategoryWsDTO getParentVariantCategory() 
	{
		return parentVariantCategory;
	}
	
	public void setVariantOption(final VariantOptionWsDTO variantOption)
	{
		this.variantOption = variantOption;
	}

	public VariantOptionWsDTO getVariantOption() 
	{
		return variantOption;
	}
	
	public void setElements(final List<VariantMatrixElementWsDTO> elements)
	{
		this.elements = elements;
	}

	public List<VariantMatrixElementWsDTO> getElements() 
	{
		return elements;
	}
	
	public void setIsLeaf(final Boolean isLeaf)
	{
		this.isLeaf = isLeaf;
	}

	public Boolean getIsLeaf() 
	{
		return isLeaf;
	}
	

}