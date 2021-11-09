/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.user;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of an UserSignUp. Consists of fields required to register new customer
 */
@ApiModel(value="UserSignUp", description="Representation of an UserSignUp. Consists of fields required to register new customer")
public  class UserSignUpWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** user id, unique string required to create new user. It can be email<br/><br/><i>Generated property</i> for <code>UserSignUpWsDTO.uid</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="uid", value="user id, unique string required to create new user. It can be email", required=true) 	
	private String uid;

	/** first name of the user<br/><br/><i>Generated property</i> for <code>UserSignUpWsDTO.firstName</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="firstName", value="first name of the user", required=true) 	
	private String firstName;

	/** last name of the user<br/><br/><i>Generated property</i> for <code>UserSignUpWsDTO.lastName</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="lastName", value="last name of the user", required=true) 	
	private String lastName;

	/** <i>Generated property</i> for <code>UserSignUpWsDTO.titleCode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="titleCode") 	
	private String titleCode;

	/** user password<br/><br/><i>Generated property</i> for <code>UserSignUpWsDTO.password</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="password", value="user password", required=true) 	
	private String password;
	
	public UserSignUpWsDTO()
	{
		// default constructor
	}
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	public String getUid() 
	{
		return uid;
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
	
	public void setTitleCode(final String titleCode)
	{
		this.titleCode = titleCode;
	}

	public String getTitleCode() 
	{
		return titleCode;
	}
	
	public void setPassword(final String password)
	{
		this.password = password;
	}

	public String getPassword() 
	{
		return password;
	}
	

}