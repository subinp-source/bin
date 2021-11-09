/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException;
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueConverter;

/**
 * Implementation for converting astring to a {@link de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel}.
 */
public class SingleClassificationAttributeValueConverter extends AbstractClassificationAttributeValueConverter implements
        ClassificationAttributeValueConverter
{
    @Override
    public boolean isApplicable(final ClassificationTypeAttributeDescriptor attribute)
    {
        return isAttributeTypeEnum(attribute) && !attribute.isLocalized() && !attribute.isCollection();
    }

    @Override
    public Object convert(final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        if (value == null)
        {
            return null;
        }
        else if (value instanceof String)
        {
            final var enumValue = getAttributeValue(attribute, (String) value);
            addValueToClassificationAttributeAssignmentAttributeValues(attribute, enumValue);
            return enumValue;
        }
        throw new InvalidAttributeValueException(value, attribute);
    }
}
