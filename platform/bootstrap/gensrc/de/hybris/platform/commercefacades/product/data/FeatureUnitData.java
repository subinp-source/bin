/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;


import java.util.Objects;
public  class FeatureUnitData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>FeatureUnitData.symbol</code> property defined at extension <code>commercefacades</code>. */
		
	private String symbol;

	/** <i>Generated property</i> for <code>FeatureUnitData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>FeatureUnitData.unitType</code> property defined at extension <code>commercefacades</code>. */
		
	private String unitType;
	
	public FeatureUnitData()
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