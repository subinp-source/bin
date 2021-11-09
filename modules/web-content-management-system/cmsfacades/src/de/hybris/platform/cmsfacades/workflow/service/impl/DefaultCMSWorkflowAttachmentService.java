/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.service.impl;

import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_ACTIVE_STATUSES;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.cmsfacades.workflow.service.CMSWorkflowAttachmentService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;


/**
 * Default implementation of {@link CMSWorkflowAttachmentService}
 */
public class DefaultCMSWorkflowAttachmentService implements CMSWorkflowAttachmentService
{
	private CMSWorkflowService cmsWorkflowService;
	private CMSWorkflowParticipantService cmsWorkflowParticipantService;

	@Override
	public boolean isWorkflowAttachedItems(final List<? extends CMSItemModel> cmsItemModels)
	{
		final List<WorkflowModel> workflows = getCmsWorkflowService().findAllWorkflowsByAttachedItems(cmsItemModels,
				CMS_WORKFLOW_ACTIVE_STATUSES);
		return CollectionUtils.isNotEmpty(workflows);
	}

	@Override
	public boolean validateAttachmentsAndParticipant(final HttpServletResponse response,
			final List<? extends CMSItemModel> cmsItems) throws IOException
	{
		final boolean isWorkflowAttachment = isWorkflowAttachedItems(cmsItems);
		final boolean notWorkflowParticipant = !getCmsWorkflowParticipantService().isParticipantForAttachedItems(cmsItems);
		if (isWorkflowAttachment && notWorkflowParticipant)
		{
			response.sendError(HttpStatus.CONFLICT.value(),
					"The item cannot be modified because it is currently part of a workflow that is in progress. "
							+ "Please try again once the workflow is completed or terminated.");
			return false;
		}
		return true;
	}

	@Override
	public <T extends CMSItemModel> boolean validateAttachmentAndParticipant(final HttpServletResponse response, final T cmsItem)
			throws IOException
	{
		return validateAttachmentsAndParticipant(response, Collections.singletonList(cmsItem));
	}

	protected CMSWorkflowService getCmsWorkflowService()
	{
		return cmsWorkflowService;
	}

	@Required
	public void setCmsWorkflowService(final CMSWorkflowService cmsWorkflowService)
	{
		this.cmsWorkflowService = cmsWorkflowService;
	}

	protected CMSWorkflowParticipantService getCmsWorkflowParticipantService()
	{
		return cmsWorkflowParticipantService;
	}

	@Required
	public void setCmsWorkflowParticipantService(final CMSWorkflowParticipantService cmsWorkflowParticipantService)
	{
		this.cmsWorkflowParticipantService = cmsWorkflowParticipantService;
	}
}
