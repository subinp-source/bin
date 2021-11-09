/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.workflow.service.impl;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSWorkflowParticipantServiceTest
{
	@InjectMocks
	private DefaultCMSWorkflowParticipantService workflowParticipantService;

	@Mock
	private CMSWorkflowService cmsWorkflowService;
	@Mock
	private UserService userService;

	@Mock
	private WorkflowModel workflow;
	@Mock
	private WorkflowTemplateModel workflowTemplate;
	@Mock
	private UserModel user;
	@Mock
	private UserGroupModel userGroup;
	@Mock
	private CMSItemModel item;

	@Before
	public void setUp()
	{
		when(workflow.getJob()).thenReturn(workflowTemplate);

		when(userService.getCurrentUser()).thenReturn(user);
		when(user.getAllGroups()).thenReturn(Collections.singleton(userGroup));

		when(workflow.getJob()).thenReturn(workflowTemplate);
		when(workflowTemplate.getVisibleForPrincipals()).thenReturn(Collections.singleton(userGroup));
	}

	@Test
	public void shouldBeWorkflowParticipant()
	{
		// WHEN
		final boolean isParticipant = workflowParticipantService.isWorkflowParticipant(workflow);

		// THEN
		assertTrue(isParticipant);
	}

	@Test
	public void shouldNotBeWorkflowParticipant()
	{
		// GIVEN
		when(workflowTemplate.getVisibleForPrincipals()).thenReturn(Collections.emptyList());

		// WHEN
		final boolean isParticipant = workflowParticipantService.isWorkflowParticipant(workflow);

		// THEN
		assertFalse(isParticipant);
	}

	@Test
	public void shouldBeParticipantForAttachedItems()
	{
		when(cmsWorkflowService.findAllWorkflowsByAttachedItems(any(), any())).thenReturn(Collections.singletonList(workflow));

		final boolean isParticipantForAttachedItems = workflowParticipantService
				.isParticipantForAttachedItems(Collections.singletonList(item));

		assertTrue(isParticipantForAttachedItems);
	}

	@Test
	public void shouldBeParticipantForNonAttachedItems()
	{
		when(cmsWorkflowService.findAllWorkflowsByAttachedItems(any(), any())).thenReturn(Collections.emptyList());

		final boolean isParticipantForAttachedItems = workflowParticipantService
				.isParticipantForAttachedItems(Collections.singletonList(item));

		assertTrue(isParticipantForAttachedItems);
	}

	@Test
	public void shouldNotBeParticipantForAttachedItems()
	{
		when(cmsWorkflowService.findAllWorkflowsByAttachedItems(any(), any())).thenReturn(Collections.singletonList(workflow));
		when(workflowTemplate.getVisibleForPrincipals()).thenReturn(Collections.emptyList());

		final boolean isParticipantForAttachedItems = workflowParticipantService
				.isParticipantForAttachedItems(Collections.singletonList(item));

		assertFalse(isParticipantForAttachedItems);
	}

	@Test
	public void shouldGetRelatedPrincipals()
	{
		assertThat(workflowParticipantService.getRelatedPrincipals(user), hasSize(2));
	}
}
