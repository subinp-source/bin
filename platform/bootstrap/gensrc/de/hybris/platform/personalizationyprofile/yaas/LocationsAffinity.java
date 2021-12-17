/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 */

package de.hybris.platform.personalizationyprofile.yaas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.LinkedHashMap;
import java.util.Map;

import de.hybris.platform.personalizationyprofile.yaas.Affinity;
import de.hybris.platform.personalizationyprofile.yaas.Location;

/**
 * @deprecated Deprecated because Profile structure has changed
 */
@Deprecated

@JsonDeserialize(builder = LocationsAffinity.LocationsAffinityBuilder.class)
public class LocationsAffinity extends Affinity implements Map<String, Object> {

	public static class LocationsAffinityBuilder extends AffinityBuilder  {

		public LocationsAffinityBuilder withRecentCount(final Integer recentCount) {
			this.put("recentCount", recentCount);
			return this;
		}

		public LocationsAffinityBuilder withLocation(final Location location) {
			this.put("location", location);
			return this;
		}
		public LocationsAffinity build() {
			final LocationsAffinity dto = new LocationsAffinity();
			dto.putAll(this);
			return dto;
		}
	}



	public LocationsAffinity()
	{
		// default constructor
	}


	/** <i>Generated property</i> for <code>LocationsAffinity.recentCount</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setRecentCount(final Integer recentCount)
	{
		this.put("recentCount", recentCount);
	}

	public Integer getRecentCount()
	{
	 	return (Integer)this.get("recentCount");
	}

	/** <i>Generated property</i> for <code>LocationsAffinity.location</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setLocation(final Location location)
	{
		this.put("location", location);
	}

	public Location getLocation()
	{
	 	return (Location)this.get("location");
	}

}
