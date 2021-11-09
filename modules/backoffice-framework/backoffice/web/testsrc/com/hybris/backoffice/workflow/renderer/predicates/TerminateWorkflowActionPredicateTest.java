/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.renderer.predicates;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.workflow.WorkflowFacade;


@RunWith(MockitoJUnitRunner.class)
public class TerminateWorkflowActionPredicateTest
{
	@Mock
	private WorkflowFacade workflowFacade;

	@Mock
	private WorkflowModel workflowModel;

	private final TerminateWorkflowActionPredicate predicate = new TerminateWorkflowActionPredicate();

	@Before
	public void setUp()
	{
		predicate.setWorkflowFacade(workflowFacade);
	}

	@Test
	public void testPredicateWhenWorkflowCanBeDeleted()
	{
		// given
		given(workflowFacade.getWorkflowStatus(workflowModel)).willReturn(WorkflowStatus.RUNNING);

		// when
		final boolean decision = predicate.test(workflowModel);

		// then
		assertTrue(decision);
	}

	@Test
	public void testPredicateWhenWorkflowCannotBeDeleted()
	{
		// given
		given(workflowFacade.getWorkflowStatus(workflowModel)).willReturn(WorkflowStatus.TERMINATED);

		// when
		final boolean decision = predicate.test(workflowModel);

		// then
		assertFalse(decision);
	}
}
