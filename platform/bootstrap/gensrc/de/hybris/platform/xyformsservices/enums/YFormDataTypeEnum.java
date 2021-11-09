/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum YFormDataTypeEnum declared at extension xyformsservices.
 */
public enum YFormDataTypeEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for YFormDataTypeEnum.DRAFT declared at extension xyformsservices.
	 */
	DRAFT("DRAFT"),
	/**
	 * Generated enum value for YFormDataTypeEnum.DATA declared at extension xyformsservices.
	 */
	DATA("DATA");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "YFormDataTypeEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "YFormDataTypeEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private YFormDataTypeEnum(final String code)
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
