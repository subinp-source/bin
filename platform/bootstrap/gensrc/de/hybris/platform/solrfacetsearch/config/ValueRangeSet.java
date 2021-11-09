/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;
import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.config.ValueRangeType;
import java.util.List;


import java.util.Objects;
public  class ValueRangeSet  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ValueRangeSet.qualifier</code> property defined at extension <code>solrfacetsearch</code>. */
		
	private String qualifier;

	/** <i>Generated property</i> for <code>ValueRangeSet.valueRanges</code> property defined at extension <code>solrfacetsearch</code>. */
		
	private List<ValueRange> valueRanges;

	/** <i>Generated property</i> for <code>ValueRangeSet.type</code> property defined at extension <code>solrfacetsearch</code>. */
		
	private ValueRangeType type;
	
	public ValueRangeSet()
	{
		// default constructor
	}
	
	public void setQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
	}

	public String getQualifier() 
	{
		return qualifier;
	}
	
	public void setValueRanges(final List<ValueRange> valueRanges)
	{
		this.valueRanges = valueRanges;
	}

	public List<ValueRange> getValueRanges() 
	{
		return valueRanges;
	}
	
	public void setType(final ValueRangeType type)
	{
		this.type = type;
	}

	public ValueRangeType getType() 
	{
		return type;
	}
	

}