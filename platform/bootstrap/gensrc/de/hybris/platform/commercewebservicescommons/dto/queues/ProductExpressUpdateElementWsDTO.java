/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.queues;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Product Express Update Element
 */
@ApiModel(value="ProductExpressUpdateElement", description="Representation of a Product Express Update Element")
public  class ProductExpressUpdateElementWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Code of product express update element<br/><br/><i>Generated property</i> for <code>ProductExpressUpdateElementWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="code", value="Code of product express update element") 	
	private String code;

	/** Catalog identifier<br/><br/><i>Generated property</i> for <code>ProductExpressUpdateElementWsDTO.catalogId</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="catalogId", value="Catalog identifier") 	
	private String catalogId;

	/** Catalog version<br/><br/><i>Generated property</i> for <code>ProductExpressUpdateElementWsDTO.catalogVersion</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="catalogVersion", value="Catalog version") 	
	private String catalogVersion;
	
	public ProductExpressUpdateElementWsDTO()
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
	

}