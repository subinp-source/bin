/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 08-Nov-2021, 4:51:27 PM
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

import de.hybris.platform.yaasyprofileconnect.yaas.Insights;
import de.hybris.platform.yaasyprofileconnect.yaas.UserAgent;
import java.util.Map;


public class Profile extends LinkedHashMap<String, Object> implements Map<String, Object> {

	public static class ProfileBuilder extends LinkedHashMap<String, Object> {

		public ProfileBuilder withInsights(final Insights insights) {
			this.put("insights", insights);
			return this;
		}

		public ProfileBuilder withUserAgents(final Map<String,UserAgent> userAgents) {
			this.put("userAgents", userAgents);
			return this;
		}
		public Profile build() {
			final Profile dto = new Profile();
			dto.putAll(this);
			return dto;
		}
	}

	/** Default serialVersionUID value. */
 	private static final long serialVersionUID = 1L;

    @JsonCreator
	public Profile(@JsonProperty("insights") final Insights insights, @JsonProperty("userAgents") final Map<String,UserAgent> userAgents)
	{
		this.put("insights", insights);
		this.put("userAgents", userAgents);
	}

	public Profile()
	{
		// default constructor
	}

	/** <i>Generated property</i> for <code>Profile.insights</code> property defined at extension <code>yaasyprofileconnect</code>. */
	public void setInsights(final Insights insights)
	{
		this.put("insights", insights);
	}

	public Insights getInsights()
	{
	 	return (Insights)this.get("insights");
	}

	/** <i>Generated property</i> for <code>Profile.userAgents</code> property defined at extension <code>yaasyprofileconnect</code>. */
	public void setUserAgents(final Map<String,UserAgent> userAgents)
	{
		this.put("userAgents", userAgents);
	}

	public Map<String,UserAgent> getUserAgents()
	{
	 	return (Map<String,UserAgent>)this.get("userAgents");
	}

}
