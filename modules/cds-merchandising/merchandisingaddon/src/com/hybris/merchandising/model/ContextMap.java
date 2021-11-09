/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.model;

import java.io.Serializable;
import java.util.HashMap;


/**
 * A key-value Map to store various Merchandising related contextual data.
 *
 */
public class ContextMap implements Serializable
{
	private static final long serialVersionUID = 1L;
	private HashMap<String, Serializable> properties;

	public ContextMap()
	{
		this.properties = new HashMap<>();
	}

	public void removeProperty(final String propertyName)
	{
		this.properties.remove(propertyName);
	}

	public void addProperty(final String propertyName, final Serializable propertyValue)
	{
		this.properties.put(propertyName, propertyValue);
	}

	public Object getProperty(final String propertyName)
	{
		return this.properties.get(propertyName);
	}

	public HashMap<String, Serializable> getProperties()
	{
		return this.properties;
	}
}
