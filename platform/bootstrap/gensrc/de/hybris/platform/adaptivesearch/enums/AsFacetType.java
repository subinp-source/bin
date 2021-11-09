/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum AsFacetType declared at extension adaptivesearch.
 */
public enum AsFacetType implements HybrisEnumValue
{
	/**
	 * Generated enum value for AsFacetType.REFINE declared at extension adaptivesearch.
	 */
	REFINE("REFINE"),
	/**
	 * Generated enum value for AsFacetType.MULTISELECT_OR declared at extension adaptivesearch.
	 */
	MULTISELECT_OR("MULTISELECT_OR"),
	/**
	 * Generated enum value for AsFacetType.MULTISELECT_AND declared at extension adaptivesearch.
	 */
	MULTISELECT_AND("MULTISELECT_AND"),
	/**
	 * Generated enum value for AsFacetType.MULTISELECT declared at extension adaptivesearch.
	 */
	MULTISELECT("MULTISELECT");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AsFacetType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "AsFacetType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private AsFacetType(final String code)
	{
		this.code = code.intern();
	}
	
	
	/**
	 * Gets the code of this enum value.
	 *  
	 * @return code of value
	 */
	@Override
	public String getCode()
	{
		return this.code;
	}
	
	/**
	 * Gets the type this enum value belongs to.
	 *  
	 * @return code of type
	 */
	@Override
	public String getType()
	{
		return SIMPLE_CLASSNAME;
	}
	
}
