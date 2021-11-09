/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.strategies;

import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;
import java.util.Optional;


/**
 * Resolves the stores for a given index type.
 */
public interface SnStoreSelectionStrategy
{
	/**
	 * Returns the default store for the given index type.
	 *
	 * @param indexTypeId - the index type id
	 * @return the default store
	 */
	Optional<BaseStoreModel> getDefaultStore(String indexTypeId);

	/**
	 * Returns the default stores for the given index type.
	 *
	 * @param indexType - the index type
	 * @return the default store
	 */
	Optional<BaseStoreModel> getDefaultStore(SnIndexTypeModel indexType);

	/**
	 * Returns the stores for the given index type.
	 *
	 * @param indexTypeId - the index type id
	 * @return the stores
	 */
	List<BaseStoreModel> getStores(String indexTypeId);

	/**
	 * Returns the stores for the given index type.
	 *
	 * @param indexType - the index type
	 * @return the stores
	 */
	List<BaseStoreModel> getStores(SnIndexTypeModel indexType);
}
