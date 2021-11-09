/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search.impl;

import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.search.IntegrationItemToMapConverter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultIntegrationItemToMapConverter implements IntegrationItemToMapConverter
{
	@Override
	public Map<String, Object> convert(final IntegrationItem integrationItem)
	{
		final Map<String, Object> integrationItemMap = new HashMap<>();

		final Collection<TypeAttributeDescriptor> attributes = integrationItem.getAttributes();
		attributes.forEach(a -> integrationItemMap.put(a.getAttributeName(), integrationItem.getAttribute(a.getAttributeName())));

		integrationItemMap.entrySet().stream()
		                  .filter(e -> e.getValue() instanceof IntegrationItem)
		                  .forEach(e -> integrationItemMap.put(e.getKey(), convert((IntegrationItem) e.getValue())));

		return integrationItemMap;
	}
}
