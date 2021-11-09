/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsfacades.integrationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.util.Config;
import de.hybris.platform.xyformsservices.form.YFormService;
import de.hybris.platform.xyformsservices.model.YFormDataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * Port of old ATDD-framework tests to Spock framework
 * <p>
 * Get, Create, Update and Create or update YFormDefinition model
 */
@IntegrationTest
public class YFormsIntegrationTest extends AbstractYFormFacadeKeywords
{
	private static final Logger LOG = Logger.getLogger(YFormsIntegrationTest.class);

	@Resource
	private YFormService yformService;

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
		importCsv("/test/impex/testYFormDefinitionAutosave.csv", "utf-8");
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);

		getModelService().detachAll();
	}

	@Test
	public void getYFormDefinitionForProduct()
	{
		// Test_Return_All_The_YFormDefinitionData_That_Relate_To_All_Of_The_Supercategories_Of_The_Product

		verifyReturnsAllYFormDefinitionDataForProduct("Product1", "applicationId:formId2", "applicationId:formId4",
				"applicationId:formId3", "applicationId:formId1");
		verifyReturnsAllYFormDefinitionDataForProduct("Product2", "applicationId:formId2", "applicationId:formId1");
	}

	@Test
	public void getYFormDefinitionsForUnexistingProductThrowsException()
	{
		// Test_Exception_Thrown_When_Get_YFormDefinitionData_That_Relate_To_All_Of_The_Supercategories_Of_A_Product_That_Does_Not_Exist

		verifyThrowsExceptionWhenProductDoesNotExistWhenGetYFormDefinitionDataForProduct("Product3");
	}

	@Test
	public void getYFormDataByRefId()
	{
		// Test_Get_Y_Form_Data_By_Ref_Id

		verifyYFormDataRetrievedByRefId("ref1", "44ba893a657df5595c228bc60c0a195baa5e8be6",
				"44ba893a657df5595c228bc60c0a191234324324");
	}

	/**
	 * Verify YForm Data List is returned by refId
	 *
	 * @param refId
	 * @param expectedFormDataIds
	 */
	private void verifyYFormDataRetrievedByRefId(final String refId, final String... expectedFormDataIds)
	{
		final List<YFormDataModel> yFormDataModels = yformService.getYFormDataByRefId(refId);
		assertNotNull("YFormDataData is not null", yFormDataModels);
		assertEquals(expectedFormDataIds.length, yFormDataModels.size());

		final List<String> actualFormDataIds = new ArrayList<String>();

		for (final YFormDataModel yFormDataModel : yFormDataModels)
		{
			actualFormDataIds.add(yFormDataModel.getId());
		}

		final Collection disjunction = CollectionUtils.disjunction(actualFormDataIds, Arrays.asList(expectedFormDataIds));
		assertTrue("Different set of YFormData has been returned by refId [" + refId + "] than expected", disjunction.isEmpty());
	}
}

