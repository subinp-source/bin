/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.interceptor;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_PAGE_ID;
import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_SLOT_ID;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.CMSRelationModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.workflow.service.CMSWorkflowAttachmentService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Default interceptor to run before the Pages controller's execution to verify if the affected page is already attached
 * to a workflow. If the page is already a workflow attachment, users should not be able to modify it anymore in order
 * to preserve a consistent state. In such case, we return an error in the HTTP response using the status code 409 -
 * Conflict.
 */
public class PageWorkflowInterceptor extends HandlerInterceptorAdapter
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PageWorkflowInterceptor.class);

	private CMSAdminPageService cmsAdminPageService;
	private CMSAdminContentSlotService cmsAdminContentSlotService;
	private CMSWorkflowAttachmentService cmsWorkflowAttachmentService;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws IOException
	{
		final boolean isTargetedRequestMethod = Stream.of(POST.name(), PUT.name(), DELETE.name())
				.anyMatch(request.getMethod()::equalsIgnoreCase);

		if (isTargetedRequestMethod)
		{
			final String pageId = resolveValueFromRequest(request, URI_PAGE_ID);
			final String slotId = findValueFromRequestUrl(request, URI_SLOT_ID);
			try
			{
				if (StringUtils.isNotEmpty(pageId))
				{
					final AbstractPageModel pageModel = getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(pageId);
					return getCmsWorkflowAttachmentService().validateAttachmentAndParticipant(response, pageModel);
				}
				// No pageId is passed for DELETE requests; find the related pages from the ContentSlotForPage relations
				else if (DELETE.name().equalsIgnoreCase(request.getMethod()) && StringUtils.isNotEmpty(slotId))
				{
					final ContentSlotModel contentSlotModel = getCmsAdminContentSlotService().getContentSlotForId(slotId);
					final Collection<CMSRelationModel> slotRelations = getCmsAdminContentSlotService()
							.getOnlyContentSlotRelationsForSlot(contentSlotModel);

					final List<AbstractPageModel> relatedPages = slotRelations.stream() //
							.map(relation -> (ContentSlotForPageModel) relation) //
							.map(ContentSlotForPageModel::getPage) //
							.collect(Collectors.toList());
					return getCmsWorkflowAttachmentService().validateAttachmentsAndParticipant(response, relatedPages);
				}
			}
			catch (final UnknownIdentifierException | AmbiguousIdentifierException e)
			{
				LOGGER.debug("Skipping interceptor due to invalid pageId and/or slotId.", e);
			}
		}
		return true;
	}

	/**
	 * Retrieves the value associated to the given key from the provided request using the payload or the URL.
	 *
	 * @param request
	 *           - the HTTP request containing the request body and URL
	 * @param key
	 *           - the key used to extract the desired value
	 * @return the value associated to the provided key; can be <tt>null</tt>
	 */
	protected String resolveValueFromRequest(final HttpServletRequest request, final String key)
	{
		String value = null;
		switch (request.getMethod())
		{
			case "POST":
				// Get the value from the request body
				value = findValueFromRequestPayload(request, key);
				break;
			case "PUT":
				// Get the value from the request URL
				value = findValueFromRequestUrl(request, key);
				break;
			default:
				break;
		}
		return value;
	}

	/**
	 * Retrieves the value associated to the given key from the provided request payload.
	 *
	 * @param request
	 *           - the HTTP request containing the request body
	 * @param key
	 *           - the key used to extract the desired value
	 * @return the value associated to the provided key; can be <tt>null</tt>
	 */
	protected String findValueFromRequestPayload(final HttpServletRequest request, final String key)
	{
		try
		{
			final HashMap bodyData = new ObjectMapper().readValue(request.getInputStream(), HashMap.class);
			return (String) bodyData.get(key);
		}
		catch (final IOException e)
		{
			LOGGER.info("Failed to read the pageId from the POST request payload", e);
		}
		return null;
	}

	/**
	 * Retrieves the value associated to the given key from the provided request URL: path or query parameter.
	 *
	 * @param request
	 *           - the HTTP request containing the request URL
	 * @param key
	 *           - the key used to extract the desired value
	 * @return the value associated to the provided key; can be <tt>null</tt>
	 */
	protected String findValueFromRequestUrl(final HttpServletRequest request, final String key)
	{
		final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		return pathVariables.get(key);
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

	protected CMSAdminContentSlotService getCmsAdminContentSlotService()
	{
		return cmsAdminContentSlotService;
	}

	@Required
	public void setCmsAdminContentSlotService(final CMSAdminContentSlotService cmsAdminContentSlotService)
	{
		this.cmsAdminContentSlotService = cmsAdminContentSlotService;
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