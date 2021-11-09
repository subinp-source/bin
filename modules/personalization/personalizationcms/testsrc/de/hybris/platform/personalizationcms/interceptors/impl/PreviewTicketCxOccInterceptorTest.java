/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationcms.interceptors.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.personalizationservices.voters.CxOccInterceptorVote;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class PreviewTicketCxOccInterceptorTest
{

	@Mock
	private SessionService sessionService;

	@Mock
	private HttpServletRequest httpServletRequest;

	private PreviewTicketCxOccInterceptor previewTicketCxOccInterceptor;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		previewTicketCxOccInterceptor = new PreviewTicketCxOccInterceptor();
		previewTicketCxOccInterceptor.setSessionService(sessionService);
	}

	@Test
	public void shouldReturnConclusiveVoteWhenPreviewTicketInSession()
	{
		//given

		when(sessionService.getAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM)).thenReturn("PreviewTicketID");

		//when

		final CxOccInterceptorVote cxOccInterceptorVote = previewTicketCxOccInterceptor
				.shouldPersonalizeRequestVote(httpServletRequest);

		//then

		assertTrue(cxOccInterceptorVote.isConclusive());
		assertTrue(cxOccInterceptorVote.isVote());
	}

	@Test
	public void shouldReturnDefaultVoteWhenNoPreviewTicketInSession()
	{
		//given

		when(sessionService.getAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM)).thenReturn(null);

		//when

		final CxOccInterceptorVote cxOccInterceptorVote = previewTicketCxOccInterceptor
				.shouldPersonalizeRequestVote(httpServletRequest);

		//then

		assertFalse(cxOccInterceptorVote.isConclusive());
		assertTrue(cxOccInterceptorVote.isVote());
	}
}
