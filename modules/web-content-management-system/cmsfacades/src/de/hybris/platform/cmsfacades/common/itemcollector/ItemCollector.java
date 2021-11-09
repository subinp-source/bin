/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.itemcollector;

import de.hybris.platform.core.model.ItemModel;

import java.util.List;


/**
 * Interface definition for CMS Item Collectors.
 * For generic purposes, this interface can be used to identify related items for a given item model.
 * @param <T> the type parameter which extends the {@link ItemModel} type
 */
public interface ItemCollector<T extends ItemModel>
{
	/**
	 * Collects the items that are related to a given {@link ItemModel}
	 * @param item the itemModel that will be inspected to return the related item models.
	 * @return a list with the related item models, never {@code null}.
	 * @throws NullPointerException if item is {@code null}.
	 */
	List<? extends ItemModel> collect(final T item);
}
