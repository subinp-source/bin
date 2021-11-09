/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.services.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.data.CmsPageRequestContextData;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.servlet.ServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


/**
 * Unit test for {@link DefaultCMSPageContextService}
 */
@UnitTest
public class DefaultCMSPageContextServiceTest
{

	private static final String SESSION_ID = "sessionid";
	private static final String PREVIEW_TICKET_ID = "previewTicketId";

	@Spy
	private final DefaultCMSPageContextService cmsPageContextService = new DefaultCMSPageContextService();
	private CmsPageRequestContextData contextData;

	@Mock
	private ServletRequest request;
	@Mock
	private CMSPreviewService cmsPreviewService;
	@Mock
	private SessionService sessionService;
	@Mock
	private CMSPreviewTicketModel previewTicket;
	@Mock
	private PreviewDataModel previewData;
	@Mock
	private Session session;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		cmsPageContextService.setCmsPreviewService(cmsPreviewService);
		cmsPageContextService.setSessionService(sessionService);

		contextData = new CmsPageRequestContextData();

		given(cmsPageContextService.lookupCmsPageRequestContextData(request)).willReturn(contextData);
		given(cmsPageContextService.getPreviewTicketId(request)).willReturn(PREVIEW_TICKET_ID);
		given(cmsPreviewService.getPreviewTicket(PREVIEW_TICKET_ID)).willReturn(previewTicket);
		when(previewTicket.getPreviewData()).thenReturn(previewData);
		when(sessionService.getCurrentSession()).thenReturn(session);
		when(session.getSessionId()).thenReturn(SESSION_ID);
	}

	@Test
	public void test_initialiseCmsPageContextForRequest_with_storefronPreviewDisabled()
	{

		when(cmsPageContextService.isStoreFrontPreviewEnabled()).thenReturn(false);

		final CmsPageRequestContextData result = cmsPageContextService.initialiseCmsPageContextForRequest(request);
		Assert.assertNull(result.getPreviewData());
		Assert.assertFalse(result.isPreview());
	}

	@Test
	public void test_initialiseCmsPageContextForRequest_with_storefronPreviewEnabled()
	{
		when(cmsPageContextService.isStoreFrontPreviewEnabled()).thenReturn(true);

		final CmsPageRequestContextData result = cmsPageContextService.initialiseCmsPageContextForRequest(request);
		Assert.assertNotNull(result.getPreviewData());
		Assert.assertTrue(result.isPreview());
	}

}
