/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.ReferencePath;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * This path uses {@link TypeAttributeDescriptor}s to define references from one item to another.
 */
class AttributeReferencePath implements ReferencePath
{
	private static final Logger LOG = LoggerFactory.getLogger(AttributeReferencePath.class);
	private static ModelService modelService;

	private final List<TypeAttributeDescriptor> attributeChain;

	AttributeReferencePath(final TypeAttributeDescriptor attr)
	{
		this(Collections.singletonList(attr));
	}

	AttributeReferencePath(final List<TypeAttributeDescriptor> attributes)
	{
		Preconditions.checkArgument(CollectionUtils.isNotEmpty(attributes), "Path cannot be null or empty");
		Preconditions.checkArgument(!attributes.contains(null), "Path cannot contain null attributes");

		attributeChain = Collections.unmodifiableList(new ArrayList<>(attributes));
	}

	private TypeDescriptor getSource()
	{
		return attributeChain.get(0).getTypeDescriptor();
	}

	@Override
	public TypeDescriptor getDestination()
	{
		return attributeChain.get(attributeChain.size() - 1).getAttributeType();
	}

	@Override
	public int length()
	{
		return attributeChain.size();
	}

	@Override
	public boolean reaches(final TypeDescriptor type)
	{
		return uniqueItemTypesInThisPath().contains(type);
	}

	@Override
	public Collection<ReferencePath> expand()
	{
		return getDestination().getAttributes().stream()
				.filter(attr -> !attr.isPrimitive())
				.filter(this::notFormingLoop)
				.peek(attr -> LOG.trace("Expanding {}", attr))
				.map(this::expandAttributeChain)
				.map(AttributeReferencePath::new)
				.collect(Collectors.toSet());
	}

	private Set<TypeDescriptor> uniqueItemTypesInThisPath()
	{
		final Set<TypeDescriptor> uniqueTypes = attributeChain.stream()
				.map(TypeAttributeDescriptor::getAttributeType)
				.collect(Collectors.toSet());
		uniqueTypes.add(getSource());
		return uniqueTypes;
	}

	private boolean notFormingLoop(final TypeAttributeDescriptor attr)
	{
		return ! reaches(attr.getAttributeType());
	}

	private List<TypeAttributeDescriptor> expandAttributeChain(final TypeAttributeDescriptor attr)
	{
		final List<TypeAttributeDescriptor> expanded = new ArrayList<>(attributeChain);
		expanded.add(attr);
		LOG.trace("Attribute chain expanded to {}", expanded);
		return expanded;
	}

	@Override
	public Collection<Object> execute(final ItemModel item)
	{
		Collection<Object> values = Collections.singleton(item);
		for (final TypeAttributeDescriptor attr : attributeChain)
		{
			values = values.stream()
					.map(v -> readObjectAttributeValue(v, attr))
					.flatMap(Collection::stream)
					.collect(Collectors.toList());
		}
		return values;
	}

	private Collection<Object> readObjectAttributeValue(final Object item, final TypeAttributeDescriptor attr)
	{
		LOG.trace("Reading value of {} attribute from {}", attr.getQualifier(), item);
		if (item instanceof ItemModel)
		{
			return readItemAttributeValue((ItemModel) item, attr);
		}
		if (item instanceof Collection) {
			return collectionCast(item).stream()
					.map(o -> readObjectAttributeValue(o, attr))
					.flatMap(Collection::stream)
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private Collection<Object> readItemAttributeValue(final ItemModel item, final TypeAttributeDescriptor attr)
	{
		Preconditions.checkArgument(attr.getTypeDescriptor().isInstance(item), "Item must be instance of " + attr.getTypeDescriptor().getTypeCode() + " when processing " + attr);
		final Object value = getModelService().getAttributeValue(item, attr.getQualifier());
		return value instanceof Collection
				? collectionCast(value)
				: asCollection(value);
	}

	private Collection<Object> collectionCast(final Object value)
	{
		//noinspection unchecked
		return (Collection<Object>) value;
	}

	private Collection<Object> asCollection(final Object value)
	{
		return value != null
				? Collections.singleton(value)
				: Collections.emptyList();
	}

	@Override
	public boolean equals(final Object o)
	{
		return this == o || (o != null && getClass() == o.getClass()
				&& attributeChain.equals(((AttributeReferencePath) o).attributeChain));
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(AttributeReferencePath.class)
				.append(attributeChain)
				.toHashCode();
	}

	@Override
	public String toString()
	{
		return "AttributeNavigationPath: " + StringUtils.join(attributeChain, " -> ");
	}

	protected static ModelService getModelService()
	{
		if (modelService == null)
		{
			modelService = Registry.getApplicationContext().getBean("modelService", ModelService.class);
		}
		return modelService;
	}

	protected static void setModelService(final ModelService service)
	{
		modelService = service;
	}
}
