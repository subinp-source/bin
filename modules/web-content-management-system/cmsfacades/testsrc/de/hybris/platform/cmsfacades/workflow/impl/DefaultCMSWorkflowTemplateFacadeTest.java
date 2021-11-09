/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowTemplateService;
import de.hybris.platform.cmsfacades.data.WorkflowTemplateData;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSWorkflowTemplateFacadeTest
{

	private static final String CATALOG_ID = "catalogID";
	private static final String VERSION_NAME = "versionName";

	private final String WORKFLOW_TEMPLATE_1_CODE = "workflowTemplate1Code";
	private final String WORKFLOW_TEMPLATE_1_NAME = "workflowTemplate1Name";

	private final String WORKFLOW_TEMPLATE_2_CODE = "workflowTemplate2Code";
	private final String WORKFLOW_TEMPLATE_2_NAME = "workflowTemplate2Name";

	@InjectMocks
	private DefaultCMSWorkflowTemplateFacade workflowTemplateFacade;

	@Mock
	private CatalogVersionService catalogVersionService;

	@Mock
	private CMSWorkflowTemplateService cmsWorkflowTemplateService;

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private WorkflowTemplateModel workflowTemplate1;

	@Mock
	private WorkflowTemplateModel workflowTemplate2;

	@Before
	public void setup()
	{
		when(catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_NAME)).thenReturn(catalogVersion);

		when(workflowTemplate1.getCode()).thenReturn(WORKFLOW_TEMPLATE_1_CODE);
		when(workflowTemplate1.getName()).thenReturn(WORKFLOW_TEMPLATE_1_NAME);

		when(workflowTemplate2.getCode()).thenReturn(WORKFLOW_TEMPLATE_2_CODE);
		when(workflowTemplate2.getName()).thenReturn(WORKFLOW_TEMPLATE_2_NAME);
	}

	@Test
	public void getWorkflowTemplatesReturnsEmptyWhenNoTemplatesForCatalogVersion()
	{

		// WHEN
		final List<WorkflowTemplateData> result = workflowTemplateFacade.getWorkflowTemplates(CATALOG_ID, VERSION_NAME);

		// THEN
		assertThat(result, empty());
	}

	@Test
	public void getWorkflowTemplatesReturnsTemplatesForCatalogVersion()
	{

		// GIVEN
		when(cmsWorkflowTemplateService.getVisibleWorkflowTemplatesForCatalogVersion(catalogVersion))
				.thenReturn(Arrays.asList(workflowTemplate1, workflowTemplate2));

		// WHEN
		final List<WorkflowTemplateData> result = workflowTemplateFacade.getWorkflowTemplates(CATALOG_ID, VERSION_NAME);

		// THEN
		assertThat(result,
				hasItems(
						allOf(hasProperty(WorkflowTemplateModel.CODE, equalTo(WORKFLOW_TEMPLATE_1_CODE)),
								hasProperty(WorkflowTemplateModel.NAME, equalTo(WORKFLOW_TEMPLATE_1_NAME))),
						allOf(hasProperty(WorkflowTemplateModel.CODE, equalTo(WORKFLOW_TEMPLATE_2_CODE)),
								hasProperty(WorkflowTemplateModel.NAME, equalTo(WORKFLOW_TEMPLATE_2_NAME))) //
				));
	}

}
