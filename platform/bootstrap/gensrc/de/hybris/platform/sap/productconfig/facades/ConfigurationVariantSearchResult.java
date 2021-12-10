/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.facades.ConfigurationVariantData;
import java.util.List;


import java.util.Objects;
public  class ConfigurationVariantSearchResult  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ConfigurationVariantSearchResult.variantsList</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<ConfigurationVariantData> variantsList;

	/** <i>Generated property</i> for <code>ConfigurationVariantSearchResult.numberOfVariants</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private int numberOfVariants;
	
	public ConfigurationVariantSearchResult()
	{
		// default constructor
	}
	
	public void setVariantsList(final List<ConfigurationVariantData> variantsList)
	{
		this.variantsList = variantsList;
	}

	public List<ConfigurationVariantData> getVariantsList() 
	{
		return variantsList;
	}
	
	public void setNumberOfVariants(final int numberOfVariants)
	{
		this.numberOfVariants = numberOfVariants;
	}

	public int getNumberOfVariants() 
	{
		return numberOfVariants;
	}
	

}