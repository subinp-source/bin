/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum YFormDataActionEnum declared at extension xyformsservices.
 */
public enum YFormDataActionEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for YFormDataActionEnum.VIEW declared at extension xyformsservices.
	 */
	VIEW("VIEW"),
	/**
	 * Generated enum value for YFormDataActionEnum.EDIT declared at extension xyformsservices.
	 */
	EDIT("EDIT"),
	/**
	 * Generated enum value for YFormDataActionEnum.NEW declared at extension xyformsservices.
	 */
	NEW("NEW");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "YFormDataActionEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "YFormDataActionEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private YFormDataActionEnum(final String code)
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
