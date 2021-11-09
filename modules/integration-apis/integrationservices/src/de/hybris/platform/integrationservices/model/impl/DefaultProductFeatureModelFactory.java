/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.catalog.ClassificationUtils;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.integrationservices.model.ProductFeatureModelFactory;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;

/**
 * Default implementation of {@link ProductFeatureModelFactory}.
 */
public class DefaultProductFeatureModelFactory implements ProductFeatureModelFactory
{
	private ModelService modelService;

	@Override
	public ProductFeatureModel create(final ProductModel product, final ClassAttributeAssignmentModel classAttributeAssignment,
	                                  final Object value, final int valuePosition)
	{
		Preconditions.checkArgument(product != null, "ProductModel cannot be null");
		Preconditions.checkArgument(classAttributeAssignment != null, "ClassAttributeAssignmentModel cannot be null");
		Preconditions.checkArgument(value != null, "value cannot be null");
		Preconditions.checkArgument(valuePosition >= 0,
				"valuePosition must be 0 or a positive integer value");

		final ProductFeatureModel feature = modelService.create(ProductFeatureModel.class);
		feature.setProduct(product);
		feature.setClassificationAttributeAssignment(classAttributeAssignment);
		feature.setQualifier(getFeatureQualifier(classAttributeAssignment));
		feature.setValuePosition(valuePosition);
		feature.setUnit(classAttributeAssignment.getUnit());
		feature.setValue(value);
		return feature;
	}

	@Override
	public ProductFeatureModel create(final ProductModel product, final ClassAttributeAssignmentModel classAttributeAssignment,
	                                  final Object value, final int valuePosition, final LanguageModel language)
	{
		final ProductFeatureModel feature = create(product, classAttributeAssignment, value, valuePosition);
		feature.setLanguage(language);
		return feature;
	}

	private String getFeatureQualifier(final ClassAttributeAssignmentModel classAttributeAssignment)
	{
		return ClassificationUtils.createFeatureQualifier(classAttributeAssignment);
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
