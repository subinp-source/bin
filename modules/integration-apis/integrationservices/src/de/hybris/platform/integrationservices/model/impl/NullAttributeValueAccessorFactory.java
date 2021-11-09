/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.integrationservices.model.AttributeValueAccessor;
import de.hybris.platform.integrationservices.model.AttributeValueAccessorFactory;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import javax.validation.constraints.NotNull;

/**
 * An {@link AttributeValueAccessorFactory} that creates an accessor with {@link NullAttributeValueGetter} and
 * {@link NullAttributeValueSetter} by default.
 * @deprecated Use {@link NullAttributeValueGetterFactory} instead
 */
@Deprecated(since = "1905.11-CEP", forRemoval = true)
public class NullAttributeValueAccessorFactory implements AttributeValueAccessorFactory
{
    private static final AttributeValueAccessor NULL_ATTRIBUTE_VALUE_ACCESSOR =
		    new DelegatingAttributeValueAccessor(new NullAttributeValueGetter(), new NullAttributeValueSetter());

    @Override
    public AttributeValueAccessor create(@NotNull final TypeAttributeDescriptor descriptor)
    {
        return NULL_ATTRIBUTE_VALUE_ACCESSOR;
    }

    @Override
    public AttributeValueAccessor create(@NotNull final TypeAttributeDescriptor descriptor, @NotNull final ClassAttributeAssignmentModel classAttributeAssignmentModel)
    {
        return NULL_ATTRIBUTE_VALUE_ACCESSOR;
    }
}
