/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengineservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum OrderEntrySelectionStrategy declared at extension ruleengineservices.
 */
public enum OrderEntrySelectionStrategy implements HybrisEnumValue
{
	/**
	 * Generated enum value for OrderEntrySelectionStrategy.CHEAPEST declared at extension ruleengineservices.
	 */
	CHEAPEST("CHEAPEST"),
	/**
	 * Generated enum value for OrderEntrySelectionStrategy.MOST_EXPENSIVE declared at extension ruleengineservices.
	 */
	MOST_EXPENSIVE("MOST_EXPENSIVE"),
	/**
	 * Generated enum value for OrderEntrySelectionStrategy.DEFAULT declared at extension ruleengineservices.
	 */
	DEFAULT("DEFAULT");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "OrderEntrySelectionStrategy";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "OrderEntrySelectionStrategy";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private OrderEntrySelectionStrategy(final String code)
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
