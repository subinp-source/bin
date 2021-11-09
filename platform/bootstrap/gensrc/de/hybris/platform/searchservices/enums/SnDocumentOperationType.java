/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum SnDocumentOperationType declared at extension searchservices.
 */
public enum SnDocumentOperationType implements HybrisEnumValue
{
	/**
	 * Generated enum value for SnDocumentOperationType.CREATE declared at extension searchservices.
	 */
	CREATE("CREATE"),
	/**
	 * Generated enum value for SnDocumentOperationType.CREATE_UPDATE declared at extension searchservices.
	 */
	CREATE_UPDATE("CREATE_UPDATE"),
	/**
	 * Generated enum value for SnDocumentOperationType.DELETE declared at extension searchservices.
	 */
	DELETE("DELETE");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SnDocumentOperationType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SnDocumentOperationType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SnDocumentOperationType(final String code)
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
