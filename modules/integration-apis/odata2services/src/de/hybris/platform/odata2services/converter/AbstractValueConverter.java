/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.processor.ODataContext;

/**
 * Base implementation of a {@link ValueConverter} to be extended for reuse.
 */
public abstract class AbstractValueConverter implements ValueConverter
{
	private final ODataEntryToIntegrationItemConverter oDataEntryConverter;
	private final PayloadAttributeValueConverter attributeValueConverter;

	/**
	 * A constructor injecting dependencies for reuse.
	 *
	 * @param entryConverter an instance of {@link ODataEntryToIntegrationItemConverter} to use.
	 * @param valueConverter an instance of {@link PayloadAttributeValueConverter} to use.
	 */
	protected AbstractValueConverter(@NotNull final ODataEntryToIntegrationItemConverter entryConverter,
	                                 @NotNull final PayloadAttributeValueConverter valueConverter)
	{
		oDataEntryConverter = entryConverter;
		attributeValueConverter = valueConverter;
	}

	/**
	 * Converts ODataEntry nested as an attribute value inside another ODataEntry to an {@link IntegrationItem} by
	 * delegating to {@link ODataEntryToIntegrationItemConverter}
	 *
	 * @param context   OData context for the entry being converted.
	 * @param typeDesc  item type corresponding to the ODataEntry being converted.
	 * @param entry     a value of an ODataEntry attribute.
	 * @param container an item in which the converted value will be set as an attribute value.
	 * @return result of the {@code entry} conversion, which will be nested as an attribute value inside the
	 * {@code container} item
	 */
	protected IntegrationItem toIntegrationItem(@NotNull final ODataContext context,
	                                            @NotNull final TypeDescriptor typeDesc,
	                                            @NotNull final ODataEntry entry,
	                                            final IntegrationItem container)
	{
		return oDataEntryConverter.convert(context, typeDesc, entry, container);
	}

	/**
	 * Converts a nested OData specific value to a value acceptable for {@link IntegrationItem} by delegating to
	 * {@link PayloadAttributeValueConverter}. This method can be used by the {@link ValueConverter}s dealing with
	 * collection or map values, to convert elements of the Collection or values in the Map.
	 *
	 * @param parameters carry the value to be converted and the context for the value conversion.
	 * @return a converted value.
	 */
	protected Object toValue(final ConversionParameters parameters)
	{
		return attributeValueConverter.convertAttributeValue(parameters);
	}
}
