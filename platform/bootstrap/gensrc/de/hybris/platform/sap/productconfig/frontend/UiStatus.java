/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.frontend;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.frontend.FilterData;
import de.hybris.platform.sap.productconfig.frontend.UiGroupStatus;
import java.util.List;
import java.util.Map;
import org.springframework.validation.FieldError;


import java.util.Objects;
public  class UiStatus  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UiStatus.specificationTreeCollapsed</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private boolean specificationTreeCollapsed;

	/** <i>Generated property</i> for <code>UiStatus.quantity</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private long quantity;

	/** <i>Generated property</i> for <code>UiStatus.priceSummaryCollapsed</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private boolean priceSummaryCollapsed;

	/** <i>Generated property</i> for <code>UiStatus.hideImageGallery</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private boolean hideImageGallery;

	/** <i>Generated property</i> for <code>UiStatus.groupIdToDisplay</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String groupIdToDisplay;

	/** <i>Generated property</i> for <code>UiStatus.lastNoneConflictGroupId</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String lastNoneConflictGroupId;

	/** <i>Generated property</i> for <code>UiStatus.userInputToRemember</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private Map<String,FieldError> userInputToRemember;

	/** <i>Generated property</i> for <code>UiStatus.userInputToRestore</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private Map<String,FieldError> userInputToRestore;

	/** <i>Generated property</i> for <code>UiStatus.groups</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private List<UiGroupStatus> groups;

	/** <i>Generated property</i> for <code>UiStatus.numberOfConflictsToDisplay</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private int numberOfConflictsToDisplay;

	/** <i>Generated property</i> for <code>UiStatus.csticFilterList</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private List<FilterData> csticFilterList;

	/** <i>Generated property</i> for <code>UiStatus.maxGroupFilterList</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private List<FilterData> maxGroupFilterList;

	/** <i>Generated property</i> for <code>UiStatus.firstErrorCsticId</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String firstErrorCsticId;
	
	public UiStatus()
	{
		// default constructor
	}
	
	public void setSpecificationTreeCollapsed(final boolean specificationTreeCollapsed)
	{
		this.specificationTreeCollapsed = specificationTreeCollapsed;
	}

	public boolean isSpecificationTreeCollapsed() 
	{
		return specificationTreeCollapsed;
	}
	
	public void setQuantity(final long quantity)
	{
		this.quantity = quantity;
	}

	public long getQuantity() 
	{
		return quantity;
	}
	
	public void setPriceSummaryCollapsed(final boolean priceSummaryCollapsed)
	{
		this.priceSummaryCollapsed = priceSummaryCollapsed;
	}

	public boolean isPriceSummaryCollapsed() 
	{
		return priceSummaryCollapsed;
	}
	
	public void setHideImageGallery(final boolean hideImageGallery)
	{
		this.hideImageGallery = hideImageGallery;
	}

	public boolean isHideImageGallery() 
	{
		return hideImageGallery;
	}
	
	public void setGroupIdToDisplay(final String groupIdToDisplay)
	{
		this.groupIdToDisplay = groupIdToDisplay;
	}

	public String getGroupIdToDisplay() 
	{
		return groupIdToDisplay;
	}
	
	public void setLastNoneConflictGroupId(final String lastNoneConflictGroupId)
	{
		this.lastNoneConflictGroupId = lastNoneConflictGroupId;
	}

	public String getLastNoneConflictGroupId() 
	{
		return lastNoneConflictGroupId;
	}
	
	public void setUserInputToRemember(final Map<String,FieldError> userInputToRemember)
	{
		this.userInputToRemember = userInputToRemember;
	}

	public Map<String,FieldError> getUserInputToRemember() 
	{
		return userInputToRemember;
	}
	
	public void setUserInputToRestore(final Map<String,FieldError> userInputToRestore)
	{
		this.userInputToRestore = userInputToRestore;
	}

	public Map<String,FieldError> getUserInputToRestore() 
	{
		return userInputToRestore;
	}
	
	public void setGroups(final List<UiGroupStatus> groups)
	{
		this.groups = groups;
	}

	public List<UiGroupStatus> getGroups() 
	{
		return groups;
	}
	
	public void setNumberOfConflictsToDisplay(final int numberOfConflictsToDisplay)
	{
		this.numberOfConflictsToDisplay = numberOfConflictsToDisplay;
	}

	public int getNumberOfConflictsToDisplay() 
	{
		return numberOfConflictsToDisplay;
	}
	
	public void setCsticFilterList(final List<FilterData> csticFilterList)
	{
		this.csticFilterList = csticFilterList;
	}

	public List<FilterData> getCsticFilterList() 
	{
		return csticFilterList;
	}
	
	public void setMaxGroupFilterList(final List<FilterData> maxGroupFilterList)
	{
		this.maxGroupFilterList = maxGroupFilterList;
	}

	public List<FilterData> getMaxGroupFilterList() 
	{
		return maxGroupFilterList;
	}
	
	public void setFirstErrorCsticId(final String firstErrorCsticId)
	{
		this.firstErrorCsticId = firstErrorCsticId;
	}

	public String getFirstErrorCsticId() 
	{
		return firstErrorCsticId;
	}
	

}