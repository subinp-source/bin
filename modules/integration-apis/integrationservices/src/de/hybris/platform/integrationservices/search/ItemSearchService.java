/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search;

import de.hybris.platform.core.model.ItemModel;

import java.util.Optional;

import javax.validation.constraints.NotNull;

/**
 * A service for searching items in the platform.
 */
public interface ItemSearchService
{
	/**
	 * Searches for a single {@code Item} matching the {@code request} criteria.
	 *
	 * @param request item search request
	 * @return an Optional containing the matching item model or an empty Optional, if a matching item is not found.
	 * @throws NonUniqueItemFoundException if more than one matching item found for the request criteria.
	 */
	Optional<ItemModel> findUniqueItem(@NotNull ItemSearchRequest request);

	/**
	 * Searches for {@code Item}s in the platform based on the provided request criteria.
	 *
	 * @param request a request containing search conditions, i.e. type of items to find, pagination parameters, etc
	 * @return result of the item search.
	 */
	@NotNull ItemSearchResult<ItemModel> findItems(@NotNull ItemSearchRequest request);

	/**
	 * Counts how many items in the platform match the provided request conditions.
	 * @param request a request specifying the search criteria.
	 * @return number of items in the platform matching the search criteria.
	 */
	int countItems(@NotNull ItemSearchRequest request);
}
