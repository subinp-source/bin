/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 08-Nov-2021, 4:51:28 PM
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

import de.hybris.platform.yaasyprofileconnect.yaas.Affinity;
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
		public Affinities build() {
			final Affinities dto = new Affinities();
			dto.putAll(this);
			return dto;
		}
	}

	/** Default serialVersionUID value. */
 	private static final long serialVersionUID = 1L;

    @JsonCreator
	public Affinities(@JsonProperty("categories") final Map<String,Affinity> categories, @JsonProperty("products") final Map<String,Affinity> products)
	{
		this.put("categories", categories);
		this.put("products", products);
	}

	public Affinities()
	{
		// default constructor
	}

	/** <i>Generated property</i> for <code>Affinities.categories</code> property defined at extension <code>yaasyprofileconnect</code>. */
	public void setCategories(final Map<String,Affinity> categories)
	{
		this.put("categories", categories);
	}

	public Map<String,Affinity> getCategories()
	{
	 	return (Map<String,Affinity>)this.get("categories");
	}

	/** <i>Generated property</i> for <code>Affinities.products</code> property defined at extension <code>yaasyprofileconnect</code>. */
	public void setProducts(final Map<String,Affinity> products)
	{
		this.put("products", products);
	}

	public Map<String,Affinity> getProducts()
	{
	 	return (Map<String,Affinity>)this.get("products");
	}

}
