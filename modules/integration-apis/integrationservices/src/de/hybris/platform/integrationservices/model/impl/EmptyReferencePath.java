/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.ReferencePath;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

/**
 * A path that does not contain attribute references for the cases when source item type and the destination item type is the same.
 */
class EmptyReferencePath implements ReferencePath
{
	private final TypeDescriptor pathType;

	EmptyReferencePath(final TypeDescriptor type)
	{
		Preconditions.checkArgument(type != null, "TypeDescriptor is required");
		pathType = type;
	}

	@Override
	public TypeDescriptor getDestination()
	{
		return pathType;
	}

	@Override
	public int length()
	{
		return 0;
	}

	@Override
	public boolean reaches(final TypeDescriptor type)
	{
		return pathType.equals(type);
	}

	@Override
	public Collection<ReferencePath> expand()
	{
		return pathType.getAttributes().stream()
				.filter(attr -> !attr.isPrimitive())
				.filter(attr -> ! pathType.equals(attr.getAttributeType()))
				.map(AttributeReferencePath::new)
				.collect(Collectors.toSet());
	}

	@Override
	public Collection<Object> execute(final ItemModel item)
	{
		return item == null
				? Collections.emptyList()
				: asCollection(item);
	}

	private Collection<Object> asCollection(final ItemModel item)
	{
		Preconditions.checkArgument(pathType.isInstance(item), "Item must be an instance of " + pathType.getTypeCode());
		return Collections.singletonList(item);
	}

	@Override
	public boolean equals(final Object o)
	{
		return this == o || (o != null
				&& getClass() == o.getClass()
				&& pathType.equals(((EmptyReferencePath) o).pathType));
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(EmptyReferencePath.class)
				.append(pathType)
				.build();
	}

	@Override
	public String toString()
	{
		return "EmptyNavigationPath: " + pathType;
	}
}
