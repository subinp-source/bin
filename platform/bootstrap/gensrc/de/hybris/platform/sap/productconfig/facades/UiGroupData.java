/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.FirstOrLastGroupType;
import de.hybris.platform.sap.productconfig.facades.GroupStatusType;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import java.util.List;


import java.util.Objects;
public  class UiGroupData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UiGroupData.id</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>UiGroupData.name</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>UiGroupData.description</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>UiGroupData.summaryText</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String summaryText;

	/** <i>Generated property</i> for <code>UiGroupData.collapsed</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean collapsed;

	/** <i>Generated property</i> for <code>UiGroupData.collapsedInSpecificationTree</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean collapsedInSpecificationTree;

	/** <i>Generated property</i> for <code>UiGroupData.configurable</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean configurable;

	/** <i>Generated property</i> for <code>UiGroupData.visited</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean visited;

	/** <i>Generated property</i> for <code>UiGroupData.complete</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean complete;

	/** <i>Generated property</i> for <code>UiGroupData.consistent</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean consistent;

	/** <i>Generated property</i> for <code>UiGroupData.groupType</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private GroupType groupType;

	/** <i>Generated property</i> for <code>UiGroupData.groupStatus</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private GroupStatusType groupStatus;

	/** <i>Generated property</i> for <code>UiGroupData.cstics</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<CsticData> cstics;

	/** <i>Generated property</i> for <code>UiGroupData.subGroups</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<UiGroupData> subGroups;

	/** <i>Generated property</i> for <code>UiGroupData.oneConfigurableSubGroup</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean oneConfigurableSubGroup;

	/** <i>Generated property</i> for <code>UiGroupData.numberErrorCstics</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private int numberErrorCstics;

	/** <i>Generated property</i> for <code>UiGroupData.firstOrLastGroup</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private FirstOrLastGroupType firstOrLastGroup;
	
	public UiGroupData()
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
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setSummaryText(final String summaryText)
	{
		this.summaryText = summaryText;
	}

	public String getSummaryText() 
	{
		return summaryText;
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
	
	public void setConfigurable(final boolean configurable)
	{
		this.configurable = configurable;
	}

	public boolean isConfigurable() 
	{
		return configurable;
	}
	
	public void setVisited(final boolean visited)
	{
		this.visited = visited;
	}

	public boolean isVisited() 
	{
		return visited;
	}
	
	public void setComplete(final boolean complete)
	{
		this.complete = complete;
	}

	public boolean isComplete() 
	{
		return complete;
	}
	
	public void setConsistent(final boolean consistent)
	{
		this.consistent = consistent;
	}

	public boolean isConsistent() 
	{
		return consistent;
	}
	
	public void setGroupType(final GroupType groupType)
	{
		this.groupType = groupType;
	}

	public GroupType getGroupType() 
	{
		return groupType;
	}
	
	public void setGroupStatus(final GroupStatusType groupStatus)
	{
		this.groupStatus = groupStatus;
	}

	public GroupStatusType getGroupStatus() 
	{
		return groupStatus;
	}
	
	public void setCstics(final List<CsticData> cstics)
	{
		this.cstics = cstics;
	}

	public List<CsticData> getCstics() 
	{
		return cstics;
	}
	
	public void setSubGroups(final List<UiGroupData> subGroups)
	{
		this.subGroups = subGroups;
	}

	public List<UiGroupData> getSubGroups() 
	{
		return subGroups;
	}
	
	public void setOneConfigurableSubGroup(final boolean oneConfigurableSubGroup)
	{
		this.oneConfigurableSubGroup = oneConfigurableSubGroup;
	}

	public boolean isOneConfigurableSubGroup() 
	{
		return oneConfigurableSubGroup;
	}
	
	public void setNumberErrorCstics(final int numberErrorCstics)
	{
		this.numberErrorCstics = numberErrorCstics;
	}

	public int getNumberErrorCstics() 
	{
		return numberErrorCstics;
	}
	
	public void setFirstOrLastGroup(final FirstOrLastGroupType firstOrLastGroup)
	{
		this.firstOrLastGroup = firstOrLastGroup;
	}

	public FirstOrLastGroupType getFirstOrLastGroup() 
	{
		return firstOrLastGroup;
	}
	

}