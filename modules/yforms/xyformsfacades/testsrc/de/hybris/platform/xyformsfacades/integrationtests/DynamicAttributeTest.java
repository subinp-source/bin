/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsfacades.integrationtests;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.util.Config;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DynamicAttributeTest extends AbstractYFormFacadeKeywords
{
	private static final Logger LOG = Logger.getLogger(DynamicAttributeTest.class);

	// is run before every test
	@Before
	public void setUp() throws ImpExException
	{
		// importing test csv
		final String legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		LOG.info("Existing value for " + ImpExConstants.Params.LEGACY_MODE_KEY + " :" + legacyModeBackup);
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		importCsv("/test/impex/testYForm.csv", "utf-8");
		importCsv("/test/impex/testAllYFormDefinitionsDynamicAttributes.csv", "utf-8");
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);

		getModelService().detachAll();
	}

	@Test
	public void categoryHasReferencesToAllYFormDefinitions()
	{
		// Test_Categories_Have_All_The_YFormDefinitions_That_Assigned_To_It_And_Its_Supercategories

		verifyCategoryHasAllYFormDefinitions("Category1", "applicationId:formId1");
		verifyCategoryHasAllYFormDefinitions("Category2", "applicationId:formId2", "applicationId:formId1");
		verifyCategoryHasAllYFormDefinitions("Category3", "applicationId:formId2", "applicationId:formId1");
		verifyCategoryHasAllYFormDefinitions("Category4", "applicationId:formId2", "applicationId:formId3", "applicationId:formId1");
		verifyCategoryHasAllYFormDefinitions("Category5", "applicationId:formId4", "applicationId:formId1");
	}

	@Test
	public void productHasReferencesToAllYFormDefinitionsAssignedToSupercategories()
	{
		// Test_Products_Have_All_The_YFormDefinitions_That_Assigned_To_All_Of_Its_Supercategories

		verifyProductHasAllYFormDefinitions("Product1", "applicationId:formId2", "applicationId:formId4", "applicationId:formId3", "applicationId:formId1");
		verifyProductHasAllYFormDefinitions("Product2", "applicationId:formId2", "applicationId:formId1");
	}
}

