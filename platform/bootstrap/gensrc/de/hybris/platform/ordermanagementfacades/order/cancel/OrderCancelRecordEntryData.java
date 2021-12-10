/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ordermanagementfacades.order.cancel;

import java.io.Serializable;
import de.hybris.platform.basecommerce.enums.CancelReason;


import java.util.Objects;
public  class OrderCancelRecordEntryData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderCancelRecordEntryData.cancelResult</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String cancelResult;

	/** <i>Generated property</i> for <code>OrderCancelRecordEntryData.refusedMessage</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String refusedMessage;

	/** <i>Generated property</i> for <code>OrderCancelRecordEntryData.cancelReason</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private CancelReason cancelReason;
	
	public OrderCancelRecordEntryData()
	{
		// default constructor
	}
	
	public void setCancelResult(final String cancelResult)
	{
		this.cancelResult = cancelResult;
	}

	public String getCancelResult() 
	{
		return cancelResult;
	}
	
	public void setRefusedMessage(final String refusedMessage)
	{
		this.refusedMessage = refusedMessage;
	}

	public String getRefusedMessage() 
	{
		return refusedMessage;
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