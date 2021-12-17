/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.search.solrfacetsearch;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Solr Search Query
 */
@ApiModel(value="SolrSearchQuery", description="Representation of a Solr Search Query")
public  class SolrSearchQueryWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Code of category<br/><br/><i>Generated property</i> for <code>SolrSearchQueryWsDTO.categoryCode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="categoryCode", value="Code of category") 	
	private String categoryCode;
	
	public SolrSearchQueryWsDTO()
	{
		// default constructor
	}
	
	public void setCategoryCode(final String categoryCode)
	{
		this.categoryCode = categoryCode;
	}

	public String getCategoryCode() 
	{
		return categoryCode;
	}
	

}