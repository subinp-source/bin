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
import de.hybris.platform.searchservices.enums.SnFieldType;
import java.util.Locale;
import java.util.Map;


import java.util.Objects;
public  class SnField  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SnField.id</code> property defined at extension <code>searchservices</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>SnField.name</code> property defined at extension <code>searchservices</code>. */
		
	private Map<Locale,String> name;

	/** <i>Generated property</i> for <code>SnField.fieldType</code> property defined at extension <code>searchservices</code>. */
		
	private SnFieldType fieldType;

	/** <i>Generated property</i> for <code>SnField.valueProvider</code> property defined at extension <code>searchservices</code>. */
		
	private String valueProvider;

	/** <i>Generated property</i> for <code>SnField.valueProviderParameters</code> property defined at extension <code>searchservices</code>. */
		
	private Map<String,String> valueProviderParameters;

	/** <i>Generated property</i> for <code>SnField.retrievable</code> property defined at extension <code>searchservices</code>. */
		
	private Boolean retrievable;

	/** <i>Generated property</i> for <code>SnField.searchable</code> property defined at extension <code>searchservices</code>. */
		
	private Boolean searchable;

	/** <i>Generated property</i> for <code>SnField.localized</code> property defined at extension <code>searchservices</code>. */
		
	private Boolean localized;

	/** <i>Generated property</i> for <code>SnField.qualifierTypeId</code> property defined at extension <code>searchservices</code>. */
		
	private String qualifierTypeId;

	/** <i>Generated property</i> for <code>SnField.multiValued</code> property defined at extension <code>searchservices</code>. */
		
	private Boolean multiValued;

	/** <i>Generated property</i> for <code>SnField.useForSuggesting</code> property defined at extension <code>searchservices</code>. */
		
	private Boolean useForSuggesting;

	/** <i>Generated property</i> for <code>SnField.useForSpellchecking</code> property defined at extension <code>searchservices</code>. */
		
	private Boolean useForSpellchecking;

	/** <i>Generated property</i> for <code>SnField.weight</code> property defined at extension <code>searchservices</code>. */
		
	private Float weight;
	
	public SnField()
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
	
	public void setFieldType(final SnFieldType fieldType)
	{
		this.fieldType = fieldType;
	}

	public SnFieldType getFieldType() 
	{
		return fieldType;
	}
	
	public void setValueProvider(final String valueProvider)
	{
		this.valueProvider = valueProvider;
	}

	public String getValueProvider() 
	{
		return valueProvider;
	}
	
	public void setValueProviderParameters(final Map<String,String> valueProviderParameters)
	{
		this.valueProviderParameters = valueProviderParameters;
	}

	public Map<String,String> getValueProviderParameters() 
	{
		return valueProviderParameters;
	}
	
	public void setRetrievable(final Boolean retrievable)
	{
		this.retrievable = retrievable;
	}

	public Boolean getRetrievable() 
	{
		return retrievable;
	}
	
	public void setSearchable(final Boolean searchable)
	{
		this.searchable = searchable;
	}

	public Boolean getSearchable() 
	{
		return searchable;
	}
	
	public void setLocalized(final Boolean localized)
	{
		this.localized = localized;
	}

	public Boolean getLocalized() 
	{
		return localized;
	}
	
	public void setQualifierTypeId(final String qualifierTypeId)
	{
		this.qualifierTypeId = qualifierTypeId;
	}

	public String getQualifierTypeId() 
	{
		return qualifierTypeId;
	}
	
	public void setMultiValued(final Boolean multiValued)
	{
		this.multiValued = multiValued;
	}

	public Boolean getMultiValued() 
	{
		return multiValued;
	}
	
	public void setUseForSuggesting(final Boolean useForSuggesting)
	{
		this.useForSuggesting = useForSuggesting;
	}

	public Boolean getUseForSuggesting() 
	{
		return useForSuggesting;
	}
	
	public void setUseForSpellchecking(final Boolean useForSpellchecking)
	{
		this.useForSpellchecking = useForSpellchecking;
	}

	public Boolean getUseForSpellchecking() 
	{
		return useForSpellchecking;
	}
	
	public void setWeight(final Float weight)
	{
		this.weight = weight;
	}

	public Float getWeight() 
	{
		return weight;
	}
	

	@Override
	public boolean equals(final Object o)
	{
		if (o == null) return false;
		if (o == this) return true;

        if (getClass() != o.getClass()) return false;

		final SnField other = (SnField) o;
		return Objects.equals(getId(), other.getId())
			&& Objects.equals(getName(), other.getName())
			&& Objects.equals(getFieldType(), other.getFieldType())
			&& Objects.equals(getValueProvider(), other.getValueProvider())
			&& Objects.equals(getValueProviderParameters(), other.getValueProviderParameters())
			&& Objects.equals(getRetrievable(), other.getRetrievable())
			&& Objects.equals(getSearchable(), other.getSearchable())
			&& Objects.equals(getMultiValued(), other.getMultiValued())
			&& Objects.equals(getUseForSuggesting(), other.getUseForSuggesting())
			&& Objects.equals(getUseForSpellchecking(), other.getUseForSpellchecking())
			&& Objects.equals(getWeight(), other.getWeight());
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
		attribute = fieldType;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = valueProvider;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = valueProviderParameters;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = retrievable;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = searchable;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = multiValued;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = useForSuggesting;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = useForSpellchecking;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = weight;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());

		return result;
	}
}