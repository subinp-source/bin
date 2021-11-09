/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.populator;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.common.service.TimeDiffService;
import de.hybris.platform.cmsfacades.data.CMSWorkflowActionData;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSWorkflowActionDataPopulatorTest
{

	private final String IS_CURRENT_USER_PARTICIPANT = "isCurrentUserParticipant";

	private final String WORKFLOW_ACTION1_CODE = "action1Code";
	private final String WORKFLOW_ACTION1_NAME = "action1Name";
	private final String WORKFLOW_ACTION1_STATUS = "action1Status";
	private final String WORKFLOW_ACTION1_DESCRIPTION = "action1Description";

	private final String WORKFLOW_DECISION1_CODE = "decision1Code";
	private final String WORKFLOW_DECISION2_CODE = "decision2Code";
	private final String WORKFLOW_DECISION1_NAME = "decision1Name";
	private final String WORKFLOW_DECISION2_NAME = "decision2Name";
	private final String WORKFLOW_DECISION1_DESCRIPTION = "decision1Desc";
	private final String WORKFLOW_DECISION2_DESCRIPTION = "decision2Desc";

	@Mock
	private TimeDiffService timeDiffService;

	@Mock
	private WorkflowActionModel workflowAction1Model;

	@Mock
	private WorkflowDecisionModel workflowDecision1Model;

	@Mock
	private WorkflowDecisionModel workflowDecision2Model;

	@Mock
	private WorkflowTemplateModel workflowTemplate;

	@Mock
	private CMSWorkflowParticipantService cmsWorkflowParticipantService;

	private final CMSWorkflowActionData cmsWorkflowActionData = new CMSWorkflowActionData();

	@InjectMocks
	private CMSWorkflowActionDataPopulator cmsWorkflowActionDataPopulator;

	@Before
	public void setUp()
	{
		when(cmsWorkflowParticipantService.isWorkflowActionParticipant(workflowAction1Model)).thenReturn(true);

		when(workflowAction1Model.getActionType()).thenReturn(WorkflowActionType.START);
		when(workflowAction1Model.getCode()).thenReturn(WORKFLOW_ACTION1_CODE);
		when(workflowAction1Model.getName()).thenReturn(WORKFLOW_ACTION1_NAME);
		when(workflowAction1Model.getDescription()).thenReturn(WORKFLOW_ACTION1_DESCRIPTION);
		when(workflowAction1Model.getStatus()).thenReturn(WorkflowActionStatus.PENDING);
		when(workflowAction1Model.getDecisions()).thenReturn(Arrays.asList(workflowDecision1Model, workflowDecision2Model));

		when(workflowDecision1Model.getCode()).thenReturn(WORKFLOW_DECISION1_CODE);
		when(workflowDecision1Model.getName()).thenReturn(WORKFLOW_DECISION1_NAME);
		when(workflowDecision1Model.getDescription()).thenReturn(WORKFLOW_DECISION1_DESCRIPTION);

		when(workflowDecision2Model.getCode()).thenReturn(WORKFLOW_DECISION2_CODE);
		when(workflowDecision2Model.getName()).thenReturn(WORKFLOW_DECISION2_NAME);
		when(workflowDecision2Model.getDescription()).thenReturn(WORKFLOW_DECISION2_DESCRIPTION);

	}

	@Test
	public void whenPopulateIsCalled_ThenItAddsAllTheRequiredInformation()
	{
		// WHEN
		cmsWorkflowActionDataPopulator.populate(workflowAction1Model, cmsWorkflowActionData);

		// THEN
		assertThat(cmsWorkflowActionData.getActionType(), is(WorkflowActionType.START.name()));
		assertThat(cmsWorkflowActionData.getCode(), is(WORKFLOW_ACTION1_CODE));
		assertThat(cmsWorkflowActionData.getName(), is(WORKFLOW_ACTION1_NAME));
		assertThat(cmsWorkflowActionData.getDescription(), is(WORKFLOW_ACTION1_DESCRIPTION));
		assertThat(cmsWorkflowActionData.getStatus(), is(WorkflowActionStatus.PENDING.name()));
		assertThat(cmsWorkflowActionData.isIsCurrentUserParticipant(), is(true));

		assertThat(cmsWorkflowActionData.getDecisions(), hasItems( //
				allOf(hasProperty(WorkflowDecisionModel.CODE, equalTo(WORKFLOW_DECISION1_CODE))), //
				allOf(hasProperty(WorkflowDecisionModel.CODE, equalTo(WORKFLOW_DECISION2_CODE))), //
				allOf(hasProperty(WorkflowDecisionModel.NAME, equalTo(WORKFLOW_DECISION1_NAME))), //
				allOf(hasProperty(WorkflowDecisionModel.NAME, equalTo(WORKFLOW_DECISION2_NAME))), //
				allOf(hasProperty(WorkflowDecisionModel.DESCRIPTION, equalTo(WORKFLOW_DECISION1_DESCRIPTION))), //
				allOf(hasProperty(WorkflowDecisionModel.DESCRIPTION, equalTo(WORKFLOW_DECISION2_DESCRIPTION))) //
		));

	}

}
