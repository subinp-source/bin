/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ticket.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum CsEmailRecipients declared at extension ticketsystem.
 */
public enum CsEmailRecipients implements HybrisEnumValue
{
	/**
	 * Generated enum value for CsEmailRecipients.Customer declared at extension ticketsystem.
	 */
	CUSTOMER("Customer"),
	/**
	 * Generated enum value for CsEmailRecipients.AssignedAgent declared at extension ticketsystem.
	 */
	ASSIGNEDAGENT("AssignedAgent"),
	/**
	 * Generated enum value for CsEmailRecipients.AssignedGroup declared at extension ticketsystem.
	 */
	ASSIGNEDGROUP("AssignedGroup");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CsEmailRecipients";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CsEmailRecipients";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CsEmailRecipients(final String code)
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
