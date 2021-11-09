/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import java.util.Collection;

/**
 * A converter that handles a collection attribute value in PATCH requests.
 */
public class ReplaceAttributeODataCollectionValueConverter extends ReplaceAttributeCollectionValueConverter
{
	/**
	 * A constructor that injects dependencies to be reused by subclasses.
	 *
	 * @param entryConverter an implementation of {@link ODataEntryToIntegrationItemConverter} to use.
	 * @param valueConverter an implementation of {@link PayloadAttributeValueConverter} to use.
	 * @param provider       an implementation of {@link LocalizedValueProvider} to use.
	 */
	public ReplaceAttributeODataCollectionValueConverter(final ODataEntryToIntegrationItemConverter entryConverter,
	                                                     final PayloadAttributeValueConverter valueConverter,
	                                                     final LocalizedValueProvider provider)
	{
		super(entryConverter, valueConverter, provider);
	}

	@Override
	public boolean isApplicable(final ConversionParameters parameters)
	{
		return super.isApplicable(parameters) && parameters.getAttributeValue() instanceof Collection;
	}

	@Override
	protected Collection<?> getAttributeValue(final Object attrValue)
	{
		return (Collection<?>) attrValue;
	}
}
