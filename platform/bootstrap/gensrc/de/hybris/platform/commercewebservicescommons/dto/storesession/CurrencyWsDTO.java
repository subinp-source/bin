/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.storesession;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Currency
 */
@ApiModel(value="Currency", description="Representation of a Currency")
public  class CurrencyWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Code of the currency in iso format<br/><br/><i>Generated property</i> for <code>CurrencyWsDTO.isocode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="isocode", value="Code of the currency in iso format") 	
	private String isocode;

	/** Name of the currency<br/><br/><i>Generated property</i> for <code>CurrencyWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the currency") 	
	private String name;

	/** Boolean flag whether currency is active<br/><br/><i>Generated property</i> for <code>CurrencyWsDTO.active</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="active", value="Boolean flag whether currency is active") 	
	private Boolean active;

	/** Symbol of the currency<br/><br/><i>Generated property</i> for <code>CurrencyWsDTO.symbol</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="symbol", value="Symbol of the currency") 	
	private String symbol;
	
	public CurrencyWsDTO()
	{
		// default constructor
	}
	
	public void setIsocode(final String isocode)
	{
		this.isocode = isocode;
	}

	public String getIsocode() 
	{
		return isocode;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setActive(final Boolean active)
	{
		this.active = active;
	}

	public Boolean getActive() 
	{
		return active;
	}
	
	public void setSymbol(final String symbol)
	{
		this.symbol = symbol;
	}

	public String getSymbol() 
	{
		return symbol;
	}
	

}