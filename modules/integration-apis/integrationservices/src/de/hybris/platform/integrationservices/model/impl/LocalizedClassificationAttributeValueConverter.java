/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.ClassificationAttributeValueConverter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Converts localized string values to localized
 * {@link de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel}.
 */
public class LocalizedClassificationAttributeValueConverter extends AbstractClassificationAttributeValueConverter
        implements ClassificationAttributeValueConverter
{
    @Override
    public boolean isApplicable(final ClassificationTypeAttributeDescriptor attribute)
    {
        return isAttributeTypeEnum(attribute) && attribute.isLocalized() && !attribute.isCollection();
    }

    @Override
    public Object convert(final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        return createValuesForLocalizedEnums(attribute, value);
    }

    private Object createValuesForLocalizedEnums(final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        if (value instanceof Map)
        {
            final var localizedEnums = ((Map<?, ?>) value).entrySet().stream()
                                                          .filter(entry -> entry.getKey() instanceof Locale)
                                                          .filter(entry -> entry.getValue() instanceof String)
                                                          .collect(Collectors.toMap(
                                                                  Map.Entry::getKey,
                                                                  e -> getAttributeValue(attribute, (String) e.getValue())));
            localizedEnums.values()
                          .forEach(localizedValue -> addValueToClassificationAttributeAssignmentAttributeValues(attribute,
                                  localizedValue));
            return localizedEnums;
        }
        return new HashMap<>();
    }
}
