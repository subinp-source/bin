/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum SolrCommitMode declared at extension solrfacetsearch.
 */
public enum SolrCommitMode implements HybrisEnumValue
{
	/**
	 * Generated enum value for SolrCommitMode.NEVER declared at extension solrfacetsearch.
	 */
	NEVER("NEVER"),
	/**
	 * Generated enum value for SolrCommitMode.AFTER_INDEX declared at extension solrfacetsearch.
	 */
	AFTER_INDEX("AFTER_INDEX"),
	/**
	 * Generated enum value for SolrCommitMode.AFTER_BATCH declared at extension solrfacetsearch.
	 */
	AFTER_BATCH("AFTER_BATCH"),
	/**
	 * Generated enum value for SolrCommitMode.MIXED declared at extension solrfacetsearch.
	 */
	MIXED("MIXED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SolrCommitMode";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SolrCommitMode";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SolrCommitMode(final String code)
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
