/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import java.util.Collection;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;

/**
 * A converter of {@link ODataFeed} value to a {@link Collection}.
 */
public class ODataFeedValueConverter extends CollectionValueConverter
{
	/**
	 * A constructor injecting dependencies for reuse.
	 *
	 * @param entryConverter an instance of {@link ODataEntryToIntegrationItemConverter} to use.
	 * @param valueConverter an instance of {@link PayloadAttributeValueConverter} to use.
	 */
	public ODataFeedValueConverter(final ODataEntryToIntegrationItemConverter entryConverter,
	                               final PayloadAttributeValueConverter valueConverter)
	{
		super(entryConverter, valueConverter);
	}

	@Override
	public boolean isApplicable(final ConversionParameters parameters)
	{
		return !parameters.isReplaceAttributesRequest()
				&& !parameters.isMapAttributeValue()
				&& parameters.getAttributeValue() instanceof ODataFeed;
	}

	@Override
	protected Collection<ODataEntry> getAttributeValue(final Object value)
	{
		return ((ODataFeed) value).getEntries();
	}
}
