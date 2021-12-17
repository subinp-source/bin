/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 15-Dec-2021, 3:07:45 PM
* ----------------------------------------------------------------
*
* Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Objects;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import java.math.BigDecimal;

public  class OrderEntryConsumedRAO  implements Serializable 
{

	/** <i>Generated property</i> for <code>OrderEntryConsumedRAO.firedRuleCode</code> property defined at extension <code>ruleengineservices</code>. */
	private String firedRuleCode;
	/** <i>Generated property</i> for <code>OrderEntryConsumedRAO.orderEntry</code> property defined at extension <code>ruleengineservices</code>. */
	private OrderEntryRAO orderEntry;
	/** <i>Generated property</i> for <code>OrderEntryConsumedRAO.quantity</code> property defined at extension <code>ruleengineservices</code>. */
	private int quantity;
	/** <i>Generated property</i> for <code>OrderEntryConsumedRAO.adjustedUnitPrice</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal adjustedUnitPrice;
		
	public OrderEntryConsumedRAO()
	{
		// default constructor
	}
	
		public void setFiredRuleCode(final String firedRuleCode)
	{
		this.firedRuleCode = firedRuleCode;
	}
		public String getFiredRuleCode() 
	{
		return firedRuleCode;
	}
		
		public void setOrderEntry(final OrderEntryRAO orderEntry)
	{
		this.orderEntry = orderEntry;
	}
		public OrderEntryRAO getOrderEntry() 
	{
		return orderEntry;
	}
		
		public void setQuantity(final int quantity)
	{
		this.quantity = quantity;
	}
		public int getQuantity() 
	{
		return quantity;
	}
		
		public void setAdjustedUnitPrice(final BigDecimal adjustedUnitPrice)
	{
		this.adjustedUnitPrice = adjustedUnitPrice;
	}
		public BigDecimal getAdjustedUnitPrice() 
	{
		return adjustedUnitPrice;
	}
		
	
		@Override
	public boolean equals(final Object o)
	{
		
		if (o == null) return false;
		if (o == this) return true;

		if (getClass() != o.getClass()) return false;

		final OrderEntryConsumedRAO other = (OrderEntryConsumedRAO) o;
		return				Objects.equals(getFiredRuleCode(), other.getFiredRuleCode())
 &&  			Objects.equals(getOrderEntry(), other.getOrderEntry())
 &&  			Objects.equals(getQuantity(), other.getQuantity())
 &&  			Objects.equals(getAdjustedUnitPrice(), other.getAdjustedUnitPrice())
  ;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

				attribute = firedRuleCode;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
				attribute = orderEntry;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
				attribute = quantity;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
				attribute = adjustedUnitPrice;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		
		return result;
	}
	}
