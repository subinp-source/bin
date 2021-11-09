/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.dao;

import java.util.Collection;

import com.hybris.merchandising.model.MerchSynchronizationConfigModel;

/**
 * The {@link MerchSynchronizationConfigModel} DAO.
 */
public interface MerchSynchronizationConfigDao
{
	/**
	 * Finds all merchandising synchronization configurations.
	 *
	 * @return list of merchandising synch configurations or empty list if no configuration is found
	 */
	Collection<MerchSynchronizationConfigModel> findAllMerchSynchronizationConfig();
}
