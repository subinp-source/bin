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


import java.util.Objects;
public  class PageTypeRestrictionTypeData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PageTypeRestrictionTypeData.pageType</code> property defined at extension <code>cmsfacades</code>. */
		
	private String pageType;

	/** <i>Generated property</i> for <code>PageTypeRestrictionTypeData.restrictionType</code> property defined at extension <code>cmsfacades</code>. */
		
	private String restrictionType;
	
	public PageTypeRestrictionTypeData()
	{
		// default constructor
	}
	
	public void setPageType(final String pageType)
	{
		this.pageType = pageType;
	}

	public String getPageType() 
	{
		return pageType;
	}
	
	public void setRestrictionType(final String restrictionType)
	{
		this.restrictionType = restrictionType;
	}

	public String getRestrictionType() 
	{
		return restrictionType;
	}
	

}