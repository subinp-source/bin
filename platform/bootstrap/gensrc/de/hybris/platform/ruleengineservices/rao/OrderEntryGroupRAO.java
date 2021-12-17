/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 15-Dec-2021, 3:07:46 PM
* ----------------------------------------------------------------
*
* Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.ruleengineservices.rao;

import java.util.Objects;
import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryGroupRAO;

public  class OrderEntryGroupRAO extends AbstractActionedRAO 
{

	/** <i>Generated property</i> for <code>OrderEntryGroupRAO.rootEntryGroup</code> property defined at extension <code>ruleengineservices</code>. */
	private OrderEntryGroupRAO rootEntryGroup;
	/** <i>Generated property</i> for <code>OrderEntryGroupRAO.entryGroupId</code> property defined at extension <code>ruleengineservices</code>. */
	private Integer entryGroupId;
	/** <i>Generated property</i> for <code>OrderEntryGroupRAO.externalReferenceId</code> property defined at extension <code>ruleengineservices</code>. */
	private String externalReferenceId;
	/** <i>Generated property</i> for <code>OrderEntryGroupRAO.groupType</code> property defined at extension <code>ruleengineservices</code>. */
	private String groupType;
		
	public OrderEntryGroupRAO()
	{
		// default constructor
	}
	
		public void setRootEntryGroup(final OrderEntryGroupRAO rootEntryGroup)
	{
		this.rootEntryGroup = rootEntryGroup;
	}
		public OrderEntryGroupRAO getRootEntryGroup() 
	{
		return rootEntryGroup;
	}
		
		public void setEntryGroupId(final Integer entryGroupId)
	{
		this.entryGroupId = entryGroupId;
	}
		public Integer getEntryGroupId() 
	{
		return entryGroupId;
	}
		
		public void setExternalReferenceId(final String externalReferenceId)
	{
		this.externalReferenceId = externalReferenceId;
	}
		public String getExternalReferenceId() 
	{
		return externalReferenceId;
	}
		
		public void setGroupType(final String groupType)
	{
		this.groupType = groupType;
	}
		public String getGroupType() 
	{
		return groupType;
	}
		
	
		@Override
	public boolean equals(final Object o)
	{
		
		if (o == null) return false;
		if (o == this) return true;

		if (getClass() != o.getClass()) return false;

		final OrderEntryGroupRAO other = (OrderEntryGroupRAO) o;
		return				Objects.equals(getEntryGroupId(), other.getEntryGroupId())
 &&  			Objects.equals(getExternalReferenceId(), other.getExternalReferenceId())
 &&  			Objects.equals(getGroupType(), other.getGroupType())
  ;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

				attribute = entryGroupId;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
				attribute = externalReferenceId;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
				attribute = groupType;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		
		return result;
	}
	}
