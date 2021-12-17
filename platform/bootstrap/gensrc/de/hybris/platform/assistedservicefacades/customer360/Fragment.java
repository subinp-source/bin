/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicefacades.customer360;

import java.io.Serializable;
import java.util.Map;


import java.util.Objects;
public  class Fragment  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>Fragment.title</code> property defined at extension <code>assistedservicefacades</code>. */
		
	private String title;

	/** <i>Generated property</i> for <code>Fragment.id</code> property defined at extension <code>assistedservicefacades</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>Fragment.jspPath</code> property defined at extension <code>assistedservicefacades</code>. */
		
	private String jspPath;

	/** <i>Generated property</i> for <code>Fragment.data</code> property defined at extension <code>assistedservicefacades</code>. */
		
	private Object data;

	/** <i>Generated property</i> for <code>Fragment.priority</code> property defined at extension <code>assistedservicefacades</code>. */
		
	private Integer priority;

	/** <i>Generated property</i> for <code>Fragment.properties</code> property defined at extension <code>assistedservicefacades</code>. */
		
	private Map<String,String> properties;
	
	public Fragment()
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
	
	public void setJspPath(final String jspPath)
	{
		this.jspPath = jspPath;
	}

	public String getJspPath() 
	{
		return jspPath;
	}
	
	public void setData(final Object data)
	{
		this.data = data;
	}

	public Object getData() 
	{
		return data;
	}
	
	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}

	public Integer getPriority() 
	{
		return priority;
	}
	
	public void setProperties(final Map<String,String> properties)
	{
		this.properties = properties;
	}

	public Map<String,String> getProperties() 
	{
		return properties;
	}
	

}