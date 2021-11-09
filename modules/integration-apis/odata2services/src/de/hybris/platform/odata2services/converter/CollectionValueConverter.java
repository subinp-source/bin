/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import static de.hybris.platform.odata2services.converter.ConversionParameters.ConversionParametersBuilder.conversionParametersBuilder;

import de.hybris.platform.odata2services.constants.Odata2servicesConstants;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

/**
 * A base class for {@link Collection} value converters.
 */
public abstract class CollectionValueConverter extends AbstractValueConverter
{
	/**
	 * A constructor injecting dependencies for reuse.
	 *
	 * @param entryConverter an instance of {@link ODataEntryToIntegrationItemConverter} to use.
	 * @param valueConverter an instance of {@link PayloadAttributeValueConverter} to use.
	 */
	protected CollectionValueConverter(@NotNull final ODataEntryToIntegrationItemConverter entryConverter,
	                                   @NotNull final PayloadAttributeValueConverter valueConverter)
	{
		super(entryConverter, valueConverter);
	}

	/**
	 * Presents attribute value, that is conceptually a collection but may not be presented so in the ODataEntry, as a {@link Collection}
	 *
	 * @param value a value that represents a collection of elements but may not implement one of the Java {@link Collection}
	 *              interfaces.
	 * @return same value presented as a Java {@link Collection}
	 */
	protected abstract Collection getAttributeValue(final Object value);

	@Override
	public Object convert(final ConversionParameters parameters)
	{
		final Collection<?> collection = getAttributeValue(parameters.getAttributeValue());
		final ConversionParameters updatedParams = conversionParametersBuilder().from(parameters)
		                                                                        .withAttributeValue(collection)
		                                                                        .build();
		return isReferringLocalizedAttributesType(updatedParams)
				? handleLocalizedAttributes(updatedParams)
				: handleCollection(updatedParams);
	}

	/**
	 * Converts a "classic" collection value, where the collection in EDMX corresponds to a collection in the type system.
	 *
	 * @param parameters context parameters for the conversion, where {@link ConversionParameters#getAttributeValue()}
	 *                   returns an instance of {@link Collection}
	 * @return a collection, in which each element is converted from the elements of
	 * {@link ConversionParameters#getAttributeValue()} collection.
	 */
	protected Object handleCollection(final ConversionParameters parameters)
	{
		final Collection<?> collection = (Collection<?>) parameters.getAttributeValue();
		final Collection<Object> converted = new ArrayList<>(collection.size());
		for (final Object el : collection)
		{
			final ConversionParameters updatedParams = conversionParametersBuilder().from(parameters)
			                                                                        .withAttributeValue(el)
			                                                                        .build();
			converted.add(toValue(updatedParams));
		}
		return converted;
	}

	/**
	 * Converts a collection of Localized___xxx entities representing localized attribute in the type system.
	 *
	 * @param parameters a collection of localized values, in which each element is an ODataEntry of Localized___xxx type.
	 * @return localized attribute values.
	 */
	protected LocalizedAttributes handleLocalizedAttributes(final ConversionParameters parameters)
	{
		final Collection<?> collection = (Collection<?>) parameters.getAttributeValue();
		final Collection<Object> converted = convertCollectionToLocalizedAttributes(collection);
		return converted.stream()
		                .map(LocalizedAttributes.class::cast)
		                .reduce(LocalizedAttributes.EMPTY, LocalizedAttributes::combine);
	}

	private Collection<Object> convertCollectionToLocalizedAttributes(final Collection<?> value)
	{
		final Collection<Object> converted = new ArrayList<>(value.size());
		for (final Object entry : value)
		{
			converted.add(LocalizedAttributes.createFrom((ODataEntry) entry));
		}
		return converted;
	}

	private boolean isReferringLocalizedAttributesType(final ConversionParameters parameters)
	{
		return Odata2servicesConstants.LOCALIZED_ATTRIBUTE_NAME.equals(parameters.getAttributeName());
	}
}