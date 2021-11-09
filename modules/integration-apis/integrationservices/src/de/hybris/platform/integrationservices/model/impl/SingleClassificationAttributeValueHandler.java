/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueHandler;

public class SingleClassificationAttributeValueHandler extends AbstractClassificationAttributeValueHandler implements
        ClassificationAttributeValueHandler
{
    @Override
    public boolean isApplicable(final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        return !attribute.isCollection() && !attribute.isLocalized() && value != null;
    }

    @Override
    public void set(final ProductModel product, final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        final var features = getProductFeaturesForAttributeAssignment(product, attribute);
        if (features.isEmpty())
        {
            final var newFeature = createNewFeature(product, attribute, value, 0);
            addNewFeatureToProductFeatures(product, newFeature);
        }
        else
        {
            setValueSafelyOnExistingFeature(attribute, features.get(0), value);
        }
        addProductToClassificationClassIfNotPresent(product, attribute);
    }

    private void addNewFeatureToProductFeatures(final ProductModel product, final ProductFeatureModel newFeature)
    {
        final var productFeatures = getFeatures(product);
        productFeatures.add(newFeature);
        product.setFeatures(productFeatures);
    }
}
