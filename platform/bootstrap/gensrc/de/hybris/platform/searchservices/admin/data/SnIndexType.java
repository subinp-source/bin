/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.data;

import java.io.Serializable;
import de.hybris.platform.searchservices.admin.data.SnCatalogVersion;
import de.hybris.platform.searchservices.admin.data.SnField;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import java.util.Objects;
public  class SnIndexType  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SnIndexType.id</code> property defined at extension <code>searchservices</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>SnIndexType.name</code> property defined at extension <code>searchservices</code>. */
		
	private Map<Locale,String> name;

	/** <i>Generated property</i> for <code>SnIndexType.itemComposedType</code> property defined at extension <code>searchservices</code>. */
		
	private String itemComposedType;

	/** <i>Generated property</i> for <code>SnIndexType.identityProvider</code> property defined at extension <code>searchservices</code>. */
		
	private String identityProvider;

	/** <i>Generated property</i> for <code>SnIndexType.identityProviderParameters</code> property defined at extension <code>searchservices</code>. */
		
	private Map<String,String> identityProviderParameters;

	/** <i>Generated property</i> for <code>SnIndexType.defaultValueProvider</code> property defined at extension <code>searchservices</code>. */
		
	private String defaultValueProvider;

	/** <i>Generated property</i> for <code>SnIndexType.defaultValueProviderParameters</code> property defined at extension <code>searchservices</code>. */
		
	private Map<String,String> defaultValueProviderParameters;

	/** <i>Generated property</i> for <code>SnIndexType.listeners</code> property defined at extension <code>searchservices</code>. */
		
	private List<String> listeners;

	/** <i>Generated property</i> for <code>SnIndexType.indexConfigurationId</code> property defined at extension <code>searchservices</code>. */
		
	private String indexConfigurationId;

	/** <i>Generated property</i> for <code>SnIndexType.fields</code> property defined at extension <code>searchservices</code>. */
		
	private Map<String,SnField> fields;

	/** <i>Generated property</i> for <code>SnIndexType.catalogsIds</code> property defined at extension <code>searchservices</code>. */
		
	private List<String> catalogsIds;

	/** <i>Generated property</i> for <code>SnIndexType.catalogVersions</code> property defined at extension <code>searchservices</code>. */
		
	private List<SnCatalogVersion> catalogVersions;
	
	public SnIndexType()
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
	
	public void setItemComposedType(final String itemComposedType)
	{
		this.itemComposedType = itemComposedType;
	}

	public String getItemComposedType() 
	{
		return itemComposedType;
	}
	
	public void setIdentityProvider(final String identityProvider)
	{
		this.identityProvider = identityProvider;
	}

	public String getIdentityProvider() 
	{
		return identityProvider;
	}
	
	public void setIdentityProviderParameters(final Map<String,String> identityProviderParameters)
	{
		this.identityProviderParameters = identityProviderParameters;
	}

	public Map<String,String> getIdentityProviderParameters() 
	{
		return identityProviderParameters;
	}
	
	public void setDefaultValueProvider(final String defaultValueProvider)
	{
		this.defaultValueProvider = defaultValueProvider;
	}

	public String getDefaultValueProvider() 
	{
		return defaultValueProvider;
	}
	
	public void setDefaultValueProviderParameters(final Map<String,String> defaultValueProviderParameters)
	{
		this.defaultValueProviderParameters = defaultValueProviderParameters;
	}

	public Map<String,String> getDefaultValueProviderParameters() 
	{
		return defaultValueProviderParameters;
	}
	
	public void setListeners(final List<String> listeners)
	{
		this.listeners = listeners;
	}

	public List<String> getListeners() 
	{
		return listeners;
	}
	
	public void setIndexConfigurationId(final String indexConfigurationId)
	{
		this.indexConfigurationId = indexConfigurationId;
	}

	public String getIndexConfigurationId() 
	{
		return indexConfigurationId;
	}
	
	public void setFields(final Map<String,SnField> fields)
	{
		this.fields = fields;
	}

	public Map<String,SnField> getFields() 
	{
		return fields;
	}
	
	public void setCatalogsIds(final List<String> catalogsIds)
	{
		this.catalogsIds = catalogsIds;
	}

	public List<String> getCatalogsIds() 
	{
		return catalogsIds;
	}
	
	public void setCatalogVersions(final List<SnCatalogVersion> catalogVersions)
	{
		this.catalogVersions = catalogVersions;
	}

	public List<SnCatalogVersion> getCatalogVersions() 
	{
		return catalogVersions;
	}
	

	@Override
	public boolean equals(final Object o)
	{
		if (o == null) return false;
		if (o == this) return true;

        if (getClass() != o.getClass()) return false;

		final SnIndexType other = (SnIndexType) o;
		return Objects.equals(getId(), other.getId())
			&& Objects.equals(getName(), other.getName())
			&& Objects.equals(getItemComposedType(), other.getItemComposedType())
			&& Objects.equals(getIdentityProvider(), other.getIdentityProvider())
			&& Objects.equals(getIdentityProviderParameters(), other.getIdentityProviderParameters())
			&& Objects.equals(getDefaultValueProvider(), other.getDefaultValueProvider())
			&& Objects.equals(getDefaultValueProviderParameters(), other.getDefaultValueProviderParameters())
			&& Objects.equals(getListeners(), other.getListeners())
			&& Objects.equals(getIndexConfigurationId(), other.getIndexConfigurationId())
			&& Objects.equals(getFields(), other.getFields())
			&& Objects.equals(getCatalogsIds(), other.getCatalogsIds())
			&& Objects.equals(getCatalogVersions(), other.getCatalogVersions());
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
		attribute = itemComposedType;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = identityProvider;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = identityProviderParameters;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = defaultValueProvider;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = defaultValueProviderParameters;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = listeners;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = indexConfigurationId;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = fields;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = catalogsIds;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = catalogVersions;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());

		return result;
	}
}