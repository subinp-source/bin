/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 11-Dec-2021, 12:33:00 AM
* ----------------------------------------------------------------
*
* Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.ruleengineservices.rao;

import java.util.Objects;
import de.hybris.platform.couponservices.rao.CouponRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountValueRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.PaymentModeRAO;
import de.hybris.platform.ruleengineservices.rao.UserRAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public  class AbstractOrderRAO extends AbstractActionedRAO 
{

	/** <i>Generated property</i> for <code>AbstractOrderRAO.code</code> property defined at extension <code>ruleengineservices</code>. */
	private String code;
	/** <i>Generated property</i> for <code>AbstractOrderRAO.total</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal total;
	/** <i>Generated property</i> for <code>AbstractOrderRAO.totalIncludingCharges</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal totalIncludingCharges;
	/** <i>Generated property</i> for <code>AbstractOrderRAO.subTotal</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal subTotal;
	/** <i>Generated property</i> for <code>AbstractOrderRAO.deliveryCost</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal deliveryCost;
	/** <i>Generated property</i> for <code>AbstractOrderRAO.paymentCost</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal paymentCost;
	/** <i>Generated property</i> for <code>AbstractOrderRAO.currencyIsoCode</code> property defined at extension <code>ruleengineservices</code>. */
	private String currencyIsoCode;
	/** <i>Generated property</i> for <code>AbstractOrderRAO.entries</code> property defined at extension <code>ruleengineservices</code>. */
	private Set<OrderEntryRAO> entries;
	/** <i>Generated property</i> for <code>AbstractOrderRAO.discountValues</code> property defined at extension <code>ruleengineservices</code>. */
	private List<DiscountValueRAO> discountValues;
	/** <i>Generated property</i> for <code>AbstractOrderRAO.user</code> property defined at extension <code>ruleengineservices</code>. */
	private UserRAO user;
	/** <i>Generated property</i> for <code>AbstractOrderRAO.paymentMode</code> property defined at extension <code>ruleengineservices</code>. */
	private PaymentModeRAO paymentMode;
	/** <i>Generated property</i> for <code>AbstractOrderRAO.coupons</code> property defined at extension <code>couponservices</code>. */
	private List<CouponRAO> coupons;
		
	public AbstractOrderRAO()
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
		
		public void setTotal(final BigDecimal total)
	{
		this.total = total;
	}
		public BigDecimal getTotal() 
	{
		return total;
	}
		
		public void setTotalIncludingCharges(final BigDecimal totalIncludingCharges)
	{
		this.totalIncludingCharges = totalIncludingCharges;
	}
		public BigDecimal getTotalIncludingCharges() 
	{
		return totalIncludingCharges;
	}
		
		public void setSubTotal(final BigDecimal subTotal)
	{
		this.subTotal = subTotal;
	}
		public BigDecimal getSubTotal() 
	{
		return subTotal;
	}
		
		public void setDeliveryCost(final BigDecimal deliveryCost)
	{
		this.deliveryCost = deliveryCost;
	}
		public BigDecimal getDeliveryCost() 
	{
		return deliveryCost;
	}
		
		public void setPaymentCost(final BigDecimal paymentCost)
	{
		this.paymentCost = paymentCost;
	}
		public BigDecimal getPaymentCost() 
	{
		return paymentCost;
	}
		
		public void setCurrencyIsoCode(final String currencyIsoCode)
	{
		this.currencyIsoCode = currencyIsoCode;
	}
		public String getCurrencyIsoCode() 
	{
		return currencyIsoCode;
	}
		
		public void setEntries(final Set<OrderEntryRAO> entries)
	{
		this.entries = entries;
	}
		public Set<OrderEntryRAO> getEntries() 
	{
		return entries;
	}
		
		public void setDiscountValues(final List<DiscountValueRAO> discountValues)
	{
		this.discountValues = discountValues;
	}
		public List<DiscountValueRAO> getDiscountValues() 
	{
		return discountValues;
	}
		
		public void setUser(final UserRAO user)
	{
		this.user = user;
	}
		public UserRAO getUser() 
	{
		return user;
	}
		
		public void setPaymentMode(final PaymentModeRAO paymentMode)
	{
		this.paymentMode = paymentMode;
	}
		public PaymentModeRAO getPaymentMode() 
	{
		return paymentMode;
	}
		
		public void setCoupons(final List<CouponRAO> coupons)
	{
		this.coupons = coupons;
	}
		public List<CouponRAO> getCoupons() 
	{
		return coupons;
	}
		
	
		@Override
	public boolean equals(final Object o)
	{
		
		if (o == null) return false;
		if (o == this) return true;

		if (getClass() != o.getClass()) return false;

		final AbstractOrderRAO other = (AbstractOrderRAO) o;
		return				Objects.equals(getCode(), other.getCode())
  ;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

				attribute = code;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		
		return result;
	}
	}
