/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 */

package de.hybris.platform.personalizationyprofile.yaas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

import java.math.BigDecimal;


public class OrderMetrics extends LinkedHashMap<String, Object> implements Map<String, Object> {

	public static class OrderMetricsBuilder extends LinkedHashMap<String, Object> {

		public OrderMetricsBuilder withAllOrdersValuesSum(final BigDecimal allOrdersValuesSum) {
			this.put("allOrdersValuesSum", allOrdersValuesSum);
			return this;
		}

		public OrderMetricsBuilder withAllOrdersCount(final Integer allOrdersCount) {
			this.put("allOrdersCount", allOrdersCount);
			return this;
		}

		public OrderMetricsBuilder withAvgOrderValue(final BigDecimal avgOrderValue) {
			this.put("avgOrderValue", avgOrderValue);
			return this;
		}

		public OrderMetricsBuilder withAvgOrderValueAllCustomers(final BigDecimal avgOrderValueAllCustomers) {
			this.put("avgOrderValueAllCustomers", avgOrderValueAllCustomers);
			return this;
		}
		public OrderMetrics build() {
			final OrderMetrics dto = new OrderMetrics();
			dto.putAll(this);
			return dto;
		}
	}

	/** Default serialVersionUID value. */
 	private static final long serialVersionUID = 1L;

    @JsonCreator
	public OrderMetrics(@JsonProperty("allOrdersValuesSum") final BigDecimal allOrdersValuesSum, @JsonProperty("allOrdersCount") final Integer allOrdersCount, @JsonProperty("avgOrderValue") final BigDecimal avgOrderValue, @JsonProperty("avgOrderValueAllCustomers") final BigDecimal avgOrderValueAllCustomers)
	{
		this.put("allOrdersValuesSum", allOrdersValuesSum);
		this.put("allOrdersCount", allOrdersCount);
		this.put("avgOrderValue", avgOrderValue);
		this.put("avgOrderValueAllCustomers", avgOrderValueAllCustomers);
	}

	public OrderMetrics()
	{
		// default constructor
	}


	/** <i>Generated property</i> for <code>OrderMetrics.allOrdersValuesSum</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setAllOrdersValuesSum(final BigDecimal allOrdersValuesSum)
	{
		this.put("allOrdersValuesSum", allOrdersValuesSum);
	}

	public BigDecimal getAllOrdersValuesSum()
	{
	 	return (BigDecimal)this.get("allOrdersValuesSum");
	}

	/** <i>Generated property</i> for <code>OrderMetrics.allOrdersCount</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setAllOrdersCount(final Integer allOrdersCount)
	{
		this.put("allOrdersCount", allOrdersCount);
	}

	public Integer getAllOrdersCount()
	{
	 	return (Integer)this.get("allOrdersCount");
	}

	/** <i>Generated property</i> for <code>OrderMetrics.avgOrderValue</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setAvgOrderValue(final BigDecimal avgOrderValue)
	{
		this.put("avgOrderValue", avgOrderValue);
	}

	public BigDecimal getAvgOrderValue()
	{
	 	return (BigDecimal)this.get("avgOrderValue");
	}

	/** <i>Generated property</i> for <code>OrderMetrics.avgOrderValueAllCustomers</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setAvgOrderValueAllCustomers(final BigDecimal avgOrderValueAllCustomers)
	{
		this.put("avgOrderValueAllCustomers", avgOrderValueAllCustomers);
	}

	public BigDecimal getAvgOrderValueAllCustomers()
	{
	 	return (BigDecimal)this.get("avgOrderValueAllCustomers");
	}

}
