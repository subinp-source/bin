/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.AttributeValueGetter;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.servicelayer.model.ModelService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import com.google.common.base.Preconditions;

/**
 * Gets the standard platform attributes, which are modeled as {@code attribute} element in items.xml.
 */
class StandardAttributeValueGetter implements AttributeValueGetter
{
	private final TypeAttributeDescriptor attributeDescriptor;
	private final ModelService modelService;

	/**
	 * Instantiates this attribute value getter
	 *
	 * @param attribute attribute whose values should be accessed
	 * @param service   model service for reading attribute values.
	 */
	StandardAttributeValueGetter(final TypeAttributeDescriptor attribute, final ModelService service)
	{
		Preconditions.checkArgument(attribute != null, "Type attribute descriptor is required and cannot be null");
		Preconditions.checkArgument(service != null, "Model service is required and cannot be null");
		attributeDescriptor = attribute;
		modelService = service;
	}

	@Override
	public Object getValue(final Object model)
	{
		return readableAttributeIsPresent(model)
				? modelService.getAttributeValue(model, attributeDescriptor.getQualifier())
				: null;
	}

	@Override
	public Object getValue(final Object model, final Locale locale)
	{
		return readableAttributeIsPresent(model)
				? modelService.getAttributeValue(model, attributeDescriptor.getQualifier(), locale)
				: null;
	}

	@Override
	public Map<Locale, Object> getValues(final Object model, final Locale... locales)
	{
		return readableAttributeIsPresent(model)
				? modelService.getAttributeValues(model, attributeDescriptor.getQualifier(), locales)
				: Collections.emptyMap();
	}

	private boolean readableAttributeIsPresent(final Object model)
	{
		return model != null && attributeDescriptor.isReadable() && hasAttributeDefinedOnModel(model);
	}

	private boolean hasAttributeDefinedOnModel(final Object model)
	{
		return attributeDescriptor.getTypeDescriptor().isInstance(model) || hasMatchingField(model);
	}

	private boolean hasMatchingField(final Object value)
	{
		return Arrays.stream(value.getClass().getFields())
		             .map(Field::getName)
		             .anyMatch(name -> name.equalsIgnoreCase(attributeDescriptor.getQualifier()));
	}
}
