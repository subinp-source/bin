/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueHandler;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CollectionClassificationAttributeValueHandler extends AbstractClassificationAttributeValueHandler
        implements ClassificationAttributeValueHandler
{
    @Override
    public boolean isApplicable(final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        return attribute.isCollection() && !attribute.isLocalized();
    }

    @Override
    public void set(final ProductModel product, final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        if (value instanceof Collection)
        {
            updateCollectionValues(product, attribute, (Collection<?>) value);
            addProductToClassificationClassIfNotPresent(product, attribute);
        }
    }

    private void updateCollectionValues(final ProductModel product, final ClassificationTypeAttributeDescriptor attribute,
                                        final Collection<?> values)
    {
        final List<ProductFeatureModel> features = getProductFeaturesForAttributeAssignment(product, attribute);
        removeFeatures(product, features);
        createNewFeatures(product, attribute, values);
    }

    private void createNewFeatures(final ProductModel product, final ClassificationTypeAttributeDescriptor attribute,
                                   final Collection<?> values)
    {
        final AtomicInteger valuePosition = new AtomicInteger(0);
        final List<ProductFeatureModel> features = getFeatures(product);
        values.forEach(value ->
                features.add(createNewFeature(product, attribute, value, valuePosition.getAndIncrement())));
        product.setFeatures(features);
    }
}
