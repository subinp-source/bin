/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.integrationservices.model.AttributeValueGetter;
import de.hybris.platform.integrationservices.model.AttributeValueGetterFactory;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutorFactory;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of the {@link AttributeValueGetterFactory}
 */
public class DefaultAttributeValueGetterFactory implements AttributeValueGetterFactory
{
	private static final AttributeValueGetter DEFAULT_GETTER = new NullAttributeValueGetter();
	private ModelService modelService;
	private ClassificationService classificationService;
	private LogicExecutorFactory logicExecutorFactory;

	@Override
	public AttributeValueGetter create(final TypeAttributeDescriptor descriptor)
	{
		if (descriptor instanceof ClassificationTypeAttributeDescriptor)
		{
			return new ClassificationAttributeValueGetter(
					(ClassificationTypeAttributeDescriptor) descriptor, getClassificationService(), getModelService());
		}
		if (descriptor instanceof VirtualTypeAttributeDescriptor)
		{
			return new VirtualAttributeValueGetter(
					(VirtualTypeAttributeDescriptor) descriptor, getLogicExecutorFactory());
		}
		if (descriptor != null)
		{
			return new StandardAttributeValueGetter(descriptor, getModelService());
		}
		return DEFAULT_GETTER;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected ClassificationService getClassificationService()
	{
		return classificationService;
	}

	@Required
	public void setClassificationService(final ClassificationService classificationService)
	{
		this.classificationService = classificationService;
	}

	protected LogicExecutorFactory getLogicExecutorFactory()
	{
		return logicExecutorFactory;
	}

	@Required
	public void setLogicExecutorFactory(final LogicExecutorFactory logicExecutorFactory)
	{
		this.logicExecutorFactory = logicExecutorFactory;
	}
}
