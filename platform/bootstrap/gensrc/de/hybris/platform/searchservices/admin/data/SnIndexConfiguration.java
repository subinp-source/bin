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
import de.hybris.platform.searchservices.admin.data.SnCurrency;
import de.hybris.platform.searchservices.admin.data.SnLanguage;
import de.hybris.platform.searchservices.spi.data.AbstractSnSearchProviderConfiguration;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import java.util.Objects;
public  class SnIndexConfiguration  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SnIndexConfiguration.id</code> property defined at extension <code>searchservices</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>SnIndexConfiguration.name</code> property defined at extension <code>searchservices</code>. */
		
	private Map<Locale,String> name;

	/** <i>Generated property</i> for <code>SnIndexConfiguration.user</code> property defined at extension <code>searchservices</code>. */
		
	private String user;

	/** <i>Generated property</i> for <code>SnIndexConfiguration.languages</code> property defined at extension <code>searchservices</code>. */
		
	private List<SnLanguage> languages;

	/** <i>Generated property</i> for <code>SnIndexConfiguration.currencies</code> property defined at extension <code>searchservices</code>. */
		
	private List<SnCurrency> currencies;

	/** <i>Generated property</i> for <code>SnIndexConfiguration.listeners</code> property defined at extension <code>searchservices</code>. */
		
	private List<String> listeners;

	/** <i>Generated property</i> for <code>SnIndexConfiguration.searchProviderConfiguration</code> property defined at extension <code>searchservices</code>. */
		
	private AbstractSnSearchProviderConfiguration searchProviderConfiguration;

	/** <i>Generated property</i> for <code>SnIndexConfiguration.synonymDictionaryIds</code> property defined at extension <code>searchservices</code>. */
		
	private List<String> synonymDictionaryIds;
	
	public SnIndexConfiguration()
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
	
	public void setUser(final String user)
	{
		this.user = user;
	}

	public String getUser() 
	{
		return user;
	}
	
	public void setLanguages(final List<SnLanguage> languages)
	{
		this.languages = languages;
	}

	public List<SnLanguage> getLanguages() 
	{
		return languages;
	}
	
	public void setCurrencies(final List<SnCurrency> currencies)
	{
		this.currencies = currencies;
	}

	public List<SnCurrency> getCurrencies() 
	{
		return currencies;
	}
	
	public void setListeners(final List<String> listeners)
	{
		this.listeners = listeners;
	}

	public List<String> getListeners() 
	{
		return listeners;
	}
	
	public void setSearchProviderConfiguration(final AbstractSnSearchProviderConfiguration searchProviderConfiguration)
	{
		this.searchProviderConfiguration = searchProviderConfiguration;
	}

	public AbstractSnSearchProviderConfiguration getSearchProviderConfiguration() 
	{
		return searchProviderConfiguration;
	}
	
	public void setSynonymDictionaryIds(final List<String> synonymDictionaryIds)
	{
		this.synonymDictionaryIds = synonymDictionaryIds;
	}

	public List<String> getSynonymDictionaryIds() 
	{
		return synonymDictionaryIds;
	}
	

	@Override
	public boolean equals(final Object o)
	{
		if (o == null) return false;
		if (o == this) return true;

        if (getClass() != o.getClass()) return false;

		final SnIndexConfiguration other = (SnIndexConfiguration) o;
		return Objects.equals(getId(), other.getId())
			&& Objects.equals(getName(), other.getName())
			&& Objects.equals(getUser(), other.getUser())
			&& Objects.equals(getLanguages(), other.getLanguages())
			&& Objects.equals(getCurrencies(), other.getCurrencies())
			&& Objects.equals(getListeners(), other.getListeners())
			&& Objects.equals(getSearchProviderConfiguration(), other.getSearchProviderConfiguration())
			&& Objects.equals(getSynonymDictionaryIds(), other.getSynonymDictionaryIds());
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
		attribute = user;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = languages;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = currencies;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = listeners;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = searchProviderConfiguration;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = synonymDictionaryIds;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());

		return result;
	}
}