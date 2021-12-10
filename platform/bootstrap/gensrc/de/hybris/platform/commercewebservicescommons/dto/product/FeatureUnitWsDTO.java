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
 * Representation of a Feature Unit
 */
@ApiModel(value="FeatureUnit", description="Representation of a Feature Unit")
public  class FeatureUnitWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Symbol of the feature unit<br/><br/><i>Generated property</i> for <code>FeatureUnitWsDTO.symbol</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="symbol", value="Symbol of the feature unit") 	
	private String symbol;

	/** Name of the feature unit<br/><br/><i>Generated property</i> for <code>FeatureUnitWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the feature unit") 	
	private String name;

	/** Type of the feature unit<br/><br/><i>Generated property</i> for <code>FeatureUnitWsDTO.unitType</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="unitType", value="Type of the feature unit") 	
	private String unitType;
	
	public FeatureUnitWsDTO()
	{
		// default constructor
	}
	
	public void setSymbol(final String symbol)
	{
		this.symbol = symbol;
	}

	public String getSymbol() 
	{
		return symbol;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setUnitType(final String unitType)
	{
		this.unitType = unitType;
	}

	public String getUnitType() 
	{
		return unitType;
	}
	

}