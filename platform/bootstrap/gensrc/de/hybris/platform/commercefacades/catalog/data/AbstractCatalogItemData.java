/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.catalog.data;

import java.io.Serializable;
import java.util.Date;


import java.util.Objects;
public  class AbstractCatalogItemData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AbstractCatalogItemData.id</code> property defined at extension <code>commercefacades</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>AbstractCatalogItemData.lastModified</code> property defined at extension <code>commercefacades</code>. */
		
	private Date lastModified;

	/** <i>Generated property</i> for <code>AbstractCatalogItemData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>AbstractCatalogItemData.url</code> property defined at extension <code>commercefacades</code>. */
		
	private String url;
	
	public AbstractCatalogItemData()
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
	
	public void setLastModified(final Date lastModified)
	{
		this.lastModified = lastModified;
	}

	public Date getLastModified() 
	{
		return lastModified;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setUrl(final String url)
	{
		this.url = url;
	}

	public String getUrl() 
	{
		return url;
	}
	

}