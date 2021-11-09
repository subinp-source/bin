/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Category is a POJO representing a Category within Hybris EC.
 */
public class Category
{
	@JsonProperty(value = "id")
	@NotNull
	private String id;

	@JsonProperty(value = "name")
	@NotNull
	private String name;

	/**
	 * @return the id, this is the identifier of the category.
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *           the id to set, this is the identifier of the category.
	 */
	public void setId(final String id)
	{
		this.id = id;
	}

	/**
	 * @return the name, this is the name of the category.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set, this is the name of the category.
	 */
	public void setName(final String name)
	{
		this.name = name;
	}
}
