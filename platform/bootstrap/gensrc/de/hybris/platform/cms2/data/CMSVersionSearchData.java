/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.data;

import java.io.Serializable;


import java.util.Objects;
public  class CMSVersionSearchData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CMSVersionSearchData.mask</code> property defined at extension <code>cms2</code>. */
		
	private String mask;

	/** <i>Generated property</i> for <code>CMSVersionSearchData.itemUid</code> property defined at extension <code>cms2</code>. */
		
	private String itemUid;

	/** <i>Generated property</i> for <code>CMSVersionSearchData.itemCatalogId</code> property defined at extension <code>cms2</code>. */
		
	private String itemCatalogId;

	/** <i>Generated property</i> for <code>CMSVersionSearchData.itemCatalogVersion</code> property defined at extension <code>cms2</code>. */
		
	private String itemCatalogVersion;
	
	public CMSVersionSearchData()
	{
		// default constructor
	}
	
	public void setMask(final String mask)
	{
		this.mask = mask;
	}

	public String getMask() 
	{
		return mask;
	}
	
	public void setItemUid(final String itemUid)
	{
		this.itemUid = itemUid;
	}

	public String getItemUid() 
	{
		return itemUid;
	}
	
	public void setItemCatalogId(final String itemCatalogId)
	{
		this.itemCatalogId = itemCatalogId;
	}

	public String getItemCatalogId() 
	{
		return itemCatalogId;
	}
	
	public void setItemCatalogVersion(final String itemCatalogVersion)
	{
		this.itemCatalogVersion = itemCatalogVersion;
	}

	public String getItemCatalogVersion() 
	{
		return itemCatalogVersion;
	}
	

}