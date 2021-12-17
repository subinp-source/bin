/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.urlencoder.data;

import java.io.Serializable;


import java.util.Objects;
public  class UrlEncoderData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UrlEncoderData.attributeName</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private String attributeName;

	/** <i>Generated property</i> for <code>UrlEncoderData.currentValue</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private String currentValue;

	/** <i>Generated property</i> for <code>UrlEncoderData.defaultValue</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private String defaultValue;
	
	public UrlEncoderData()
	{
		// default constructor
	}
	
	public void setAttributeName(final String attributeName)
	{
		this.attributeName = attributeName;
	}

	public String getAttributeName() 
	{
		return attributeName;
	}
	
	public void setCurrentValue(final String currentValue)
	{
		this.currentValue = currentValue;
	}

	public String getCurrentValue() 
	{
		return currentValue;
	}
	
	public void setDefaultValue(final String defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() 
	{
		return defaultValue;
	}
	

}