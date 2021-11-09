/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.AttributeValueSetter;
import de.hybris.platform.integrationservices.model.AttributeValueSetterFactory;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

/**
 * An {@link AttributeValueSetterFactory} that creates an {@link NullAttributeValueSetter} by default
 */
public final class NullAttributeValueSetterFactory implements AttributeValueSetterFactory
{
	private static final AttributeValueSetter NULL_ATTRIBUTE_VALUE_SETTER = new NullAttributeValueSetter();

	@Override
	public AttributeValueSetter create(final TypeAttributeDescriptor descriptor)
	{
		return NULL_ATTRIBUTE_VALUE_SETTER;
	}
}
