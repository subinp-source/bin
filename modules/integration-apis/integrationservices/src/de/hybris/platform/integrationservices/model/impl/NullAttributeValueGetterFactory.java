/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.AttributeValueGetter;
import de.hybris.platform.integrationservices.model.AttributeValueGetterFactory;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import javax.validation.constraints.NotNull;

/**
 * An {@link AttributeValueGetterFactory} that creates an {@link NullAttributeValueGetter} by default
 */
public final class NullAttributeValueGetterFactory implements AttributeValueGetterFactory
{
	private static final AttributeValueGetter NULL_ATTRIBUTE_VALUE_GETTER = new NullAttributeValueGetter();

	@Override
	public AttributeValueGetter create(@NotNull final TypeAttributeDescriptor descriptor)
	{
		return NULL_ATTRIBUTE_VALUE_GETTER;
	}
}
