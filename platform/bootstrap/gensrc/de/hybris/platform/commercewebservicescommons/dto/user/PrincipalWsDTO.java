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
 * Representation of a Principal webservice DTO used for defining User data types
 */
@ApiModel(value="Principal", description="Representation of a Principal webservice DTO used for defining User data types")
public  class PrincipalWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Unique user identifier<br/><br/><i>Generated property</i> for <code>PrincipalWsDTO.uid</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="uid", value="Unique user identifier") 	
	private String uid;

	/** Name of the user<br/><br/><i>Generated property</i> for <code>PrincipalWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the user") 	
	private String name;
	
	public PrincipalWsDTO()
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
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	

}