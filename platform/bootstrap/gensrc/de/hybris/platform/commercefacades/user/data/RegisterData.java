/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.user.data;

import java.io.Serializable;


import java.util.Objects;
public  class RegisterData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>RegisterData.login</code> property defined at extension <code>commercefacades</code>. */
		
	private String login;

	/** <i>Generated property</i> for <code>RegisterData.password</code> property defined at extension <code>commercefacades</code>. */
		
	private String password;

	/** <i>Generated property</i> for <code>RegisterData.titleCode</code> property defined at extension <code>commercefacades</code>. */
		
	private String titleCode;

	/** <i>Generated property</i> for <code>RegisterData.firstName</code> property defined at extension <code>commercefacades</code>. */
		
	private String firstName;

	/** <i>Generated property</i> for <code>RegisterData.lastName</code> property defined at extension <code>commercefacades</code>. */
		
	private String lastName;
	
	public RegisterData()
	{
		// default constructor
	}
	
	public void setLogin(final String login)
	{
		this.login = login;
	}

	public String getLogin() 
	{
		return login;
	}
	
	public void setPassword(final String password)
	{
		this.password = password;
	}

	public String getPassword() 
	{
		return password;
	}
	
	public void setTitleCode(final String titleCode)
	{
		this.titleCode = titleCode;
	}

	public String getTitleCode() 
	{
		return titleCode;
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
	

}