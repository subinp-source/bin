/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.service;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;

import java.util.Optional;

/**
 * Convenience service to search for {@link ItemModel}s
 */
public interface ItemModelSearchService
{
	/**
	 * Finds an {@link ItemModel} by the given {@link PK}.
	 * Warning: this method clears the cache before doing the search.
	 * This may impact performance.
	 *
	 * @param <T> Expected type of the item found
	 * @param pk  PK of the item to find
	 * @return the ItemModel wrapped in an {@link Optional} if found, otherwise {@link Optional#empty()}
	 */
	<T extends ItemModel> Optional<T> nonCachingFindByPk(PK pk);
}
