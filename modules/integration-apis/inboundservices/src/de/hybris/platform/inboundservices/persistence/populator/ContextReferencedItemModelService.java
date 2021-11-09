/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.Collection;

/**
 * A service for looking up items nested in the payload of the item to be persisted.
 */
public interface ContextReferencedItemModelService
{
	/**
	 *
	 * Using the context derives an existing {@code ItemModel} that is referenced in the payload of the item to be persisted.
	 * @param attribute an attribute that represents the relationship between the item model and the referenced item model that will be derived.
	 * @param referencedItemContext a persistence context containing information about the item to be found or, in other words,
	 * payload of the item to be resolved and converted to an {@code ItemModel}.
	 * @return an item specified by {@link PersistenceContext#getIntegrationItem()} or {@code null}, if such item does
	 * not exist in the platform. If the item is found, then it's already updated with the values from the {@code referencedItemContext}.
	 */
	ItemModel deriveReferencedItemModel(TypeAttributeDescriptor attribute, PersistenceContext referencedItemContext);

	/**
	 * Derives a collection of {@code ItemModel}s referenced by the specified attribute in the item payload present in the provided
	 * persistence context.
	 * @param context a persistence context containing information about the item being persisted.
	 * @param attribute an attribute in the context item payload referencing a collection of items to be derived.
	 * @return a collection of items corresponding to the values of the specified attribute in {@link PersistenceContext#getIntegrationItem()}
	 * or an empty collection, if the payload either does not contain the attribute or the attribute value in the payload is empty
	 * or {@code null}. Found items are already updated with the values from their payloads.
	 */
	Collection<ItemModel> deriveItemsReferencedInAttributeValue(PersistenceContext context, TypeAttributeDescriptor attribute);
}
