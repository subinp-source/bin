/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueHandler;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;

public class LocalizedClassificationAttributeValueHandler extends AbstractClassificationAttributeValueHandler
        implements ClassificationAttributeValueHandler
{
    private CommonI18NService i18NService;

    @Override
    public boolean isApplicable(final ClassificationTypeAttributeDescriptor attribute, final Object value)
    {
        return attribute.isLocalized() && attribute.isPrimitive() && !attribute.isCollection();
    }

    @Override
    public void set(final ProductModel product,
                    final ClassificationTypeAttributeDescriptor attribute,
                    final Object values)
    {
        if (values instanceof Map)
        {
            final Map<Locale, ?> newValues = ((Map<?, ?>) values).entrySet().stream()
                                                                 .filter(e -> e.getKey() instanceof Locale)
                                                                 .collect(Collectors.toMap(
                                                                         e -> (Locale) e.getKey(),
                                                                         Map.Entry::getValue));

            final List<ProductFeatureModel> oldFeatures = getProductFeaturesForAttributeAssignment(product, attribute);
            final Map<LanguageModel, Object> existingValues = convertToMap(oldFeatures);
            newValues.forEach((locale, value) -> existingValues.put(getLanguage(locale), value));
            removeFeatures(product, oldFeatures);
            createLocalizedFeatures(product, attribute, existingValues);

            addProductToClassificationClassIfNotPresent(product, attribute);
        }
    }

    private Map<LanguageModel, Object> convertToMap(final List<ProductFeatureModel> existingFeatures)
    {
        final Map<LanguageModel, Object> localizedValues = new HashMap<>();
        existingFeatures.forEach(f -> localizedValues.put(f.getLanguage(), f.getValue()));
        return localizedValues;
    }

    private void createLocalizedFeatures(final ProductModel product, final ClassificationTypeAttributeDescriptor attribute,
                                         final Map<LanguageModel, ?> localizedValues)
    {
        final AtomicInteger valPosition = new AtomicInteger(0);
        final List<ProductFeatureModel> newFeatures = new ArrayList<>(localizedValues.size());

        localizedValues.entrySet()
                       .stream()
                       .filter(entry -> Objects.nonNull(entry.getValue()))
                       .forEach(entry -> newFeatures.add(
                               getFeatureFactory().create(product, getClassAttributeAssignmentModel(attribute), entry.getValue(),
                                       valPosition.getAndIncrement(), entry.getKey())));

        final List<ProductFeatureModel> features = getFeatures(product);
        features.addAll(newFeatures);
        product.setFeatures(features);
    }

    private LanguageModel getLanguage(final Locale locale)
    {
        final var languageTag = locale.toLanguageTag().replace("-", "_");
        return i18NService.getLanguage(languageTag);
    }

    @Required
    public void setI18NService(final CommonI18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
