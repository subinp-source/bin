/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import javax.validation.constraints.NotNull;

/**
 * A factory to create {@link AttributeValueGetter}s
 */
public interface AttributeValueGetterFactory
{
    /**
     * Creates an {@link AttributeValueGetter} for the given {@link TypeAttributeDescriptor}
     * @param descriptor The TypeAttributeDescriptor to associate with this AttributeValueGetter
     * @return The corresponding AttributeValueGetter for the descriptor. If the condition isn't met to create the appropriate getter,
     * a default value getter is returned.
     */
    AttributeValueGetter create(@NotNull TypeAttributeDescriptor descriptor);
}
