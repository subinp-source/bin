/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum OrderReturnEntryStatus declared at extension basecommerce.
 */
public enum OrderReturnEntryStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for OrderReturnEntryStatus.ARRIVED declared at extension basecommerce.
	 */
	ARRIVED("ARRIVED"),
	/**
	 * Generated enum value for OrderReturnEntryStatus.WAITING declared at extension basecommerce.
	 */
	WAITING("WAITING");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "OrderReturnEntryStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "OrderReturnEntryStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private OrderReturnEntryStatus(final String code)
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
