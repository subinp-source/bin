/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.data;

import java.io.Serializable;


import java.util.Objects;
public  class AsCatalogVersion  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AsCatalogVersion.catalogId</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String catalogId;

	/** <i>Generated property</i> for <code>AsCatalogVersion.version</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String version;
	
	public AsCatalogVersion()
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

		final AsCatalogVersion other = (AsCatalogVersion) o;
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