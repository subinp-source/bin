/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a return request entry which contains information about the returned product
 */
@ApiModel(value="ReturnRequestEntry", description="Representation of a return request entry which contains information about the returned product")
public  class ReturnRequestEntryWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Order entry related to the return request entry<br/><br/><i>Generated property</i> for <code>ReturnRequestEntryWsDTO.orderEntry</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="orderEntry", value="Order entry related to the return request entry") 	
	private OrderEntryWsDTO orderEntry;

	/** Quantity which is expected to be returned for this return request entry<br/><br/><i>Generated property</i> for <code>ReturnRequestEntryWsDTO.expectedQuantity</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="expectedQuantity", value="Quantity which is expected to be returned for this return request entry", example="5") 	
	private Long expectedQuantity;

	/** Refund amount of the entry<br/><br/><i>Generated property</i> for <code>ReturnRequestEntryWsDTO.refundAmount</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="refundAmount", value="Refund amount of the entry") 	
	private PriceWsDTO refundAmount;
	
	public ReturnRequestEntryWsDTO()
	{
		// default constructor
	}
	
	public void setOrderEntry(final OrderEntryWsDTO orderEntry)
	{
		this.orderEntry = orderEntry;
	}

	public OrderEntryWsDTO getOrderEntry() 
	{
		return orderEntry;
	}
	
	public void setExpectedQuantity(final Long expectedQuantity)
	{
		this.expectedQuantity = expectedQuantity;
	}

	public Long getExpectedQuantity() 
	{
		return expectedQuantity;
	}
	
	public void setRefundAmount(final PriceWsDTO refundAmount)
	{
		this.refundAmount = refundAmount;
	}

	public PriceWsDTO getRefundAmount() 
	{
		return refundAmount;
	}
	

}