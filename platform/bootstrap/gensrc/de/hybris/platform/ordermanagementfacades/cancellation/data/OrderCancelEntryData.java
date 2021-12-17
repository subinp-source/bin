/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ordermanagementfacades.cancellation.data;

import java.io.Serializable;
import de.hybris.platform.basecommerce.enums.CancelReason;


import java.util.Objects;
public  class OrderCancelEntryData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderCancelEntryData.orderEntryNumber</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private Integer orderEntryNumber;

	/** <i>Generated property</i> for <code>OrderCancelEntryData.cancelQuantity</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private Long cancelQuantity;

	/** <i>Generated property</i> for <code>OrderCancelEntryData.notes</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String notes;

	/** <i>Generated property</i> for <code>OrderCancelEntryData.cancelReason</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private CancelReason cancelReason;
	
	public OrderCancelEntryData()
	{
		// default constructor
	}
	
	public void setOrderEntryNumber(final Integer orderEntryNumber)
	{
		this.orderEntryNumber = orderEntryNumber;
	}

	public Integer getOrderEntryNumber() 
	{
		return orderEntryNumber;
	}
	
	public void setCancelQuantity(final Long cancelQuantity)
	{
		this.cancelQuantity = cancelQuantity;
	}

	public Long getCancelQuantity() 
	{
		return cancelQuantity;
	}
	
	public void setNotes(final String notes)
	{
		this.notes = notes;
	}

	public String getNotes() 
	{
		return notes;
	}
	
	public void setCancelReason(final CancelReason cancelReason)
	{
		this.cancelReason = cancelReason;
	}

	public CancelReason getCancelReason() 
	{
		return cancelReason;
	}
	

}