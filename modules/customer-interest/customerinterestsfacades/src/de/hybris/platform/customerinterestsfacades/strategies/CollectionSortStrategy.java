/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.strategies;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Strategy to sort a given collection
 */
public interface CollectionSortStrategy<T extends Collection>
{
	/**
	 * ascending order of a given attribute
	 *
	 * @param list
	 */
	void ascendingSort(T list);

	/**
	 * descending order of a given attribute
	 *
	 * @param list
	 */
	default void descendingSort(final T list)
	{
		ascendingSort(list);
		Collections.reverse((List<?>) list);
	}
}
