/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.product.dynamic;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.classification.ClassificationClassesResolverStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Dynamic Attribute Handler for attribute 'classificationClasses' from Product
 */
public class ProductClassificationClassesAttributeHandler
		implements DynamicAttributeHandler<List<ClassificationClassModel>, ProductModel>
{
	private ClassificationClassesResolverStrategy classificationClassesResolverStrategy;

	@Override
	public List<ClassificationClassModel> get(final ProductModel product)
	{
		final Set<ClassificationClassModel> resolvedClasificationClasses = classificationClassesResolverStrategy.resolve(product);

		return resolvedClasificationClasses == null ? Collections.EMPTY_LIST
				: resolvedClasificationClasses.stream().collect(Collectors.toList());
	}

	@Override
	public void set(final ProductModel product, final List<ClassificationClassModel> classificationClassModels)
	{
		throw new UnsupportedOperationException();
	}

	@Required
	public void setClassificationClassesResolverStrategy(
			final ClassificationClassesResolverStrategy classificationClassesResolverStrategy)
	{
		this.classificationClassesResolverStrategy = classificationClassesResolverStrategy;
	}
}
