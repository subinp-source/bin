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
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Reference
 */
@ApiModel(value="Reference", description="Representation of a Reference")
public  class ReferenceWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Reference type<br/><br/><i>Generated property</i> for <code>ReferenceWsDTO.referenceType</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="referenceType", value="Reference type") 	
	private String referenceType;

	/** Reference description<br/><br/><i>Generated property</i> for <code>ReferenceWsDTO.description</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="description", value="Reference description") 	
	private String description;

	/** Reference quantity<br/><br/><i>Generated property</i> for <code>ReferenceWsDTO.quantity</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="quantity", value="Reference quantity") 	
	private Integer quantity;

	/** Target product<br/><br/><i>Generated property</i> for <code>ReferenceWsDTO.target</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="target", value="Target product") 	
	private ProductWsDTO target;
	
	public ReferenceWsDTO()
	{
		// default constructor
	}
	
	public void setReferenceType(final String referenceType)
	{
		this.referenceType = referenceType;
	}

	public String getReferenceType() 
	{
		return referenceType;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setQuantity(final Integer quantity)
	{
		this.quantity = quantity;
	}

	public Integer getQuantity() 
	{
		return quantity;
	}
	
	public void setTarget(final ProductWsDTO target)
	{
		this.target = target;
	}

	public ProductWsDTO getTarget() 
	{
		return target;
	}
	

}