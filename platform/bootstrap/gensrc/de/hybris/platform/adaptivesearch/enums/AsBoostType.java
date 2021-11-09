/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum AsBoostType declared at extension adaptivesearch.
 */
public enum AsBoostType implements HybrisEnumValue
{
	/**
	 * Generated enum value for AsBoostType.MULTIPLICATIVE declared at extension adaptivesearch.
	 */
	MULTIPLICATIVE("MULTIPLICATIVE"),
	/**
	 * Generated enum value for AsBoostType.ADDITIVE declared at extension adaptivesearch.
	 */
	ADDITIVE("ADDITIVE");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AsBoostType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "AsBoostType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private AsBoostType(final String code)
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
