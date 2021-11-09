/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.model.CollectionDescriptor;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;

public abstract class AbstractCollectionAttributePopulator extends AbstractAttributePopulator
{
	/**
	 * Get the collection of values from the context to be persisted
	 *
	 * @param attributeDescriptor Contains information that describes the attribute
	 * @param context Contains the data to persist
	 * @return Collection of values to persist
	 */
	protected abstract Collection<Object> getNewCollection(ItemModel item, TypeAttributeDescriptor attributeDescriptor, PersistenceContext context);

	@Override
	protected void populateAttribute(final ItemModel item, final TypeAttributeDescriptor attribute, final PersistenceContext context)
	{
		final CollectionDescriptor descriptor = attribute.getCollectionDescriptor();
		final Collection<Object> existingValues = getExistingCollection(item, attribute);
		final Collection<Object> newValues = getNewCollection(item, attribute, context);
		final Collection<Object> combinedValues = combineAttributeValues(newValues, existingValues, descriptor);
		attribute.accessor().setValue(item, combinedValues);
	}

	protected Collection<Object> getExistingCollection(final ItemModel item, final TypeAttributeDescriptor attr)
	{
		final var obj = attr.accessor().getValue(item);
		//noinspection unchecked
		return obj instanceof Collection
				? (Collection<Object>) obj
				: Collections.emptyList();
	}

	/**
	 * Combines the existing and new values to formulate a new collection without duplicates. The default behavior
	 * is to append the new values to the collection. User can override this method to define a different
	 * strategy in combining the attribute values.
	 *
	 * @param newValues Update values to combine
	 * @param existingValues Existing values to combine
	 * @param descriptor Describes the collection that stores the values
	 * @return A new collection containing the attribute values
	 */
	protected Collection<Object> combineAttributeValues(final Collection<Object> newValues, final Collection<Object> existingValues, final CollectionDescriptor descriptor)
	{
		Collection<Object> collection = descriptor.newCollection();
		if (collection == null)
		{
			collection = Lists.newArrayList();
		}
		collection.addAll(existingValues);
		collection.addAll(removeDuplicates(collection, newValues));
		return collection;
	}

	/**
	 * Removes the values in the {@code currentCollection} from the {@code newCollection}
	 *
	 * @param currentCollection Collection containing the values to deduplicate from the new collection
	 * @param newCollection Collection of values
	 * @return A collection of the {@code newCollection} with duplicates removed
	 */
	private Collection<Object> removeDuplicates(final Collection<Object> currentCollection, final Collection<Object> newCollection)
	{
		return newCollection.stream()
				.filter(element -> !currentCollection.contains(element))
				.collect(Collectors.toList());
	}
}
