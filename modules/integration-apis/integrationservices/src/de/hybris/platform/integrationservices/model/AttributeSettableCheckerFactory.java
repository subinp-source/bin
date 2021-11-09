/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import javax.validation.constraints.NotNull;

/**
 * A factory for creating {@link AttributeSettableChecker}s.
 */
public interface AttributeSettableCheckerFactory
{
	/**
	 * Create an attribute settable checker for the given attribute descriptor.
	 *
	 * @param descriptor the descriptor to create a checker for
	 * @return the created attribute settable checker
	 */
	AttributeSettableChecker create(@NotNull TypeAttributeDescriptor descriptor);
}
