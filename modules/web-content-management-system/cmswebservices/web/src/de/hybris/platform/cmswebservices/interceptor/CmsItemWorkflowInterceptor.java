/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.interceptor;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_UUID;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.cmsfacades.workflow.service.CMSWorkflowAttachmentService;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * Default interceptor to run before the controller's execution to verify if the affected CMS item is already attached
 * to a workflow. If the item is already a workflow attachment, users should not be able to modify it anymore in order
 * to preserve a consistent state for the item. In such case, we return an error in the HTTP response using the status
 * code 409 - Conflict.
 */
public class CmsItemWorkflowInterceptor extends HandlerInterceptorAdapter
{
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	private CMSWorkflowAttachmentService cmsWorkflowAttachmentService;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception
	{
		final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		final boolean isTargetedRequestMethod = Stream.of(PUT.name(), DELETE.name())
				.anyMatch(request.getMethod()::equalsIgnoreCase);

		if (Objects.nonNull(pathVariables) && isTargetedRequestMethod)
		{
			final String itemUUID = pathVariables.get(URI_UUID);

			if (StringUtils.isNotEmpty(itemUUID))
			{
				final Optional<CMSItemModel> cmsItemModel = getUniqueItemIdentifierService().getItemModel(itemUUID,
						CMSItemModel.class);

				if (cmsItemModel.isPresent())
				{
					return getCmsWorkflowAttachmentService().validateAttachmentAndParticipant(response, cmsItemModel.get());
				}
			}
		}

		return true;
	}

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

	@Required
	public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}

	protected CMSWorkflowAttachmentService getCmsWorkflowAttachmentService()
	{
		return cmsWorkflowAttachmentService;
	}

	@Required
	public void setCmsWorkflowAttachmentService(final CMSWorkflowAttachmentService cmsWorkflowAttachmentService)
	{
		this.cmsWorkflowAttachmentService = cmsWorkflowAttachmentService;
	}
}