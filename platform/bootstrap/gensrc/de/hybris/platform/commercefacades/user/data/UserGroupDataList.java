/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.user.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.user.data.UserGroupData;
import java.util.List;


import java.util.Objects;
public  class UserGroupDataList  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UserGroupDataList.userGroups</code> property defined at extension <code>commercefacades</code>. */
		
	private List<UserGroupData> userGroups;

	/** <i>Generated property</i> for <code>UserGroupDataList.totalNumber</code> property defined at extension <code>commercefacades</code>. */
		
	private Integer totalNumber;

	/** <i>Generated property</i> for <code>UserGroupDataList.pageSize</code> property defined at extension <code>commercefacades</code>. */
		
	private Integer pageSize;

	/** <i>Generated property</i> for <code>UserGroupDataList.numberOfPages</code> property defined at extension <code>commercefacades</code>. */
		
	private Integer numberOfPages;

	/** <i>Generated property</i> for <code>UserGroupDataList.currentPage</code> property defined at extension <code>commercefacades</code>. */
		
	private Integer currentPage;
	
	public UserGroupDataList()
	{
		// default constructor
	}
	
	public void setUserGroups(final List<UserGroupData> userGroups)
	{
		this.userGroups = userGroups;
	}

	public List<UserGroupData> getUserGroups() 
	{
		return userGroups;
	}
	
	public void setTotalNumber(final Integer totalNumber)
	{
		this.totalNumber = totalNumber;
	}

	public Integer getTotalNumber() 
	{
		return totalNumber;
	}
	
	public void setPageSize(final Integer pageSize)
	{
		this.pageSize = pageSize;
	}

	public Integer getPageSize() 
	{
		return pageSize;
	}
	
	public void setNumberOfPages(final Integer numberOfPages)
	{
		this.numberOfPages = numberOfPages;
	}

	public Integer getNumberOfPages() 
	{
		return numberOfPages;
	}
	
	public void setCurrentPage(final Integer currentPage)
	{
		this.currentPage = currentPage;
	}

	public Integer getCurrentPage() 
	{
		return currentPage;
	}
	

}