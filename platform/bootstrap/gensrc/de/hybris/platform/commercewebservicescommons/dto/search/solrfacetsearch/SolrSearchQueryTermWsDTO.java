/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
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
 * Representation of a Solr Search Query Term
 */
@ApiModel(value="SolrSearchQueryTerm", description="Representation of a Solr Search Query Term")
public  class SolrSearchQueryTermWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Key of solr search query term<br/><br/><i>Generated property</i> for <code>SolrSearchQueryTermWsDTO.key</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="key", value="Key of solr search query term") 	
	private String key;

	/** Value of solr search query term<br/><br/><i>Generated property</i> for <code>SolrSearchQueryTermWsDTO.value</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="value", value="Value of solr search query term") 	
	private String value;
	
	public SolrSearchQueryTermWsDTO()
	{
		// default constructor
	}
	
	public void setKey(final String key)
	{
		this.key = key;
	}

	public String getKey() 
	{
		return key;
	}
	
	public void setValue(final String value)
	{
		this.value = value;
	}

	public String getValue() 
	{
		return value;
	}
	

}