/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.frontend;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.frontend.UiCsticStatus;
import de.hybris.platform.sap.productconfig.frontend.UiGroupStatus;
import java.util.List;


import java.util.Objects;
public  class UiGroupStatus  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UiGroupStatus.id</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>UiGroupStatus.collapsed</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private boolean collapsed;

	/** <i>Generated property</i> for <code>UiGroupStatus.collapsedInSpecificationTree</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private boolean collapsedInSpecificationTree;

	/** <i>Generated property</i> for <code>UiGroupStatus.visited</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private boolean visited;

	/** <i>Generated property</i> for <code>UiGroupStatus.subGroups</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private List<UiGroupStatus> subGroups;

	/** <i>Generated property</i> for <code>UiGroupStatus.cstics</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private List<UiCsticStatus> cstics;
	
	public UiGroupStatus()
	{
		// default constructor
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setCollapsed(final boolean collapsed)
	{
		this.collapsed = collapsed;
	}

	public boolean isCollapsed() 
	{
		return collapsed;
	}
	
	public void setCollapsedInSpecificationTree(final boolean collapsedInSpecificationTree)
	{
		this.collapsedInSpecificationTree = collapsedInSpecificationTree;
	}

	public boolean isCollapsedInSpecificationTree() 
	{
		return collapsedInSpecificationTree;
	}
	
	public void setVisited(final boolean visited)
	{
		this.visited = visited;
	}

	public boolean isVisited() 
	{
		return visited;
	}
	
	public void setSubGroups(final List<UiGroupStatus> subGroups)
	{
		this.subGroups = subGroups;
	}

	public List<UiGroupStatus> getSubGroups() 
	{
		return subGroups;
	}
	
	public void setCstics(final List<UiCsticStatus> cstics)
	{
		this.cstics = cstics;
	}

	public List<UiCsticStatus> getCstics() 
	{
		return cstics;
	}
	

}