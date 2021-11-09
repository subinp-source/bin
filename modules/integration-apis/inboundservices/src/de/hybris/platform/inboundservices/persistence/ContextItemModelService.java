/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence;

import de.hybris.platform.core.model.ItemModel;

/**
 * A service for deriving an {@link ItemModel} to be used for persistence.
 */
public interface ContextItemModelService
{
	/**
	 * Using the context searches for an {@code ItemModel} existing in context or in the platform; if not found creates a new item
	 * model from the information available in the context. This method should be used when creating
	 * a new item for the root or nested integration item is desired in cases when the item was not found.
	 * @param context a persistence context containing information about the item to be persisted.
	 * @return an item specified by {@link PersistenceContext#getIntegrationItem()}. It may be an item existing in the platform
	 * or a new item instance, if such item does not exist yet. In either case the item is already updated/populated with values
	 * provided in the {@code context}.
	 */
	ItemModel findOrCreateItem(PersistenceContext context);
}
