/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.interceptor;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_PAGE_ID;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.workflow.service.CMSWorkflowAttachmentService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * Default interceptor to run before the PagesRestrictions controller's execution to verify if the affected page is
 * already attached to a workflow. If the page is already a workflow attachment, users should not be able to modify it
 * anymore in order to preserve a consistent state. In such case, we return an error in the HTTP response using the
 * status code 409 - Conflict.
 */
public class PageRestrictionWorkflowInterceptor extends HandlerInterceptorAdapter
{
	private static final Logger LOG = LoggerFactory.getLogger(PageRestrictionWorkflowInterceptor.class);

	private CMSAdminPageService cmsAdminPageService;
	private CMSWorkflowAttachmentService cmsWorkflowAttachmentService;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws IOException
	{
		final boolean isTargetedRequestMethod = PUT.name().equalsIgnoreCase(request.getMethod());

		if (isTargetedRequestMethod)
		{
			final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			final String pageId = pathVariables.get(URI_PAGE_ID);
			try
			{
				if (StringUtils.isNotEmpty(pageId))
				{
					final AbstractPageModel pageModel = getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(pageId);
					return getCmsWorkflowAttachmentService().validateAttachmentAndParticipant(response, pageModel);
				}
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.debug("Skipping interceptor due to invalid pageId.", e);
			}
		}
		return true;
	}

	protected CMSAdminPageService getCmsAdminPageService()
	{
		return cmsAdminPageService;
	}

	@Required
	public void setCmsAdminPageService(final CMSAdminPageService cmsAdminPageService)
	{
		this.cmsAdminPageService = cmsAdminPageService;
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
