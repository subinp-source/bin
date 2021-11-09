/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum SearchQueryContext declared at extension commerceservices.
 * <p/>
 * Different sources of the search query.
 */
public enum SearchQueryContext implements HybrisEnumValue
{
	/**
	 * Generated enum value for SearchQueryContext.DEFAULT declared at extension commerceservices.
	 */
	DEFAULT("DEFAULT"),
	/**
	 * Generated enum value for SearchQueryContext.SUGGESTIONS declared at extension commerceservices.
	 */
	SUGGESTIONS("SUGGESTIONS");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SearchQueryContext";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SearchQueryContext";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SearchQueryContext(final String code)
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
