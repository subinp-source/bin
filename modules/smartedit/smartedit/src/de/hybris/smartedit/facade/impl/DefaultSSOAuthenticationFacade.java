/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.facade.impl;

import de.hybris.platform.webservicescommons.oauth2.token.provider.HybrisOAuthTokenServices;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;

import de.hybris.smartedit.facade.SSOAuthenticationFacade;

/**
 * Implementation of {@link SSOAuthenticationFacade}
 */
public class DefaultSSOAuthenticationFacade implements SSOAuthenticationFacade
{
	private HybrisOAuthTokenServices hybrisOAuthTokenServices;
	private OAuth2RequestFactory smarteditOAuth2RequestFactory;
	private ClientDetailsService clientDetailsService;
	private UserDetailsService userDetailsService;

	@Override
	public OAuth2AccessToken generateOAuthTokenForUser(final String oAuth2ClientId, final String userId)
	{
		assert StringUtils.isNotBlank(oAuth2ClientId);
		assert StringUtils.isNotBlank(userId);

		final ClientDetails clientDetails = getClientDetailsService().loadClientByClientId(oAuth2ClientId);
		final OAuth2Authentication authentication = new OAuth2Authentication(createOAuth2Request(clientDetails),
				createUsernamePasswordAuthenticationToken(userId));

		return getHybrisOAuthTokenServices().createAccessToken(authentication);
	}

	/**
	 * Method used to create {@link UsernamePasswordAuthenticationToken} object, basing on user id
	 *
	 * @param userId
	 * @return
	 */
	private UsernamePasswordAuthenticationToken createUsernamePasswordAuthenticationToken(final String userId)
	{
		final UserDetails userDetails = getUserDetailsService().loadUserByUsername(userId);

		return new UsernamePasswordAuthenticationToken(userId, null, userDetails.getAuthorities());
	}

	/**
	 * Method used to create {@link OAuth2Request} object, basing on OAuth2 client id
	 *
	 * @param clientDetails
	 * @return
	 */
	private OAuth2Request createOAuth2Request(final ClientDetails clientDetails)
	{
		final TokenRequest storedRequest = getSmarteditOAuth2RequestFactory().createTokenRequest(Collections.emptyMap(),
				clientDetails);

		return storedRequest.createOAuth2Request(clientDetails);
	}

	@Required
	public void setHybrisOAuthTokenServices(final HybrisOAuthTokenServices hybrisOAuthTokenServices)
	{
		this.hybrisOAuthTokenServices = hybrisOAuthTokenServices;
	}

	protected HybrisOAuthTokenServices getHybrisOAuthTokenServices()
	{
		return hybrisOAuthTokenServices;
	}

	@Required
	public void setSmarteditOAuth2RequestFactory(final OAuth2RequestFactory smarteditOAuth2RequestFactory)
	{
		this.smarteditOAuth2RequestFactory = smarteditOAuth2RequestFactory;
	}

	protected OAuth2RequestFactory getSmarteditOAuth2RequestFactory()
	{
		return smarteditOAuth2RequestFactory;
	}

	@Required
	public void setClientDetailsService(final ClientDetailsService clientDetailsService)
	{
		this.clientDetailsService = clientDetailsService;
	}

	protected ClientDetailsService getClientDetailsService()
	{
		return clientDetailsService;
	}

	@Required
	public void setUserDetailsService(final UserDetailsService userDetailsService)
	{
		this.userDetailsService = userDetailsService;
	}

	protected UserDetailsService getUserDetailsService()
	{
		return userDetailsService;
	}

}
