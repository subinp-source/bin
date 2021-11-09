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

import de.hybris.platform.personalizationyprofile.yaas.Affinity;
import de.hybris.platform.personalizationyprofile.yaas.LocationsAffinity;
import java.util.Map;


public class Affinities extends LinkedHashMap<String, Object> implements Map<String, Object> {

	public static class AffinitiesBuilder extends LinkedHashMap<String, Object> {

		public AffinitiesBuilder withCategories(final Map<String,Affinity> categories) {
			this.put("categories", categories);
			return this;
		}

		public AffinitiesBuilder withProducts(final Map<String,Affinity> products) {
			this.put("products", products);
			return this;
		}

		public AffinitiesBuilder withLocations(final Map<String,LocationsAffinity> locations) {
			this.put("locations", locations);
			return this;
		}
		public Affinities build() {
			final Affinities dto = new Affinities();
			dto.putAll(this);
			return dto;
		}
	}

	/** Default serialVersionUID value. */
 	private static final long serialVersionUID = 1L;

    @JsonCreator
	public Affinities(@JsonProperty("categories") final Map<String,Affinity> categories, @JsonProperty("products") final Map<String,Affinity> products, @JsonProperty("locations") final Map<String,LocationsAffinity> locations)
	{
		this.put("categories", categories);
		this.put("products", products);
		this.put("locations", locations);
	}

	public Affinities()
	{
		// default constructor
	}


	/** <i>Generated property</i> for <code>Affinities.categories</code> property defined at extension <code>personalizationyprofile</code>. */
@Deprecated
	/**
	* @deprecated Deprecated because Profile structure has changed
	*/
	public void setCategories(final Map<String,Affinity> categories)
	{
		this.put("categories", categories);
	}

@Deprecated
	/**
	* @deprecated Deprecated because Profile structure has changed
	*/
	public Map<String,Affinity> getCategories()
	{
	 	return (Map<String,Affinity>)this.get("categories");
	}

	/** <i>Generated property</i> for <code>Affinities.products</code> property defined at extension <code>personalizationyprofile</code>. */
@Deprecated
	/**
	* @deprecated Deprecated because Profile structure has changed
	*/
	public void setProducts(final Map<String,Affinity> products)
	{
		this.put("products", products);
	}

@Deprecated
	/**
	* @deprecated Deprecated because Profile structure has changed
	*/
	public Map<String,Affinity> getProducts()
	{
	 	return (Map<String,Affinity>)this.get("products");
	}

	/** <i>Generated property</i> for <code>Affinities.locations</code> property defined at extension <code>personalizationyprofile</code>. */
@Deprecated
	/**
	* @deprecated Deprecated because Profile structure has changed
	*/
	public void setLocations(final Map<String,LocationsAffinity> locations)
	{
		this.put("locations", locations);
	}

@Deprecated
	/**
	* @deprecated Deprecated because Profile structure has changed
	*/
	public Map<String,LocationsAffinity> getLocations()
	{
	 	return (Map<String,LocationsAffinity>)this.get("locations");
	}

}
