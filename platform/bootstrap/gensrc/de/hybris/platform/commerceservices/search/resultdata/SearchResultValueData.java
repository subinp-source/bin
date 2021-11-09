/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.resultdata;

import java.io.Serializable;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import java.util.Collection;
import java.util.Map;
import java.util.Set;


import java.util.Objects;
public  class SearchResultValueData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SearchResultValueData.values</code> property defined at extension <code>commerceservices</code>. */
		
	private Map<String, Object> values;

	/** <i>Generated property</i> for <code>SearchResultValueData.featureValues</code> property defined at extension <code>commerceservices</code>. */
		
	private Map<ClassAttributeAssignmentModel, Object> featureValues;

	/** <i>Generated property</i> for <code>SearchResultValueData.variants</code> property defined at extension <code>commerceservices</code>. */
		
	private Collection<SearchResultValueData> variants;

	/** <i>Generated property</i> for <code>SearchResultValueData.tags</code> property defined at extension <code>commerceservices</code>. */
		
	private Set<String> tags;
	
	public SearchResultValueData()
	{
		// default constructor
	}
	
	public void setValues(final Map<String, Object> values)
	{
		this.values = values;
	}

	public Map<String, Object> getValues() 
	{
		return values;
	}
	
	public void setFeatureValues(final Map<ClassAttributeAssignmentModel, Object> featureValues)
	{
		this.featureValues = featureValues;
	}

	public Map<ClassAttributeAssignmentModel, Object> getFeatureValues() 
	{
		return featureValues;
	}
	
	public void setVariants(final Collection<SearchResultValueData> variants)
	{
		this.variants = variants;
	}

	public Collection<SearchResultValueData> getVariants() 
	{
		return variants;
	}
	
	public void setTags(final Set<String> tags)
	{
		this.tags = tags;
	}

	public Set<String> getTags() 
	{
		return tags;
	}
	

}