/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 08-Nov-2021, 4:51:27 PM
* ----------------------------------------------------------------
*
* Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Objects;

public  class UserGroupRAO  implements Serializable 
{

	/** <i>Generated property</i> for <code>UserGroupRAO.id</code> property defined at extension <code>ruleengineservices</code>. */
	private String id;
		
	public UserGroupRAO()
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
		
	
		@Override
	public boolean equals(final Object o)
	{
		
		if (o == null) return false;
		if (o == this) return true;

		if (getClass() != o.getClass()) return false;

		final UserGroupRAO other = (UserGroupRAO) o;
		return				Objects.equals(getId(), other.getId())
  ;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

				attribute = id;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		
		return result;
	}
	}
