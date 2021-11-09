/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.workflow.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSWorkflowActionServiceTest
{
	public static final String INVALID_ID = "invalid-id";
	public static final String ACTION_ID = "action1-id";
	public static final String ACTION2_ID = "action2-id";
	public static final String ACTION3_ID = "action3-id";
	public static final String DECISION_ID = "decision-id";
	public static final String DECISION2_ID = "decision2-id";

	@InjectMocks
	private DefaultCMSWorkflowActionService workflowActionService;

	@Mock
	private WorkflowModel workflow;
	@Mock
	private WorkflowActionModel action;
	@Mock
	private WorkflowActionModel action2;
	@Mock
	private WorkflowActionModel action3;
	@Mock
	private WorkflowDecisionModel decision;
	@Mock
	private WorkflowDecisionModel decision2;

	@Test
	public void testFindWorkflowActionForCode()
	{
		when(workflow.getActions()).thenReturn(Arrays.asList(action, action2, action3));
		when(action.getCode()).thenReturn(ACTION_ID);
		when(action2.getCode()).thenReturn(ACTION2_ID);

		final WorkflowActionModel result = workflowActionService.getWorkflowActionForCode(workflow, ACTION2_ID);

		assertThat(result, equalTo(action2));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testFailFindWorkflowActionForInvalidCode()
	{
		when(workflow.getActions()).thenReturn(Arrays.asList(action, action2, action3));
		when(action.getCode()).thenReturn(ACTION_ID);
		when(action2.getCode()).thenReturn(ACTION2_ID);
		when(action3.getCode()).thenReturn(ACTION3_ID);

		final WorkflowActionModel result = workflowActionService.getWorkflowActionForCode(workflow, INVALID_ID);
	}

	@Test
	public void testFindActionDecisionForCode()
	{
		when(action.getDecisions()).thenReturn(Arrays.asList(decision, decision2));
		when(decision.getCode()).thenReturn(DECISION_ID);

		final WorkflowDecisionModel result = workflowActionService.getActionDecisionForCode(action, DECISION_ID);

		assertThat(result, equalTo(decision));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testFailFindActionDecisionForInvalidCode()
	{
		when(action.getDecisions()).thenReturn(Arrays.asList(decision, decision2));
		when(decision.getCode()).thenReturn(DECISION_ID);
		when(decision2.getCode()).thenReturn(DECISION2_ID);

		final WorkflowDecisionModel result = workflowActionService.getActionDecisionForCode(action, INVALID_ID);
	}

}
