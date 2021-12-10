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
 * Representation of a Promotion Restriction
 */
@ApiModel(value="PromotionRestriction", description="Representation of a Promotion Restriction")
public  class PromotionRestrictionWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Type of the promotion restriction<br/><br/><i>Generated property</i> for <code>PromotionRestrictionWsDTO.restrictionType</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="restrictionType", value="Type of the promotion restriction") 	
	private String restrictionType;

	/** Description of the promotion restriction<br/><br/><i>Generated property</i> for <code>PromotionRestrictionWsDTO.description</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="description", value="Description of the promotion restriction") 	
	private String description;
	
	public PromotionRestrictionWsDTO()
	{
		// default constructor
	}
	
	public void setRestrictionType(final String restrictionType)
	{
		this.restrictionType = restrictionType;
	}

	public String getRestrictionType() 
	{
		return restrictionType;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	

}