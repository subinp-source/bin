/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.integrationservices.model.AttributeValueGetter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Preconditions;

/**
 * Gets the classification attribute values in the platform
 */
class ClassificationAttributeValueGetter implements AttributeValueGetter
{
	private final ClassificationTypeAttributeDescriptor attribute;
	private final ClassificationService classificationService;
	private final ModelService modelService;
	private final boolean isLocalized;

	/**
	 * Instantiates this getter.
	 *
	 * @param descriptor   classification attribute descriptor
	 * @param classService a classification service implementation
	 * @param modelService a model service implementation
	 */
	ClassificationAttributeValueGetter(final ClassificationTypeAttributeDescriptor descriptor,
	                                   final ClassificationService classService,
	                                   final ModelService modelService)
	{
		Preconditions.checkArgument(descriptor != null, "Attribute descriptor must be provided");
		Preconditions.checkArgument(classService != null, "Classification service must be provided");
		Preconditions.checkArgument(modelService != null, "Model service must be provided");
		attribute = descriptor;
		classificationService = classService;
		this.modelService = modelService;
		isLocalized = Boolean.TRUE.equals(getClassAttributeAssignment().getLocalized());
	}

	@Override
	public Object getValue(final Object model)
	{
		if (isAnExistingProduct(model))
		{
			final Feature feature = classificationService.getFeature((ProductModel) model, getClassAttributeAssignment());
			final Object value = getValueFromFeature(feature);
			return extractValue(value);
		}
		return null;
	}

	@Override
	public Object getValue(final Object model, final Locale locale)
	{
		if (isAnExistingProduct(model) && isLocalized())
		{
			final Feature feature = classificationService.getFeature((ProductModel) model, getClassAttributeAssignment());
			if (feature instanceof LocalizedFeature)
			{
				final LocalizedFeature localizedFeature = (LocalizedFeature) feature;
				return extractValue(getLocalizedValue(localizedFeature, locale));
			}
		}
		return null;
	}

	@Override
	public Map<Locale, Object> getValues(final Object model, final Locale... locales)
	{
		if (isAnExistingProduct(model) && isLocalized())
		{
			final Feature feature = classificationService.getFeature((ProductModel) model, getClassAttributeAssignment());
			if (feature instanceof LocalizedFeature)
			{
				return getValuesForRequestedLocales((LocalizedFeature) feature, locales);
			}
		}
		return new HashMap<>();
	}

	private boolean isAnExistingProduct(final Object model)
	{
		return !modelService.isNew(model) && isProduct(model);
	}

	private Object extractFromClassificationAttrValue(final Object value)
	{
		if (value instanceof ClassificationAttributeValueModel)
		{
			return ((ClassificationAttributeValueModel) value).getCode();
		}
		else if (value instanceof List)
		{
			return ((List<?>) value).stream()
			                        .filter(i -> i instanceof ClassificationAttributeValueModel)
			                        .map(ClassificationAttributeValueModel.class::cast)
			                        .map(ClassificationAttributeValueModel::getCode)
			                        .collect(Collectors.toList());
		}
		return null;
	}

	private boolean isAttributeTypeEnum()
	{
		return ClassificationAttributeTypeEnum.ENUM.equals(getClassAttributeAssignment().getAttributeType());
	}

	private Map<Locale, Object> getValuesForRequestedLocales(final LocalizedFeature localizedFeature, final Locale[] locales)
	{
		final Map<Locale, List<FeatureValue>> valuesForAllLocales = localizedFeature.getValuesForAllLocales();
		final Map<Locale, Object> valuesForRequestedLocales = new HashMap<>();
		if (valuesForAllLocales != null)
		{
			Stream.of(locales)
			      .filter(locale -> !CollectionUtils.isEmpty(valuesForAllLocales.get(locale)))
			      .forEach(locale -> {
						final var featureValues = valuesForAllLocales.get(locale);
						final var value = extractValueFromFeatureValues(featureValues);
						valuesForRequestedLocales.put(locale, value);
			      });
		}
		return valuesForRequestedLocales;
	}

	private Object getValueFromFeature(final Feature feature)
	{
		if (feature != null && attribute.isCollection())
		{
			return getMultipleValues(feature);
		}
		return feature != null && feature.getValue() != null
				? feature.getValue().getValue()
				: null;
	}

	private Object getMultipleValues(final Feature feature)
	{
		return extractValues(feature.getValues());
	}

	private Object getMultipleValues(final LocalizedFeature feature, final Locale locale)
	{
 		return extractValues(feature.getValues(locale));
	}

	private Object extractValueFromFeatureValues(final List<FeatureValue> featureValues)
	{
		if(attribute.isCollection())
		{
			return extractValues(featureValues);
		}
		return !featureValues.isEmpty() && featureValues.get(0) != null
				? extractValue(featureValues.get(0).getValue()) : null;
	}

	private Object extractValues(final Collection<FeatureValue> featureValues)
	{
		return featureValues.stream()
		              .filter(Objects::nonNull)
		              .map(FeatureValue::getValue)
		              .collect(Collectors.toList());
	}

	private Object extractValue(final Object featureValue)
	{
		return isAttributeTypeEnum() ?
				extractFromClassificationAttrValue(featureValue) :
				featureValue;
	}

	private Object getLocalizedValue(final LocalizedFeature feature, final Locale locale)
	{
		return attribute.isCollection() ?
				getMultipleValues(feature, locale) :
				getSingleValue(feature, locale);
	}

	private Object getSingleValue(final LocalizedFeature feature, final Locale locale) {
		return feature != null && feature.getValue(locale) != null
				? feature.getValue(locale).getValue()
				: null;
	}

	private boolean isProduct(final Object model)
	{
		return model instanceof ProductModel;
	}

	private ClassAttributeAssignmentModel getClassAttributeAssignment()
	{
		return attribute.getClassAttributeAssignment();
	}

	boolean isLocalized()
	{
		return isLocalized;
	}
}
