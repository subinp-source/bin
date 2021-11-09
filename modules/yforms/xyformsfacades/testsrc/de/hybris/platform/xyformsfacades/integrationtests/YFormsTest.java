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


/**
 * Port of old ATDD-framework tests to Spock framework
 * <p>
 * Get, Create, Update and Create or update YFormDefinition model
 */
@Ignore
@IntegrationTest
public class YFormsTest extends AbstractYFormFacadeKeywords
{
	private static final Logger LOG = Logger.getLogger(YFormsTest.class);

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
	public void getUnexistingYFormDefinitionThrowsException()
	{
		// Test_Get_Y_Form_Definition_YFormServiceException_If_Not_Existed

		verifyYFormDefinitionNotExistedThrowsException("applicationId", "weddingForm01", "YFormServiceException");
	}

	@Test
	public void getYFormDefinition()
	{
		// Test_Get_Y_Form_Definition

		verifyYFormDefinitionExists("applicationId", "testForm01");
	}

	@Test
	public void createYFormDefinition()
	{
		// Test_Get_Y_Form_Definition

		verifyYFormDefinitionExists("applicationId", "testForm01");
	}

	@Test
	public void updateYFormDefinition()
	{
		// Test_Update_Y_Form_Definition

		final String newContent = getYFormDefinitionFromTemplate("applicationId", "weddingForm01", "new content");

		updateYFormDefinition("applicationId", "weddingForm01", newContent);
		verifyYFormDefinitionUpdated("applicationId", "weddingForm01", newContent);
	}

	@Test
	public void updateUnexistingYFormDefinitionThrowsException()
	{
		// Test_Update_Y_Form_Definition_YFromServiceException

		final String content = getYFormDefinitionFromTemplate("applicationId", "weddingForm01", "content");

		verifyYFormDefinitionNotExistedOnUpdateThrowsException("applicationId", "weddingForm01", content, "YFormServiceException");
	}

	@Test
	public void createOrUpdateYFormDefinition()
	{
		// Test_Create_Or_Update_Y_Form_Definition

		final String content = getYFormDefinitionFromTemplate("applicationId", "weddingForm01", "content");
		final String newContent = getYFormDefinitionFromTemplate("applicationId", "weddingForm01", "new content");

		verifyYFormDefinitionNotExistedThrowsException("applicationId", "weddingForm01", "YFormServiceException");

		createYFormDefinition("applicationId", "weddingForm01", content);
		verifyYFormDefinitionExists("applicationId", "weddingForm01");
		verifyYFormDefinitionUpdated("applicationId", "weddingForm01", content);

		createYFormDefinition("applicationId", "weddingForm01", newContent);
		verifyYFormDefinitionUpdated("applicationId", "weddingForm01", newContent);
	}

	@Test
	public void getUnexistingYFormDataThrowsException()
	{
		// Test_Get_Y_Form_Data_Got_YFormServiceException_If_Not_Existed

		verifyYFormDataNotExistedThrowsException("55ba893a657df5595c228bc60c0a195baa5e8be6", "YFormServiceException");
	}

	@Test
	public void getYFormData()
	{
		// Test_Get_Y_Form_Data

		verifyYFormDataExists("44ba893a657df5595c228bc60c0a195baa5e8be6");
		verifyYFormDataHistoryCreated("44ba893a657df5595c228bc60c0a195baa5e8be6");
	}

	@Test
	public void createYFormData()
	{
		// Test_Create_Y_Form_Data

		createYFormData("applicationId", "testForm01", "55ba893a657df5595c228bc60c0a195baa5e8be6", "ref1", "content");

		verifyYFormDataExists("55ba893a657df5595c228bc60c0a195baa5e8be6");
		verifyYFormDataExists("55ba893a657df5595c228bc60c0a195baa5e8be6", "ref1", "content");
		verifyYFormDataHistoryCreated("55ba893a657df5595c228bc60c0a195baa5e8be6");
	}

	@Test
	public void updateYFormData()
	{
		// Test_Update_Y_Form_Data

		updateYFormDataById("44ba893a657df5595c228bc60c0a195baa5e8be6", "new content");

		verifyYFormDataUpdated("44ba893a657df5595c228bc60c0a195baa5e8be6", "new content");
		verifyYFormDataHistoryCreated("44ba893a657df5595c228bc60c0a195baa5e8be6");
	}

	@Test
	public void updateUnexistingYFormDataThrowsException()
	{
		// Test_Update_Y_Form_Data_YFromServiceException

		verifyYFormDataNotExistedThrowsException("55ba893a657df5595c228bc60c0a195baa5e8be6", "YFormServiceException");
	}

	@Test
	public void createOrUpdateYFormData()
	{
		// Test_Create_Or_Update_Y_Form_Data

		verifyYFormDataNotExistedThrowsException("55ba893a657df5595c228bc60c0a195baa5e8be6", "YFormServiceException");

		createOrUpdateYFormData("applicationId", "testForm01", "55ba893a657df5595c228bc60c0a195baa5e8be6", "ref1", "content");

		verifyYFormDataExists("55ba893a657df5595c228bc60c0a195baa5e8be6");
		verifyYFormDataUpdated("55ba893a657df5595c228bc60c0a195baa5e8be6", "content");
		verifyYFormDataHistoryCreated("55ba893a657df5595c228bc60c0a195baa5e8be6");

		createOrUpdateYFormData("applicationId", "testForm01", "55ba893a657df5595c228bc60c0a195baa5e8be6", "ref1", "new content");

		verifyYFormDataExists("55ba893a657df5595c228bc60c0a195baa5e8be6");
		verifyYFormDataUpdated("55ba893a657df5595c228bc60c0a195baa5e8be6", "new content");
		verifyYFormDataHistoryCreated("55ba893a657df5595c228bc60c0a195baa5e8be6");
	}
}

