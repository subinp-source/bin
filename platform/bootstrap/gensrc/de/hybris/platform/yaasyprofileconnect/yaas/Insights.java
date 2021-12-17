/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 15-Dec-2021, 3:07:46 PM
* ----------------------------------------------------------------
*
* [y] hybris Platform
*
* Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
*
* This software is the confidential and proprietary information of SAP
* ("Confidential Information"). You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms of the
* license agreement you entered into with SAP.
*/

package de.hybris.platform.yaasyprofileconnect.yaas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

import de.hybris.platform.yaasyprofileconnect.yaas.Affinities;


public class Insights extends LinkedHashMap<String, Object> implements Map<String, Object> {

	public static class InsightsBuilder extends LinkedHashMap<String, Object> {

		public InsightsBuilder withAffinities(final Affinities affinities) {
			this.put("affinities", affinities);
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
	public Insights(@JsonProperty("affinities") final Affinities affinities)
	{
		this.put("affinities", affinities);
	}

	public Insights()
	{
		// default constructor
	}

	/** <i>Generated property</i> for <code>Insights.affinities</code> property defined at extension <code>yaasyprofileconnect</code>. */
	public void setAffinities(final Affinities affinities)
	{
		this.put("affinities", affinities);
	}

	public Affinities getAffinities()
	{
	 	return (Affinities)this.get("affinities");
	}

}
