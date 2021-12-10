/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:32:59 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;


import java.util.Objects;
public  class UiGroupForDisplayData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UiGroupForDisplayData.group</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private UiGroupData group;

	/** <i>Generated property</i> for <code>UiGroupForDisplayData.path</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String path;

	/** <i>Generated property</i> for <code>UiGroupForDisplayData.groupIdPath</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String groupIdPath;
	
	public UiGroupForDisplayData()
	{
		// default constructor
	}
	
	public void setGroup(final UiGroupData group)
	{
		this.group = group;
	}

	public UiGroupData getGroup() 
	{
		return group;
	}
	
	public void setPath(final String path)
	{
		this.path = path;
	}

	public String getPath() 
	{
		return path;
	}
	
	public void setGroupIdPath(final String groupIdPath)
	{
		this.groupIdPath = groupIdPath;
	}

	public String getGroupIdPath() 
	{
		return groupIdPath;
	}
	

}