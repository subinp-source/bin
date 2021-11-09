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
public class YFormDefinitionAutosaveTest extends AbstractYFormFacadeKeywords
{
	private static final Logger LOG = Logger.getLogger(YFormDefinitionAutosaveTest.class);

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
		importCsv("/test/impex/testYFormDefinitionAutosave.csv", "utf-8");
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);

		getModelService().detachAll();
	}

	@Test
	public void getDataRecord()
	{
		// Test_Get_Data_Record

		verifyYFormDataWithTypeExists("dataIdAutosave1", "DATA");
	}

	@Test
	public void getDraftRecord()
	{
		// Test_Get_Draft_Record

		verifyYFormDataWithTypeExists("dataIdAutosave1", "DRAFT");
	}

	@Test
	public void saveDataRecord()
	{
		// Test_Save_Data_Record_Without_Previous_Draft_Record

		verifyYFormDataWithTypeNotExistedThrowsException("dataIdAutosave0", "DATA", "YFormServiceException");
		verifyYFormDataWithTypeNotExistedThrowsException("dataIdAutosave0", "DRAFT", "YFormServiceException");

		createOrUpdateYFormDataWithType("applicationId", "testFormAutosave01", "dataIdAutosave0", "DATA", "new content");

		verifyYFormDataWithTypeExists("dataIdAutosave0", "DATA");
	}

	@Test
	public void saveNewDataRecordWithoutPreviousDraftRecord()
	{
		// Test_Save_Data_Record_Without_Previous_Draft_Record

		createOrUpdateYFormDataWithType("applicationId", "testFormAutosave01", "dataIdAutosave3", "DRAFT", "new content");
		verifyYFormDataWithTypeExists("dataIdAutosave3", "DRAFT");
		verifyYFormDataWithTypeNotExistedThrowsException("dataIdAutosave3", "DATA", "YFormServiceException");

		createOrUpdateYFormDataWithType("applicationId", "testFormAutosave01", "dataIdAutosave3", "DATA", "new content");

		verifyYFormDataWithTypeExists("dataIdAutosave3", "DATA");
		verifyYFormDataWithTypeNotExistedThrowsException("dataIdAutosave3", "DRAFT", "YFormServiceException");
	}

	@Test
	public void saveDraftRecord()
	{
		// Test_Save_Draft_Record_Without_Previous_Data_Record

		verifyYFormDataWithTypeNotExistedThrowsException("dataIdAutosave4", "DATA", "YFormServiceException");
		verifyYFormDataWithTypeNotExistedThrowsException("dataIdAutosave4", "DRAFT", "YFormServiceException");

		createOrUpdateYFormDataWithType("applicationId", "testFormAutosave01", "dataIdAutosave4", "DRAFT", "new content");

		verifyYFormDataWithTypeExists("dataIdAutosave4", "DRAFT");
		verifyYFormDataWithTypeNotExistedThrowsException("dataIdAutosave4", "DATA", "YFormServiceException");
	}

	@Test
	public void saveNewDraftRecordWithPreviousDataRecord()
	{
		// Test_Save_Draft_Record_With_Previous_Data_Record

		verifyYFormDataWithTypeNotExistedThrowsException("dataIdAutosave5", "DATA", "YFormServiceException");
		verifyYFormDataWithTypeNotExistedThrowsException("dataIdAutosave5", "DRAFT", "YFormServiceException");

		createOrUpdateYFormDataWithType("applicationId", "testFormAutosave01", "dataIdAutosave5", "DATA", "new content");

		verifyYFormDataWithTypeExists("dataIdAutosave5", "DATA");
		verifyYFormDataWithTypeNotExistedThrowsException("dataIdAutosave5", "DRAFT", "YFormServiceException");

		createOrUpdateYFormDataWithType("applicationId", "testFormAutosave01", "dataIdAutosave5", "DRAFT", "new content");

		verifyYFormDataWithTypeExists("dataIdAutosave5", "DRAFT");
		verifyYFormDataWithTypeExists("dataIdAutosave5", "DATA");
	}
}

