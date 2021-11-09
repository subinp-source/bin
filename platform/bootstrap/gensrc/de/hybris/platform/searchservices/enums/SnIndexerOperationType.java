/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum SnIndexerOperationType declared at extension searchservices.
 */
public enum SnIndexerOperationType implements HybrisEnumValue
{
	/**
	 * Generated enum value for SnIndexerOperationType.FULL declared at extension searchservices.
	 */
	FULL("FULL"),
	/**
	 * Generated enum value for SnIndexerOperationType.INCREMENTAL declared at extension searchservices.
	 */
	INCREMENTAL("INCREMENTAL");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SnIndexerOperationType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SnIndexerOperationType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SnIndexerOperationType(final String code)
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
