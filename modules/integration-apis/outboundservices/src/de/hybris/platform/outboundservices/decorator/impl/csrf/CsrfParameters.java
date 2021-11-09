/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.decorator.impl.csrf;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.http.HttpHeaders;

import com.google.common.base.Preconditions;

/**
 * Value object for CSRF (cross-site request forgery) token related parameters.
 */
class CsrfParameters
{
	private static final String X_CSRF_TOKEN = "X-CSRF-Token";
	private static final String X_CSRF_SET_COOKIES = "Set-Cookie";
	private static final String COOKIE_SEPARATOR = "; ";

	private final String csrfToken;
	private final String csrfCookie;
	private final int hashCode;

	private CsrfParameters(final String token, final String cookie)
	{
		Preconditions.checkArgument(StringUtils.isNotBlank(token), "Token must be non-blank string");
		Preconditions.checkArgument(StringUtils.isNotBlank(cookie), "Cookies must be non-blank string");
		csrfToken = token;
		csrfCookie = cookie;
		hashCode = new HashCodeBuilder().append(csrfToken).append(csrfCookie).toHashCode();
	}

	public static CsrfParameters create(final HttpHeaders headers) {
		final List<String> tokens = headers.get(X_CSRF_TOKEN);
		final List<String> cookies = headers.get(X_CSRF_SET_COOKIES);
		final String csrfToken = CollectionUtils.isNotEmpty(tokens) ? tokens.get(0) : "";
		final String csrfCookies = StringUtils.join(cookies, COOKIE_SEPARATOR);
		return new CsrfParameters(csrfToken, csrfCookies);
	}

	public String getCsrfToken()
	{
		return csrfToken;
	}

	public String getCsrfCookie()
	{
		return csrfCookie;
	}

	@Override
	public boolean equals(final Object o)
	{
		return  this == o
				|| (o != null && getClass() == o.getClass()	&& equalParameters((CsrfParameters) o));
	}

	private boolean equalParameters(final CsrfParameters o)
	{
		return csrfToken.equals(o.csrfToken) && csrfCookie.equals(o.csrfCookie);
	}

	@Override
	public int hashCode()
	{
		return hashCode;
	}

	@Override
	public String toString()
	{
		return "CsrfParameters{" +
				"token='" + csrfToken + "'" +
				", cookie='" + csrfCookie + "'" +
				'}';
	}
}
