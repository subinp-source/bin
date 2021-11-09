/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import java.io.Serializable;


import java.util.Objects;
/**
 * Represents a single keyword suggestion.
 */
public  class AutocompleteSuggestion  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AutocompleteSuggestion.term</code> property defined at extension <code>commerceservices</code>. */
		
	private String term;
	
	public AutocompleteSuggestion()
	{
		// default constructor
	}
	
	public void setTerm(final String term)
	{
		this.term = term;
	}

	public String getTerm() 
	{
		return term;
	}
	

}