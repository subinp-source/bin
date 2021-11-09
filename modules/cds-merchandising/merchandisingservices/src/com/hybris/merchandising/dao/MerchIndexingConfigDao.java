/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.dao;

import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;

import java.util.Collection;
import java.util.Optional;

import com.hybris.merchandising.model.MerchIndexingConfigModel;

/**
 * The {@link MerchIndexingConfigModel} DAO.
 */
public interface MerchIndexingConfigDao
{
	/**
	 * Finds all merchandising listener configurations.
	 *
	 * @return list of merchandising configurations or empty list if no configuration is found
	 */
	Collection<MerchIndexingConfigModel> findAllMerchIndexingConfigs();

	/**
	 * Finds the merchandising listener configuration for a specific indexed type.
	 *
	 * @param indexedType
	 *           - the indexed type
	 *
	 * @return the search configuration
	 */
	Optional<MerchIndexingConfigModel> findMerchIndexingConfigByIndexedType(final SolrIndexedTypeModel indexedType);
}
