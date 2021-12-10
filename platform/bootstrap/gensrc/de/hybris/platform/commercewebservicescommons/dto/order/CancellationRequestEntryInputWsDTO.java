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
 * Representation of a cancellation request entry input for an order
 */
@ApiModel(value="CancellationRequestEntryInput", description="Representation of a cancellation request entry input for an order")
public  class CancellationRequestEntryInputWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Order entry number of the cancelled product<br/><br/><i>Generated property</i> for <code>CancellationRequestEntryInputWsDTO.orderEntryNumber</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="orderEntryNumber", value="Order entry number of the cancelled product", required=true, example="1") 	
	private Integer orderEntryNumber;

	/** Quantity of the product which belongs to the order entry and is requested to be cancelled<br/><br/><i>Generated property</i> for <code>CancellationRequestEntryInputWsDTO.quantity</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="quantity", value="Quantity of the product which belongs to the order entry and is requested to be cancelled", required=true, example="5") 	
	private Long quantity;
	
	public CancellationRequestEntryInputWsDTO()
	{
		// default constructor
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