/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.integrationservices.populator;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;


/**
 * Populate the atomic type of item model's attribute to Map.
 */
public class DefaultAtomicType2MapPopulator extends AbstractItemToMapPopulator
{
	private Converter<Object, Object> converter = new AtomicTypeValueConverter();

	@Override
	protected void populateToMap(
			final TypeAttributeDescriptor attribute,
			final ItemToMapConversionContext context,
			final Map<String, Object> target)
	{
		final Object value = attribute.accessor().getValue(context.getItemModel());

		if (value == null)
		{
			return;
		}

		final String attributeName = attribute.getAttributeName();
		final Object convertedValue = converter.convert(value);
		target.put(attributeName, convertedValue);
	}

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attributeDescriptor)
	{
		return attributeDescriptor.isPrimitive() && !attributeDescriptor.isCollection();
	}

	public void setConverter(final Converter<Object, Object> converter)
	{
		this.converter = converter;
	}
}
