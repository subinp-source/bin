/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.model;

import de.hybris.platform.core.model.ItemModel;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;


/**
 * Service interface for sorting collections of model instances.
 *
 * @param <T> item type
 */
public interface ModelSortService<T extends ItemModel>
{
	/**
	 * Returns a sorted {@link List} of the elements in the given {@link Collection}.
	 * 
	 * @param collection
	 *           a Collection of elements to be sorted
	 * @return the sorted List of elements
	 */
	@Nullable
	List<T> sort(@Nullable Collection<T> collection);
}
