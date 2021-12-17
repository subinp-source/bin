/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerticketingfacades.data;

import java.io.Serializable;
import java.util.Date;


import java.util.Objects;
/**
 * This Data Object is used to hold all the objects which can associate with the customer tickets
 */
public  class TicketAssociatedData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>TicketAssociatedData.code</code> property defined at extension <code>customerticketingfacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>TicketAssociatedData.modifiedtime</code> property defined at extension <code>customerticketingfacades</code>. */
		
	private Date modifiedtime;

	/** <i>Generated property</i> for <code>TicketAssociatedData.siteUid</code> property defined at extension <code>customerticketingfacades</code>. */
		
	private String siteUid;

	/** <i>Generated property</i> for <code>TicketAssociatedData.type</code> property defined at extension <code>customerticketingfacades</code>. */
		
	private String type;
	
	public TicketAssociatedData()
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
	
	public void setModifiedtime(final Date modifiedtime)
	{
		this.modifiedtime = modifiedtime;
	}

	public Date getModifiedtime() 
	{
		return modifiedtime;
	}
	
	public void setSiteUid(final String siteUid)
	{
		this.siteUid = siteUid;
	}

	public String getSiteUid() 
	{
		return siteUid;
	}
	
	public void setType(final String type)
	{
		this.type = type;
	}

	public String getType() 
	{
		return type;
	}
	

}