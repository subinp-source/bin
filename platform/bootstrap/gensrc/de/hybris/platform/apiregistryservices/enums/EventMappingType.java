/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum EventMappingType declared at extension apiregistryservices.
 * <p/>
 * Event data extracting types.
 */
public enum EventMappingType implements HybrisEnumValue
{
	/**
	 * Generated enum value for EventMappingType.GENERIC declared at extension apiregistryservices.
	 */
	GENERIC("GENERIC"),
	/**
	 * Generated enum value for EventMappingType.BEAN declared at extension apiregistryservices.
	 */
	BEAN("BEAN"),
	/**
	 * Generated enum value for EventMappingType.PROCESS declared at extension apiregistryservices.
	 */
	PROCESS("PROCESS");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "EventMappingType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "EventMappingType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private EventMappingType(final String code)
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
