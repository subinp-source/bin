/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.service;

import java.util.Collection;
import java.util.Optional;

import com.hybris.merchandising.model.MerchIndexingConfigModel;

/**
 * Service that provides basic functionality for merchandising listener configurations.
 */
public interface MerchIndexingConfigService
{
	/**
	 * Returns all merchandising listener configurations.
	 *
	 * @return list of merchandising configurations or empty list if no configuration is found
	 */
	Collection<MerchIndexingConfigModel> getAllMerchIndexingConfigs();

	/**
	 * Returns the merchandising listener configuration for a specific indexed type.
	 *
	 * @param indexedType
	 *           - the indexed type identifier
	 *
	 * @return the search configuration
	 */
	Optional<MerchIndexingConfigModel> getMerchIndexingConfigForIndexedType(final String indexedType);
}
