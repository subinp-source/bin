/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.integrationservices.model.AttributeValueAccessor;
import de.hybris.platform.integrationservices.model.AttributeValueAccessorFactory;
import de.hybris.platform.integrationservices.model.AttributeValueGetter;
import de.hybris.platform.integrationservices.model.AttributeValueGetterFactory;
import de.hybris.platform.integrationservices.model.AttributeValueSetter;
import de.hybris.platform.integrationservices.model.AttributeValueSetterFactory;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * Default implementation of the {@code AttributeValueAccessorFactory}. It assembles the accessor by combining  a getter and a
 * setter produced by the corresponding factories.
 */
public class DefaultAttributeValueAccessorFactory implements AttributeValueAccessorFactory
{
	private static final NullAttributeValueGetterFactory DEFAULT_GETTER_FACTORY = new NullAttributeValueGetterFactory();
	private static final NullAttributeValueSetterFactory DEFAULT_SETTER_FACTORY = new NullAttributeValueSetterFactory();

	private ModelService modelService;
	private ClassificationService classificationService;
	private AttributeValueGetterFactory getterFactory = DEFAULT_GETTER_FACTORY;
	private AttributeValueSetterFactory setterFactory = DEFAULT_SETTER_FACTORY;

    @Override
    public AttributeValueAccessor create(final TypeAttributeDescriptor descriptor)
    {
    	final AttributeValueGetter getter = getterFactory.create(descriptor);
    	final AttributeValueSetter setter = setterFactory.create(descriptor);
        return new DelegatingAttributeValueAccessor(getter, setter);
    }

    @Override
    public AttributeValueAccessor create(final TypeAttributeDescriptor descriptor, final ClassAttributeAssignmentModel model)
    {
	    return create(descriptor);
    }

	/**
	 * @deprecated not used anymore
	 */
	@Deprecated(since = "1905.11-CEP", forRemoval = true)
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @deprecated not used anymore
	 */
	@Deprecated(since = "1905.11-CEP", forRemoval = true)
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @deprecated not used anymore
	 */
	@Deprecated(since = "1905.11-CEP", forRemoval = true)
	protected ClassificationService getClassificationService()
	{
		return classificationService;
	}

	/**
	 * @deprecated not used anymore
	 */
	@Deprecated(since = "1905.11-CEP", forRemoval = true)
	public void setClassificationService(final ClassificationService classificationService)
	{
		this.classificationService = classificationService;
	}

	public void setGetterFactory(final AttributeValueGetterFactory factory)
	{
		getterFactory = factory != null ? factory : DEFAULT_GETTER_FACTORY;
	}

	public void setSetterFactory(final AttributeValueSetterFactory factory)
	{
		setterFactory = factory != null ? factory : DEFAULT_SETTER_FACTORY;
	}
}
