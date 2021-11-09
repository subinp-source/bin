/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum UiExperienceLevel declared at extension commerceservices.
 * <p/>
 * This enumeration denotes different user interface experiences.
 */
public enum UiExperienceLevel implements HybrisEnumValue
{
	/**
	 * Generated enum value for UiExperienceLevel.Mobile declared at extension commerceservices.
	 */
	MOBILE("Mobile"),
	/**
	 * Generated enum value for UiExperienceLevel.Desktop declared at extension commerceservices.
	 */
	DESKTOP("Desktop");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "UiExperienceLevel";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "UiExperienceLevel";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private UiExperienceLevel(final String code)
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
