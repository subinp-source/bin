/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.data;

import de.hybris.platform.adaptivesearch.data.AbstractAsItemConfiguration;
import de.hybris.platform.adaptivesearch.data.AsExcludedFacetValue;
import de.hybris.platform.adaptivesearch.data.AsFacetRange;
import de.hybris.platform.adaptivesearch.data.AsPromotedFacetValue;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import java.util.List;


import java.util.Objects;
public abstract  class AbstractAsFacetConfiguration extends AbstractAsItemConfiguration 
{

 

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.indexProperty</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String indexProperty;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.priority</code> property defined at extension <code>adaptivesearch</code>. */
		
	private Integer priority;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.facetType</code> property defined at extension <code>adaptivesearch</code>. */
		
	private AsFacetType facetType;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.valuesSortProvider</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String valuesSortProvider;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.valuesDisplayNameProvider</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String valuesDisplayNameProvider;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.topValuesProvider</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String topValuesProvider;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.topValuesSize</code> property defined at extension <code>adaptivesearch</code>. */
		
	private Integer topValuesSize;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.promotedValues</code> property defined at extension <code>adaptivesearch</code>. */
		
	private List<AsPromotedFacetValue> promotedValues;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.excludedValues</code> property defined at extension <code>adaptivesearch</code>. */
		
	private List<AsExcludedFacetValue> excludedValues;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.sort</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String sort;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.ranged</code> property defined at extension <code>adaptivesearch</code>. */
		
	private boolean ranged;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.rangeIncludeFrom</code> property defined at extension <code>adaptivesearch</code>. */
		
	private boolean rangeIncludeFrom;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.rangeIncludeTo</code> property defined at extension <code>adaptivesearch</code>. */
		
	private boolean rangeIncludeTo;

	/** <i>Generated property</i> for <code>AbstractAsFacetConfiguration.ranges</code> property defined at extension <code>adaptivesearch</code>. */
		
	private List<AsFacetRange> ranges;
	
	public AbstractAsFacetConfiguration()
	{
		// default constructor
	}
	
	public void setIndexProperty(final String indexProperty)
	{
		this.indexProperty = indexProperty;
	}

	public String getIndexProperty() 
	{
		return indexProperty;
	}
	
	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}

	public Integer getPriority() 
	{
		return priority;
	}
	
	public void setFacetType(final AsFacetType facetType)
	{
		this.facetType = facetType;
	}

	public AsFacetType getFacetType() 
	{
		return facetType;
	}
	
	public void setValuesSortProvider(final String valuesSortProvider)
	{
		this.valuesSortProvider = valuesSortProvider;
	}

	public String getValuesSortProvider() 
	{
		return valuesSortProvider;
	}
	
	public void setValuesDisplayNameProvider(final String valuesDisplayNameProvider)
	{
		this.valuesDisplayNameProvider = valuesDisplayNameProvider;
	}

	public String getValuesDisplayNameProvider() 
	{
		return valuesDisplayNameProvider;
	}
	
	public void setTopValuesProvider(final String topValuesProvider)
	{
		this.topValuesProvider = topValuesProvider;
	}

	public String getTopValuesProvider() 
	{
		return topValuesProvider;
	}
	
	public void setTopValuesSize(final Integer topValuesSize)
	{
		this.topValuesSize = topValuesSize;
	}

	public Integer getTopValuesSize() 
	{
		return topValuesSize;
	}
	
	public void setPromotedValues(final List<AsPromotedFacetValue> promotedValues)
	{
		this.promotedValues = promotedValues;
	}

	public List<AsPromotedFacetValue> getPromotedValues() 
	{
		return promotedValues;
	}
	
	public void setExcludedValues(final List<AsExcludedFacetValue> excludedValues)
	{
		this.excludedValues = excludedValues;
	}

	public List<AsExcludedFacetValue> getExcludedValues() 
	{
		return excludedValues;
	}
	
	public void setSort(final String sort)
	{
		this.sort = sort;
	}

	public String getSort() 
	{
		return sort;
	}
	
	public void setRanged(final boolean ranged)
	{
		this.ranged = ranged;
	}

	public boolean isRanged() 
	{
		return ranged;
	}
	
	public void setRangeIncludeFrom(final boolean rangeIncludeFrom)
	{
		this.rangeIncludeFrom = rangeIncludeFrom;
	}

	public boolean isRangeIncludeFrom() 
	{
		return rangeIncludeFrom;
	}
	
	public void setRangeIncludeTo(final boolean rangeIncludeTo)
	{
		this.rangeIncludeTo = rangeIncludeTo;
	}

	public boolean isRangeIncludeTo() 
	{
		return rangeIncludeTo;
	}
	
	public void setRanges(final List<AsFacetRange> ranges)
	{
		this.ranges = ranges;
	}

	public List<AsFacetRange> getRanges() 
	{
		return ranges;
	}
	

}