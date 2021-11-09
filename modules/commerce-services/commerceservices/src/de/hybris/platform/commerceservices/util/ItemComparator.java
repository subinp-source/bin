/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.util;

import de.hybris.platform.core.model.ItemModel;

/**
 * hybris item comparator that orders results by PK.
 * This comparator provides a stable order over any type of hybris item.
 * The ordering is unknown but it won't change.
 */
public class ItemComparator extends AbstractComparator<ItemModel>
{
	public static final ItemComparator INSTANCE = new ItemComparator();

	@Override
	protected int compareInstances(final ItemModel item1, final ItemModel item2)
	{
		return item1.getPk().compareTo(item2.getPk());
	}
}
