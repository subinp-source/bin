/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum SnIndexerOperationStatus declared at extension searchservices.
 */
public enum SnIndexerOperationStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for SnIndexerOperationStatus.RUNNING declared at extension searchservices.
	 */
	RUNNING("RUNNING"),
	/**
	 * Generated enum value for SnIndexerOperationStatus.COMPLETED declared at extension searchservices.
	 */
	COMPLETED("COMPLETED"),
	/**
	 * Generated enum value for SnIndexerOperationStatus.ABORTED declared at extension searchservices.
	 */
	ABORTED("ABORTED"),
	/**
	 * Generated enum value for SnIndexerOperationStatus.FAILED declared at extension searchservices.
	 */
	FAILED("FAILED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SnIndexerOperationStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SnIndexerOperationStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SnIndexerOperationStatus(final String code)
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
