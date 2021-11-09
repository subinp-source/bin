/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

import de.hybris.platform.integrationservices.item.IntegrationItem;

import java.util.Map;

/**
 * Converts IntegrationItem to its Map representation.
 */
public interface IntegrationItemToMapConverter
{
	/**
	 * Generates Map of property name - values from the Integration Item in a hierarchical way; when nested referenced
	 * attributes are present, they will also be represented as a nested map.
	 *
	 * @param integrationItem Integration Item from a request that will be transformed into a Map representation.
	 * @return Map representation of the specified integration item. Keys of the map contain attribute names of the integration
	 * item and map values are values of the corresponding attributes. When integration item attribute value is a nested
	 * integration item or a collection of items, then that nested item(s) is converted to a Map(s).
	 */
	Map<String, Object> convert(IntegrationItem integrationItem);
}
