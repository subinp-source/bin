/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ordermanagementfacades.order.data;

import java.io.Serializable;
import de.hybris.platform.core.enums.OrderStatus;
import java.util.List;


import java.util.Objects;
public  class OrderStatusDataList  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderStatusDataList.statuses</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private List<OrderStatus> statuses;
	
	public OrderStatusDataList()
	{
		// default constructor
	}
	
	public void setStatuses(final List<OrderStatus> statuses)
	{
		this.statuses = statuses;
	}

	public List<OrderStatus> getStatuses() 
	{
		return statuses;
	}
	

}