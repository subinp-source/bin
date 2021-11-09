/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocctests.setup;

import de.hybris.platform.commercewebservicestests.setup.CommercewebservicesTestSetup;


public class B2BOCCTestSetup extends CommercewebservicesTestSetup
{
	public void loadData()
	{
		getSetupImpexService().importImpexFile("/b2bocctests/import/sampledata/wsCommerceOrg/essential-data.impex", false);
		getSetupImpexService().importImpexFile("/b2bocctests/import/sampledata/wsCommerceOrg/products.impex", false);
		getSetupImpexService()
				.importImpexFile("/b2bocctests/import/sampledata/wsCommerceOrg/essential-data-user-rights.impex", false);
		getSetupImpexService()
				.importImpexFile("/b2bocctests/import/sampledata/wsCommerceOrg/standalonePermissionManagementTestData.impex",
						false);
		getSetupImpexService()
				.importImpexFile("/b2bocctests/import/sampledata/wsCommerceOrg/standaloneBudgetManagementTestData.impex", false);
		getSetupImpexService()
				.importImpexFile("/b2bocctests/import/sampledata/wsCommerceOrg/standaloneUnitManagementTestData.impex", false);
		getSetupImpexService()
				.importImpexFile("/b2bocctests/import/sampledata/wsCommerceOrg/standaloneUserManagementTestData.impex", false);
		getSetupImpexService()
				.importImpexFile("/b2bocctests/import/sampledata/wsCommerceOrg/standaloneOrderApprovalsTestData.impex", false);
		getSetupImpexService()
				.importImpexFile("/b2bocctests/import/sampledata/wsCommerceOrg/standaloneUnitGroupsManagementTestData.impex",
						false);
		getSetupImpexService()
				.importImpexFile("/b2bocctests/import/sampledata/wsCommerceOrg/standaloneReplenishmentOrderTestData.impex", false);
        getSetupImpexService()
                .importImpexFile("/b2bocctests/import/sampledata/wsCommerceOrg/standaloneCostCentersTestData.impex", false);
		getSetupImpexService()
				.importImpexFile("/b2bocctests/import/sampledata/wsCommerceOrg/standaloneOrdersTestData.impex", false);
		getSetupSolrIndexerService().executeSolrIndexerCronJob(String.format("%sIndex", WS_TEST), true);
	}
}
