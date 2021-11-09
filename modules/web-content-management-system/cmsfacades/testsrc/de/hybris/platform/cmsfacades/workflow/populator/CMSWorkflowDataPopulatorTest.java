/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.populator;

import static java.lang.Boolean.TRUE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSWorkflowDataPopulatorTest
{
	private final String WORKFLOW_CODE = "some code";
	private final String WORKFLOW_DESCRIPTION = "some description";

	private CronJobStatus cronJobStatus;

	@Mock
	private WorkflowModel workflowModel;
	@Mock
	private CMSWorkflowData workflowData;

	@Mock
	private CMSWorkflowParticipantService cmsWorkflowParticipantService;
	@Mock
	private WorkflowTemplateService workflowTemplateService;

	@InjectMocks
	private CMSWorkflowDataPopulator cmsWorkflowDataPopulator;

	@Before
	public void setUp()
	{
		cronJobStatus = CronJobStatus.RUNNING;

		when(workflowModel.getCode()).thenReturn(WORKFLOW_CODE);
		when(workflowModel.getStatus()).thenReturn(cronJobStatus);
		when(workflowModel.getDescription()).thenReturn(WORKFLOW_DESCRIPTION);

		when(cmsWorkflowParticipantService.isWorkflowParticipant(workflowModel)).thenReturn(TRUE);
	}

	@Test
	public void WhenPopulateIsCalled_ThenItAddsAllTheRequiredInformation()
	{
		// WHEN
		cmsWorkflowDataPopulator.populate(workflowModel, workflowData);

		// THEN
		verify(workflowData).setWorkflowCode(WORKFLOW_CODE);
		verify(workflowData).setDescription(WORKFLOW_DESCRIPTION);
		verify(workflowData).setStatus(cronJobStatus.getCode());
		verify(workflowData).setIsAvailableForCurrentPrincipal(TRUE);
	}

}
