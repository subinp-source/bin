/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.data;

import java.util.Map;


import java.util.Objects;
/**
 * @deprecated no longer needed
 */
@Deprecated(since = "6.6", forRemoval = true)
public  class EmailPageData extends AbstractPageData 
{

 

	/** <i>Generated property</i> for <code>EmailPageData.fromEmail</code> property defined at extension <code>cmsfacades</code>. */
		
	private Map<String,String> fromEmail;

	/** <i>Generated property</i> for <code>EmailPageData.fromName</code> property defined at extension <code>cmsfacades</code>. */
		
	private Map<String,String> fromName;
	
	public EmailPageData()
	{
		// default constructor
	}
	
	public void setFromEmail(final Map<String,String> fromEmail)
	{
		this.fromEmail = fromEmail;
	}

	public Map<String,String> getFromEmail() 
	{
		return fromEmail;
	}
	
	public void setFromName(final Map<String,String> fromName)
	{
		this.fromName = fromName;
	}

	public Map<String,String> getFromName() 
	{
		return fromName;
	}
	

}