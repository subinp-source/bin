/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.data;

import java.io.Serializable;


import java.util.Objects;
public  class SnCatalogVersion  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SnCatalogVersion.catalogId</code> property defined at extension <code>searchservices</code>. */
		
	private String catalogId;

	/** <i>Generated property</i> for <code>SnCatalogVersion.version</code> property defined at extension <code>searchservices</code>. */
		
	private String version;
	
	public SnCatalogVersion()
	{
		// default constructor
	}
	
	public void setCatalogId(final String catalogId)
	{
		this.catalogId = catalogId;
	}

	public String getCatalogId() 
	{
		return catalogId;
	}
	
	public void setVersion(final String version)
	{
		this.version = version;
	}

	public String getVersion() 
	{
		return version;
	}
	

	@Override
	public boolean equals(final Object o)
	{
		if (o == null) return false;
		if (o == this) return true;

        if (getClass() != o.getClass()) return false;

		final SnCatalogVersion other = (SnCatalogVersion) o;
		return Objects.equals(getCatalogId(), other.getCatalogId())
			&& Objects.equals(getVersion(), other.getVersion());
    }

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

		attribute = catalogId;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = version;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());

		return result;
	}
}