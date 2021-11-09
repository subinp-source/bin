/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.ClassificationAttributeValueConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Implementation for converting collection of strings to a collection of
 * {@link de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel}
 */
public class CollectionClassificationAttributeValueConverter extends AbstractClassificationAttributeValueConverter
        implements ClassificationAttributeValueConverter
{
    @Override
    public boolean isApplicable(final ClassificationTypeAttributeDescriptor attribute)
    {
        return isAttributeTypeEnum(attribute) && attribute.isCollection() && !attribute.isLocalized();
    }

    @Override
    public Object convert(final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        return createValuesForEnums(attribute, value);
    }

    private Collection<?> createValuesForEnums(final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        return value instanceof Collection ?
                getClassificationValueModelsForCodes(attribute, ((Collection<?>) value)) :
                getClassificationValueModelsForCodes(attribute, Collections.singletonList(value));
    }

    private Collection<?> getClassificationValueModelsForCodes(final ClassificationTypeAttributeDescriptor attribute,
                                                               final Collection<?> streamOfEnumCodes)
    {
        final var newValues = streamOfEnumCodes.stream()
                                               .filter(code -> code instanceof String)
                                               .map(String.class::cast)
                                               .map(strValue -> getAttributeValue(attribute, strValue))
                                               .collect(Collectors.toList());
        newValues.forEach(newValue -> addValueToClassificationAttributeAssignmentAttributeValues(attribute, newValue));
        return newValues;
    }
}
