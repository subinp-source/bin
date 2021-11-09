/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import java.util.List;


import java.util.Objects;
public  class OrderHistoriesData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderHistoriesData.orders</code> property defined at extension <code>commercefacades</code>. */
		
	private List<OrderHistoryData> orders;

	/** <i>Generated property</i> for <code>OrderHistoriesData.sorts</code> property defined at extension <code>commercefacades</code>. */
		
	private List<SortData> sorts;

	/** <i>Generated property</i> for <code>OrderHistoriesData.pagination</code> property defined at extension <code>commercefacades</code>. */
		
	private PaginationData pagination;
	
	public OrderHistoriesData()
	{
		// default constructor
	}
	
	public void setOrders(final List<OrderHistoryData> orders)
	{
		this.orders = orders;
	}

	public List<OrderHistoryData> getOrders() 
	{
		return orders;
	}
	
	public void setSorts(final List<SortData> sorts)
	{
		this.sorts = sorts;
	}

	public List<SortData> getSorts() 
	{
		return sorts;
	}
	
	public void setPagination(final PaginationData pagination)
	{
		this.pagination = pagination;
	}

	public PaginationData getPagination() 
	{
		return pagination;
	}
	

}