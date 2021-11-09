/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationfacades.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum CustomerSegmentationConversionOptions declared at extension personalizationfacades.
 */
public enum CustomerSegmentationConversionOptions implements HybrisEnumValue
{
	/**
	 * Generated enum value for CustomerSegmentationConversionOptions.BASE declared at extension personalizationfacades.
	 */
	BASE("BASE"),
	/**
	 * Generated enum value for CustomerSegmentationConversionOptions.FOR_CUSTOMER declared at extension personalizationfacades.
	 */
	FOR_CUSTOMER("FOR_CUSTOMER"),
	/**
	 * Generated enum value for CustomerSegmentationConversionOptions.FOR_SEGMENT declared at extension personalizationfacades.
	 */
	FOR_SEGMENT("FOR_SEGMENT"),
	/**
	 * Generated enum value for CustomerSegmentationConversionOptions.FULL declared at extension personalizationfacades.
	 */
	FULL("FULL");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CustomerSegmentationConversionOptions";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CustomerSegmentationConversionOptions";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CustomerSegmentationConversionOptions(final String code)
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
