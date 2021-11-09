/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengine.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum DroolsEqualityBehavior declared at extension ruleengine.
 */
public enum DroolsEqualityBehavior implements HybrisEnumValue
{
	/**
	 * Generated enum value for DroolsEqualityBehavior.EQUALITY declared at extension ruleengine.
	 */
	EQUALITY("EQUALITY"),
	/**
	 * Generated enum value for DroolsEqualityBehavior.IDENTITY declared at extension ruleengine.
	 */
	IDENTITY("IDENTITY");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DroolsEqualityBehavior";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "DroolsEqualityBehavior";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private DroolsEqualityBehavior(final String code)
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
