/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum SolrWildcardType declared at extension solrfacetsearch.
 */
public enum SolrWildcardType implements HybrisEnumValue
{
	/**
	 * Generated enum value for SolrWildcardType.PREFIX declared at extension solrfacetsearch.
	 */
	PREFIX("PREFIX"),
	/**
	 * Generated enum value for SolrWildcardType.POSTFIX declared at extension solrfacetsearch.
	 */
	POSTFIX("POSTFIX"),
	/**
	 * Generated enum value for SolrWildcardType.PREFIX_AND_POSTFIX declared at extension solrfacetsearch.
	 */
	PREFIX_AND_POSTFIX("PREFIX_AND_POSTFIX");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SolrWildcardType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SolrWildcardType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SolrWildcardType(final String code)
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
