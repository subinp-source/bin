/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleocctests.setup;

import de.hybris.platform.commercewebservicestests.setup.CommercewebservicesTestSetup;


/**
 * Utility class to be used in test suites to manage tests (e.g. start server, load data).
 */
public class ConfigurableBundleOCCTestSetup extends CommercewebservicesTestSetup
{
	public void loadData()
	{
		getSetupImpexService().importImpexFile("/configurablebundleocctests/import/sampledata/productCatalogs/wsTestProductCatalog/products-bundles.impex", false);
		getSetupImpexService().importImpexFile("/configurablebundleocctests/import/sampledata/productCatalogs/wsTestProductCatalog/products-stocklevels.impex", false);
		getSetupImpexService().importImpexFile("/configurablebundleocctests/import/sampledata/stores/wsTest/solr.impex", false);

		getSetupSolrIndexerService().executeSolrIndexerCronJob(String.format("%sIndex", WS_TEST), true);
	}
}
