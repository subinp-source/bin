/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.setup;

import de.hybris.platform.core.initialization.SystemSetupContext;


public interface AddOnSystemSetupSupport // NOSONAR
{
	String IMPORT_SITES = "importSites";
	String IMPORT_SYNC_CATALOGS = "syncProducts&ContentCatalogs";
	String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";

	boolean synchronizeContentCatalog(final SystemSetupContext context, final String catalogName);

	boolean synchronizeProductCatalog(final SystemSetupContext context, final String catalogName);

	boolean getBooleanSystemSetupParameter(final SystemSetupContext context, final String key);

	void executeSolrIndexerCronJob(final String solrFacetSearchConfigName, final boolean fullReIndex);
}
