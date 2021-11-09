/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import java.util.Collection;

/**
 * A converter that handles "classic", i.e. non-localized attribute, Collection value.
 */
public class ODataCollectionValueConverter extends CollectionValueConverter
{
	public ODataCollectionValueConverter(final ODataEntryToIntegrationItemConverter entryConverter,
	                                     final PayloadAttributeValueConverter valueConverter)
	{
		super(entryConverter, valueConverter);
	}

	@Override
	public boolean isApplicable(final ConversionParameters parameters)
	{
		return !parameters.isReplaceAttributesRequest()
				&& parameters.getAttributeValue() instanceof Collection;
	}

	@Override
	protected Collection<?> getAttributeValue(final Object attrValue)
	{
		return (Collection<?>) attrValue;
	}
}
