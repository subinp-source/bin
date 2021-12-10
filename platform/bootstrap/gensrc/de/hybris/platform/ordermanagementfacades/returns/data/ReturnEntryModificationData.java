/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ordermanagementfacades.returns.data;

import java.io.Serializable;
import java.math.BigDecimal;


import java.util.Objects;
public  class ReturnEntryModificationData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ReturnEntryModificationData.productCode</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String productCode;

	/** <i>Generated property</i> for <code>ReturnEntryModificationData.deliveryModeCode</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String deliveryModeCode;

	/** <i>Generated property</i> for <code>ReturnEntryModificationData.refundAmount</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private BigDecimal refundAmount;
	
	public ReturnEntryModificationData()
	{
		// default constructor
	}
	
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}

	public String getProductCode() 
	{
		return productCode;
	}
	
	public void setDeliveryModeCode(final String deliveryModeCode)
	{
		this.deliveryModeCode = deliveryModeCode;
	}

	public String getDeliveryModeCode() 
	{
		return deliveryModeCode;
	}
	
	public void setRefundAmount(final BigDecimal refundAmount)
	{
		this.refundAmount = refundAmount;
	}

	public BigDecimal getRefundAmount() 
	{
		return refundAmount;
	}
	

}