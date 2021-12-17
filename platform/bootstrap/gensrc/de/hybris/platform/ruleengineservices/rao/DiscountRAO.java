/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengineservices.rao;

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import java.math.BigDecimal;


import java.util.Objects;
public  class DiscountRAO extends AbstractRuleActionRAO 
{

 

	/** <i>Generated property</i> for <code>DiscountRAO.value</code> property defined at extension <code>ruleengineservices</code>. */
		
	private BigDecimal value;

	/** <i>Generated property</i> for <code>DiscountRAO.currencyIsoCode</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String currencyIsoCode;

	/** <i>Generated property</i> for <code>DiscountRAO.appliedToQuantity</code> property defined at extension <code>ruleengineservices</code>. */
		
	private long appliedToQuantity;

	/** <i>Generated property</i> for <code>DiscountRAO.perUnit</code> property defined at extension <code>ruleengineservices</code>. */
		
	private boolean perUnit;
	
	public DiscountRAO()
	{
		// default constructor
	}
	
	public void setValue(final BigDecimal value)
	{
		this.value = value;
	}

	public BigDecimal getValue() 
	{
		return value;
	}
	
	public void setCurrencyIsoCode(final String currencyIsoCode)
	{
		this.currencyIsoCode = currencyIsoCode;
	}

	public String getCurrencyIsoCode() 
	{
		return currencyIsoCode;
	}
	
	public void setAppliedToQuantity(final long appliedToQuantity)
	{
		this.appliedToQuantity = appliedToQuantity;
	}

	public long getAppliedToQuantity() 
	{
		return appliedToQuantity;
	}
	
	public void setPerUnit(final boolean perUnit)
	{
		this.perUnit = perUnit;
	}

	public boolean isPerUnit() 
	{
		return perUnit;
	}
	

}