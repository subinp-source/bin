/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

/**
 *  A strategies for removing cart entries
 *
 */
public interface CommerceRemoveEntriesStrategy
{
	/**
	 * Removes all entries from the given {@link de.hybris.platform.core.model.order.CartModel}.
	 *
	 * @param parameter A parameter object holding the {@link de.hybris.platform.core.model.order.CartModel} that will be emptied
	 */
	 void removeAllEntries(final CommerceCartParameter parameter);
}
