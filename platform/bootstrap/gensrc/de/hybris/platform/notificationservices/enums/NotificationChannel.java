/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum NotificationChannel declared at extension notificationservices.
 */
public enum NotificationChannel implements HybrisEnumValue
{
	/**
	 * Generated enum value for NotificationChannel.EMAIL declared at extension notificationservices.
	 */
	EMAIL("EMAIL"),
	/**
	 * Generated enum value for NotificationChannel.SMS declared at extension notificationservices.
	 */
	SMS("SMS"),
	/**
	 * Generated enum value for NotificationChannel.SITE_MESSAGE declared at extension notificationservices.
	 */
	SITE_MESSAGE("SITE_MESSAGE");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "NotificationChannel";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "NotificationChannel";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private NotificationChannel(final String code)
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
