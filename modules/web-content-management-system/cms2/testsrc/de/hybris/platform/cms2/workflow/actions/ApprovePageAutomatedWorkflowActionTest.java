/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.workflow.actions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ApprovePageAutomatedWorkflowActionTest
{

	@InjectMocks
	private ApprovePageAutomatedWorkflowAction automatedAction;
	@Mock
	private ModelService modelService;
	@Mock
	private Predicate<ItemModel> pageTypePredicate;
	@Mock
	private PlatformTransactionManager transactionManager;

	@Mock
	private WorkflowActionModel action;
	@Mock
	private WorkflowModel workflow;
	@Mock
	private WorkflowDecisionModel decision;
	@Mock
	private WorkflowItemAttachmentModel itemAttachment;
	@Mock
	private WorkflowItemAttachmentModel contentPageAttachment;
	@Mock
	private WorkflowItemAttachmentModel abstractPageAttachment;
	@Mock
	private ItemModel item;
	@Mock
	private ContentPageModel contentPage;
	@Mock
	private AbstractPageModel abstractPage;

	@Before
	public void setUp()
	{
		when(action.getWorkflow()).thenReturn(workflow);
		when(workflow.getAttachments()).thenReturn(Arrays.asList(itemAttachment, contentPageAttachment, abstractPageAttachment));
		when(itemAttachment.getItem()).thenReturn(item);
		when(contentPageAttachment.getItem()).thenReturn(contentPage);
		when(abstractPageAttachment.getItem()).thenReturn(abstractPage);
		when(pageTypePredicate.test(item)).thenReturn(Boolean.FALSE);
		when(pageTypePredicate.test(contentPage)).thenReturn(Boolean.TRUE);
		when(pageTypePredicate.test(abstractPage)).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testUpdatePageStatusAndReturnDecision()
	{
		// GIVEN
		when(action.getDecisions()).thenReturn(Arrays.asList(decision));

		// WHEN
		final WorkflowDecisionModel result = automatedAction.perform(action);

		// THEN
		verify(modelService).saveAll(Sets.newHashSet(contentPage, abstractPage));
		assertThat(result, equalTo(decision));
	}

	@Test
	public void testUpdatePageStatusAndReturnNoDecision()
	{
		// GIVEN
		when(action.getDecisions()).thenReturn(Collections.emptyList());

		// WHEN
		final WorkflowDecisionModel result = automatedAction.perform(action);

		// THEN
		verify(modelService).saveAll(Sets.newHashSet(contentPage, abstractPage));
		assertThat(result, nullValue());
	}

	@Test
	public void testNotUpdatePageStatusForNonPageItems()
	{
		// GIVEN
		when(pageTypePredicate.test(any())).thenReturn(Boolean.FALSE);

		// WHEN
		automatedAction.perform(action);

		// THEN
		verify(modelService).saveAll(Collections.emptySet());
	}

}
