/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 08-Nov-2021, 4:51:28 PM
* ----------------------------------------------------------------
*
* Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Objects;

public  class CustomerSupportRAO  implements Serializable 
{

	/** <i>Generated property</i> for <code>CustomerSupportRAO.customerSupportAgentActive</code> property defined at extension <code>ruleengineservices</code>. */
	private Boolean customerSupportAgentActive;
	/** <i>Generated property</i> for <code>CustomerSupportRAO.customerEmulationActive</code> property defined at extension <code>ruleengineservices</code>. */
	private Boolean customerEmulationActive;
		
	public CustomerSupportRAO()
	{
		// default constructor
	}
	
		public void setCustomerSupportAgentActive(final Boolean customerSupportAgentActive)
	{
		this.customerSupportAgentActive = customerSupportAgentActive;
	}
		public Boolean getCustomerSupportAgentActive() 
	{
		return customerSupportAgentActive;
	}
		
		public void setCustomerEmulationActive(final Boolean customerEmulationActive)
	{
		this.customerEmulationActive = customerEmulationActive;
	}
		public Boolean getCustomerEmulationActive() 
	{
		return customerEmulationActive;
	}
		
	
		@Override
	public boolean equals(final Object o)
	{
		
		if (o == null) return false;
		if (o == this) return true;

		if (getClass() != o.getClass()) return false;

		final CustomerSupportRAO other = (CustomerSupportRAO) o;
		return				Objects.equals(getCustomerSupportAgentActive(), other.getCustomerSupportAgentActive())
 &&  			Objects.equals(getCustomerEmulationActive(), other.getCustomerEmulationActive())
  ;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

				attribute = customerSupportAgentActive;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
				attribute = customerEmulationActive;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		
		return result;
	}
	}
