/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.daos.impl;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;



@IntegrationTest
public class DefaultCMSWorkflowTemplateDaoIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String CATALOG_ID = "cms_Catalog";
	private static final String CATALOG_VERSION_2_NAME = "CatalogVersion2";
	private static final String CATALOG_VERSION_3_NAME = "CatalogVersion3";

	private static final String WORKFLOW_TEMPLATE_1_CODE = "WorkflowTemplate1";
	private static final String WORKFLOW_TEMPLATE_1_NAME = "Workflow Template 1";

	private static final String WORKFLOW_TEMPLATE_2_CODE = "WorkflowTemplate2";
	private static final String WORKFLOW_TEMPLATE_2_NAME = "Workflow Template 2";

	@Resource
	private DefaultCMSWorkflowTemplateDao cmsWorkflowTemplateDao;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private UserService userService;

	private CatalogVersionModel catalogVersion2;
	private CatalogVersionModel catalogVersion3;
	private PrincipalModel user;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/cmsWorkflowsTestData.impex", "UTF-8");

		catalogVersion2 = catalogVersionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION_2_NAME);
		catalogVersion3 = catalogVersionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION_3_NAME);
		user = userService.getUserForUID("cmsmanager");
	}

	@Test
	public void shouldFindVisibleWorkflowTemplatesForProvidedCatalogVersion()
	{
		final List<WorkflowTemplateModel> workflowTemplates = cmsWorkflowTemplateDao
				.getVisibleWorkflowTemplatesForCatalogVersion(catalogVersion2, user);

		assertThat(workflowTemplates,
				hasItems(
						allOf(hasProperty(WorkflowTemplateModel.CODE, equalTo(WORKFLOW_TEMPLATE_1_CODE)),
								hasProperty(WorkflowTemplateModel.NAME, equalTo(WORKFLOW_TEMPLATE_1_NAME))),
						allOf(hasProperty(WorkflowTemplateModel.CODE, equalTo(WORKFLOW_TEMPLATE_2_CODE)),
								hasProperty(WorkflowTemplateModel.NAME, equalTo(WORKFLOW_TEMPLATE_2_NAME))) //
				));
	}

	@Test
	public void shouldReturnEmptyListIfNoWorkflowTemplatesVisibleToUser()
	{
		user = userService.getUserForUID("cmsreader");

		final List<WorkflowTemplateModel> workflowTemplates = cmsWorkflowTemplateDao
				.getVisibleWorkflowTemplatesForCatalogVersion(catalogVersion2, user);

		assertThat(workflowTemplates, empty());
	}

	@Test
	public void shouldReturnEmptyListIfNoWorkflowTemplatesForProvidedCatalogVersion()
	{
		final List<WorkflowTemplateModel> workflowTemplates = cmsWorkflowTemplateDao
				.getVisibleWorkflowTemplatesForCatalogVersion(catalogVersion3, user);

		assertThat(workflowTemplates, empty());
	}

}
