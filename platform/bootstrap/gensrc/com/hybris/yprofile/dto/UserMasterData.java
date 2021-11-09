/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.dto;

import java.io.Serializable;
import java.util.List;


import java.util.Objects;
public  class UserMasterData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UserMasterData.address</code> property defined at extension <code>profileservices</code>. */
		
	private Address address;

	/** <i>Generated property</i> for <code>UserMasterData.addresses</code> property defined at extension <code>profileservices</code>. */
		
	private List<Address> addresses;
	
	public UserMasterData()
	{
		// default constructor
	}
	
	public void setAddress(final Address address)
	{
		this.address = address;
	}

	public Address getAddress() 
	{
		return address;
	}
	
	public void setAddresses(final List<Address> addresses)
	{
		this.addresses = addresses;
	}

	public List<Address> getAddresses() 
	{
		return addresses;
	}
	

}