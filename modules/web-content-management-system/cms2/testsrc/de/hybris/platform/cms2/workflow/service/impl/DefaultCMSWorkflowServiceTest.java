/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.workflow.service.impl;

import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_ACTIVE_STATUSES;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relateditems.RelatedItemsService;
import de.hybris.platform.cms2.servicelayer.daos.CMSWorkflowDao;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSWorkflowServiceTest
{
	private List<CMSItemModel> cmsItems;

	@Mock
	private RelatedItemsService relatedItemsService;

	@Mock
	private WorkflowModel workflowModel1;

	@Mock
	private WorkflowModel workflowModel2;

	@Mock
	private CMSItemModel item;

	@Mock
	private CMSItemModel contextItem;

	@Mock
	private AbstractPageModel page;

	@Mock
	private CMSWorkflowDao cmsWorkflowDao;

	@Mock
	private CMSWorkflowParticipantService cmsWorkflowParticipantService;

	@InjectMocks
	private DefaultCMSWorkflowService cmsWorkflowService;

	@Before
	public void setUp()
	{
		cmsItems = Collections.singletonList(item);
	}

	@Test
	public void givenItemsAreNotPartOfAnyWorkflow_WhenIsAnyItemInWorkflowIsCalled_ThenItReturnsFalse()
	{
		// GIVEN
		when(cmsWorkflowDao.findAllWorkflowsByAttachedItems(cmsItems, CMS_WORKFLOW_ACTIVE_STATUSES))
				.thenReturn(Collections.emptyList());

		// WHEN
		final boolean result = cmsWorkflowService.isAnyItemInWorkflow(cmsItems);

		// THEN
		assertFalse(result);
	}

	@Test
	public void givenOneItemIsPartOfAWorkflow_WhenIsAnyItemInWorkflowIsCalled_ThenItReturnsTrue()
	{
		// GIVEN
		when(cmsWorkflowDao.findAllWorkflowsByAttachedItems(cmsItems, CMS_WORKFLOW_ACTIVE_STATUSES))
				.thenReturn(Collections.singletonList(workflowModel1));

		// WHEN
		final boolean result = cmsWorkflowService.isAnyItemInWorkflow(cmsItems);

		// THEN
		assertTrue(result);
	}

	@Test
	public void givenItemThatIsPartOfWorkflow_ThenItCanOnlyBeEditedByThisWorkflow()
	{
		// GIVEN
		final List<CMSItemModel> relatedItems = new ArrayList<>();
		relatedItems.add(item);
		relatedItems.add(page);

		when(relatedItemsService.getRelatedItems(item)).thenReturn(relatedItems);
		when(cmsWorkflowDao.findAllWorkflowsByAttachedItems(relatedItems, CMS_WORKFLOW_ACTIVE_STATUSES)).thenReturn(Arrays.asList(
				workflowModel1));

		// WHEN
		final List<WorkflowModel> itemRelatedWorkflows = cmsWorkflowService
				.getRelatedWorkflowsForItem(item, CMS_WORKFLOW_ACTIVE_STATUSES);

		// THEN
		assertThat(itemRelatedWorkflows, hasSize(1));
	}

	@Test
	public void givenItemIsNotPartOfAnyWorkflow_TheListOfRelatedWorkflowsIsEmpty()
	{
		// GIVEN
		when(relatedItemsService.getRelatedItems(item)).thenReturn(Collections.emptyList());

		// WHEN
		final List<WorkflowModel> itemRelatedWorkflows = cmsWorkflowService
				.getRelatedWorkflowsForItem(item, CMS_WORKFLOW_ACTIVE_STATUSES);

		// THEN
		assertThat(itemRelatedWorkflows, empty());
	}

	@Test
	public void givenItemIsPartOfTwoWorkflows_WhenGetWorkflowWhereItemEditableCalled_ThenTheOldestWorkflowIsReturned()
	{
		// GIVEN
		final List<CMSItemModel> cmsItemModels = Arrays.asList(item);
		when(relatedItemsService.getRelatedItems(item)).thenReturn(cmsItemModels);
		when(cmsWorkflowDao.findAllWorkflowsByAttachedItems(cmsItemModels, CMS_WORKFLOW_ACTIVE_STATUSES))
				.thenReturn(Arrays.asList(workflowModel1, workflowModel2));
		final Date workflow1CreationDate = new Date();
		when(workflowModel1.getCreationtime()).thenReturn(workflow1CreationDate);
		final Date workflow2CreationDate = new Date(workflow1CreationDate.getTime() + 1);
		when(workflowModel2.getCreationtime()).thenReturn(workflow2CreationDate);

		// WHEN
		final Optional<WorkflowModel> workflowWhereItemEditable = cmsWorkflowService.getWorkflowWhereItemEditable(item);

		// THEN
		assertTrue(workflowWhereItemEditable.isPresent());
		assertThat(workflowWhereItemEditable.get(), is(workflowModel1));
	}

	@Test
	public void shouldBeEditableIfItemIsPartOfWorkflowAndCurrentUserIsParticipantOfCurrentAction()
	{
		// GIVEN
		prepareItemWorkflow(Arrays.asList(workflowModel1));
		when(cmsWorkflowParticipantService.isActiveWorkflowActionParticipant(workflowModel1)).thenReturn(true);

		// WHEN
		final boolean isEditable = cmsWorkflowService.isItemEditableBySessionUser(item);

		// THEN
		assertTrue(isEditable);
	}

	@Test
	public void shouldItemToBeNotEditableIfItemIsPartOfWorkflowAndCurrentUserIsNotParticipantOfCurrentAction()
	{
		// GIVEN
		prepareItemWorkflow(Arrays.asList(workflowModel1));
		when(cmsWorkflowParticipantService.isActiveWorkflowActionParticipant(workflowModel1)).thenReturn(false);

		// WHEN
		final boolean isEditable = cmsWorkflowService.isItemEditableBySessionUser(item);

		// THEN
		assertFalse(isEditable);
	}

	@Test
	public void shouldBeEditableIfItemIsNotPartOfWorkflow()
	{
		// GIVEN
		prepareItemWorkflow(Arrays.asList());

		// WHEN
		final boolean isEditable = cmsWorkflowService.isItemEditableBySessionUser(item);

		// THEN
		assertTrue(isEditable);
	}

	protected void prepareItemWorkflow(final List<WorkflowModel> resultWorkflows)
	{
		final List<CMSItemModel> itemModels = Collections.singletonList(item);
		when(relatedItemsService.getRelatedItems(item)).thenReturn(itemModels);
		when(cmsWorkflowDao.findAllWorkflowsByAttachedItems(itemModels, CMS_WORKFLOW_ACTIVE_STATUSES))
				.thenReturn(resultWorkflows);
	}
}
