/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.dto;

import java.io.Serializable;
import java.util.List;


import java.util.Objects;
public  class UserBody  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UserBody.date</code> property defined at extension <code>profileservices</code>. */
		
	private String date;

	/** <i>Generated property</i> for <code>UserBody.identity</code> property defined at extension <code>profileservices</code>. */
		
	private Consumer identity;

	/** <i>Generated property</i> for <code>UserBody.identities</code> property defined at extension <code>profileservices</code>. */
		
	private List<Consumer> identities;

	/** <i>Generated property</i> for <code>UserBody.type</code> property defined at extension <code>profileservices</code>. */
		
	private String type;

	/** <i>Generated property</i> for <code>UserBody.masterData</code> property defined at extension <code>profileservices</code>. */
		
	private UserMasterData masterData;

	/** <i>Generated property</i> for <code>UserBody.personalDetails</code> property defined at extension <code>profileservices</code>. */
		
	private PersonalDetails personalDetails;
	
	public UserBody()
	{
		// default constructor
	}
	
	public void setDate(final String date)
	{
		this.date = date;
	}

	public String getDate() 
	{
		return date;
	}
	
	public void setIdentity(final Consumer identity)
	{
		this.identity = identity;
	}

	public Consumer getIdentity() 
	{
		return identity;
	}
	
	public void setIdentities(final List<Consumer> identities)
	{
		this.identities = identities;
	}

	public List<Consumer> getIdentities() 
	{
		return identities;
	}
	
	public void setType(final String type)
	{
		this.type = type;
	}

	public String getType() 
	{
		return type;
	}
	
	public void setMasterData(final UserMasterData masterData)
	{
		this.masterData = masterData;
	}

	public UserMasterData getMasterData() 
	{
		return masterData;
	}
	
	public void setPersonalDetails(final PersonalDetails personalDetails)
	{
		this.personalDetails = personalDetails;
	}

	public PersonalDetails getPersonalDetails() 
	{
		return personalDetails;
	}
	

}