/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengine.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum DroolsSessionType declared at extension ruleengine.
 */
public enum DroolsSessionType implements HybrisEnumValue
{
	/**
	 * Generated enum value for DroolsSessionType.STATEFUL declared at extension ruleengine.
	 */
	STATEFUL("STATEFUL"),
	/**
	 * Generated enum value for DroolsSessionType.STATELESS declared at extension ruleengine.
	 */
	STATELESS("STATELESS");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DroolsSessionType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "DroolsSessionType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private DroolsSessionType(final String code)
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
