/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum AsBoostItemsMergeMode declared at extension adaptivesearch.
 */
public enum AsBoostItemsMergeMode implements HybrisEnumValue
{
	/**
	 * Generated enum value for AsBoostItemsMergeMode.ADD_AFTER declared at extension adaptivesearch.
	 */
	ADD_AFTER("ADD_AFTER"),
	/**
	 * Generated enum value for AsBoostItemsMergeMode.ADD_BEFORE declared at extension adaptivesearch.
	 */
	ADD_BEFORE("ADD_BEFORE"),
	/**
	 * Generated enum value for AsBoostItemsMergeMode.REPLACE declared at extension adaptivesearch.
	 */
	REPLACE("REPLACE");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AsBoostItemsMergeMode";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "AsBoostItemsMergeMode";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private AsBoostItemsMergeMode(final String code)
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
