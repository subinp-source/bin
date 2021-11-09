/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.textfieldconfiguratortemplateocctests.setup;

import de.hybris.platform.commercewebservicestests.setup.CommercewebservicesTestSetup;



public class TextfieldConfiguratorOCCTestSetup extends CommercewebservicesTestSetup
{
	public void loadData()
	{
		getSetupImpexService().importImpexFile(
				"/textfieldconfiguratortemplateocctests/import/sampledata/productCatalogs/wsTestProductCatalog/standaloneTestData.impex",
				false);
		getSetupSolrIndexerService().executeSolrIndexerCronJob(String.format("%sIndex", WS_TEST), true);
	}
}
