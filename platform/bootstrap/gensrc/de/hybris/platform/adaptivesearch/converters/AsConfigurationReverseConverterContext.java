/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.converters;

import java.io.Serializable;
import de.hybris.platform.catalog.model.CatalogVersionModel;


import java.util.Objects;
public  class AsConfigurationReverseConverterContext  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AsConfigurationReverseConverterContext.catalogVersion</code> property defined at extension <code>adaptivesearch</code>. */
		
	private CatalogVersionModel catalogVersion;
	
	public AsConfigurationReverseConverterContext()
	{
		// default constructor
	}
	
	public void setCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		this.catalogVersion = catalogVersion;
	}

	public CatalogVersionModel getCatalogVersion() 
	{
		return catalogVersion;
	}
	

}