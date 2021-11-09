/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

import de.hybris.platform.integrationservices.item.IntegrationItem;

import java.util.Map;

/**
 * Generator of Map of property name - values.
 */
public interface IntegrationObjectMapGenerator
{
	/**
	 * Generates Map of property name - values from the Integration Item in a hierarchical way; when nested referenced
	 * attributes are present, they will also be represented as a nested map.
	 *
	 * @param integrationItem Integration Item from a request that will be transformed into a Map representation.
	 * @return Hierarchical map representation of the property name - value of the Integration Item sent as a parameter.
	 */
	Map<String, Object> generateMap(IntegrationItem integrationItem);
}
