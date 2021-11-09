/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.service;

import java.util.Collection;
import java.util.Optional;

import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;

/**
 * Service that provides basic functionality for merchandising product directory configurations.
 */
public interface MerchProductDirectoryConfigService
{
	/**
	 * Returns all merchandising product directory configurations.
	 *
	 * @return list of merchandising product directories or empty list if none found
	 */
	Collection<MerchProductDirectoryConfigModel> getAllMerchProductDirectoryConfigs();

	/**
	 * Returns the merchandising product directory configuration for a specific indexed type.
	 *
	 * @param indexedType
	 *           - the indexed type identifier
	 *
	 * @return the product directory configuration
	 */
	Optional<MerchProductDirectoryConfigModel> getMerchProductDirectoryConfigForIndexedType(String indexedType);

	/**
	 * Returns the merchandising product directory configuration being used by the current base site.
	 * @return the product directory configuration being used by the current base site.
	 */
	Optional<MerchProductDirectoryConfigModel> getMerchProductDirectoryConfigForCurrentBaseSite();

	/**
	 * Updates persistence for provided {@link MerchProductDirectoryConfigModel}.
	 * @param model the product directory config model to update.
	 */
	void updateMerchProductDirectory(MerchProductDirectoryConfigModel model);
}