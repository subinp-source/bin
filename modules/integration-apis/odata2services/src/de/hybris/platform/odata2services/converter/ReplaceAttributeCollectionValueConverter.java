/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.item.LocalizedValue;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import javax.validation.constraints.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A base class for collection converters applicable to PATCH requests, which replace all existing values of the item
 * in the platform.
 */
public abstract class ReplaceAttributeCollectionValueConverter extends CollectionValueConverter
{
	private final LocalizedValueProvider localizationProvider;

	/**
	 * A constructor that injects dependencies to be reused by subclasses.
	 *
	 * @param entryConverter an implementation of {@link ODataEntryToIntegrationItemConverter} to use.
	 * @param valueConverter an implementation of {@link PayloadAttributeValueConverter} to use.
	 * @param provider       an implementation of {@link LocalizedValueProvider} to use.
	 */
	public ReplaceAttributeCollectionValueConverter(@NotNull final ODataEntryToIntegrationItemConverter entryConverter,
	                                                @NotNull final PayloadAttributeValueConverter valueConverter,
	                                                @NotNull final LocalizedValueProvider provider)
	{
		super(entryConverter, valueConverter);
		localizationProvider = provider;
	}

	@Override
	public boolean isApplicable(final ConversionParameters parameters)
	{
		return parameters.isReplaceAttributesRequest();
	}

	@Override
	protected LocalizedAttributes handleLocalizedAttributes(final ConversionParameters parameters)
	{
		final Collection<?> collection = (Collection<?>) parameters.getAttributeValue();
		return collection.isEmpty() ?
				handleLocalizedAttributesEmptyCollection(parameters) :
				handleLocalizedAttributesNonEmptyCollection(parameters);
	}

	private LocalizedAttributes handleLocalizedAttributesEmptyCollection(final ConversionParameters parameters)
	{
		final LocalizedValue nullLocalizedValue = localizationProvider.toNullLocalizedValue(parameters);
		final IntegrationItem item = parameters.getIntegrationItem();

		final Map<String, LocalizedValue> resetAttributes = new HashMap<>();
		getNamesOfLocalizedAttributes(item).forEach(attr -> resetAttributes.put(attr, nullLocalizedValue));
		return LocalizedAttributes.createWithValues(resetAttributes);
	}

	private List<String> getNamesOfLocalizedAttributes(final IntegrationItem item)
	{
		return item
				.getItemType()
				.getAttributes()
				.stream()
				.filter(TypeAttributeDescriptor::isLocalized)
				.map(TypeAttributeDescriptor::getAttributeName)
				.collect(Collectors.toList());
	}

	private LocalizedAttributes handleLocalizedAttributesNonEmptyCollection(final ConversionParameters parameters)
	{
		final LocalizedValue nullLocalizedValue = localizationProvider.getNullLocalizedValueForAllLanguages();
		final LocalizedAttributes localizedAttributes = super.handleLocalizedAttributes(parameters);
		final LocalizedAttributes resetAttributes = localizedAttributes.setAll(nullLocalizedValue);
		return resetAttributes.combine(localizedAttributes);
	}
}
