/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 15-Dec-2021, 3:07:46 PM
* ----------------------------------------------------------------
*
* Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Objects;

public  class PaymentModeRAO  implements Serializable 
{

	/** <i>Generated property</i> for <code>PaymentModeRAO.code</code> property defined at extension <code>ruleengineservices</code>. */
	private String code;
		
	public PaymentModeRAO()
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
		
	
		@Override
	public boolean equals(final Object o)
	{
		
		if (o == null) return false;
		if (o == this) return true;

		if (getClass() != o.getClass()) return false;

		final PaymentModeRAO other = (PaymentModeRAO) o;
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
