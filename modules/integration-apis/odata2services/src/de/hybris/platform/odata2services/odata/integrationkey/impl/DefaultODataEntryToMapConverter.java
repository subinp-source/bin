/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.integrationkey.impl;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.PRIMITIVE_ENTITY_PROPERTY_NAME;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.odata2services.odata.integrationkey.ODataEntryToMapConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;

import com.google.common.collect.ImmutableMap;

/**
 * Temporary class that will be removed when retrieval refactoring of OData dependent code is made.
 */
final class DefaultODataEntryToMapConverter implements ODataEntryToMapConverter
{
	/**
	 * Converts an ODataEntry to a Map<String, Object>, where the keys represent attribute names and the values represent the
	 * attribute values as a simple Object, Map, or List value. If the attribute represents a referenced Item or referenced Enum then
	 * it will be represented with a Map. If the attribute represents a collection or a map attribute then the value will be
	 * represented by a List.
	 * The Map does not convert the 'integrationKey' or `localizedAttributes' ODataEntry properties if they are present. The Map
	 * should be used to generate the integrationKey value for an Item, and localizedAttributes cannot be part of the integrationKey.
	 *
	 * @param typeDesc describes integration object item
	 * @param entry payload of the request/response for the item
	 * @return a Map representation of the entry properties that can be part of the Item's integrationKey
	 */
	@Override
	public Map<String, Object> convert(final TypeDescriptor typeDesc, final ODataEntry entry)
	{
		final Map<String, Object> entryMap = new HashMap<>();
		typeDesc.getAttributes().stream()
		        .filter(attr -> entry.getProperties().containsKey(attr.getAttributeName()))
		        .forEach(attr -> entryMap.put(attr.getAttributeName(), getConvertedAttributeValue(attr, entry.getProperties().get(attr.getAttributeName()))));
		return entryMap;
	}

	private Object getConvertedAttributeValue(final TypeAttributeDescriptor attribute, final Object value)
	{
		if (value instanceof ODataFeed)
		{
			return getCollectionOfElements(attribute, (ODataFeed) value);
		}
		if (value instanceof ODataEntry)
		{
			return convert(attribute.getAttributeType(),
					(ODataEntry) value);
		}
		return value;
	}

	private List<Map<String, Object>> getCollectionOfElements(final TypeAttributeDescriptor attribute,
	                                                          final ODataFeed collectionElements)
	{
		final List<Map<String, Object>> mapEntries = new ArrayList<>();
		if (attribute.isPrimitive())
		{
			collectionElements.getEntries().stream()
			                  .filter(entry -> entry.getProperties().containsKey(PRIMITIVE_ENTITY_PROPERTY_NAME))
			                  .forEach(entry -> mapEntries.add(ImmutableMap.of(PRIMITIVE_ENTITY_PROPERTY_NAME,
					                  entry.getProperties().get(PRIMITIVE_ENTITY_PROPERTY_NAME))));
		}
		else
		{
			collectionElements.getEntries()
			                  .forEach(entry -> mapEntries.add(convert(attribute.getAttributeType(), entry)));
		}
		return mapEntries;
	}
}
