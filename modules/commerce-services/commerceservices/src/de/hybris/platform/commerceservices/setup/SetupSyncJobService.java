/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.setup;

import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Set;


/**
 * Service that handles creating synchronization jobs.
 */
public interface SetupSyncJobService
{
	/**
	 * Ensure that a product catalog sync job exists for the specified catalog id. The sync job is created between the
	 * Staged and Online catalog versions only if there is no existing sync job.
	 * 
	 * @param catalogId
	 *           the catalog id to search sync job for.
	 */
	void createProductCatalogSyncJob(String catalogId);

	/**
	 * Ensure that a cms content catalog sync job exists for the specified catalog id. The sync job is created between
	 * the Staged and Online catalog versions only if there is no existing sync job.
	 * 
	 * @param catalogId
	 *           the catalog id
	 */
	void createContentCatalogSyncJob(String catalogId);

	/**
	 * Sets up a dependency relationship between the CatalogVersionSyncJob for a catalog and the CatalogVersionSyncJobs for a set of dependant catalogs.
	 *
	 * @param catalogId
	 *           the catalog id
	 * @param dependentCatalogIds
	 *           the dependant catalog ids
	 */
	void assignDependentSyncJobs(String catalogId, Set<String> dependentCatalogIds);

	/**
	 * Run the catalog sync for the specified catalog.
	 * 
	 * @param catalogId
	 *           the catalog id
	 * @return an instance of {@link PerformResult} containing the sync job result and status
	 */
	PerformResult executeCatalogSyncJob(String catalogId);
}
