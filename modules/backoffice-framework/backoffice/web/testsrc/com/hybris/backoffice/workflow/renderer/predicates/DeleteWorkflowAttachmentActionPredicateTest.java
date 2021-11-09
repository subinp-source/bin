/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.renderer.predicates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.workflow.WorkflowFacade;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class DeleteWorkflowAttachmentActionPredicateTest
{
	@Mock
	private WorkflowFacade facade;
	@Mock
	private WorkflowItemAttachmentModel workflowItemAttachment;
	@Mock
	private WorkflowModel workflow;

	@InjectMocks
	private DeleteWorkflowAttachmentActionPredicate predicate;

	@Before
	public void setUp()
	{
		when(workflowItemAttachment.getWorkflow()).thenReturn(workflow);
	}

	@Test
	public void testPredicateWhenWorkflowIsPlanned()
	{
		//given
		when(facade.getWorkflowStatus(workflow)).thenReturn(WorkflowStatus.PLANNED);

		//when
		final boolean result = predicate.test(workflowItemAttachment);

		//then
		assertThat(result).isTrue();
	}

	@Test
	public void testPredicateWhenWorkflowIsRunning()
	{
		//given
		when(facade.getWorkflowStatus(workflow)).thenReturn(WorkflowStatus.RUNNING);

		//when
		final boolean result = predicate.test(workflowItemAttachment);

		//then
		assertThat(result).isFalse();
	}

	@Test
	public void testCanNotPredicateWhenWorkflowIsNotInPreparedStatus()
	{
		//given
		final EnumSet<WorkflowStatus> workflowStatuses = WorkflowStatus.getAll();
		final List<WorkflowStatus> list = workflowStatuses.stream().filter(status -> !status.equals(WorkflowStatus.PLANNED))
				.collect(Collectors.toList());

		for (final WorkflowStatus workflowStatus : list)
		{
			//given
			final WorkflowModel workflow = mock(WorkflowModel.class);
			when(facade.getWorkflowStatus(workflow)).thenReturn(workflowStatus);
			//when
			final boolean result = predicate.test(workflowItemAttachment);
			//then
			assertThat(result).isFalse();
		}
	}

}