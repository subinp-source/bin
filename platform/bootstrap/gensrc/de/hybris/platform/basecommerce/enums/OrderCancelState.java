/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum OrderCancelState declared at extension basecommerce.
 */
public enum OrderCancelState implements HybrisEnumValue
{
	/**
	 * Generated enum value for OrderCancelState.PendingOrHoldingArea declared at extension basecommerce.
	 */
	PENDINGORHOLDINGAREA("PendingOrHoldingArea"),
	/**
	 * Generated enum value for OrderCancelState.SentToWarehouse declared at extension basecommerce.
	 */
	SENTTOWAREHOUSE("SentToWarehouse"),
	/**
	 * Generated enum value for OrderCancelState.Shipping declared at extension basecommerce.
	 */
	SHIPPING("Shipping"),
	/**
	 * Generated enum value for OrderCancelState.PartiallyShipped declared at extension basecommerce.
	 */
	PARTIALLYSHIPPED("PartiallyShipped"),
	/**
	 * Generated enum value for OrderCancelState.CancelImpossible declared at extension basecommerce.
	 */
	CANCELIMPOSSIBLE("CancelImpossible");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "OrderCancelState";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "OrderCancelState";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private OrderCancelState(final String code)
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
