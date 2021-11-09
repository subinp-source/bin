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
import org.junit.Ignore;
import org.junit.Test;


@Ignore
@IntegrationTest
public class YFormDefinitionVersioningTest extends AbstractYFormFacadeKeywords
{
	private static final Logger LOG = Logger.getLogger(YFormDefinitionVersioningTest.class);

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
		importCsv("/test/impex/testYFormDefinitionVersioning.csv", "utf-8");
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);

		getModelService().detachAll();
	}

	@Test
	public void getYFormDefinitionWithVersion()
	{
		// Test_Get_YFormDefinition_With_Version

		verifyYFormDefinitionWithVersionExists("applicationId", "testFormVersioning01", "1");
	}

	@Test
	public void getUnexistingVersionOfYFormDefinitionThrowsException()
	{
		// Test_Get_YFormDefinition_With_Version_Not_Exists

		verifyYFormDefinitionWithVersionNotExistedThrowsException("applicationId", "testFormVersioning01", "2",
				"YFormServiceException");
	}


	@Test
	public void getLatestVersionOfYFormDefinition()
	{
		// Test_Get_YFormDefinition_Return_Latest_Version

		final String newContent = getYFormDefinitionFromTemplate("applicationId", "testFormVersioning01", "updated content");
		verifyYFormDefinitionWithVersionExists("applicationId", "testFormVersioning01", "1");
		verifyYFormDefinitionWithVersionNotExistedThrowsException("applicationId", "testFormVersioning01", "2",
				"YFormServiceException");

		createYFormDefinition("applicationId", "testFormVersioning01", newContent);

		verifyYFormDefinitionWithVersionExists("applicationId", "testFormVersioning01", "2");
	}

	@Test
	public void createYFormDefinitionWithVersion1()
	{
		// Test_Create_YFormDefinition_With_No_Previous_Version

		final String newContent = getYFormDefinitionFromTemplate("applicationId", "testFormVersionong00", "new content");
		verifyYFormDefinitionWithVersionNotExistedThrowsException("applicationId", "testFormVersionong00", "1",
				"YFormServiceException");

		createYFormDefinition("applicationId", "testFormVersionong00", newContent);

		verifyYFormDefinitionWithVersionExists("applicationId", "testFormVersionong00", "1");
	}


	@Test
	public void createYFormDefinitionWithVersion2()
	{
		// Test_Create_YFormDefinition_With_Previous_Version

		final String newContent = getYFormDefinitionFromTemplate("applicationId", "testFormVersioning02", "updated content");
		final String content = getYFormDefinitionFromTemplate("applicationId", "testFormVersioning02", "content2");
		verifyYFormDefinitionWithVersionExists("applicationId", "testFormVersioning02", "1");
		verifyYFormDefinitionWithVersionNotExistedThrowsException("applicationId", "testFormVersioning02", "2",
				"YFormServiceException");

		createYFormDefinition("applicationId", "testFormVersioning02", newContent);

		verifyYFormDefinitionWithVersionExists("applicationId", "testFormVersioning02", "1");
		verifyYFormDefinitionWithVersionUpdated("applicationId", "testFormVersioning02", "1", content);
		verifyYFormDefinitionWithVersionExists("applicationId", "testFormVersioning02", "2");
		verifyYFormDefinitionWithVersionUpdated("applicationId", "testFormVersioning02", "2", newContent);
	}


	@Test
	public void updateYFormDefinitionKeepsVersionNumber()
	{
		// Test_Update_YFormDefinition_Keeps_Version_Number

		final String content = getYFormDefinitionFromTemplate("applicationId", "testFormVersioning03", "content");
		final String newContent = getYFormDefinitionFromTemplate("applicationId", "testFormVersioning03", "new content");

		createYFormDefinition("applicationId", "testFormVersioning03", content);

		verifyYFormDefinitionWithVersionExists("applicationId", "testFormVersioning03", "1");

		updateYFormDefinition("applicationId", "testFormVersioning03", newContent);

		verifyYFormDefinitionWithVersionNotExistedThrowsException("applicationId", "testFormVersioning03", "2",
				"YFormServiceException");
		verifyYFormDefinitionWithVersionUpdated("applicationId", "testFormVersioning03", "1", newContent);
	}

	@Test
	public void getYFormDefinitionWithFormDataId()
	{
		// Test_Get_YFormDefinition_With_FormDataId_Returns_The_Right_Version

		final String content = getYFormDefinitionFromTemplate("applicationId", "testFormVersioning04", "content");
		final String newContent = getYFormDefinitionFromTemplate("applicationId", "testFormVersioning04", "new content");

		createYFormDefinition("applicationId", "testFormVersioning04", content);
		createYFormData("applicationId", "testFormVersioning04", "id1", "ref1", content);
		createYFormDefinition("applicationId", "testFormVersioning04", newContent);
		createYFormData("applicationId", "testFormVersioning04", "id2", "ref1", content);

		verifyYFormDefinitionWithFormDataId("id1", "applicationId", "testFormVersioning04", "1", content);
		verifyYFormDefinitionWithFormDataId("id2", "applicationId", "testFormVersioning04", "2", newContent);
	}

}
