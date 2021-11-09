/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.integrationservices.model.CollectionDescriptor;

import java.util.Collection;

import com.google.common.collect.Lists;

/**
 * Implementation of the {@link CollectionDescriptor} relating to classification attributes. If the underlying
 * attribute is not of <code>multivalued</code>, this implementation
 * does not consider the attribute as a collection. Therefore, the new collection will be null. If the attribute is multivalued,
 * an empty {@link java.util.List} will be returned since multivalued classification attributes are always stored in a List
 */
public class ClassificationCollectionDescriptor implements CollectionDescriptor
{
	private Collection<Object> collection;

	private ClassificationCollectionDescriptor(final ClassAttributeAssignmentModel classAttributeAssignment)
	{
		if (Boolean.TRUE.equals(classAttributeAssignment.getMultiValued()))
		{
			collection = Lists.newArrayList();
		}
	}

	public static CollectionDescriptor create(final ClassAttributeAssignmentModel classAttributeAssignment)
	{
		return new ClassificationCollectionDescriptor(classAttributeAssignment);
	}

	@Override
	public Collection<Object> newCollection()
	{
		return collection;
	}
}
