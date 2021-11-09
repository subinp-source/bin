/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum LinkTargets declared at extension cms2.
 */
public enum LinkTargets implements HybrisEnumValue
{
	/**
	 * Generated enum value for LinkTargets.sameWindow declared at extension cms2.
	 */
	SAMEWINDOW("sameWindow"),
	/**
	 * Generated enum value for LinkTargets.newWindow declared at extension cms2.
	 */
	NEWWINDOW("newWindow");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "LinkTargets";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "LinkTargets";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private LinkTargets(final String code)
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
