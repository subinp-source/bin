/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.strategies;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.List;

import javax.annotation.Nonnull;


/**
 * Cart entry merge strategy: create new entry vs update an existing one.
 *
 * @see de.hybris.platform.commerceservices.order.strategies.impl.DefaultEntryMergeStrategy
 * @see de.hybris.platform.commerceservices.order.EntryMergeFilter
 * @see de.hybris.platform.commerceservices.order.impl.DefaultCommerceAddToCartStrategy
 */
public interface EntryMergeStrategy
{
	/**
	 * Returns cart entry can be updated instead of creation of separate {@code newEntry}.
	 *
	 * @param entries
	 *           list of existing entries
	 * @param newEntry
	 *           the merge candidate (can be an item of {@code entries}
	 * @return merge target ({@code null} if no applicable entries found)
	 */
	AbstractOrderEntryModel getEntryToMerge(List<AbstractOrderEntryModel> entries, @Nonnull AbstractOrderEntryModel newEntry);
}
