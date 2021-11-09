/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class BackofficeWorkflowServiceTest
{
	private static final int START_INDEX = 0;
	private static final int PAGE_SIZE = 5;
	private static final Date TODAY = toDate(LocalDateTime.now());
	private static final Date TOMORROW = toDate(LocalDateTime.now().plusDays(1));

	@Spy
	private BackofficeWorkflowService workflowService = new BackofficeWorkflowService();

	private EnumSet<WorkflowStatus> allWorkflowStatuses = EnumSet.allOf(WorkflowStatus.class);

	private static Date toDate(final LocalDateTime localDateTime)
	{
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	@Test
	public void testAllWorkflowsWhenDateRangeIncorrect()
	{
		//when
		final List<WorkflowModel> workflows = workflowService.getAllWorkflows(allWorkflowStatuses, TOMORROW, TODAY);

		//then
		verify(workflowService, times(1)).dateToBeforeDateFrom(TOMORROW, TODAY);
		assertThat(workflows).isEmpty();
	}

	@Test
	public void testAllWorkflowsPageWhenDateRangeIncorrect()
	{
		//when
		final SearchResult<WorkflowModel> workflows = workflowService.getAllWorkflows(allWorkflowStatuses, TOMORROW, TODAY,
				START_INDEX, PAGE_SIZE);

		//then
		verify(workflowService, times(1)).dateToBeforeDateFrom(TOMORROW, TODAY);
		assertThat(workflows.getResult()).isEmpty();
	}

	@Test
	public void testAllAdhocWorkflowsWhenDateRangeIncorrect()
	{
		//when
		final List<WorkflowModel> adhocWorkflows = workflowService.getAllAdhocWorkflows(allWorkflowStatuses, TOMORROW, TODAY);

		//then
		verify(workflowService, times(1)).dateToBeforeDateFrom(TOMORROW, TODAY);
		assertThat(adhocWorkflows).isEmpty();
	}

	@Test
	public void testAllAdhocWorkflowsPageWhenDateRangeIncorrect()
	{
		//when
		final SearchResult<WorkflowModel> adhocWorkflows = workflowService.getAllAdhocWorkflows(allWorkflowStatuses, TOMORROW,
				TODAY, START_INDEX, PAGE_SIZE);

		//then
		verify(workflowService, times(1)).dateToBeforeDateFrom(TOMORROW, TODAY);
		assertThat(adhocWorkflows.getResult()).isEmpty();
	}

}
