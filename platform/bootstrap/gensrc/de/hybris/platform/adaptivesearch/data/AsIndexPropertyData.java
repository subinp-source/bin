/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.data;

import java.io.Serializable;
import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import java.util.Set;


import java.util.Objects;
public  class AsIndexPropertyData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AsIndexPropertyData.code</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>AsIndexPropertyData.name</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>AsIndexPropertyData.type</code> property defined at extension <code>adaptivesearch</code>. */
		
	private Class<?> type;

	/** <i>Generated property</i> for <code>AsIndexPropertyData.supportedBoostOperators</code> property defined at extension <code>adaptivesearch</code>. */
		
	private Set<AsBoostOperator> supportedBoostOperators;
	
	public AsIndexPropertyData()
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
	
	public void setType(final Class<?> type)
	{
		this.type = type;
	}

	public Class<?> getType() 
	{
		return type;
	}
	
	public void setSupportedBoostOperators(final Set<AsBoostOperator> supportedBoostOperators)
	{
		this.supportedBoostOperators = supportedBoostOperators;
	}

	public Set<AsBoostOperator> getSupportedBoostOperators() 
	{
		return supportedBoostOperators;
	}
	

	@Override
	public boolean equals(final Object o)
	{
		if (o == null) return false;
		if (o == this) return true;

        if (getClass() != o.getClass()) return false;

		final AsIndexPropertyData other = (AsIndexPropertyData) o;
		return Objects.equals(getCode(), other.getCode())
			&& Objects.equals(getName(), other.getName())
			&& Objects.equals(getType(), other.getType())
			&& Objects.equals(getSupportedBoostOperators(), other.getSupportedBoostOperators());
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
		attribute = type;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = supportedBoostOperators;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());

		return result;
	}
}