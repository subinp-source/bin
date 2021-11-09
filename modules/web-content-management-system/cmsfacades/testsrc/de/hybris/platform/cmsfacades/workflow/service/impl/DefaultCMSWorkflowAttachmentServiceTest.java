/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.service.impl;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSWorkflowAttachmentServiceTest
{
	@InjectMocks
	private DefaultCMSWorkflowAttachmentService workflowAttachmentService;

	@Mock
	private CMSWorkflowService cmsWorkflowService;
	@Mock
	private CMSWorkflowParticipantService cmsWorkflowParticipantService;

	@Mock
	private HttpServletResponse response;
	@Mock
	private CMSItemModel cmsItem;
	@Mock
	private WorkflowModel workflow;

	@Test
	public void shouldBeFalseWhenItemIsNotAttachedToWorkflow() throws IOException
	{
		when(cmsWorkflowService.findAllWorkflowsByAttachedItems(any(), any())).thenReturn(Collections.emptyList());

		final boolean result = workflowAttachmentService.isWorkflowAttachedItems(Collections.singletonList(cmsItem));

		assertFalse(result);
	}

	@Test
	public void shouldBeTrueWhenItemIsAttachedToWorkflow() throws IOException
	{
		when(cmsWorkflowService.findAllWorkflowsByAttachedItems(any(), any())).thenReturn(Collections.singletonList(workflow));

		final boolean result = workflowAttachmentService.isWorkflowAttachedItems(Collections.singletonList(cmsItem));

		assertTrue(result);
	}

	@Test
	public void shouldBeTrueValidateAttachmentAndParticipantWhenItemIsNotAttachedToWorkflow() throws IOException
	{
		when(cmsWorkflowService.findAllWorkflowsByAttachedItems(any(), any())).thenReturn(Collections.emptyList());
		when(cmsWorkflowParticipantService.isParticipantForAttachedItems(Collections.singletonList(cmsItem))).thenReturn(TRUE);

		final boolean result = workflowAttachmentService.validateAttachmentAndParticipant(response, cmsItem);

		assertTrue(result);
		verifyZeroInteractions(response);
	}

	@Test
	public void shouldBeTrueValidateAttachmentAndParticipantWhenItemIsAttachedToWorkflow() throws IOException
	{
		when(cmsWorkflowService.findAllWorkflowsByAttachedItems(any(), any())).thenReturn(Collections.singletonList(workflow));
		when(cmsWorkflowParticipantService.isParticipantForAttachedItems(Collections.singletonList(cmsItem))).thenReturn(TRUE);

		final boolean result = workflowAttachmentService.validateAttachmentAndParticipant(response, cmsItem);

		assertTrue(result);
		verifyZeroInteractions(response);
	}

	@Test
	public void shouldBeFalseValidateAttachmentAndParticipantWhenItemIsAttachedToWorkflowAndUserIsNotParticipant()
			throws IOException
	{
		when(cmsWorkflowService.findAllWorkflowsByAttachedItems(any(), any())).thenReturn(Collections.singletonList(workflow));
		when(cmsWorkflowParticipantService.isParticipantForAttachedItems(Collections.singletonList(cmsItem))).thenReturn(FALSE);

		final boolean result = workflowAttachmentService.validateAttachmentAndParticipant(response, cmsItem);

		assertFalse(result);
		verify(response).sendError(eq(HttpStatus.CONFLICT.value()), anyString());
	}
}
