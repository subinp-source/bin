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
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import java.util.List;


import java.util.Objects;
public  class ClonePageData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ClonePageData.pageData</code> property defined at extension <code>cmsfacades</code>. */
		
	private AbstractPageData pageData;

	/** <i>Generated property</i> for <code>ClonePageData.cloneComponents</code> property defined at extension <code>cmsfacades</code>. */
		
	private boolean cloneComponents;

	/** <i>Generated property</i> for <code>ClonePageData.restrictions</code> property defined at extension <code>cmsfacades</code>. */
		
	private List<String> restrictions;
	
	public ClonePageData()
	{
		// default constructor
	}
	
	public void setPageData(final AbstractPageData pageData)
	{
		this.pageData = pageData;
	}

	public AbstractPageData getPageData() 
	{
		return pageData;
	}
	
	public void setCloneComponents(final boolean cloneComponents)
	{
		this.cloneComponents = cloneComponents;
	}

	public boolean isCloneComponents() 
	{
		return cloneComponents;
	}
	
	public void setRestrictions(final List<String> restrictions)
	{
		this.restrictions = restrictions;
	}

	public List<String> getRestrictions() 
	{
		return restrictions;
	}
	

}