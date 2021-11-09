/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;

/**
 * A factory for creating {@link ProductFeatureModel}s.
 */
public interface ProductFeatureModelFactory
{
	/**
	 * Creates a feature for product with classification attribute.
	 *
	 * @param product                  product to create features for
	 * @param classAttributeAssignment classification attribute assignment for the feature
	 * @param value                    values to assign to the feature
	 * @param valuePosition            the position to place the feature in the product
	 * @return new product feature model
	 */
	ProductFeatureModel create(ProductModel product, ClassAttributeAssignmentModel classAttributeAssignment,
	                           Object value, int valuePosition);

	/**
	 * Creates a feature for product with classification attribute for the given language.
	 *
	 * @param product                  product to create features for
	 * @param classAttributeAssignment classification attribute assignment for the feature
	 * @param value                    values to assign to the feature
	 * @param valuePosition            the position to place the feature in the product
	 * @param language                 the language of the feature value
	 * @return new product feature model
	 */
	ProductFeatureModel create(ProductModel product, ClassAttributeAssignmentModel classAttributeAssignment,
	                           Object value, int valuePosition, LanguageModel language);
}
