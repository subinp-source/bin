/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ordermanagementfacades.cancellation.data;

import java.io.Serializable;
import de.hybris.platform.ordermanagementfacades.cancellation.data.OrderCancelEntryData;
import java.util.List;


import java.util.Objects;
public  class OrderCancelRequestData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderCancelRequestData.orderCode</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String orderCode;

	/** <i>Generated property</i> for <code>OrderCancelRequestData.entries</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private List<OrderCancelEntryData> entries;

	/** <i>Generated property</i> for <code>OrderCancelRequestData.userId</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String userId;
	
	public OrderCancelRequestData()
	{
		// default constructor
	}
	
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

	public String getOrderCode() 
	{
		return orderCode;
	}
	
	public void setEntries(final List<OrderCancelEntryData> entries)
	{
		this.entries = entries;
	}

	public List<OrderCancelEntryData> getEntries() 
	{
		return entries;
	}
	
	public void setUserId(final String userId)
	{
		this.userId = userId;
	}

	public String getUserId() 
	{
		return userId;
	}
	

}