/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.controllers;

import de.hybris.platform.jalo.user.CookieBasedLoginToken;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.smartedit.dto.SSOCredentials;
import de.hybris.platform.util.Config;
import de.hybris.smartedit.facade.SSOAuthenticationFacade;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Controller used to generate OAuth2 token based on SSO authentication
 */
@RestController
@RequestMapping("/authenticate")
@Api(tags = "authentications")
public class SSOAuthenticationController
{
	private static final String SSO_COOKIE_NAME = "sso.cookie.name";

	@Resource(name = "smarteditSSOAuthenticationFacade")
	private SSOAuthenticationFacade authenticationFacade;

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation( //
			value = "Create OAuth token from single sign-on authentication", //
			notes = "Endpoint to create an OAuth token from a single sign-on authentication",
			nickname = "doGenerateOAuthTokenFromSSO")
	public OAuth2AccessToken generateOAuthTokenFromSSO(final HttpServletRequest request, //
			@ApiParam(value = "Single sign-on credentials", required = true) //
			@RequestBody
			final SSOCredentials credentials)
	{
		final LoginToken token = new CookieBasedLoginToken(getSamlCookie(request));
		return authenticationFacade.generateOAuthTokenForUser(credentials.getClient_id(), token.getUser().getUid());
	}

	protected Cookie getSamlCookie(final HttpServletRequest request)
	{
		final String cookieName = Config.getString(SSO_COOKIE_NAME, null);
		return cookieName != null ? WebUtils.getCookie(request, cookieName) : null;
	}
}
