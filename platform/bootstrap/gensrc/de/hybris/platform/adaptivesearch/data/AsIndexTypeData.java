/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.data;

import java.io.Serializable;


import java.util.Objects;
public  class AsIndexTypeData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AsIndexTypeData.code</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>AsIndexTypeData.name</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>AsIndexTypeData.itemType</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String itemType;

	/** <i>Generated property</i> for <code>AsIndexTypeData.catalogVersionAware</code> property defined at extension <code>adaptivesearch</code>. */
		
	private boolean catalogVersionAware;
	
	public AsIndexTypeData()
	{
		// default constructor
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setItemType(final String itemType)
	{
		this.itemType = itemType;
	}

	public String getItemType() 
	{
		return itemType;
	}
	
	public void setCatalogVersionAware(final boolean catalogVersionAware)
	{
		this.catalogVersionAware = catalogVersionAware;
	}

	public boolean isCatalogVersionAware() 
	{
		return catalogVersionAware;
	}
	

	@Override
	public boolean equals(final Object o)
	{
		if (o == null) return false;
		if (o == this) return true;

        if (getClass() != o.getClass()) return false;

		final AsIndexTypeData other = (AsIndexTypeData) o;
		return Objects.equals(getCode(), other.getCode())
			&& Objects.equals(getName(), other.getName())
			&& Objects.equals(getItemType(), other.getItemType())
			&& Objects.equals(isCatalogVersionAware(), other.isCatalogVersionAware());
    }

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

		attribute = code;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = name;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = itemType;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = catalogVersionAware;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());

		return result;
	}
}