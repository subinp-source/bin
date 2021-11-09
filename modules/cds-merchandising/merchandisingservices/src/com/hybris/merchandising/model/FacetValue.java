/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.model;

/**
 * FacetValue represents a value for a given {@link ProductFacet}.
 */
public class FacetValue
{
	private Object id;
	private Object name;

	/**
	 * Retrieves the ID of the facet value.
	 * @return the ID of the value.
	 */
	public Object getId()
	{
		return id;
	}

	/**
	 * Sets the ID of the facet value.
	 * @param id the facet value ID to set.
	 */
	public void setId(final Object id)
	{
		this.id = id;
	}

	/**
	 * Retrieves the name of the facet value.
	 * @return the Facet value name.
	 */
	public Object getName()
	{
		return name;
	}

	/**
	 * Sets the facet value name.
	 * @param name the Facet value name.
	 */
	public void setName(final Object name)
	{
		this.name = name;
	}
}
