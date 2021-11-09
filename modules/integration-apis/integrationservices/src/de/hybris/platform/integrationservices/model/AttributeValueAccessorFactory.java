/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

import javax.validation.constraints.NotNull;

/**
 * A factory to create {@link AttributeValueAccessor}s
 */
public interface AttributeValueAccessorFactory
{
    /**
     * Creates an {@link AttributeValueAccessor} for the given {@link TypeAttributeDescriptor}
     * @param descriptor The TypeAttributeDescriptor to associate with this AttributeValueAccessor
     * @return The corresponding AttributeValueAccessor for the descriptor. If the condition isn't met to create the appropriate accessor,
     * a default value accessor is returned.
     */
    AttributeValueAccessor create(@NotNull TypeAttributeDescriptor descriptor);

    /**
     * Creates an {@link AttributeValueAccessor} for the given {@link TypeAttributeDescriptor} related to Classifications
     * @param descriptor The TypeAttributeDescriptor to associate with this AttributeValueAccessor
     * @param classAttributeAssignmentModel The model provides details into retrieving the value of a classification attribute
     * @return The corresponding AttributeValueAccessor for the descriptor. If the condition isn't met to create the appropriate accessor,
     * a default value accessor is returned
     * @deprecated use {@link #create(TypeAttributeDescriptor)}
     */
    @Deprecated(since = "1905.11-CEP", forRemoval = true)
    AttributeValueAccessor create(@NotNull TypeAttributeDescriptor descriptor, @NotNull ClassAttributeAssignmentModel classAttributeAssignmentModel);
}
