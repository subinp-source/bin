/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum WarehouseConsignmentState declared at extension commerceservices.
 */
public enum WarehouseConsignmentState implements HybrisEnumValue
{
	/**
	 * Generated enum value for WarehouseConsignmentState.COMPLETE declared at extension commerceservices.
	 */
	COMPLETE("COMPLETE"),
	/**
	 * Generated enum value for WarehouseConsignmentState.CANCEL declared at extension commerceservices.
	 */
	CANCEL("CANCEL"),
	/**
	 * Generated enum value for WarehouseConsignmentState.PARTIAL declared at extension commerceservices.
	 */
	PARTIAL("PARTIAL");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "WarehouseConsignmentState";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "WarehouseConsignmentState";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private WarehouseConsignmentState(final String code)
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
