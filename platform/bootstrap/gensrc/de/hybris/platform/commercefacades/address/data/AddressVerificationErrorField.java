/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.address.data;

import java.io.Serializable;


import java.util.Objects;
public  class AddressVerificationErrorField  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AddressVerificationErrorField.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>AddressVerificationErrorField.missing</code> property defined at extension <code>commercefacades</code>. */
		
	private boolean missing;

	/** <i>Generated property</i> for <code>AddressVerificationErrorField.invalid</code> property defined at extension <code>commercefacades</code>. */
		
	private boolean invalid;
	
	public AddressVerificationErrorField()
	{
		// default constructor
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setMissing(final boolean missing)
	{
		this.missing = missing;
	}

	public boolean isMissing() 
	{
		return missing;
	}
	
	public void setInvalid(final boolean invalid)
	{
		this.invalid = invalid;
	}

	public boolean isInvalid() 
	{
		return invalid;
	}
	

}