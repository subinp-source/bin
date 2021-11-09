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
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.UserGroupRAO;
import java.util.Set;

public  class UserRAO  implements Serializable 
{

	/** <i>Generated property</i> for <code>UserRAO.id</code> property defined at extension <code>ruleengineservices</code>. */
	private String id;
	/** <i>Generated property</i> for <code>UserRAO.pk</code> property defined at extension <code>ruleengineservices</code>. */
	private String pk;
	/** <i>Generated property</i> for <code>UserRAO.orders</code> property defined at extension <code>ruleengineservices</code>. */
	private Set<AbstractOrderRAO> orders;
	/** <i>Generated property</i> for <code>UserRAO.groups</code> property defined at extension <code>ruleengineservices</code>. */
	private Set<UserGroupRAO> groups;
		
	public UserRAO()
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
		
		public void setPk(final String pk)
	{
		this.pk = pk;
	}
		public String getPk() 
	{
		return pk;
	}
		
		public void setOrders(final Set<AbstractOrderRAO> orders)
	{
		this.orders = orders;
	}
		public Set<AbstractOrderRAO> getOrders() 
	{
		return orders;
	}
		
		public void setGroups(final Set<UserGroupRAO> groups)
	{
		this.groups = groups;
	}
		public Set<UserGroupRAO> getGroups() 
	{
		return groups;
	}
		
	
		@Override
	public boolean equals(final Object o)
	{
		
		if (o == null) return false;
		if (o == this) return true;

		if (getClass() != o.getClass()) return false;

		final UserRAO other = (UserRAO) o;
		return				Objects.equals(getId(), other.getId())
 &&  			Objects.equals(getPk(), other.getPk())
  ;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

				attribute = id;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
				attribute = pk;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		
		return result;
	}
	}
