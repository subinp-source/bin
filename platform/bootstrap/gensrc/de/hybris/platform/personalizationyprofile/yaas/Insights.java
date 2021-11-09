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

import de.hybris.platform.personalizationyprofile.yaas.Affinities;
import de.hybris.platform.personalizationyprofile.yaas.Metrics;


public class Insights extends LinkedHashMap<String, Object> implements Map<String, Object> {

	public static class InsightsBuilder extends LinkedHashMap<String, Object> {

		public InsightsBuilder withAffinities(final Affinities affinities) {
			this.put("affinities", affinities);
			return this;
		}

		public InsightsBuilder withMetrics(final Metrics metrics) {
			this.put("metrics", metrics);
			return this;
		}
		public Insights build() {
			final Insights dto = new Insights();
			dto.putAll(this);
			return dto;
		}
	}

	/** Default serialVersionUID value. */
 	private static final long serialVersionUID = 1L;

    @JsonCreator
	public Insights(@JsonProperty("affinities") final Affinities affinities, @JsonProperty("metrics") final Metrics metrics)
	{
		this.put("affinities", affinities);
		this.put("metrics", metrics);
	}

	public Insights()
	{
		// default constructor
	}


	/** <i>Generated property</i> for <code>Insights.affinities</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setAffinities(final Affinities affinities)
	{
		this.put("affinities", affinities);
	}

	public Affinities getAffinities()
	{
	 	return (Affinities)this.get("affinities");
	}

	/** <i>Generated property</i> for <code>Insights.metrics</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setMetrics(final Metrics metrics)
	{
		this.put("metrics", metrics);
	}

	public Metrics getMetrics()
	{
	 	return (Metrics)this.get("metrics");
	}

}
