/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.security;

import static com.hybris.backoffice.security.BackofficeRememberMeAuthenticationFilter.REMEMBER_ME_AUTH_FAILED_PARAM;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.hybris.platform.servicelayer.session.SessionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.RememberMeServices;


@RunWith(MockitoJUnitRunner.class)
public class BackofficeRememberMeAuthenticationFilterTest
{

	@Mock
	private RememberMeServices rememberMeServices;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private SessionService sessionService;

	@InjectMocks
	private BackofficeRememberMeAuthenticationFilter filter;

	@Test
	public void shouldSetSessionParameterForRememberMeAuthFailure()
	{
		// given
		final HttpServletRequest request = mock(HttpServletRequest.class);
		final HttpServletResponse response = mock(HttpServletResponse.class);
		final AuthenticationException exception = mock(AuthenticationException.class);
		final HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);

		// when
		filter.onUnsuccessfulAuthentication(request, response, exception);

		// then
		verify(session).setAttribute(REMEMBER_ME_AUTH_FAILED_PARAM, true);
	}

	@Test
	public void shouldCloseSessionWhenRememberMeAuthFails()
	{
		// given
		final HttpServletRequest request = mock(HttpServletRequest.class);
		final HttpServletResponse response = mock(HttpServletResponse.class);
		final AuthenticationException exception = mock(AuthenticationException.class);
		final HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);

		// when
		filter.onUnsuccessfulAuthentication(request, response, exception);

		// then
		verify(sessionService).closeCurrentSession();
	}
}
