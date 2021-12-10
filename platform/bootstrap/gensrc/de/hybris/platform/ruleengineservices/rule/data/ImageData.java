/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengineservices.rule.data;

import java.io.Serializable;


import java.util.Objects;
public  class ImageData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ImageData.url</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String url;

	/** <i>Generated property</i> for <code>ImageData.altText</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String altText;

	/** <i>Generated property</i> for <code>ImageData.format</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String format;
	
	public ImageData()
	{
		// default constructor
	}
	
	public void setUrl(final String url)
	{
		this.url = url;
	}

	public String getUrl() 
	{
		return url;
	}
	
	public void setAltText(final String altText)
	{
		this.altText = altText;
	}

	public String getAltText() 
	{
		return altText;
	}
	
	public void setFormat(final String format)
	{
		this.format = format;
	}

	public String getFormat() 
	{
		return format;
	}
	

}