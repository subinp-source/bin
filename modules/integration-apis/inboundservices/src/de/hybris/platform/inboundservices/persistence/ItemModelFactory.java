/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence;

import de.hybris.platform.core.model.ItemModel;

/**
 * A factory for creating new {@link ItemModel} instances based on a {@link PersistenceContext}
 */
public interface ItemModelFactory
{
	/**
	 * Creates new {@code ItemModel} instance.
	 * @param context context containing information about exact type of the instance to be created.
	 * @return a new item model of the type specified by context.
	 */
	ItemModel createItem(PersistenceContext context);
}
