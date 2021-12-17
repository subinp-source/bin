/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.data;

import java.io.Serializable;
import java.util.List;


import java.util.Objects;
public  class SnSynonymEntry  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SnSynonymEntry.id</code> property defined at extension <code>searchservices</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>SnSynonymEntry.input</code> property defined at extension <code>searchservices</code>. */
		
	private List<String> input;

	/** <i>Generated property</i> for <code>SnSynonymEntry.synonyms</code> property defined at extension <code>searchservices</code>. */
		
	private List<String> synonyms;
	
	public SnSynonymEntry()
	{
		// default constructor
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setInput(final List<String> input)
	{
		this.input = input;
	}

	public List<String> getInput() 
	{
		return input;
	}
	
	public void setSynonyms(final List<String> synonyms)
	{
		this.synonyms = synonyms;
	}

	public List<String> getSynonyms() 
	{
		return synonyms;
	}
	

	@Override
	public boolean equals(final Object o)
	{
		if (o == null) return false;
		if (o == this) return true;

        if (getClass() != o.getClass()) return false;

		final SnSynonymEntry other = (SnSynonymEntry) o;
		return Objects.equals(getId(), other.getId())
			&& Objects.equals(getInput(), other.getInput())
			&& Objects.equals(getSynonyms(), other.getSynonyms());
    }

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

		attribute = id;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = input;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = synonyms;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());

		return result;
	}
}