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
import java.util.Locale;
import java.util.Map;


import java.util.Objects;
public  class SnExpressionInfo  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SnExpressionInfo.expression</code> property defined at extension <code>searchservices</code>. */
		
	private String expression;

	/** <i>Generated property</i> for <code>SnExpressionInfo.name</code> property defined at extension <code>searchservices</code>. */
		
	private Map<Locale,String> name;
	
	public SnExpressionInfo()
	{
		// default constructor
	}
	
	public void setExpression(final String expression)
	{
		this.expression = expression;
	}

	public String getExpression() 
	{
		return expression;
	}
	
	public void setName(final Map<Locale,String> name)
	{
		this.name = name;
	}

	public Map<Locale,String> getName() 
	{
		return name;
	}
	

	@Override
	public boolean equals(final Object o)
	{
		if (o == null) return false;
		if (o == this) return true;

        if (getClass() != o.getClass()) return false;

		final SnExpressionInfo other = (SnExpressionInfo) o;
		return Objects.equals(getExpression(), other.getExpression())
			&& Objects.equals(getName(), other.getName());
    }

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

		attribute = expression;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = name;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());

		return result;
	}
}