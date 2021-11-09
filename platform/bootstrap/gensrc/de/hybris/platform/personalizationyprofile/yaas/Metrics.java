/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 */

package de.hybris.platform.personalizationyprofile.yaas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

import de.hybris.platform.personalizationyprofile.yaas.OrderMetrics;


public class Metrics extends LinkedHashMap<String, Object> implements Map<String, Object> {

	public static class MetricsBuilder extends LinkedHashMap<String, Object> {

		public MetricsBuilder withOrders(final OrderMetrics orders) {
			this.put("orders", orders);
			return this;
		}
		public Metrics build() {
			final Metrics dto = new Metrics();
			dto.putAll(this);
			return dto;
		}
	}

	/** Default serialVersionUID value. */
 	private static final long serialVersionUID = 1L;

    @JsonCreator
	public Metrics(@JsonProperty("orders") final OrderMetrics orders)
	{
		this.put("orders", orders);
	}

	public Metrics()
	{
		// default constructor
	}


	/** <i>Generated property</i> for <code>Metrics.orders</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setOrders(final OrderMetrics orders)
	{
		this.put("orders", orders);
	}

	public OrderMetrics getOrders()
	{
	 	return (OrderMetrics)this.get("orders");
	}

}
