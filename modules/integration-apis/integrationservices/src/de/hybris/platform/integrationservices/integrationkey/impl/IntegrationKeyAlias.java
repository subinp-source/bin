/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.integrationkey.impl;

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROP_DIV;
import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_TYPE_DIV;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.assertj.core.util.Lists;

/**
 * This class takes the integrationKey Alias string formatted like this {@code Type1_prop1|Type2_prop2|...} and turns it into an
 * ordered map of EntityType to its key properties.
 */
public class IntegrationKeyAlias
{
	// Using LinkedHashMap to maintain the original order defined in the integration key alias
	private final Map<String, List<String>> entityTypeToKeyProperties = new LinkedHashMap<>();

	public IntegrationKeyAlias(final String alias)
	{
		splitAliasStringToMap(alias);
	}

	private void splitAliasStringToMap(final String alias)
	{
		Pattern.compile("\\" + INTEGRATION_KEY_PROP_DIV)
				.splitAsStream(alias)
				.forEach(typeAndPropertyName ->
						addToMap(typeAndPropertyName.split(INTEGRATION_KEY_TYPE_DIV)));
	}

	private void addToMap(final String[] typeAndPropertyName)
	{
		addPropertyToType(typeAndPropertyName[0], typeAndPropertyName[1]);
	}

	private void addPropertyToType(final String entityType, final String property)
	{
		final List<String> values = entityTypeToKeyProperties.putIfAbsent(entityType, Lists.newArrayList(property));
		if (values != null)
		{
			entityTypeToKeyProperties.get(entityType).add(property);
		}
	}

	/**
	 * All types in this integrationKey Alias. <b>Note that types are not in original Alias order</b>
	 * @return types returned not in order
	 */
	public Set<String> getTypes()
	{
		return entityTypeToKeyProperties.keySet();
	}

	/**
	 * Returns all key property names for the given entity type
	 * @param type entity type to get property value for
	 * @return list of key properties
	 */
	public List<String> getProperties(final String type)
	{
		return entityTypeToKeyProperties.get(type);
	}
}
