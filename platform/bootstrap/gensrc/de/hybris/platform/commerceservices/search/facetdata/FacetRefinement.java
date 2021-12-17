/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.facetdata;

import java.io.Serializable;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import java.util.List;


import java.util.Objects;
/**
 * POJO representing a facet refinement.
 */
public  class FacetRefinement<STATE>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>FacetRefinement<STATE>.facets</code> property defined at extension <code>commerceservices</code>. */
		
	private List<FacetData<STATE>> facets;

	/** <i>Generated property</i> for <code>FacetRefinement<STATE>.breadcrumbs</code> property defined at extension <code>commerceservices</code>. */
		
	private List<BreadcrumbData<STATE>> breadcrumbs;

	/** <i>Generated property</i> for <code>FacetRefinement<STATE>.count</code> property defined at extension <code>commerceservices</code>. */
		
	private long count;
	
	public FacetRefinement()
	{
		// default constructor
	}
	
	public void setFacets(final List<FacetData<STATE>> facets)
	{
		this.facets = facets;
	}

	public List<FacetData<STATE>> getFacets() 
	{
		return facets;
	}
	
	public void setBreadcrumbs(final List<BreadcrumbData<STATE>> breadcrumbs)
	{
		this.breadcrumbs = breadcrumbs;
	}

	public List<BreadcrumbData<STATE>> getBreadcrumbs() 
	{
		return breadcrumbs;
	}
	
	public void setCount(final long count)
	{
		this.count = count;
	}

	public long getCount() 
	{
		return count;
	}
	

}