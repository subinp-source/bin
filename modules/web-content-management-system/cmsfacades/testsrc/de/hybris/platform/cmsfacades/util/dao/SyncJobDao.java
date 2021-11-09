/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao;

import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;

import java.util.List;


public interface SyncJobDao
{
	List<CatalogVersionSyncJobModel> getSyncJobsByCode(String code);
}
