/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.catalog;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.catalog.CatalogWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Catalog List
 */
@ApiModel(value="CatalogList", description="Representation of a Catalog List")
public  class CatalogListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** List of catalog items<br/><br/><i>Generated property</i> for <code>CatalogListWsDTO.catalogs</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="catalogs", value="List of catalog items") 	
	private List<CatalogWsDTO> catalogs;
	
	public CatalogListWsDTO()
	{
		// default constructor
	}
	
	public void setCatalogs(final List<CatalogWsDTO> catalogs)
	{
		this.catalogs = catalogs;
	}

	public List<CatalogWsDTO> getCatalogs() 
	{
		return catalogs;
	}
	

}