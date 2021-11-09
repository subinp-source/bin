/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.integrationservices.model.impl.ClassificationTypeAttributeDescriptor;

/**
 * Sets the given value on product if handler is applicable.
 */
public interface ClassificationAttributeValueHandler
{
    /**
     * Determines if this converter is applicable for the given attribute and value
     *
     * @param attribute the attribute checked for eligibility of handler
     * @param value     the value checked for eligibility of handler
     * @return returns true if handler is applicable, false otherwise
     */
    boolean isApplicable(ClassificationTypeAttributeDescriptor attribute, Object value);

    /**
     * Sets the given value for attribute on the product.
     *
     * @param product   the product for which attribute is going to be set on
     * @param attribute the attribute to set value on
     * @param value     the value to be set
     */
    void set(ProductModel product,
             final ClassificationTypeAttributeDescriptor attribute,
             Object value);
}
