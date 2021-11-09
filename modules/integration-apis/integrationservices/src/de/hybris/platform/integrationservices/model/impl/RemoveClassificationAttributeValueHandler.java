/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueHandler;

public class RemoveClassificationAttributeValueHandler extends AbstractClassificationAttributeValueHandler implements
        ClassificationAttributeValueHandler
{
    @Override
    public boolean isApplicable(final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        return value == null;
    }

    @Override
    public void set(final ProductModel product, final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        final var features = getProductFeaturesForAttributeAssignment(product, attribute);
        removeFeatures(product, features);
    }
}
