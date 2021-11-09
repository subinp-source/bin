/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades;

import java.io.Serializable;
import java.util.List;


import java.util.Objects;
public  class CMSPageContentSlotListData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CMSPageContentSlotListData.pageId</code> property defined at extension <code>cmsfacades</code>. */
		
	private String pageId;

	/** <i>Generated property</i> for <code>CMSPageContentSlotListData.slotIds</code> property defined at extension <code>cmsfacades</code>. */
		
	private List<String> slotIds;
	
	public CMSPageContentSlotListData()
	{
		// default constructor
	}
	
	public void setPageId(final String pageId)
	{
		this.pageId = pageId;
	}

	public String getPageId() 
	{
		return pageId;
	}
	
	public void setSlotIds(final List<String> slotIds)
	{
		this.slotIds = slotIds;
	}

	public List<String> getSlotIds() 
	{
		return slotIds;
	}
	

}