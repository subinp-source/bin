/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerticketingfacades.data;

import java.io.Serializable;


import java.util.Objects;
public  class TicketEventAttachmentData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>TicketEventAttachmentData.filename</code> property defined at extension <code>customerticketingfacades</code>. */
		
	private String filename;

	/** <i>Generated property</i> for <code>TicketEventAttachmentData.URL</code> property defined at extension <code>customerticketingfacades</code>. */
		
	private String URL;
	
	public TicketEventAttachmentData()
	{
		// default constructor
	}
	
	public void setFilename(final String filename)
	{
		this.filename = filename;
	}

	public String getFilename() 
	{
		return filename;
	}
	
	public void setURL(final String URL)
	{
		this.URL = URL;
	}

	public String getURL() 
	{
		return URL;
	}
	

}