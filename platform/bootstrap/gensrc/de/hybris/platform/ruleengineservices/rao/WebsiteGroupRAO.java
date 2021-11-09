/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 08-Nov-2021, 4:51:28 PM
* ----------------------------------------------------------------
*
* Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Objects;

public  class WebsiteGroupRAO  implements Serializable 
{

	/** <i>Generated property</i> for <code>WebsiteGroupRAO.id</code> property defined at extension <code>ruleengineservices</code>. */
	private String id;
		
	public WebsiteGroupRAO()
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

		final WebsiteGroupRAO other = (WebsiteGroupRAO) o;
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
