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

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.exception.CircularKeyReferenceException;
import de.hybris.platform.integrationservices.integrationkey.KeyAttributeValue;
import de.hybris.platform.integrationservices.integrationkey.KeyValue;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.KeyAttribute;
import de.hybris.platform.integrationservices.model.KeyDescriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

/**
 * A primary key descriptor for a complex item type.
 */
public class ItemKeyDescriptor implements KeyDescriptor
{
	private final Collection<KeyAttribute> simpleKeyAttributes;
	private final Collection<ReferenceKeyAttribute> referencedKeyAttributes;
	private final List<IntegrationObjectItemModel> referencePath;


	private ItemKeyDescriptor(final IntegrationObjectItemModel model, final List<IntegrationObjectItemModel> refPath)
	{
		Preconditions.checkArgument(model != null, "IntegrationObjectItemModel is required for ItemKeyDescriptor");
		referencePath = expand(model, refPath);
		final Collection<IntegrationObjectItemAttributeModel> keyAttributes = model.getKeyAttributes();
		simpleKeyAttributes = extractSimpleKeyAttributes(keyAttributes);
		referencedKeyAttributes = extractReferencedKeyAttributes(keyAttributes);
	}

	private List<IntegrationObjectItemModel> expand(final IntegrationObjectItemModel model, final List<IntegrationObjectItemModel> basePath)
	{
		final List<IntegrationObjectItemModel> path = new ArrayList<>(basePath);
		path.add(model);
		return path;
	}

	/**
	 * Creates new instance of this descriptor.
	 * @param item an item model, for which a key descriptor should be created.
	 * @return a key descriptor for the specified item
	 */
	static ItemKeyDescriptor create(final IntegrationObjectItemModel item)
	{
		return new ItemKeyDescriptor(item, new ArrayList<>());
	}

	@Override
	public KeyValue calculateKey(final Map<String, Object> item)
	{
		final Collection<KeyAttributeValue> thisItemValues = simpleKeyAttributes.stream()
				.map(attr -> getValue(attr, item))
				.collect(Collectors.toSet());
		final Collection<KeyAttributeValue> referencedValues = referencedKeyAttributes.stream()
				.map(attr -> attr.calculateKey(item))
				.map(KeyValue::getKeyAttributeValues)
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());

		return new KeyValue.Builder()
				.withValues(thisItemValues)
				.withValues(referencedValues)
				.build();
	}

	@Override
	public List<KeyAttribute> getKeyAttributes()
	{
		final List<KeyAttribute> allRefKeyAttributes = this.referencedKeyAttributes.stream().map(ReferenceKeyAttribute::getKeyAttributes).flatMap(Collection::stream).collect(Collectors.toList());
		return Stream.concat(simpleKeyAttributes.stream(), allRefKeyAttributes.stream()).collect(Collectors.toList());
	}

	@Override
	public boolean isKeyAttribute(final String attr)
	{
		return simpleKeyAttributes.stream().anyMatch(key -> key.getName().equals(attr))
				|| referencedKeyAttributes.stream().anyMatch(key -> key.getName().equals(attr));
	}

	private KeyAttributeValue getValue(final KeyAttribute attribute, final Map<String, Object> item)
	{
		final Object value = item == null ? null : item.get(attribute.getName());
		return new KeyAttributeValue(attribute, value);
	}

	private static Collection<KeyAttribute> extractSimpleKeyAttributes(final Collection<IntegrationObjectItemAttributeModel> attributes)
	{
		return attributes.stream()
				.filter(attr -> attr.getReturnIntegrationObjectItem() == null)
				.map(KeyAttribute::new)
				.collect(Collectors.toSet());
	}

	private Collection<ReferenceKeyAttribute> extractReferencedKeyAttributes(final Collection<IntegrationObjectItemAttributeModel> attributes)
	{
		return attributes.stream()
				.map(attr -> ReferenceKeyAttribute.create(attr, referencePath))
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	private static class ReferenceKeyAttribute
	{
		private final IntegrationObjectItemAttributeModel itemAttributeModel;
		private final ItemKeyDescriptor keyDescriptor;

		private ReferenceKeyAttribute(final IntegrationObjectItemAttributeModel attr, final List<IntegrationObjectItemModel> refPath)
		{
			itemAttributeModel = attr;
			keyDescriptor = deriveKeyDescriptor(attr.getReturnIntegrationObjectItem(), refPath);
		}

		static ReferenceKeyAttribute create(final IntegrationObjectItemAttributeModel attribute, final List<IntegrationObjectItemModel> refPath)
		{
			return attribute.getReturnIntegrationObjectItem() != null
					? new ReferenceKeyAttribute(attribute, refPath)
					: null;
		}

		private ItemKeyDescriptor deriveKeyDescriptor(final IntegrationObjectItemModel item, final List<IntegrationObjectItemModel> refPath)
		{
			final boolean referencesItemOnThePath = item != null
					&& refPath.stream().anyMatch(it -> Objects.equals(it.getCode(), item.getCode()));
			if (referencesItemOnThePath)
			{
				throw new CircularKeyReferenceException(itemAttributeModel);
			}
			return new ItemKeyDescriptor(item, refPath);
		}

		String getName()
		{
			return itemAttributeModel.getAttributeName();
		}

		KeyValue calculateKey(final Map<String, Object> item)
		{
			final Object attributeValue = item == null ? null : item.get(getName());
			return keyDescriptor.calculateKey((Map<String, Object>) attributeValue);
		}

		List<KeyAttribute> getKeyAttributes()
		{
			return keyDescriptor.getKeyAttributes();
		}
	}
}
