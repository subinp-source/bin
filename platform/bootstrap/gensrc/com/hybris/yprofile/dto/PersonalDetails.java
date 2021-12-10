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


import java.util.Objects;
public  class PersonalDetails  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PersonalDetails.firstName</code> property defined at extension <code>profileservices</code>. */
		
	private String firstName;

	/** <i>Generated property</i> for <code>PersonalDetails.lastName</code> property defined at extension <code>profileservices</code>. */
		
	private String lastName;

	/** <i>Generated property</i> for <code>PersonalDetails.title</code> property defined at extension <code>profileservices</code>. */
		
	private String title;
	
	public PersonalDetails()
	{
		// default constructor
	}
	
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	public String getFirstName() 
	{
		return firstName;
	}
	
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	public String getLastName() 
	{
		return lastName;
	}
	
	public void setTitle(final String title)
	{
		this.title = title;
	}

	public String getTitle() 
	{
		return title;
	}
	

}