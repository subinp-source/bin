/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.service;

import de.hybris.platform.cms2.model.contents.CMSItemModel;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;


/**
 * Service to manage workflow attachments.
 */
public interface CMSWorkflowAttachmentService
{
	/**
	 * Determines if at least one of the item models is an attachment to an active workflow.
	 *
	 * @param cmsItems
	 *           - the CMS item models
	 * @return {@code TRUE} when at least one of the items is an attachment in a workflow that is still in progress;
	 *         {@code FALSE} otherwise.
	 */
	boolean isWorkflowAttachedItems(List<? extends CMSItemModel> cmsItems);

	/**
	 * Validates that the current user is a workflow participant to an active workflow that contains the given item. In
	 * the case that the given item is attached to a workflow that is still in progress and the current session user is
	 * not a participant in that workflow, an error in the HTTP response with the status {@link HttpStatus#CONFLICT} is
	 * returned.
	 *
	 * @param response
	 *           - the HTTP servlet response containing the error message if the items are attached to an active workflow
	 * @param cmsItem
	 *           - the CMS item model
	 * @return {@code TRUE} when the item is not attached to any workflow or when the current user is a participant to
	 *         the active workflow containing the given item as attachment; {@code FALSE} otherwise.
	 * @throws IOException
	 *            when an error occurs while raising the error
	 */
	<T extends CMSItemModel> boolean validateAttachmentAndParticipant(HttpServletResponse response, T cmsItem) throws IOException;

	/**
	 * Validates that the current user is a workflow participant to all active workflows that contain all given items. In
	 * the case that at least one of the given item is attached to a workflow that is still in progress and the current
	 * session user is not a participant in that workflow, an error in the HTTP response with the status
	 * {@link HttpStatus#CONFLICT} is returned.
	 *
	 * @param response
	 *           - the HTTP servlet response containing the error message if the items are attached to an active workflow
	 * @param cmsItems
	 *           - the CMS item models
	 * @return {@code TRUE} when the items are not attached to any workflow or when the current user is a participant to
	 *         all active workflows containing the given items as attachments; {@code FALSE} otherwise.
	 * @throws IOException
	 *            when an error occurs while raising the error
	 */
	boolean validateAttachmentsAndParticipant(HttpServletResponse response, List<? extends CMSItemModel> cmsItems)
			throws IOException;

}
