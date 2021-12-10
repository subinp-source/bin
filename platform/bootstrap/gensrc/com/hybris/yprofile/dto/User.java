/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.dto;

import com.hybris.yprofile.dto.AbstractProfileEvent;


import java.util.Objects;
public  class User extends AbstractProfileEvent 
{

 

	/** <i>Generated property</i> for <code>User.channelRef</code> property defined at extension <code>profileservices</code>. */
		
	private String channelRef;

	/** <i>Generated property</i> for <code>User.date</code> property defined at extension <code>profileservices</code>. */
		
	private String date;

	/** <i>Generated property</i> for <code>User.type</code> property defined at extension <code>profileservices</code>. */
		
	private String type;

	/** <i>Generated property</i> for <code>User.sessionId</code> property defined at extension <code>profileservices</code>. */
		
	private String sessionId;

	/** <i>Generated property</i> for <code>User.body</code> property defined at extension <code>profileservices</code>. */
		
	private UserBody body;
	
	public User()
	{
		// default constructor
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
	
	public void setSessionId(final String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getSessionId() 
	{
		return sessionId;
	}
	
	public void setBody(final UserBody body)
	{
		this.body = body;
	}

	public UserBody getBody() 
	{
		return body;
	}
	

}