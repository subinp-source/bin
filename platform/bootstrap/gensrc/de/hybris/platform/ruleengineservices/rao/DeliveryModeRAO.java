/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 11-Dec-2021, 12:33:00 AM
* ----------------------------------------------------------------
*
* Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

public  class DeliveryModeRAO  implements Serializable 
{

	/** <i>Generated property</i> for <code>DeliveryModeRAO.code</code> property defined at extension <code>ruleengineservices</code>. */
	private String code;
	/** <i>Generated property</i> for <code>DeliveryModeRAO.cost</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal cost;
	/** <i>Generated property</i> for <code>DeliveryModeRAO.currencyIsoCode</code> property defined at extension <code>ruleengineservices</code>. */
	private String currencyIsoCode;
		
	public DeliveryModeRAO()
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
		
		public void setCost(final BigDecimal cost)
	{
		this.cost = cost;
	}
		public BigDecimal getCost() 
	{
		return cost;
	}
		
		public void setCurrencyIsoCode(final String currencyIsoCode)
	{
		this.currencyIsoCode = currencyIsoCode;
	}
		public String getCurrencyIsoCode() 
	{
		return currencyIsoCode;
	}
		
	
		@Override
	public boolean equals(final Object o)
	{
		
		if (o == null) return false;
		if (o == this) return true;

		if (getClass() != o.getClass()) return false;

		final DeliveryModeRAO other = (DeliveryModeRAO) o;
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
