/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2lib.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum FlashScale declared at extension cms2lib.
 */
public enum FlashScale implements HybrisEnumValue
{
	/**
	 * Generated enum value for FlashScale.default declared at extension cms2lib.
	 */
	DEFAULT("default"),
	/**
	 * Generated enum value for FlashScale.noorder declared at extension cms2lib.
	 */
	NOORDER("noorder"),
	/**
	 * Generated enum value for FlashScale.exactfit declared at extension cms2lib.
	 */
	EXACTFIT("exactfit");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "FlashScale";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "FlashScale";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private FlashScale(final String code)
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
