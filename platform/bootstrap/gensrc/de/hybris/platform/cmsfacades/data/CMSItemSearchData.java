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
public  class CMSItemSearchData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CMSItemSearchData.mask</code> property defined at extension <code>cmsfacades</code>. */
		
	private String mask;

	/** <i>Generated property</i> for <code>CMSItemSearchData.typeCode</code> property defined at extension <code>cmsfacades</code>. */
		
	private String typeCode;

	/** <i>Generated property</i> for <code>CMSItemSearchData.typeCodes</code> property defined at extension <code>cmsfacades</code>. */
		
	private String typeCodes;

	/** <i>Generated property</i> for <code>CMSItemSearchData.catalogId</code> property defined at extension <code>cmsfacades</code>. */
		
	private String catalogId;

	/** <i>Generated property</i> for <code>CMSItemSearchData.catalogVersion</code> property defined at extension <code>cmsfacades</code>. */
		
	private String catalogVersion;

	/** <i>Generated property</i> for <code>CMSItemSearchData.itemSearchParams</code> property defined at extension <code>cmsfacades</code>. */
		
	private String itemSearchParams;
	
	public CMSItemSearchData()
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
	
	public void setTypeCode(final String typeCode)
	{
		this.typeCode = typeCode;
	}

	public String getTypeCode() 
	{
		return typeCode;
	}
	
	public void setTypeCodes(final String typeCodes)
	{
		this.typeCodes = typeCodes;
	}

	public String getTypeCodes() 
	{
		return typeCodes;
	}
	
	public void setCatalogId(final String catalogId)
	{
		this.catalogId = catalogId;
	}

	public String getCatalogId() 
	{
		return catalogId;
	}
	
	public void setCatalogVersion(final String catalogVersion)
	{
		this.catalogVersion = catalogVersion;
	}

	public String getCatalogVersion() 
	{
		return catalogVersion;
	}
	
	public void setItemSearchParams(final String itemSearchParams)
	{
		this.itemSearchParams = itemSearchParams;
	}

	public String getItemSearchParams() 
	{
		return itemSearchParams;
	}
	

}