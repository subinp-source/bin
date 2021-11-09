/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.dto;

import com.hybris.yprofile.dto.AbstractProfileEvent;


import java.util.Objects;
public  class UID extends AbstractProfileEvent 
{

 

	/** <i>Generated property</i> for <code>UID.newUid</code> property defined at extension <code>profileservices</code>. */
		
	private String newUid;

	/** <i>Generated property</i> for <code>UID.originalUid</code> property defined at extension <code>profileservices</code>. */
		
	private String originalUid;
	
	public UID()
	{
		// default constructor
	}
	
	public void setNewUid(final String newUid)
	{
		this.newUid = newUid;
	}

	public String getNewUid() 
	{
		return newUid;
	}
	
	public void setOriginalUid(final String originalUid)
	{
		this.originalUid = originalUid;
	}

	public String getOriginalUid() 
	{
		return originalUid;
	}
	

}