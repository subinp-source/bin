/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.integrationkey.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

/**
 * Holds the property values for unique properties mapped by the entity types and maintains order according to the integrationKey Alias.
 */
public class IntegrationKeyValue
{
	private final Map<String, List<String>> typeToKeyValues;

	public IntegrationKeyValue()
	{
		// Using LinkedHashMap to maintain the original order defined in the integration key alias
		typeToKeyValues = new LinkedHashMap<>();
	}

	/**
	 * Adds the key property value to the given entity type.
	 *
	 * @param entityType the entity type to add the property value to
	 * @param propValue the key property value to add to the integration key
	 */
	public void addProperty(final String entityType, final String propValue)
	{
		final List<String> properties = typeToKeyValues.putIfAbsent(entityType, Lists.newArrayList(propValue));
		if (properties != null)
		{
			typeToKeyValues.get(entityType).add(propValue);
		}
	}

	/**
	 * Gets key property values for the given type.
	 *
	 * @param type the type to return property values for
	 * @return the key property values sorted according to the integrationKey Alias
	 */
	public List<String> getValuesFor(final String type)
	{
		return typeToKeyValues.get(type);
	}
}
