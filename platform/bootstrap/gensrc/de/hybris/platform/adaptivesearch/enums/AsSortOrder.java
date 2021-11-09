/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum AsSortOrder declared at extension adaptivesearch.
 */
public enum AsSortOrder implements HybrisEnumValue
{
	/**
	 * Generated enum value for AsSortOrder.ASCENDING declared at extension adaptivesearch.
	 */
	ASCENDING("ASCENDING"),
	/**
	 * Generated enum value for AsSortOrder.DESCENDING declared at extension adaptivesearch.
	 */
	DESCENDING("DESCENDING");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AsSortOrder";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "AsSortOrder";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private AsSortOrder(final String code)
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
