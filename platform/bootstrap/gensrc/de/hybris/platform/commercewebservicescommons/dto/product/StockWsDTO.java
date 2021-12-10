/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Stock
 */
@ApiModel(value="Stock", description="Representation of a Stock")
public  class StockWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Status of stock level<br/><br/><i>Generated property</i> for <code>StockWsDTO.stockLevelStatus</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="stockLevelStatus", value="Status of stock level") 	
	private String stockLevelStatus;

	/** Stock level expressed as number<br/><br/><i>Generated property</i> for <code>StockWsDTO.stockLevel</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="stockLevel", value="Stock level expressed as number") 	
	private Long stockLevel;

	/** Indicate whether Stock level value is rounded<br/><br/><i>Generated property</i> for <code>StockWsDTO.isValueRounded</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="isValueRounded", value="Indicate whether Stock level value is rounded") 	
	private Boolean isValueRounded;
	
	public StockWsDTO()
	{
		// default constructor
	}
	
	public void setStockLevelStatus(final String stockLevelStatus)
	{
		this.stockLevelStatus = stockLevelStatus;
	}

	public String getStockLevelStatus() 
	{
		return stockLevelStatus;
	}
	
	public void setStockLevel(final Long stockLevel)
	{
		this.stockLevel = stockLevel;
	}

	public Long getStockLevel() 
	{
		return stockLevel;
	}
	
	public void setIsValueRounded(final Boolean isValueRounded)
	{
		this.isValueRounded = isValueRounded;
	}

	public Boolean getIsValueRounded() 
	{
		return isValueRounded;
	}
	

}