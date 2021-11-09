/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductReferenceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Product Reference List
 */
@ApiModel(value="ProductReferenceList", description="Representation of a Product Reference List")
public  class ProductReferenceListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** List of product references<br/><br/><i>Generated property</i> for <code>ProductReferenceListWsDTO.references</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="references", value="List of product references") 	
	private List<ProductReferenceWsDTO> references;
	
	public ProductReferenceListWsDTO()
	{
		// default constructor
	}
	
	public void setReferences(final List<ProductReferenceWsDTO> references)
	{
		this.references = references;
	}

	public List<ProductReferenceWsDTO> getReferences() 
	{
		return references;
	}
	

}