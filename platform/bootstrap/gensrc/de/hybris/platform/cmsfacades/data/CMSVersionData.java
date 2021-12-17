/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Date;


import java.util.Objects;
public  class CMSVersionData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CMSVersionData.uid</code> property defined at extension <code>cmsfacades</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>CMSVersionData.itemUUID</code> property defined at extension <code>cmsfacades</code>. */
		
	private String itemUUID;

	/** <i>Generated property</i> for <code>CMSVersionData.label</code> property defined at extension <code>cmsfacades</code>. */
		
	private String label;

	/** <i>Generated property</i> for <code>CMSVersionData.description</code> property defined at extension <code>cmsfacades</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>CMSVersionData.creationtime</code> property defined at extension <code>cmsfacades</code>. */
		
	private Date creationtime;
	
	public CMSVersionData()
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
	
	public void setItemUUID(final String itemUUID)
	{
		this.itemUUID = itemUUID;
	}

	public String getItemUUID() 
	{
		return itemUUID;
	}
	
	public void setLabel(final String label)
	{
		this.label = label;
	}

	public String getLabel() 
	{
		return label;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setCreationtime(final Date creationtime)
	{
		this.creationtime = creationtime;
	}

	public Date getCreationtime() 
	{
		return creationtime;
	}
	

}