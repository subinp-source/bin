/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationfacades.data;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Action
 */
@ApiModel(value="action", description="Action")
public  class ActionData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Action code<br/><br/><i>Generated property</i> for <code>ActionData.code</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="code", value="Action code") 	
	private String code;

	/** Catalog name<br/><br/><i>Generated property</i> for <code>ActionData.catalog</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="catalog", value="Catalog name") 	
	private String catalog;

	/** Catalog version<br/><br/><i>Generated property</i> for <code>ActionData.catalogVersion</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="catalogVersion", value="Catalog version") 	
	private String catalogVersion;

	/** Priority of the action<br/><br/><i>Generated property</i> for <code>ActionData.rank</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="rank", value="Priority of the action") 	
	private Integer rank;
	
	public ActionData()
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
	
	public void setRank(final Integer rank)
	{
		this.rank = rank;
	}

	public Integer getRank() 
	{
		return rank;
	}
	

}