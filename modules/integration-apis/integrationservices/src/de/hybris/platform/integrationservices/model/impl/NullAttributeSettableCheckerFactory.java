/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.AttributeSettableChecker;
import de.hybris.platform.integrationservices.model.AttributeSettableCheckerFactory;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import javax.validation.constraints.NotNull;

/**
 * A {@link AttributeSettableCheckerFactory} that creates an {@link NullAttributeSettableChecker} by default
 */
public class NullAttributeSettableCheckerFactory implements AttributeSettableCheckerFactory
{
	private static final AttributeSettableChecker NULL_ATTRIBUTE_SETTABLE_CHECKER = new NullAttributeSettableChecker();

	@Override
	public AttributeSettableChecker create(@NotNull final TypeAttributeDescriptor descriptor)
	{
		return NULL_ATTRIBUTE_SETTABLE_CHECKER;
	}
}
