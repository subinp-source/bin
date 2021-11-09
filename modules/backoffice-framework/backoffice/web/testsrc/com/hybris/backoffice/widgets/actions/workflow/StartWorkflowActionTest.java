/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.actions.workflow;

import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.function.Function;
import java.util.function.Predicate;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.hybris.backoffice.workflow.renderer.actionexecutors.WorkflowStartActionExecutor;
import com.hybris.backoffice.workflow.renderer.predicates.StartWorkflowActionPredicate;


public class StartWorkflowActionTest extends AbstractWorkflowActionTest<StartWorkflowAction>
{

	@Mock
	WorkflowStartActionExecutor workflowStartActionExecutor;
	@Mock
	StartWorkflowActionPredicate startWorkflowActionPredicate;

	@Spy
	@InjectMocks
	private final StartWorkflowAction action = new StartWorkflowAction();

	@Override
	public StartWorkflowAction getActionInstance()
	{
		return action;
	}

	@Override
	public Function<WorkflowModel, Boolean> getWorkflowMockExecutor()
	{
		return workflowStartActionExecutor;
	}

	@Override
	public Predicate<WorkflowModel> getWorkflowMockPredicate()
	{
		return startWorkflowActionPredicate;
	}

}
