/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum SnDocumentOperationStatus declared at extension searchservices.
 */
public enum SnDocumentOperationStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for SnDocumentOperationStatus.CREATED declared at extension searchservices.
	 */
	CREATED("CREATED"),
	/**
	 * Generated enum value for SnDocumentOperationStatus.UPDATED declared at extension searchservices.
	 */
	UPDATED("UPDATED"),
	/**
	 * Generated enum value for SnDocumentOperationStatus.DELETED declared at extension searchservices.
	 */
	DELETED("DELETED"),
	/**
	 * Generated enum value for SnDocumentOperationStatus.FAILED declared at extension searchservices.
	 */
	FAILED("FAILED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SnDocumentOperationStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SnDocumentOperationStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SnDocumentOperationStatus(final String code)
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
