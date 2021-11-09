/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.integrationservices.model.AttributeValueGetter;
import de.hybris.platform.integrationservices.model.AttributeValueSetter;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

/**
 * Provides access to classification attribute values in the platform
 * @deprecated Use {@link ClassificationAttributeValueGetter} instead
 */
@Deprecated(since = "1905.11-CEP", forRemoval = true)
public class ClassificationAttributeValueAccessor extends DelegatingAttributeValueAccessor
{
	private TypeAttributeDescriptor attribute;
	private ClassificationService classificationService;
	private ClassAttributeAssignmentModel classAttributeAssignmentModel;
	private boolean isLocalized;

	/**
	 * Constructs an instance of the ClassificationAttributeValueAccessor
	 *
	 * @param descriptor Attribute descriptor
	 * @param model      Model needed when retrieving the value using the ClassificationService
	 * @param service    Classification service for accessing features from the model
	 */
	public ClassificationAttributeValueAccessor(
			final TypeAttributeDescriptor descriptor,
			final ClassAttributeAssignmentModel model,
			final ClassificationService service)
	{
		this(new NullAttributeValueGetter(), new NullAttributeValueSetter());
		attribute = descriptor;
		classAttributeAssignmentModel = model;
		classificationService = service;
		isLocalized = Boolean.TRUE.equals(model.getLocalized());
	}

	private ClassificationAttributeValueAccessor(final AttributeValueGetter getter, final AttributeValueSetter setter)
	{
		super(getter, setter);
	}

	protected TypeAttributeDescriptor getAttribute()
	{
		return attribute;
	}

	protected ClassificationService getClassificationService()
	{
		return classificationService;
	}

	protected ClassAttributeAssignmentModel getClassAttributeAssignmentModel()
	{
		return classAttributeAssignmentModel;
	}

	protected boolean isLocalized()
	{
		return isLocalized;
	}
}
