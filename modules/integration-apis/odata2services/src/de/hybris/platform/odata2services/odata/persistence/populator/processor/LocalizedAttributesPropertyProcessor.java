/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.persistence.populator.processor;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LANGUAGE_KEY_PROPERTY_NAME;
import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LOCALIZED_ATTRIBUTE_NAME;
import static de.hybris.platform.odata2services.odata.schema.utils.SchemaUtils.localizedEntityName;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.odata2services.odata.persistence.AbstractRequest;
import de.hybris.platform.odata2services.odata.persistence.ConversionOptions;
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException;
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;
import de.hybris.platform.odata2services.odata.persistence.MissingLanguageException;
import de.hybris.platform.odata2services.odata.persistence.ODataLocalizationService;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.edm.EdmTyped;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.core.ep.entry.EntryMetadataImpl;
import org.apache.olingo.odata2.core.ep.entry.MediaMetadataImpl;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.apache.olingo.odata2.core.uri.ExpandSelectTreeNodeImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class LocalizedAttributesPropertyProcessor extends AbstractCollectionPropertyProcessor
{
	private static final Logger LOG = Log.getLogger(LocalizedAttributesPropertyProcessor.class);
	private ODataLocalizationService oDataLocalizationService;

	@Override
	protected boolean shouldPropertyBeConverted(final ItemConversionRequest conversionRequest, final String propertyName)
	{
		return false;
	}

	@Override
	protected boolean isPropertySupported(final PropertyMetadata propertyMetadata)
	{
		return LOCALIZED_ATTRIBUTE_NAME.equals(propertyMetadata.getPropertyName());
	}

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor typeAttributeDescriptor)
	{
		return false;
	}

	@Override
	protected boolean isItemPropertySettable(final ItemModel item, final String propertyName, final StorageRequest request)
	{
		return true;
	}

	@Override
	protected void processItemInternal(final ItemModel item, final String propertyName, final Object value, final StorageRequest request)
	{
		if (value instanceof ODataFeed)
		{
			final List<ODataEntry> attributes = ((ODataFeed) value).getEntries();
			attributes.forEach(entry -> {
				try
				{
					populateAttributesFromEntry(item, entry, request);
				}
				catch (final EdmException e)
				{
					throw new InternalProcessingException(e);
				}
			});
		}
	}

	@Override
	public void processEntity(final ODataEntry oDataEntry, final ItemConversionRequest conversionRequest) throws EdmException
	{
		final EdmTyped localizedAttributes = conversionRequest.getEntityType().getProperty(LOCALIZED_ATTRIBUTE_NAME);

		if (localizedAttributes instanceof EdmNavigationProperty)
		{
			final TypeDescriptor typeDescriptor = getTypeDescriptor(conversionRequest);
			if (typeDescriptor != null)
			{
				processEntityInternal(oDataEntry, LOCALIZED_ATTRIBUTE_NAME, getSimpleLocalizedAttributes(typeDescriptor), conversionRequest);
			}
		}
	}

	private List<TypeAttributeDescriptor> getSimpleLocalizedAttributes(final TypeDescriptor typeDescriptor)
	{
		return typeDescriptor
				.getAttributes()
				.stream()
				.filter(TypeAttributeDescriptor::isPrimitive)
				.filter(TypeAttributeDescriptor::isLocalized)
				.collect(Collectors.toList());
	}

	@Override
	protected boolean canHandleEntityValue(final Object value)
	{
		return value != null;
	}

	/**
	 * Derives the Entries for the localizedAttributes property.
	 *
	 * @param request - item conversion request
	 * @param localizedProperty - should be equivalent to the LOCALIZED_ATTRIBUTE_NAME constant
	 * @param value - all localizable & primitive {@link TypeAttributeDescriptor}s
	 * that belong to the parent item derived from the entityType in the ItemConversionRequest
	 * @return a list of translations for the localizedSimpleAttributeDescriptors
	 */
	@Override
	protected List<ODataEntry> deriveDataFeedEntries(final ItemConversionRequest request, final String localizedProperty, final Object value)
	{
		final Map<Locale, Map<String, Object>> itemTranslations = new HashMap<>();
		final ConversionOptions options = request.getOptions();
		if ((options.isExpandPresent() || options.isNavigationSegmentPresent()) &&
				value instanceof List)
		{
			final Locale[] supportedLocales = getoDataLocalizationService().getSupportedLocales();
			final List<?> descriptors = (List) value;
			descriptors.stream()
					.filter(TypeAttributeDescriptor.class::isInstance)
					.map(TypeAttributeDescriptor.class::cast)
					.forEach(descriptor -> addPropertyLocalizationsToMap(itemTranslations, descriptor, request, supportedLocales));
		}
		return getEntries(itemTranslations);
	}

	private TypeDescriptor getTypeDescriptor(final AbstractRequest conversionRequest) throws EdmException
	{
		final String itemTypeCode = conversionRequest.getEntityType().getName();
		final String integrationObjectCode = conversionRequest.getIntegrationObjectCode();
		return getItemTypeDescriptorService().getTypeDescriptorByTypeCode(integrationObjectCode, itemTypeCode).orElse(null);
	}

	private void addPropertyLocalizationsToMap(final Map<Locale, Map<String, Object>> itemTranslations, final TypeAttributeDescriptor descriptor, final ItemConversionRequest request, final Locale[] supportedLocales)
	{
		final Map<Locale, Object> attrLocalizations = descriptor.accessor().getValues(request.getValue(), supportedLocales);
		if (attrLocalizations != null)
		{
			attrLocalizations.forEach((locale, localizedPropertyValue) -> {
				Map<String, Object> existingLocaleEntry = itemTranslations.get(locale);
				if (existingLocaleEntry == null)
				{
					existingLocaleEntry = createNewLocalizedEntry(locale);
				}
				addToEntry(itemTranslations, descriptor.getAttributeName(), localizedPropertyValue, locale, existingLocaleEntry);
			});
		}
	}

	private void addToEntry(final Map<Locale, Map<String, Object>> itemTranslations, final String localizedPropertyName, final Object localizedPropertyValue, final Locale locale, final Map<String, Object> existingLocaleEntry)
	{
		existingLocaleEntry.put(localizedPropertyName, localizedPropertyValue);
		itemTranslations.put(locale, existingLocaleEntry);
	}

	private Map<String, Object> createNewLocalizedEntry(final Locale locale)
	{
		final Map<String, Object> newEntry = new HashMap<>();
		newEntry.put(LANGUAGE_KEY_PROPERTY_NAME, locale.toString());
		return newEntry;
	}

	private List<ODataEntry> getEntries(final Map<Locale, Map<String, Object>> itemTranslations)
	{
		return itemTranslations.entrySet().stream().map(this::createLocalizedODataEntry).collect(Collectors.toList());
	}

	private ODataEntry createLocalizedODataEntry(final Map.Entry<Locale, Map<String, Object>> translation)
	{
		final Locale key = translation.getKey();

		final ODataEntry entry = new ODataEntryImpl(com.google.common.collect.Maps.newHashMap(), new MediaMetadataImpl(),
				new EntryMetadataImpl(), new ExpandSelectTreeNodeImpl());
		entry.getProperties().put(LANGUAGE_KEY_PROPERTY_NAME, key.getLanguage());
		entry.getProperties().putAll(translation.getValue());
		return entry;
	}

	private void populateAttributesFromEntry(final ItemModel item, final ODataEntry entry, final StorageRequest request) throws EdmException
	{
		final TypeDescriptor typeDescriptor = getTypeDescriptor(request);
		if (typeDescriptor != null)
		{
			final Map<String, Object> attributes = entry.getProperties();
			final Locale locale = getLocale(item, attributes);
			attributes.entrySet().stream()
					.filter(attr -> !LANGUAGE_KEY_PROPERTY_NAME.equals(attr.getKey()))
					.filter(attr -> isItemPropertySettableInternal(item, attr.getKey(), request))
					.filter(attr -> isPropertyLocalized(item, attr.getKey(), request))
					.forEach(attr -> populateAttribute(item, locale, attr, typeDescriptor));
		}
	}

	private void populateAttribute(final ItemModel item, final Locale locale, final Map.Entry<String, Object> attribute, final TypeDescriptor typeDescriptor)
	{
		final String attrName = attribute.getKey();
		final Object newValue = attribute.getValue();
		final Optional<TypeAttributeDescriptor> attributeDescriptor = typeDescriptor.getAttribute(attrName);
		if (attributeDescriptor.isPresent())
		{
			final Object currentValue = attributeDescriptor.get().accessor().getValue(item, locale);

			if (currentValue != null && !currentValue.equals(newValue))
			{
				LOG.info("localizedAttributes and localized properties were present in the request payload. Overriding attribute '{}', old value: '{}' -> new value: '{}'", attrName, currentValue, newValue);
			}
		}
		getModelService().setAttributeValue(item, attrName, Collections.singletonMap(locale, newValue));
	}

	private Locale getLocale(final ItemModel item, final Map<String, Object> attributes)
	{
		final Optional<Map.Entry<String, Object>> languageEntry = attributes.entrySet().stream()
				.filter(e -> LANGUAGE_KEY_PROPERTY_NAME.equals(e.getKey()))
				.findFirst();

		if (languageEntry.isPresent())
		{
			return getoDataLocalizationService().getLocaleForLanguage((String) languageEntry.get().getValue());
		}
		throw new MissingLanguageException(localizedEntityName(item.getItemtype()));
	}

	private boolean isItemPropertySettableInternal(final ItemModel item, final String propertyName, final StorageRequest request)
	{
		try
		{
			if (super.isItemPropertySettable(item, propertyName, request))
			{
				return true;
			}
			LOG.info("The attribute {} is not settable. This attribute will not be persisted in the localized attributes.", propertyName);
		}
		catch (final EdmException e)
		{
			LOG.error("An exception occurred while checking whether the attribute {} is settable", propertyName, e);
		}
		return false;
	}

	private boolean isPropertyLocalized(final ItemModel item, final String propertyName, final StorageRequest request)
	{
		try
		{
			if (getAttributeDescriptor(item, propertyName, request).getLocalized())
			{
				return true;
			}
			LOG.info("The attribute {} is not localized. This attribute will not be persisted in the localized attributes.", propertyName);
		}
		catch (final EdmException e)
		{
			LOG.error("An exception occurred while checking whether the attribute {} is localized", propertyName, e);
		}
		return false;
	}

	protected ODataLocalizationService getoDataLocalizationService()
	{
		return oDataLocalizationService;
	}

	@Required
	public void setoDataLocalizationService(final ODataLocalizationService oDataLocalizationService)
	{
		this.oDataLocalizationService = oDataLocalizationService;
	}
}
