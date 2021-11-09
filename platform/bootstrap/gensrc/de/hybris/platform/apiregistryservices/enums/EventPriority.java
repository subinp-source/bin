/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum EventPriority declared at extension apiregistryservices.
 * <p/>
 * Priority for event processing.
 */
public enum EventPriority implements HybrisEnumValue
{
	/**
	 * Generated enum value for EventPriority.CRITICAL declared at extension apiregistryservices.
	 */
	CRITICAL("CRITICAL"),
	/**
	 * Generated enum value for EventPriority.HIGH declared at extension apiregistryservices.
	 */
	HIGH("HIGH"),
	/**
	 * Generated enum value for EventPriority.MEDIUM declared at extension apiregistryservices.
	 */
	MEDIUM("MEDIUM"),
	/**
	 * Generated enum value for EventPriority.LOW declared at extension apiregistryservices.
	 */
	LOW("LOW");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "EventPriority";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "EventPriority";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private EventPriority(final String code)
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
