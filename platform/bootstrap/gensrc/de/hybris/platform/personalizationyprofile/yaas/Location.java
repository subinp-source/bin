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



public class Location extends LinkedHashMap<String, Object> implements Map<String, Object> {

	public static class LocationBuilder extends LinkedHashMap<String, Object> {

		public LocationBuilder withRegionCode(final String regionCode) {
			this.put("regionCode", regionCode);
			return this;
		}

		public LocationBuilder withCity(final String city) {
			this.put("city", city);
			return this;
		}

		public LocationBuilder withCountryCode(final String countryCode) {
			this.put("countryCode", countryCode);
			return this;
		}
		public Location build() {
			final Location dto = new Location();
			dto.putAll(this);
			return dto;
		}
	}

	/** Default serialVersionUID value. */
 	private static final long serialVersionUID = 1L;

    @JsonCreator
	public Location(@JsonProperty("regionCode") final String regionCode, @JsonProperty("city") final String city, @JsonProperty("countryCode") final String countryCode)
	{
		this.put("regionCode", regionCode);
		this.put("city", city);
		this.put("countryCode", countryCode);
	}

	public Location()
	{
		// default constructor
	}


	/** <i>Generated property</i> for <code>Location.regionCode</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setRegionCode(final String regionCode)
	{
		this.put("regionCode", regionCode);
	}

	public String getRegionCode()
	{
	 	return (String)this.get("regionCode");
	}

	/** <i>Generated property</i> for <code>Location.city</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setCity(final String city)
	{
		this.put("city", city);
	}

	public String getCity()
	{
	 	return (String)this.get("city");
	}

	/** <i>Generated property</i> for <code>Location.countryCode</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setCountryCode(final String countryCode)
	{
		this.put("countryCode", countryCode);
	}

	public String getCountryCode()
	{
	 	return (String)this.get("countryCode");
	}

}
