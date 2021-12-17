/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Set;


import java.util.Objects;
public  class UserData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UserData.uid</code> property defined at extension <code>cmsfacades</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>UserData.readableLanguages</code> property defined at extension <code>cmsfacades</code>. */
		
	private Set<String> readableLanguages;

	/** <i>Generated property</i> for <code>UserData.writeableLanguages</code> property defined at extension <code>cmsfacades</code>. */
		
	private Set<String> writeableLanguages;
	
	public UserData()
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
	
	public void setReadableLanguages(final Set<String> readableLanguages)
	{
		this.readableLanguages = readableLanguages;
	}

	public Set<String> getReadableLanguages() 
	{
		return readableLanguages;
	}
	
	public void setWriteableLanguages(final Set<String> writeableLanguages)
	{
		this.writeableLanguages = writeableLanguages;
	}

	public Set<String> getWriteableLanguages() 
	{
		return writeableLanguages;
	}
	

}