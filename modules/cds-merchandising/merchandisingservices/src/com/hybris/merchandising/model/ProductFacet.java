/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.model;

import java.util.List;


/**
 * Facet represents a Facet for a product.
 */
public class ProductFacet
{
	private String id;
	private String name;

	private List<FacetValue> values;

	/**
	 * Retrieves the ID of the facet.
	 * @return a String representing the ID.
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Sets the ID of the facet.
	 * @param id the ID to set for the Facet.
	 */
	public void setId(final String id)
	{
		this.id = id;
	}

	/**
	 * Retrieves the name of the facet.
	 * @return a String representing the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name of the facet.
	 * @param name a String representing the name.
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Retrieves a list of values for the facet.
	 * @return a List of {@link FacetValue} objects.
	 */
	public List<FacetValue> getValues()
	{
		return values;
	}

	/**
	 * Sets a list of values for the facet.
	 * @param values a List of {@link FacetValue} objects.
	 */
	public void setValues(final List<FacetValue> values)
	{
		this.values = values;
	}
}
