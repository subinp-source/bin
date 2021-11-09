/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.workflow.actions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RejectEditingAutomatedWorkflowActionTest
{
	@InjectMocks
	private RejectEditingAutomatedWorkflowAction automatedAction;

	@Mock
	private WorkflowActionService workflowActionService;

	@Mock
	private WorkflowActionModel action;
	@Mock
	private WorkflowActionModel action1;
	@Mock
	private WorkflowActionModel action2;
	@Mock
	private WorkflowDecisionModel decision;
	@Mock
	private WorkflowModel workflow;

	@Before
	public void setUp()
	{
		when(action.getWorkflow()).thenReturn(workflow);
		when(workflow.getActions()).thenReturn(Arrays.asList(action, action1, action2));

		when(workflowActionService.isActive(action)).thenReturn(true);
		when(workflowActionService.isActive(action1)).thenReturn(false);
		when(workflowActionService.isActive(action2)).thenReturn(true);
	}

	@Test
	public void shouldResetAllActiveActionsAndReturnDecision()
	{
		// GIVEN
		when(action.getDecisions()).thenReturn(Arrays.asList(decision));

		// WHEN
		final WorkflowDecisionModel result = automatedAction.perform(action);

		// THEN
		verify(workflowActionService, never()).idle(action);
		verify(workflowActionService, never()).idle(action1);
		verify(workflowActionService).idle(action2);
		assertThat(result, equalTo(decision));
	}

	@Test
	public void shouldResetAllActiveActionsAndReturnNoDecision()
	{
		// GIVEN
		when(action.getDecisions()).thenReturn(Collections.emptyList());

		// WHEN
		final WorkflowDecisionModel result = automatedAction.perform(action);

		// THEN
		verify(workflowActionService, never()).idle(action);
		verify(workflowActionService, never()).idle(action1);
		verify(workflowActionService).idle(action2);
		assertThat(result, nullValue());
	}

}
