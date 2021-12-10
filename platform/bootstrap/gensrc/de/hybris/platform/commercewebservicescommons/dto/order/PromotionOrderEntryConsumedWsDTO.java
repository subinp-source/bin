/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Promotion order entry consumed
 */
@ApiModel(value="PromotionOrderEntryConsumed", description="Representation of a Promotion order entry consumed")
public  class PromotionOrderEntryConsumedWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Order entry code<br/><br/><i>Generated property</i> for <code>PromotionOrderEntryConsumedWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="code", value="Order entry code") 	
	private String code;

	/** Adjusted unit price for promotion order entry<br/><br/><i>Generated property</i> for <code>PromotionOrderEntryConsumedWsDTO.adjustedUnitPrice</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="adjustedUnitPrice", value="Adjusted unit price for promotion order entry") 	
	private Double adjustedUnitPrice;

	/** Order entry number<br/><br/><i>Generated property</i> for <code>PromotionOrderEntryConsumedWsDTO.orderEntryNumber</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="orderEntryNumber", value="Order entry number") 	
	private Integer orderEntryNumber;

	/** Quantity of promotion order entry<br/><br/><i>Generated property</i> for <code>PromotionOrderEntryConsumedWsDTO.quantity</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="quantity", value="Quantity of promotion order entry") 	
	private Long quantity;
	
	public PromotionOrderEntryConsumedWsDTO()
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
	
	public void setAdjustedUnitPrice(final Double adjustedUnitPrice)
	{
		this.adjustedUnitPrice = adjustedUnitPrice;
	}

	public Double getAdjustedUnitPrice() 
	{
		return adjustedUnitPrice;
	}
	
	public void setOrderEntryNumber(final Integer orderEntryNumber)
	{
		this.orderEntryNumber = orderEntryNumber;
	}

	public Integer getOrderEntryNumber() 
	{
		return orderEntryNumber;
	}
	
	public void setQuantity(final Long quantity)
	{
		this.quantity = quantity;
	}

	public Long getQuantity() 
	{
		return quantity;
	}
	

}