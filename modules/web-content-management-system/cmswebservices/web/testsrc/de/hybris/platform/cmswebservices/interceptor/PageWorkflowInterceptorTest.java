/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.interceptor;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_PAGE_ID;
import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_SLOT_ID;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.workflow.service.CMSWorkflowAttachmentService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PageWorkflowInterceptorTest
{
	private static final String HTTP_REQUEST_DELETE_METHOD = "DELETE";
	private static final String HTTP_REQUEST_GET_METHOD = "GET";
	private static final String HTTP_REQUEST_PUT_METHOD = "PUT";
	private static final String INVALID = "invalid";
	private static final String PAGE_ID = "mock-page-id";
	private static final String SLOT_ID = "mock-slot-id";

	@InjectMocks
	private PageWorkflowInterceptor interceptor;

	@Mock
	private CMSAdminPageService cmsAdminPageService;
	@Mock
	private CMSAdminContentSlotService cmsAdminContentSlotService;
	@Mock
	private CMSWorkflowAttachmentService cmsWorkflowAttachmentService;

	@Mock
	private MockHttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private Object handler;
	@Mock
	private AbstractPageModel pageModel;
	@Mock
	private ContentSlotModel contentSlotModel;
	@Mock
	private ContentSlotForPageModel contentSlotForPageRelation;

	@Test
	public void shouldSkipPreHandleForGetRequest() throws Exception
	{
		when(request.getMethod()).thenReturn(HTTP_REQUEST_GET_METHOD);

		final boolean result = interceptor.preHandle(request, response, handler);

		assertTrue(result);
		verifyZeroInteractions(cmsAdminPageService, cmsAdminContentSlotService, cmsWorkflowAttachmentService);
	}

	@Test
	public void shouldSkipPreHandleWhenRequestNotContainPageAndSlotId() throws Exception
	{
		when(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(Collections.emptyMap());
		when(request.getMethod()).thenReturn(HTTP_REQUEST_PUT_METHOD);

		final boolean result = interceptor.preHandle(request, response, handler);

		assertTrue(result);
		verifyZeroInteractions(cmsAdminPageService, cmsAdminContentSlotService, cmsWorkflowAttachmentService);
	}

	@Test
	public void shouldSkipPreHandleWhenPutRequestHasInvalidPageId() throws Exception
	{
		final Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put(URI_PAGE_ID, INVALID);

		when(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathVariables);
		when(request.getMethod()).thenReturn(HTTP_REQUEST_PUT_METHOD);
		when(cmsAdminPageService.getPageForIdFromActiveCatalogVersion(INVALID))
				.thenThrow(new UnknownIdentifierException("invalid page id"));

		final boolean result = interceptor.preHandle(request, response, handler);

		assertTrue(result);
		verify(cmsAdminPageService).getPageForIdFromActiveCatalogVersion(INVALID);
		verifyZeroInteractions(cmsAdminContentSlotService, cmsWorkflowAttachmentService);
	}

	@Test
	public void shouldSkipPreHandleWhenDeleteRequestHasInvalidContentSlotId() throws Exception
	{
		final Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put(URI_SLOT_ID, INVALID);

		when(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathVariables);
		when(request.getMethod()).thenReturn(HTTP_REQUEST_DELETE_METHOD);
		when(cmsAdminContentSlotService.getContentSlotForId(INVALID)).thenThrow(new UnknownIdentifierException("invalid slot id"));

		final boolean result = interceptor.preHandle(request, response, handler);

		assertTrue(result);
		verify(cmsAdminContentSlotService).getContentSlotForId(INVALID);
		verifyZeroInteractions(cmsAdminPageService, cmsWorkflowAttachmentService);
	}

	@Test
	public void shouldPassPreHandleForNonAttachedItemPutRequest() throws Exception
	{
		final Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put(URI_PAGE_ID, PAGE_ID);

		when(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathVariables);
		when(request.getMethod()).thenReturn(HTTP_REQUEST_PUT_METHOD);
		when(cmsAdminPageService.getPageForIdFromActiveCatalogVersion(PAGE_ID)).thenReturn(pageModel);
		when(cmsWorkflowAttachmentService.validateAttachmentAndParticipant(response, pageModel)).thenReturn(TRUE);

		final boolean result = interceptor.preHandle(request, response, handler);

		assertTrue(result);
		verify(cmsAdminPageService).getPageForIdFromActiveCatalogVersion(PAGE_ID);
		verify(cmsWorkflowAttachmentService).validateAttachmentAndParticipant(response, pageModel);
	}

	@Test
	public void shouldPassPreHandleForNonAttachedItemDeleteRequest() throws Exception
	{
		final Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put(URI_SLOT_ID, SLOT_ID);

		when(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathVariables);
		when(request.getMethod()).thenReturn(HTTP_REQUEST_DELETE_METHOD);
		when(cmsAdminContentSlotService.getContentSlotForId(SLOT_ID)).thenReturn(contentSlotModel);
		when(cmsAdminContentSlotService.getOnlyContentSlotRelationsForSlot(contentSlotModel))
				.thenReturn(Collections.singletonList(contentSlotForPageRelation));
		when(contentSlotForPageRelation.getPage()).thenReturn(pageModel);
		when(cmsWorkflowAttachmentService.validateAttachmentsAndParticipant(response, Collections.singletonList(pageModel)))
				.thenReturn(TRUE);

		final boolean result = interceptor.preHandle(request, response, handler);

		assertTrue(result);
		verify(cmsAdminContentSlotService).getContentSlotForId(SLOT_ID);
		verify(cmsWorkflowAttachmentService).validateAttachmentsAndParticipant(response, Collections.singletonList(pageModel));
	}
}