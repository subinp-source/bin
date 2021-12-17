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
import java.util.List;


import java.util.Objects;
public  class SnFieldTypeInfo  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SnFieldTypeInfo.fieldType</code> property defined at extension <code>searchservices</code>. */
		
	private SnFieldType fieldType;

	/** <i>Generated property</i> for <code>SnFieldTypeInfo.valueType</code> property defined at extension <code>searchservices</code>. */
		
	private Class<?> valueType;

	/** <i>Generated property</i> for <code>SnFieldTypeInfo.facetable</code> property defined at extension <code>searchservices</code>. */
		
	private boolean facetable;

	/** <i>Generated property</i> for <code>SnFieldTypeInfo.sortable</code> property defined at extension <code>searchservices</code>. */
		
	private boolean sortable;

	/** <i>Generated property</i> for <code>SnFieldTypeInfo.searchable</code> property defined at extension <code>searchservices</code>. */
		
	private boolean searchable;

	/** <i>Generated property</i> for <code>SnFieldTypeInfo.groupable</code> property defined at extension <code>searchservices</code>. */
		
	private boolean groupable;

	/** <i>Generated property</i> for <code>SnFieldTypeInfo.supportedQueryTypes</code> property defined at extension <code>searchservices</code>. */
		
	private List<String> supportedQueryTypes;
	
	public SnFieldTypeInfo()
	{
		// default constructor
	}
	
	public void setFieldType(final SnFieldType fieldType)
	{
		this.fieldType = fieldType;
	}

	public SnFieldType getFieldType() 
	{
		return fieldType;
	}
	
	public void setValueType(final Class<?> valueType)
	{
		this.valueType = valueType;
	}

	public Class<?> getValueType() 
	{
		return valueType;
	}
	
	public void setFacetable(final boolean facetable)
	{
		this.facetable = facetable;
	}

	public boolean isFacetable() 
	{
		return facetable;
	}
	
	public void setSortable(final boolean sortable)
	{
		this.sortable = sortable;
	}

	public boolean isSortable() 
	{
		return sortable;
	}
	
	public void setSearchable(final boolean searchable)
	{
		this.searchable = searchable;
	}

	public boolean isSearchable() 
	{
		return searchable;
	}
	
	public void setGroupable(final boolean groupable)
	{
		this.groupable = groupable;
	}

	public boolean isGroupable() 
	{
		return groupable;
	}
	
	public void setSupportedQueryTypes(final List<String> supportedQueryTypes)
	{
		this.supportedQueryTypes = supportedQueryTypes;
	}

	public List<String> getSupportedQueryTypes() 
	{
		return supportedQueryTypes;
	}
	

	@Override
	public boolean equals(final Object o)
	{
		if (o == null) return false;
		if (o == this) return true;

        if (getClass() != o.getClass()) return false;

		final SnFieldTypeInfo other = (SnFieldTypeInfo) o;
		return Objects.equals(getFieldType(), other.getFieldType())
			&& Objects.equals(getValueType(), other.getValueType())
			&& Objects.equals(isFacetable(), other.isFacetable())
			&& Objects.equals(isSortable(), other.isSortable())
			&& Objects.equals(isSearchable(), other.isSearchable())
			&& Objects.equals(isGroupable(), other.isGroupable())
			&& Objects.equals(getSupportedQueryTypes(), other.getSupportedQueryTypes());
    }

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

		attribute = fieldType;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = valueType;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = facetable;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = sortable;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = searchable;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = groupable;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = supportedQueryTypes;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());

		return result;
	}
}