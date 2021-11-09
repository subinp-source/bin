/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.integrationservices.populator;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;

public class LocalizedAttribute2MapPopulator extends AbstractItemToMapPopulator
{
	private CommonI18NService commonI18NService;
	private static final String LOCALIZED_ATTRIBUTES = "localizedAttributes";
	private static final String LANGUAGE = "language";


	@Override
	protected void populateToMap(
			final TypeAttributeDescriptor attr,
			final ItemToMapConversionContext source,
			final Map<String, Object> target)
	{
		final String attributeName = attr.getAttributeName();
		final Map<Locale, Object> attrLocalizations = attr.accessor().getValues(source.getItemModel(), getSupportedLocales());

		final Object localizedAttributes = target.get(LOCALIZED_ATTRIBUTES);
		if (localizedAttributes instanceof List)
		{
			final List<Map<String, Object>> existingLocalizedAttributes = (List<Map<String, Object>>) localizedAttributes;
			attrLocalizations
					.entrySet()
					.forEach(localeAttrValueEntry ->
							addAttributeToExistingLocaleEntry(existingLocalizedAttributes, localeAttrValueEntry, attributeName));
		}
		else
		{
			final List<Map<String, Object>> itemTranslations = createLocalizedAttributes(attributeName, attrLocalizations);
			target.put(LOCALIZED_ATTRIBUTES, itemTranslations);
		}
	}

	private Locale[] getSupportedLocales()
	{
		return getCommonI18NService().getAllLanguages().stream()
		                             .map(getCommonI18NService()::getLocaleForLanguage)
		                             .toArray(Locale[]::new);
	}

	private List<Map<String, Object>> createLocalizedAttributes(final String attributeName, final Map<Locale, Object> attrTranslations)
	{
		return attrTranslations.entrySet().stream().map(localeAttrValueEntry -> createLocaleEntryWithAttribute(localeAttrValueEntry, attributeName)).collect(Collectors.toList());
	}

	private Map<String, Object> createLocaleEntryWithAttribute(final Map.Entry<Locale, Object> localeAttrValueEntry, final String attributeName)
	{
		final Map<String, Object> translation = new HashMap<>();
		translation.put(LANGUAGE, localeAttrValueEntry.getKey().toString());
		translation.put(attributeName, localeAttrValueEntry.getValue());
		return translation;
	}

	private void addAttributeToExistingLocaleEntry(final List<Map<String, Object>> existingLocalizedAttributes, final Map.Entry<Locale, Object> localeAttrValueEntry, final String attributeName)
	{
		final String language = localeAttrValueEntry.getKey().toString();
		final Object value = localeAttrValueEntry.getValue();
		final Optional<Map<String, Object>> existingLocalizationMapOptional = existingLocalizedAttributes.stream()
		                                                                                                 .filter(localizedEntry -> localizedEntry.get(LANGUAGE).equals(language))
		                                                                                                 .findFirst();

		if (existingLocalizationMapOptional.isPresent())
		{
			existingLocalizationMapOptional.get().put(attributeName, value);
		}
		else
		{
			existingLocalizedAttributes.add(createLocaleEntryWithAttribute(localeAttrValueEntry, attributeName));
		}
	}

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attributeDescriptor)
	{
		return attributeDescriptor.isLocalized();
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}
}
