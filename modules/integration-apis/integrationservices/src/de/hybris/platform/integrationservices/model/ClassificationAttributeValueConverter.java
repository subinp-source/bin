/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.integrationservices.model.impl.ClassificationTypeAttributeDescriptor;

/**
 * Converts value to {@link ClassificationAttributeValueModel} if converter is applicable.
 */
public interface ClassificationAttributeValueConverter
{
    /**
     * Determines whether this converter is applicable to the given attribute.
     *
     * @param attribute the attribute for which value is to be converted
     * @return returns true if converter is applicable to given attribute, false otherwise
     */
    boolean isApplicable(ClassificationTypeAttributeDescriptor attribute);

    /**
     * Converts given value to {@link ClassificationAttributeValueModel}
     *
     * @param attribute the metadata for the attribute
     * @param value     value to be converted to a {@link ClassificationAttributeValueModel}
     * @return {@link ClassificationAttributeValueModel} matching to given value
     */
    Object convert(ClassificationTypeAttributeDescriptor attribute, Object value);
}
