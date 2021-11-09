/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.interceptor;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_UUID;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.cmsfacades.workflow.service.CMSWorkflowAttachmentService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CmsItemWorkflowInterceptorTest
{
	private static final String HTTP_REQUEST_GET_METHOD = "GET";
	private static final String HTTP_REQUEST_PUT_METHOD = "PUT";
	private static final String INVALID = "invalid";
	private static final String UUID = "mock-item-uuid";

	@InjectMocks
	private CmsItemWorkflowInterceptor interceptor;

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	@Mock
	private CMSWorkflowAttachmentService cmsWorkflowAttachmentService;

	@Mock
	private MockHttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private Object handler;
	@Mock
	private CMSItemModel cmsItem;

	@Test
	public void shouldSkipPreHandleForGetRequest() throws Exception
	{
		when(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(Collections.emptyMap());
		when(request.getMethod()).thenReturn(HTTP_REQUEST_GET_METHOD);

		final boolean result = interceptor.preHandle(request, response, handler);

		assertTrue(result);
		verifyZeroInteractions(uniqueItemIdentifierService, cmsWorkflowAttachmentService);
	}

	@Test
	public void shouldSkipPreHandleWhenRequestNotContainUUID() throws Exception
	{
		when(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(Collections.emptyMap());
		when(request.getMethod()).thenReturn(HTTP_REQUEST_PUT_METHOD);

		final boolean result = interceptor.preHandle(request, response, handler);

		assertTrue(result);
		verifyZeroInteractions(uniqueItemIdentifierService, cmsWorkflowAttachmentService);
	}

	@Test
	public void shouldSkipPreHandleWhenRequestContainInvalidUUID() throws Exception
	{
		final Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put(URI_UUID, INVALID);

		when(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathVariables);
		when(request.getMethod()).thenReturn(HTTP_REQUEST_PUT_METHOD);
		when(uniqueItemIdentifierService.getItemModel(INVALID, CMSItemModel.class)).thenReturn(Optional.empty());

		final boolean result = interceptor.preHandle(request, response, handler);

		assertTrue(result);
		verify(uniqueItemIdentifierService).getItemModel(INVALID, CMSItemModel.class);
		verifyZeroInteractions(cmsWorkflowAttachmentService);
	}

	@Test
	public void shouldPassPreHandleForNonAttachedItem() throws Exception
	{
		final Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put(URI_UUID, UUID);

		when(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathVariables);
		when(request.getMethod()).thenReturn(HTTP_REQUEST_PUT_METHOD);
		when(uniqueItemIdentifierService.getItemModel(UUID, CMSItemModel.class)).thenReturn(Optional.of(cmsItem));
		when(cmsWorkflowAttachmentService.validateAttachmentAndParticipant(response, cmsItem)).thenReturn(TRUE);

		final boolean result = interceptor.preHandle(request, response, handler);

		assertTrue(result);
		verify(uniqueItemIdentifierService).getItemModel(UUID, CMSItemModel.class);
		verify(cmsWorkflowAttachmentService).validateAttachmentAndParticipant(response, cmsItem);
	}

	@Test
	public void shouldFailPreHandleForAttachedItem() throws Exception
	{
		final Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put(URI_UUID, UUID);

		when(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathVariables);
		when(request.getMethod()).thenReturn(HTTP_REQUEST_PUT_METHOD);
		when(uniqueItemIdentifierService.getItemModel(UUID, CMSItemModel.class)).thenReturn(Optional.of(cmsItem));
		final List<CMSItemModel> cmsItems = Collections.singletonList(cmsItem);
		when(cmsWorkflowAttachmentService.validateAttachmentAndParticipant(response, cmsItem)).thenReturn(FALSE);

		final boolean result = interceptor.preHandle(request, response, handler);

		assertFalse(result);
		verify(uniqueItemIdentifierService).getItemModel(UUID, CMSItemModel.class);
		verify(cmsWorkflowAttachmentService).validateAttachmentAndParticipant(response, cmsItem);
	}
}
