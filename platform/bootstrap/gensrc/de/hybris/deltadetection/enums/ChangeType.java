/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.deltadetection.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum changeType declared at extension deltadetection.
 */
public enum ChangeType implements HybrisEnumValue
{
	/**
	 * Generated enum value for changeType.NEW declared at extension deltadetection.
	 */
	NEW("NEW"),
	/**
	 * Generated enum value for changeType.DELETED declared at extension deltadetection.
	 */
	DELETED("DELETED"),
	/**
	 * Generated enum value for changeType.MODIFIED declared at extension deltadetection.
	 */
	MODIFIED("MODIFIED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "changeType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ChangeType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ChangeType(final String code)
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
