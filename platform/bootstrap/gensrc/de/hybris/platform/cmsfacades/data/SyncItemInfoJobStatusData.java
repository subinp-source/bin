/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import de.hybris.platform.core.model.ItemModel;


import java.util.Objects;
public  class SyncItemInfoJobStatusData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SyncItemInfoJobStatusData.item</code> property defined at extension <code>cmsfacades</code>. */
		
	private ItemModel item;

	/** <i>Generated property</i> for <code>SyncItemInfoJobStatusData.syncStatus</code> property defined at extension <code>cmsfacades</code>. */
		
	private String syncStatus;
	
	public SyncItemInfoJobStatusData()
	{
		// default constructor
	}
	
	public void setItem(final ItemModel item)
	{
		this.item = item;
	}

	public ItemModel getItem() 
	{
		return item;
	}
	
	public void setSyncStatus(final String syncStatus)
	{
		this.syncStatus = syncStatus;
	}

	public String getSyncStatus() 
	{
		return syncStatus;
	}
	

}