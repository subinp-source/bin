/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.integrationservices.model.AttributeValueSetter;
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueConverter;
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueHandler;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

/**
 * Sets the value to classification attribute values in the platform
 */
class ClassificationAttributeValueSetter implements AttributeValueSetter
{
    private final ClassificationTypeAttributeDescriptor attribute;
    private final List<ClassificationAttributeValueHandler> valueHandlers;
    private final List<ClassificationAttributeValueConverter> valueConverters;

    /**
     * Constructs an instance of the ClassificationAttributeValueSetter
     *
     * @param descriptor Attribute descriptor
     * @param converters Value converters that may convert the given value
     * @param handlers   Value handler to set the value on the attribute
     */
    ClassificationAttributeValueSetter(
            @NotNull final ClassificationTypeAttributeDescriptor descriptor,
            @NotNull final List<ClassificationAttributeValueConverter> converters,
            @NotNull final List<ClassificationAttributeValueHandler> handlers)
    {
        Preconditions.checkArgument(descriptor != null, "Attribute descriptor must be provided");
        Preconditions.checkArgument(converters != null, "Value converters cannot be null");
        Preconditions.checkArgument(handlers != null, "Value handlers cannot be null");

        attribute = descriptor;
        valueConverters = converters;
        valueHandlers = handlers;
    }

    @Override
    public void setValue(final Object model, final Object value)
    {
        if (isProduct(model))
        {
            final ProductModel product = (ProductModel) model;
            final Object convertedValue = valueConverters.stream()
                                                         .filter(converter -> converter.isApplicable(attribute))
                                                         .findFirst()
                                                         .map(converter -> converter.convert(attribute, value))
                                                         .orElse(value);

            valueHandlers.stream()
                         .filter(handler -> handler.isApplicable(attribute, convertedValue))
                         .findFirst()
                         .ifPresent(handler -> handler.set(product, attribute, convertedValue));
        }
    }

    private boolean isProduct(final Object model)
    {
        return model instanceof ProductModel;
    }
}
