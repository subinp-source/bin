/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum IndexerOperationStatus declared at extension solrfacetsearch.
 */
public enum IndexerOperationStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for IndexerOperationStatus.RUNNING declared at extension solrfacetsearch.
	 */
	RUNNING("RUNNING"),
	/**
	 * Generated enum value for IndexerOperationStatus.ABORTED declared at extension solrfacetsearch.
	 */
	ABORTED("ABORTED"),
	/**
	 * Generated enum value for IndexerOperationStatus.SUCCESS declared at extension solrfacetsearch.
	 */
	SUCCESS("SUCCESS"),
	/**
	 * Generated enum value for IndexerOperationStatus.FAILED declared at extension solrfacetsearch.
	 */
	FAILED("FAILED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "IndexerOperationStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "IndexerOperationStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private IndexerOperationStatus(final String code)
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
