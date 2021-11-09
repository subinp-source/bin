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

import de.hybris.platform.personalizationyprofile.yaas.Insights;
import de.hybris.platform.personalizationyprofile.yaas.Segment;
import java.util.Map;


public class Profile extends LinkedHashMap<String, Object> implements Map<String, Object> {

	public static class ProfileBuilder extends LinkedHashMap<String, Object> {

		public ProfileBuilder withInsights(final Insights insights) {
			this.put("insights", insights);
			return this;
		}

		public ProfileBuilder withSegments(final Map<String,Segment> segments) {
			this.put("segments", segments);
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
	public Profile(@JsonProperty("insights") final Insights insights, @JsonProperty("segments") final Map<String,Segment> segments)
	{
		this.put("insights", insights);
		this.put("segments", segments);
	}

	public Profile()
	{
		// default constructor
	}


	/** <i>Generated property</i> for <code>Profile.insights</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setInsights(final Insights insights)
	{
		this.put("insights", insights);
	}

	public Insights getInsights()
	{
	 	return (Insights)this.get("insights");
	}

	/** <i>Generated property</i> for <code>Profile.segments</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setSegments(final Map<String,Segment> segments)
	{
		this.put("segments", segments);
	}

	public Map<String,Segment> getSegments()
	{
	 	return (Map<String,Segment>)this.get("segments");
	}

}
