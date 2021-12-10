/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
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
 * Representation of a Reset Password
 */
@ApiModel(value="ResetPassword", description="Representation of a Reset Password")
public  class ResetPasswordWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** token value which will be generated as unique string that will be sent with email to allow user for completing reset-password operation<br/><br/><i>Generated property</i> for <code>ResetPasswordWsDTO.token</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="token", value="token value which will be generated as unique string that will be sent with email to allow user for completing reset-password operation", required=true) 	
	private String token;

	/** new password string which is required to complete process of resetting password<br/><br/><i>Generated property</i> for <code>ResetPasswordWsDTO.newPassword</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="newPassword", value="new password string which is required to complete process of resetting password", required=true) 	
	private String newPassword;
	
	public ResetPasswordWsDTO()
	{
		// default constructor
	}
	
	public void setToken(final String token)
	{
		this.token = token;
	}

	public String getToken() 
	{
		return token;
	}
	
	public void setNewPassword(final String newPassword)
	{
		this.newPassword = newPassword;
	}

	public String getNewPassword() 
	{
		return newPassword;
	}
	

}