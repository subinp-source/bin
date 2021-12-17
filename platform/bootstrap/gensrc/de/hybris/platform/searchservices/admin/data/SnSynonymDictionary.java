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
import java.util.Locale;
import java.util.Map;


import java.util.Objects;
public  class SnSynonymDictionary  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SnSynonymDictionary.id</code> property defined at extension <code>searchservices</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>SnSynonymDictionary.name</code> property defined at extension <code>searchservices</code>. */
		
	private Map<Locale,String> name;

	/** <i>Generated property</i> for <code>SnSynonymDictionary.languageIds</code> property defined at extension <code>searchservices</code>. */
		
	private List<String> languageIds;

	/** <i>Generated property</i> for <code>SnSynonymDictionary.entries</code> property defined at extension <code>searchservices</code>. */
		
	private List<SnSynonymEntry> entries;
	
	public SnSynonymDictionary()
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
	
	public void setName(final Map<Locale,String> name)
	{
		this.name = name;
	}

	public Map<Locale,String> getName() 
	{
		return name;
	}
	
	public void setLanguageIds(final List<String> languageIds)
	{
		this.languageIds = languageIds;
	}

	public List<String> getLanguageIds() 
	{
		return languageIds;
	}
	
	public void setEntries(final List<SnSynonymEntry> entries)
	{
		this.entries = entries;
	}

	public List<SnSynonymEntry> getEntries() 
	{
		return entries;
	}
	

	@Override
	public boolean equals(final Object o)
	{
		if (o == null) return false;
		if (o == this) return true;

        if (getClass() != o.getClass()) return false;

		final SnSynonymDictionary other = (SnSynonymDictionary) o;
		return Objects.equals(getId(), other.getId())
			&& Objects.equals(getName(), other.getName())
			&& Objects.equals(getLanguageIds(), other.getLanguageIds())
			&& Objects.equals(getEntries(), other.getEntries());
    }

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

		attribute = id;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = name;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = languageIds;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = entries;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());

		return result;
	}
}