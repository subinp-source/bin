/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.filter;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.constants.Cms2Constants;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cmsocc.redirect.strategies.PageRedirectStrategy;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;

import java.util.Collection;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * JUnit Tests for the CMSPreviewTicketFilter
 */

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSPreviewTicketFilterTest
{
	private static final String CMS_TICKET_ID = "1234";
	private static final String PREVIEW_SITE_UID = "preview-site-uid";
	private static final String BASE_SITE_UID = "base-site-uid";
	private static final Date PREVIEW_DATE = new Date();
	private static final String PREVIEW_PAGE_UID = "preview-page-uid";
	private static final String REQUEST_URL = "/api/someresource";
	private static final String PAGE_LABEL_ID = "pageLabelOrId";
	private static final String REDIRECT_URL = REQUEST_URL + "?" + CMSFilter.PREVIEW_TICKET_ID_PARAM + "=" + CMS_TICKET_ID + "&"
			+ PAGE_LABEL_ID + "=" + PREVIEW_PAGE_UID;

	@Spy
	@InjectMocks
	private CMSPreviewTicketFilter cmsPreviewTicketFilter;

	@Mock
	private CMSPreviewService cmsPreviewService;
	@Mock
	private CommerceCommonI18NService commerceCommonI18NService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private SessionService sessionService;
	@Mock
	private TimeService timeService;
	@Mock
	private PageRedirectStrategy pageRedirectStrategy;

	@Mock
	private HttpServletRequest httpRequest;
	@Mock
	private HttpServletResponse httpResponse;
	@Mock
	private FilterChain filterChain;
	@Mock
	private CMSPreviewTicketModel cmsPreviewTicket;
	@Mock
	private PreviewDataModel previewData;
	@Mock
	private CMSSiteModel previewSite;
	@Mock
	private CMSSiteModel baseSite;
	@Mock
	private LanguageModel previewLanguage;
	@Mock
	private LanguageModel currentLanguage;
	@Mock
	private Collection<CatalogVersionModel> catalogVersions;
	@Mock
	private AbstractPageModel previewPage;
	@Mock
	private ModelService modelService;

	@Before
	public void setup()
	{
		when(httpRequest.getParameter(CMSFilter.PREVIEW_TICKET_ID_PARAM)).thenReturn(CMS_TICKET_ID);
		when(httpRequest.getParameter(PAGE_LABEL_ID)).thenReturn(null);
		when(httpResponse.encodeRedirectURL(REDIRECT_URL)).thenReturn(REDIRECT_URL);

		when(cmsPreviewService.getPreviewTicket(CMS_TICKET_ID)).thenReturn(cmsPreviewTicket);
		when(cmsPreviewTicket.getId()).thenReturn(CMS_TICKET_ID);
		when(cmsPreviewTicket.getPreviewData()).thenReturn(previewData);
		when(previewData.getActiveSite()).thenReturn(previewSite);
		when(previewSite.getUid()).thenReturn(PREVIEW_SITE_UID);

		when(baseSiteService.getCurrentBaseSite()).thenReturn(baseSite);
		when(baseSite.getUid()).thenReturn(BASE_SITE_UID);

		when(previewData.getLanguage()).thenReturn(previewLanguage);
		when(previewData.getTime()).thenReturn(PREVIEW_DATE);
		when(previewData.getCatalogVersions()).thenReturn(catalogVersions);
		when(previewData.getPage()).thenReturn(previewPage);

		when(previewPage.getUid()).thenReturn(PREVIEW_PAGE_UID);

		when(commerceCommonI18NService.getCurrentLanguage()).thenReturn(currentLanguage);

		when(pageRedirectStrategy.shouldRedirect(httpRequest, previewData)).thenReturn(true);
		when(pageRedirectStrategy.getRedirectUrl(httpRequest, previewData)).thenReturn(REDIRECT_URL);
	}

	@Test
	public void shouldPopulateSessionDataWhenTicketIdValid() throws Exception
	{
		// WHEN
		cmsPreviewTicketFilter.doFilterInternal(httpRequest, httpResponse, filterChain);

		// THEN
		verify(sessionService).setAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM, CMS_TICKET_ID);

		verify(commerceCommonI18NService).setCurrentLanguage(previewLanguage);
		verify(timeService).setCurrentTime(PREVIEW_DATE);
		verify(sessionService).setAttribute(Cms2Constants.PREVIEW_TIME, PREVIEW_DATE);
		verify(catalogVersionService).setSessionCatalogVersions(catalogVersions);
		verify(baseSiteService).setCurrentBaseSite(previewSite, false);
		verify(httpResponse).sendRedirect(REDIRECT_URL);
	}

	@Test
	public void shouldNotPopulateSessionDataWhenTicketIdNull() throws Exception
	{
		// GIVEN
		when(httpRequest.getParameter(CMSFilter.PREVIEW_TICKET_ID_PARAM)).thenReturn(null);

		// WHEN
		cmsPreviewTicketFilter.doFilterInternal(httpRequest, httpResponse, filterChain);

		// THEN
		verifyZeroInteractions(sessionService, commerceCommonI18NService, timeService, catalogVersionService, baseSiteService);
	}

	@Test
	public void shouldNotStoreInSessionForMatchingSites() throws Exception
	{
		// GIVEN
		when(baseSite.getUid()).thenReturn(PREVIEW_SITE_UID);

		// WHEN
		cmsPreviewTicketFilter.doFilterInternal(httpRequest, httpResponse, filterChain);

		// THEN
		verify(baseSiteService, never()).setCurrentBaseSite(previewSite, true);
	}

	@Test
	public void shouldNotStoreInSessionForMatchingLanguages() throws Exception
	{
		// GIVEN
		when(commerceCommonI18NService.getCurrentLanguage()).thenReturn(previewLanguage);

		// WHEN
		cmsPreviewTicketFilter.setLanguageInSession(previewLanguage);

		// THEN
		verify(commerceCommonI18NService, never()).setCurrentLanguage(previewLanguage);
	}

	@Test
	public void shouldNotPopulateSessionDataWhenPreviewDataValuesNull() throws Exception
	{
		// GIVEN
		when(previewData.getLanguage()).thenReturn(null);
		when(previewData.getTime()).thenReturn(null);
		when(previewData.getCatalogVersions()).thenReturn(null);
		when(previewData.getActiveSite()).thenReturn(null);

		// WHEN
		cmsPreviewTicketFilter.doFilterInternal(httpRequest, httpResponse, filterChain);

		// THEN
		verify(sessionService, never()).setAttribute(Cms2Constants.PREVIEW_TIME, PREVIEW_DATE);
		verifyZeroInteractions(commerceCommonI18NService, timeService, catalogVersionService, baseSiteService);
	}

	@Test
	public void shouldNotRedirectWhenShouldRedirectFromStrategyReturnsFalse() throws Exception
	{
		// GIVEN
		when(pageRedirectStrategy.shouldRedirect(httpRequest, previewData)).thenReturn(false);

		// WHEN
		cmsPreviewTicketFilter.doFilterInternal(httpRequest, httpResponse, filterChain);

		// THEN
		verifyZeroInteractions(httpResponse);
	}

	@Test
	public void shouldRedirectWhenRequestUrlPageIdIsDifferentThanPreviewDataPageId() throws Exception
	{
		// GIVEN
		when(httpRequest.getParameter(PAGE_LABEL_ID)).thenReturn("page-uid");

		// WHEN
		cmsPreviewTicketFilter.doFilterInternal(httpRequest, httpResponse, filterChain);

		// THEN
		verify(httpResponse).sendRedirect(REDIRECT_URL);
	}
}