/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

/**
 * A factory to create {@link AttributeValueSetter}s
 */
public interface AttributeValueSetterFactory
{
    /**
     * Creates an {@link AttributeValueSetter} for the given {@link TypeAttributeDescriptor}
     * @param descriptor The TypeAttributeDescriptor to associate with this AttributeValueSetter
     * @return The corresponding AttributeValueSetters for the descriptor. If the condition isn't met to create the appropriate setter,
     * a default value setter is returned
     */
    AttributeValueSetter create(final TypeAttributeDescriptor descriptor);
}
