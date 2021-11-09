/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum SnFieldType declared at extension searchservices.
 */
public enum SnFieldType implements HybrisEnumValue
{
	/**
	 * Generated enum value for SnFieldType.STRING declared at extension searchservices.
	 */
	STRING("STRING"),
	/**
	 * Generated enum value for SnFieldType.TEXT declared at extension searchservices.
	 */
	TEXT("TEXT"),
	/**
	 * Generated enum value for SnFieldType.BOOLEAN declared at extension searchservices.
	 */
	BOOLEAN("BOOLEAN"),
	/**
	 * Generated enum value for SnFieldType.INTEGER declared at extension searchservices.
	 */
	INTEGER("INTEGER"),
	/**
	 * Generated enum value for SnFieldType.LONG declared at extension searchservices.
	 */
	LONG("LONG"),
	/**
	 * Generated enum value for SnFieldType.FLOAT declared at extension searchservices.
	 */
	FLOAT("FLOAT"),
	/**
	 * Generated enum value for SnFieldType.DOUBLE declared at extension searchservices.
	 */
	DOUBLE("DOUBLE"),
	/**
	 * Generated enum value for SnFieldType.DATE_TIME declared at extension searchservices.
	 */
	DATE_TIME("DATE_TIME");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SnFieldType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SnFieldType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SnFieldType(final String code)
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
