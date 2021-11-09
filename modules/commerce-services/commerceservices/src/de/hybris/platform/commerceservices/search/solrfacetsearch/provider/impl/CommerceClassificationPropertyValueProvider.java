/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.util.Feature;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.impl.ClassificationPropertyValueProvider;

import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.Logger;


/**
 * This ValueProvider will provide the value for a classification attribute on a product.
 */
@SuppressWarnings("deprecation")
public class CommerceClassificationPropertyValueProvider extends ClassificationPropertyValueProvider
{
	final static Logger LOG = Logger.getLogger(CommerceClassificationPropertyValueProvider.class);

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			if (indexedProperty.getClassAttributeAssignment() == null)
			{
				LOG.error("Failed to resolve values for product with code: " + ((ProductModel) model).getCode()
						+ ", by provider: CommerceClassificationPropertyValueProvider, for property: " + indexedProperty.getName()
						+ ", reason: Class Attribute Assignment is empty.");
				return Collections.emptyList();
			}
			final ClassAttributeAssignmentModel classAttributeAssignmentModel = indexedProperty.getClassAttributeAssignment();
			final ClassAttributeAssignment classAttributeAssignment = modelService.getSource(classAttributeAssignmentModel);
			final Product product = (Product) modelService.getSource(model);
			final FeatureContainer cont = FeatureContainer.loadTyped(product, classAttributeAssignment);
			if (cont.hasFeature(classAttributeAssignment))
			{
				final Feature feature = cont.getFeature(classAttributeAssignment);
				if (feature == null || feature.isEmpty())
				{
					return Collections.emptyList();
				}
				else
				{
					return getFeaturesValues(indexConfig, feature, indexedProperty);
				}
			}
			else
			{
				return Collections.emptyList();
			}
		}
		else
		{
			throw new FieldValueProviderException("Cannot provide classification property of non-product item");
		}
	}
}
