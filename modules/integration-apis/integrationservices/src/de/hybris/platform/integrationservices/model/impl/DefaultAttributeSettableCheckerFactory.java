/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.AttributeSettableChecker;
import de.hybris.platform.integrationservices.model.AttributeSettableCheckerFactory;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import javax.validation.constraints.NotNull;

/**
 * Default implementation of {@link AttributeSettableCheckerFactory}.
 */
public class DefaultAttributeSettableCheckerFactory implements AttributeSettableCheckerFactory
{
	private static final AttributeSettableChecker DEFAULT_CHECKER = new NullAttributeSettableChecker();
	private AttributeSettableChecker standardAttributeSettableChecker;
	private AttributeSettableChecker classificationAttributeSettableChecker;

	@Override
	public AttributeSettableChecker create(@NotNull final TypeAttributeDescriptor descriptor)
	{
		return descriptor instanceof DefaultTypeAttributeDescriptor ?
				getStandardAttributeSettableChecker() :
				getClassificationAttributeSettableChecker();
	}

	public AttributeSettableChecker getStandardAttributeSettableChecker()
	{
		return standardAttributeSettableChecker != null ?
				standardAttributeSettableChecker :
				DEFAULT_CHECKER;
	}

	public AttributeSettableChecker getClassificationAttributeSettableChecker()
	{
		return classificationAttributeSettableChecker != null ?
				classificationAttributeSettableChecker :
				DEFAULT_CHECKER;
	}

	public void setStandardAttributeSettableChecker(
			final AttributeSettableChecker standardAttributeSettableChecker)
	{
		this.standardAttributeSettableChecker = standardAttributeSettableChecker;
	}

	public void setClassificationAttributeSettableChecker(
			final AttributeSettableChecker classificationAttributeSettableChecker)
	{
		this.classificationAttributeSettableChecker = classificationAttributeSettableChecker;
	}
}
