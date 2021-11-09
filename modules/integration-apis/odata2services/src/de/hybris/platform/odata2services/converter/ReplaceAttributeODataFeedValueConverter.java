/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import java.util.Collection;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;

/**
 * A converter that handles {@link ODataFeed} attribute value in PATCH requests.
 */
public class ReplaceAttributeODataFeedValueConverter extends ReplaceAttributeCollectionValueConverter
{
	/**
	 * A constructor that injects dependencies to be reused by subclasses.
	 *
	 * @param entryConverter an implementation of {@link ODataEntryToIntegrationItemConverter} to use.
	 * @param valueConverter an implementation of {@link PayloadAttributeValueConverter} to use.
	 * @param provider       an implementation of {@link LocalizedValueProvider} to use.
	 */
	public ReplaceAttributeODataFeedValueConverter(final ODataEntryToIntegrationItemConverter entryConverter,
	                                               final PayloadAttributeValueConverter valueConverter,
	                                               final LocalizedValueProvider provider)
	{
		super(entryConverter, valueConverter, provider);
	}

	@Override
	public boolean isApplicable(final ConversionParameters parameters)
	{
		return super.isApplicable(parameters)
				&& parameters.getAttributeValue() instanceof ODataFeed
				&& !parameters.isMapAttributeValue();
	}

	@Override
	protected Collection<ODataEntry> getAttributeValue(final Object attrValue)
	{
		return ((ODataFeed) attrValue).getEntries();
	}
}
