/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationfacades.data;

import java.io.Serializable;
import de.hybris.platform.personalizationfacades.data.VariationData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Trigger
 */
@ApiModel(value="trigger", description="Trigger")
public  class TriggerData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Trigger code<br/><br/><i>Generated property</i> for <code>TriggerData.code</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="code", value="Trigger code") 	
	private String code;

	/** Catalog name<br/><br/><i>Generated property</i> for <code>TriggerData.catalog</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="catalog", value="Catalog name") 	
	private String catalog;

	/** Catalog version<br/><br/><i>Generated property</i> for <code>TriggerData.catalogVersion</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="catalogVersion", value="Catalog version") 	
	private String catalogVersion;

	/** Details of the variation<br/><br/><i>Generated property</i> for <code>TriggerData.variation</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="variation", value="Details of the variation") 	
	private VariationData variation;
	
	public TriggerData()
	{
		// default constructor
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
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
	
	public void setVariation(final VariationData variation)
	{
		this.variation = variation;
	}

	public VariationData getVariation() 
	{
		return variation;
	}
	

}