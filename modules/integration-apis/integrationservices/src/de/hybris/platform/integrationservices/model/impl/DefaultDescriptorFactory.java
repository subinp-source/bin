/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.AbstractIntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.AttributeSettableCheckerFactory;
import de.hybris.platform.integrationservices.model.AttributeValueAccessorFactory;
import de.hybris.platform.integrationservices.model.AttributeValueGetterFactory;
import de.hybris.platform.integrationservices.model.AttributeValueSetterFactory;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

/**
 * Default implementation of the {@link DescriptorFactory}
 */
public class DefaultDescriptorFactory implements DescriptorFactory
{
	private static final AttributeValueAccessorFactory ATTRIBUTE_NULL_VALUE_ACCESSOR_FACTORY = new DefaultAttributeValueAccessorFactory();
	private static final AttributeValueGetterFactory ATTRIBUTE_NULL_VALUE_GETTER_FACTORY = new NullAttributeValueGetterFactory();
	private static final AttributeValueSetterFactory ATTRIBUTE_NULL_VALUE_SETTER_FACTORY = new NullAttributeValueSetterFactory();
	private static final AttributeSettableCheckerFactory NULL_ATTRIBUTE_SETTABLE_CHECKER_FACTORY = new NullAttributeSettableCheckerFactory();
	private AttributeValueAccessorFactory attributeValueAccessorFactory;
	private AttributeValueGetterFactory attributeValueGetterFactory;
	private AttributeValueSetterFactory attributeValueSetterFactory;
	private AttributeSettableCheckerFactory attributeSettableCheckerFactory;

	@Override
	public IntegrationObjectDescriptor createIntegrationObjectDescriptor(final IntegrationObjectModel model)
	{
		return new DefaultIntegrationObjectDescriptor(model, this);
	}

	@Override
	public TypeDescriptor createItemTypeDescriptor(final IntegrationObjectItemModel model)
	{
		return new ItemTypeDescriptor(model, this);
	}

	@Override
	public TypeAttributeDescriptor createTypeAttributeDescriptor(final AbstractIntegrationObjectItemAttributeModel model)
	{
		if (model instanceof IntegrationObjectItemClassificationAttributeModel)
		{
			return new ClassificationTypeAttributeDescriptor(
					(IntegrationObjectItemClassificationAttributeModel) model, this);
		}
		else if (model instanceof IntegrationObjectItemVirtualAttributeModel)
		{
			return new VirtualTypeAttributeDescriptor(
					(IntegrationObjectItemVirtualAttributeModel) model, this);
		}

		return new DefaultTypeAttributeDescriptor(
				(IntegrationObjectItemAttributeModel) model, this);
	}

	@Override
	public AttributeValueAccessorFactory getAttributeValueAccessorFactory()
	{
		return attributeValueAccessorFactory != null ?
				attributeValueAccessorFactory :
				ATTRIBUTE_NULL_VALUE_ACCESSOR_FACTORY;
	}

	public void setAttributeValueAccessorFactory(final AttributeValueAccessorFactory attributeValueAccessorFactory)
	{
		this.attributeValueAccessorFactory = attributeValueAccessorFactory;
	}

	@Override
	public AttributeValueGetterFactory getAttributeValueGetterFactory()
	{
		return attributeValueGetterFactory != null ?
				attributeValueGetterFactory :
				ATTRIBUTE_NULL_VALUE_GETTER_FACTORY;
	}

	public void setAttributeValueGetterFactory(final AttributeValueGetterFactory attributeValueGetterFactory)
	{
		this.attributeValueGetterFactory = attributeValueGetterFactory;
	}

	@Override
	public AttributeValueSetterFactory getAttributeValueSetterFactory()
	{
		return attributeValueSetterFactory != null ?
				attributeValueSetterFactory :
				ATTRIBUTE_NULL_VALUE_SETTER_FACTORY;
	}

	public void setAttributeValueSetterFactory(final AttributeValueSetterFactory attributeValueSetterFactory)
	{
		this.attributeValueSetterFactory = attributeValueSetterFactory;
	}

	@Override
	public AttributeSettableCheckerFactory getAttributeSettableCheckerFactory()
	{
		return attributeSettableCheckerFactory != null ?
				attributeSettableCheckerFactory :
				NULL_ATTRIBUTE_SETTABLE_CHECKER_FACTORY;
	}

	public void setAttributeSettableCheckerFactory(final AttributeSettableCheckerFactory attributeSettableCheckerFactory)
	{
		this.attributeSettableCheckerFactory = attributeSettableCheckerFactory;
	}
}
