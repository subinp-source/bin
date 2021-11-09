/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum DistanceUnit declared at extension basecommerce.
 */
public enum DistanceUnit implements HybrisEnumValue
{
	/**
	 * Generated enum value for DistanceUnit.miles declared at extension basecommerce.
	 */
	MILES("miles"),
	/**
	 * Generated enum value for DistanceUnit.km declared at extension basecommerce.
	 */
	KM("km");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DistanceUnit";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "DistanceUnit";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private DistanceUnit(final String code)
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
