/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;


import java.util.Objects;
public  class PromotionOrderEntryConsumedData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PromotionOrderEntryConsumedData.code</code> property defined at extension <code>commercefacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>PromotionOrderEntryConsumedData.adjustedUnitPrice</code> property defined at extension <code>commercefacades</code>. */
		
	private Double adjustedUnitPrice;

	/** <i>Generated property</i> for <code>PromotionOrderEntryConsumedData.orderEntryNumber</code> property defined at extension <code>commercefacades</code>. */
		
	private Integer orderEntryNumber;

	/** <i>Generated property</i> for <code>PromotionOrderEntryConsumedData.quantity</code> property defined at extension <code>commercefacades</code>. */
		
	private Long quantity;
	
	public PromotionOrderEntryConsumedData()
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