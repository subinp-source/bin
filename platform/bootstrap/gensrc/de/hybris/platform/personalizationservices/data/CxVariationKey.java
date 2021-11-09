/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.data;

import java.io.Serializable;


import java.util.Objects;
public  class CxVariationKey  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CxVariationKey.variationCode</code> property defined at extension <code>personalizationservices</code>. */
		
	private String variationCode;

	/** <i>Generated property</i> for <code>CxVariationKey.customizationCode</code> property defined at extension <code>personalizationservices</code>. */
		
	private String customizationCode;

	/** <i>Generated property</i> for <code>CxVariationKey.catalog</code> property defined at extension <code>personalizationservices</code>. */
		
	private String catalog;

	/** <i>Generated property</i> for <code>CxVariationKey.catalogVersion</code> property defined at extension <code>personalizationservices</code>. */
		
	private String catalogVersion;
	
	public CxVariationKey()
	{
		// default constructor
	}
	
	public void setVariationCode(final String variationCode)
	{
		this.variationCode = variationCode;
	}

	public String getVariationCode() 
	{
		return variationCode;
	}
	
	public void setCustomizationCode(final String customizationCode)
	{
		this.customizationCode = customizationCode;
	}

	public String getCustomizationCode() 
	{
		return customizationCode;
	}
	
	public void setCatalog(final String catalog)
	{
		this.catalog = catalog;
	}

	public String getCatalog() 
	{
		return catalog;
	}
	
	public void setCatalogVersion(final String catalogVersion)
	{
		this.catalogVersion = catalogVersion;
	}

	public String getCatalogVersion() 
	{
		return catalogVersion;
	}
	

	@Override
	public boolean equals(final Object o)
	{
		if (o == null) return false;
		if (o == this) return true;

        if (getClass() != o.getClass()) return false;

		final CxVariationKey other = (CxVariationKey) o;
		return Objects.equals(getVariationCode(), other.getVariationCode())
			&& Objects.equals(getCustomizationCode(), other.getCustomizationCode())
			&& Objects.equals(getCatalog(), other.getCatalog())
			&& Objects.equals(getCatalogVersion(), other.getCatalogVersion());
    }

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

		attribute = variationCode;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = customizationCode;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = catalog;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = catalogVersion;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());

		return result;
	}
}