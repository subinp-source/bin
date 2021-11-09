/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.integrationservices.populator;

import static java.util.stream.Collectors.toMap;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.service.IntegrationObjectConversionService;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * This class is responsible to populate the value from the source as collection of ItemModel/primitive
 */
public class DefaultCollectionType2MapPopulator extends AbstractItemToMapPopulator
{
	private static final String HYBRIS_ENUM_CODE = "code";

	private IntegrationObjectConversionService conversionService;
	private AtomicTypeValueConverter atomicTypeValueConverter;

	@Override
	protected void populateToMap(
			final TypeAttributeDescriptor attribute,
			final ItemToMapConversionContext context,
			final Map<String, Object> target)
	{
		final Collection<Object> itemModels = (Collection<Object>) attribute.accessor().getValue(context.getItemModel());

		if (CollectionUtils.isNotEmpty(itemModels))
		{
			final Object value = itemModels.stream()
			                               .map(itemModel -> calculateValue(attribute, context, itemModel))
			                               .collect(Collectors.toList());

			target.put(attribute.getAttributeName(), value);
		}
	}

	private Object calculateValue(final TypeAttributeDescriptor attribute, final ItemToMapConversionContext context,
	                              final Object item)
	{
		if (item instanceof ItemModel)
		{
			return getItemModelValue(attribute, context, (ItemModel) item);
		}
		else if(item instanceof HybrisEnumValue)
		{
			return getEnumValue((HybrisEnumValue) item);
		}
		return getAtomicValue(item);
	}

	private Object getItemModelValue(final TypeAttributeDescriptor attribute, final ItemToMapConversionContext context,
	                                 final ItemModel itemModel)
	{
		final TypeDescriptor itemType = attribute.getAttributeType();
		final ItemToMapConversionContext conversionContext = context.createSubContext(itemModel, itemType);
		return getConversionService().convert(conversionContext);
	}

	private Object getEnumValue(final HybrisEnumValue enumValue)
	{
		return Collections.singletonMap(HYBRIS_ENUM_CODE, enumValue.getCode());
	}

	private Object getAtomicValue(final Object itemModel)
	{
		final Object convertedValue = atomicTypeValueConverter.convert(itemModel);
		return PrimitiveCollectionElement.create(convertedValue);
	}

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attributeDescriptor)
	{
		return attributeDescriptor.isCollection();
	}

	public IntegrationObjectConversionService getConversionService()
	{
		return conversionService;
	}

	@Required
	public void setConversionService(final IntegrationObjectConversionService service)
	{
		conversionService = service;
	}

	public void setAtomicTypeValueConverter(final AtomicTypeValueConverter converter)
	{
		atomicTypeValueConverter = converter;
	}
}
