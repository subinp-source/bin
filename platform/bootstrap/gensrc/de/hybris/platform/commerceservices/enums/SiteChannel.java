/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum SiteChannel declared at extension commerceservices.
 * <p/>
 * This enumeration denotes the channel for the BaseSite - B2B or B2C.
 */
public enum SiteChannel implements HybrisEnumValue
{
	/**
	 * Generated enum value for SiteChannel.B2B declared at extension commerceservices.
	 */
	B2B("B2B"),
	/**
	 * Generated enum value for SiteChannel.B2C declared at extension commerceservices.
	 */
	B2C("B2C");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SiteChannel";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SiteChannel";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SiteChannel(final String code)
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
