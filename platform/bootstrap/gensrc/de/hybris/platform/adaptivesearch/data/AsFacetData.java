/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.data;

import java.io.Serializable;
import de.hybris.platform.adaptivesearch.data.AsFacetValueData;
import de.hybris.platform.adaptivesearch.data.AsFacetVisibility;
import java.util.List;


import java.util.Objects;
public  class AsFacetData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AsFacetData.indexProperty</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String indexProperty;

	/** <i>Generated property</i> for <code>AsFacetData.name</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>AsFacetData.topValues</code> property defined at extension <code>adaptivesearch</code>. */
		
	private List<AsFacetValueData> topValues;

	/** <i>Generated property</i> for <code>AsFacetData.values</code> property defined at extension <code>adaptivesearch</code>. */
		
	private List<AsFacetValueData> values;

	/** <i>Generated property</i> for <code>AsFacetData.selectedValues</code> property defined at extension <code>adaptivesearch</code>. */
		
	private List<AsFacetValueData> selectedValues;

	/** <i>Generated property</i> for <code>AsFacetData.allValues</code> property defined at extension <code>adaptivesearch</code>. */
		
	private List<AsFacetValueData> allValues;

	/** <i>Generated property</i> for <code>AsFacetData.visibility</code> property defined at extension <code>adaptivesearch</code>. */
		
	private AsFacetVisibility visibility;
	
	public AsFacetData()
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
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setTopValues(final List<AsFacetValueData> topValues)
	{
		this.topValues = topValues;
	}

	public List<AsFacetValueData> getTopValues() 
	{
		return topValues;
	}
	
	public void setValues(final List<AsFacetValueData> values)
	{
		this.values = values;
	}

	public List<AsFacetValueData> getValues() 
	{
		return values;
	}
	
	public void setSelectedValues(final List<AsFacetValueData> selectedValues)
	{
		this.selectedValues = selectedValues;
	}

	public List<AsFacetValueData> getSelectedValues() 
	{
		return selectedValues;
	}
	
	public void setAllValues(final List<AsFacetValueData> allValues)
	{
		this.allValues = allValues;
	}

	public List<AsFacetValueData> getAllValues() 
	{
		return allValues;
	}
	
	public void setVisibility(final AsFacetVisibility visibility)
	{
		this.visibility = visibility;
	}

	public AsFacetVisibility getVisibility() 
	{
		return visibility;
	}
	

}