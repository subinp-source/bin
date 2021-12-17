/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.data;

import java.io.Serializable;


import java.util.Objects;
public  class BaseSegmentData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Segment code<br/><br/><i>Generated property</i> for <code>BaseSegmentData.code</code> property defined at extension <code>personalizationservices</code>. */
		
	private String code;

	/** Segment description<br/><br/><i>Generated property</i> for <code>BaseSegmentData.description</code> property defined at extension <code>personalizationservices</code>. */
		
	private String description;

	/** Segment provider<br/><br/><i>Generated property</i> for <code>BaseSegmentData.provider</code> property defined at extension <code>personalizationservices</code>. */
		
	private String provider;
	
	public BaseSegmentData()
	{
		// default constructor
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setProvider(final String provider)
	{
		this.provider = provider;
	}

	public String getProvider() 
	{
		return provider;
	}
	

}