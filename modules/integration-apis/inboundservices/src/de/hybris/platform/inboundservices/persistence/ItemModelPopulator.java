/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence;

import de.hybris.platform.core.model.ItemModel;

/**
 * Populates {@link ItemModel} with values.
 */
public interface ItemModelPopulator
{
	/**
	 * Populates the specified item with the values available in the persistence context.
	 * @param item an item, whose attributes should be populated
	 * @param context persistence context containing data for the item and other information needed to populate the item in a specific
	 * way.
	 */
	void populate(ItemModel item, PersistenceContext context);
}
