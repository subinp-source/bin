/*
    Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.xyformsweb.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;


public class PersistenceAuthHeaderFilter extends OncePerRequestFilter
{
	private String headerName;
	private String headerValue;

	@Override
	protected void doFilterInternal(
			final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse,
			final FilterChain filterChain
	) throws ServletException, IOException
	{
		final String authHeader = httpServletRequest.getHeader(headerName);

		if (isValid(authHeader))
		{
			final Authentication authentication = new PreAuthenticatedAuthenticationToken("system", null);
			authentication.setAuthenticated(true);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private boolean isValid(final String authHeader)
	{
		return authHeader != null && authHeader.equals(headerValue);
	}

	public String getHeaderName()
	{
		return headerName;
	}

	public void setHeaderName(final String headerName)
	{
		this.headerName = headerName;
	}

	public String getHeaderValue()
	{
		return headerValue;
	}

	public void setHeaderValue(final String headerValue)
	{
		this.headerValue = headerValue;
	}
}
