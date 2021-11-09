/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import java.util.HashMap;
import java.util.Map;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;

/**
 * A converter that handles ODataFeed with Map entry ODataEntries and converts them to a {@link Map}
 */
public class MapValueConverter implements ValueConverter
{
	private static final String KEY_PROPERTY = "key";
	private static final String VALUE_PROPERTY = "value";

	@Override
	public boolean isApplicable(final ConversionParameters parameters)
	{
		return parameters.isMapAttributeValue();
	}

	@Override
	public Object convert(final ConversionParameters parameters)
	{
		final Object attrValue = parameters.getAttributeValue();
		return attrValue instanceof ODataFeed
				? toMap((ODataFeed) attrValue)
				: null;
	}

	private Map<Object, Object> toMap(final ODataFeed attrValue)
	{
		final Map<Object, Object> mapValue = new HashMap<>();
		attrValue.getEntries().stream()
		         .map(ODataEntry::getProperties)
		         .forEach(e -> mapValue.put(e.get(KEY_PROPERTY), e.get(VALUE_PROPERTY)));
		return mapValue;
	}
}
