/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.dto;

import com.hybris.yprofile.dto.AbstractProfileEvent;


import java.util.Objects;
public  class Order extends AbstractProfileEvent 
{

 

	/** <i>Generated property</i> for <code>Order.sessionId</code> property defined at extension <code>profileservices</code>. */
		
	private String sessionId;

	/** <i>Generated property</i> for <code>Order.channelRef</code> property defined at extension <code>profileservices</code>. */
		
	private String channelRef;

	/** <i>Generated property</i> for <code>Order.date</code> property defined at extension <code>profileservices</code>. */
		
	private String date;

	/** <i>Generated property</i> for <code>Order.type</code> property defined at extension <code>profileservices</code>. */
		
	private String type;

	/** <i>Generated property</i> for <code>Order.consumer</code> property defined at extension <code>profileservices</code>. */
		
	private Consumer consumer;

	/** <i>Generated property</i> for <code>Order.body</code> property defined at extension <code>profileservices</code>. */
		
	private OrderBody body;
	
	public Order()
	{
		// default constructor
	}
	
	public void setSessionId(final String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getSessionId() 
	{
		return sessionId;
	}
	
	public void setChannelRef(final String channelRef)
	{
		this.channelRef = channelRef;
	}

	public String getChannelRef() 
	{
		return channelRef;
	}
	
	public void setDate(final String date)
	{
		this.date = date;
	}

	public String getDate() 
	{
		return date;
	}
	
	public void setType(final String type)
	{
		this.type = type;
	}

	public String getType() 
	{
		return type;
	}
	
	public void setConsumer(final Consumer consumer)
	{
		this.consumer = consumer;
	}

	public Consumer getConsumer() 
	{
		return consumer;
	}
	
	public void setBody(final OrderBody body)
	{
		this.body = body;
	}

	public OrderBody getBody() 
	{
		return body;
	}
	

}