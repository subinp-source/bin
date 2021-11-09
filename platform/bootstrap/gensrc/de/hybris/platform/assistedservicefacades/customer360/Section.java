/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicefacades.customer360;

import java.io.Serializable;
import de.hybris.platform.assistedservicefacades.customer360.Fragment;
import java.util.List;


import java.util.Objects;
public  class Section  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>Section.title</code> property defined at extension <code>assistedservicefacades</code>. */
		
	private String title;

	/** <i>Generated property</i> for <code>Section.id</code> property defined at extension <code>assistedservicefacades</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>Section.fragments</code> property defined at extension <code>assistedservicefacades</code>. */
		
	private List<Fragment> fragments;

	/** <i>Generated property</i> for <code>Section.priority</code> property defined at extension <code>assistedservicefacades</code>. */
		
	private Integer priority;
	
	public Section()
	{
		// default constructor
	}
	
	public void setTitle(final String title)
	{
		this.title = title;
	}

	public String getTitle() 
	{
		return title;
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setFragments(final List<Fragment> fragments)
	{
		this.fragments = fragments;
	}

	public List<Fragment> getFragments() 
	{
		return fragments;
	}
	
	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}

	public Integer getPriority() 
	{
		return priority;
	}
	

}