/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.email.data;

import java.io.Serializable;
import java.util.List;


import java.util.Objects;
public  class EmailPageData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>EmailPageData.pageUid</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String pageUid;

	/** <i>Generated property</i> for <code>EmailPageData.jsPaths</code> property defined at extension <code>acceleratorservices</code>. */
		
	private List<String> jsPaths;

	/** <i>Generated property</i> for <code>EmailPageData.cssPaths</code> property defined at extension <code>acceleratorservices</code>. */
		
	private List<String> cssPaths;
	
	public EmailPageData()
	{
		// default constructor
	}
	
	public void setPageUid(final String pageUid)
	{
		this.pageUid = pageUid;
	}

	public String getPageUid() 
	{
		return pageUid;
	}
	
	public void setJsPaths(final List<String> jsPaths)
	{
		this.jsPaths = jsPaths;
	}

	public List<String> getJsPaths() 
	{
		return jsPaths;
	}
	
	public void setCssPaths(final List<String> cssPaths)
	{
		this.cssPaths = cssPaths;
	}

	public List<String> getCssPaths() 
	{
		return cssPaths;
	}
	

}