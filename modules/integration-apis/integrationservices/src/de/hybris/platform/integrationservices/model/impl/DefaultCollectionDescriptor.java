/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.enums.TypeOfCollectionEnum;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.integrationservices.model.CollectionDescriptor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;

/**
 * Default implementation of the {@link CollectionDescriptor}. If the underlying
 * attribute is not of type {@link CollectionTypeModel}, this implementation
 * does not consider the attribute as a collection. Therefore, the new collection will be null.
 */
public class DefaultCollectionDescriptor implements CollectionDescriptor
{
	private Supplier<Collection<Object>> supplier;
	private Class type;
	private final int hashCode;

	private DefaultCollectionDescriptor(final AttributeDescriptorModel attribute)
	{
		if (isCollection(attribute))
		{
			if (isSet(attribute))
			{
				initializeSet();
			}
			else
			{
				initializeList();
			}
		}
		else
		{
			initializeNull();
		}
		hashCode = (getClass().getName() + type).hashCode();
	}

	private boolean isCollection(final AttributeDescriptorModel attribute)
	{
		return attribute.getAttributeType() instanceof CollectionTypeModel;
	}

	private boolean isSet(final AttributeDescriptorModel attribute)
	{
		return TypeOfCollectionEnum.SET == ((CollectionTypeModel) attribute.getAttributeType()).getTypeOfCollection();
	}

	private void initializeSet()
	{
		supplier = Sets::newHashSet;
		type = Set.class;
	}

	private void initializeList()
	{
		supplier = Lists::newArrayList;
		type = List.class;
	}

	private void initializeNull()
	{
		supplier = () -> null;
		type = null;
	}

	public static CollectionDescriptor create(final AttributeDescriptorModel attribute)
	{
		return new DefaultCollectionDescriptor(attribute);
	}

	@Override
	public Collection<Object> newCollection()
	{
		return supplier.get();
	}

	@Override
	public boolean equals(final Object other)
	{
		return this == other || (other != null
				&& getClass().equals(other.getClass())
				&& Objects.equals(type, ((DefaultCollectionDescriptor) other).type));
	}

	@Override
	public int hashCode()
	{
		return hashCode;
	}

	@Override
	public String toString()
	{
		return String.format("%s { collection type: %s }", this.getClass().getSimpleName(), type);
	}
}
