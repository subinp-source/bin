/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.integrationservices.model.ProductFeatureModelFactory;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;

abstract class AbstractClassificationAttributeValueHandler
{
    private ModelService modelService;
    private ProductFeatureModelFactory featureFactory;

    List<ProductFeatureModel> getFeatures(final ProductModel product)
    {
        return product.getFeatures() != null ?
                Lists.newArrayList(product.getFeatures()) :
                Lists.newArrayList();
    }

    void removeFeatures(final ProductModel product,
                        final List<ProductFeatureModel> featuresToRemove)
    {
        if (!featuresToRemove.isEmpty())
        {
            modelService.removeAll(featuresToRemove);
            final List<ProductFeatureModel> features = getFeatures(product);
            features.removeAll(featuresToRemove);
            product.setFeatures(features);
        }
    }

    List<ProductFeatureModel> getProductFeaturesForAttributeAssignment(final ProductModel product,
                                                                       final ClassificationTypeAttributeDescriptor attribute)
    {
        return Stream.ofNullable(product.getFeatures())
                     .flatMap(Collection::stream)
                     .filter(f -> getClassAttributeAssignmentModel(attribute).equals(
                             f.getClassificationAttributeAssignment()))
                     .collect(Collectors.toList());
    }

    void addProductToClassificationClassIfNotPresent(final ProductModel product,
                                                     final ClassificationTypeAttributeDescriptor attribute)
    {
        final List<ProductModel> existingClassProducts = getClassAttributeAssignmentModel(attribute).getClassificationClass()
                                                                                                    .getProducts();
        final List<ProductModel> products = existingClassProducts != null ? Lists.newArrayList(
                existingClassProducts) : Lists.newArrayList();
        if (!products.contains(product))
        {
            addProductToClassifyingClass(product, products, attribute);
        }
    }

    private void addProductToClassifyingClass(final ProductModel product, final List<ProductModel> existingClassProducts,
                                              final ClassificationTypeAttributeDescriptor attribute)
    {
        existingClassProducts.add(product);
        getClassAttributeAssignmentModel(attribute).getClassificationClass().setProducts(existingClassProducts);
    }

    ProductFeatureModel createNewFeature(final ProductModel product,
                                         final ClassificationTypeAttributeDescriptor attribute,
                                         final Object value,
                                         final int valuePosition)
    {
        saveIfNotPrimitiveAndNew(attribute, value);
        return getFeatureFactory().create(product, getClassAttributeAssignmentModel(attribute), value, valuePosition);
    }

    void setValueSafelyOnExistingFeature(final ClassificationTypeAttributeDescriptor attribute, final ProductFeatureModel feature,
                                         final Object value)
    {
        saveIfNotPrimitiveAndNew(attribute, value);
        feature.setValue(value);
    }

    private void saveIfNotPrimitiveAndNew(final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        // ItemModel PK is needed in order to set this value on the productFeature
        if (!attribute.isPrimitive() && getModelService().isNew(value))
        {
            getModelService().save(value);
        }
    }

    ClassAttributeAssignmentModel getClassAttributeAssignmentModel(final ClassificationTypeAttributeDescriptor attribute)
    {
        return attribute.getClassAttributeAssignment();
    }

    ProductFeatureModelFactory getFeatureFactory()
    {
        return featureFactory;
    }

    ModelService getModelService()
    {
        return modelService;
    }

    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }

    @Required
    public void setFeatureFactory(final ProductFeatureModelFactory featureFactory)
    {
        this.featureFactory = featureFactory;
    }
}
