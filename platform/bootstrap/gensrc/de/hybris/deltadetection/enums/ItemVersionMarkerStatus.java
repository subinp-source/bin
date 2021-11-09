/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.deltadetection.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum ItemVersionMarkerStatus declared at extension deltadetection.
 */
public enum ItemVersionMarkerStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for ItemVersionMarkerStatus.ACTIVE declared at extension deltadetection.
	 */
	ACTIVE("ACTIVE"),
	/**
	 * Generated enum value for ItemVersionMarkerStatus.DELETED declared at extension deltadetection.
	 */
	DELETED("DELETED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ItemVersionMarkerStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ItemVersionMarkerStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ItemVersionMarkerStatus(final String code)
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
