/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.facade;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * Facade used to generate OAuth2 authentication token basing on SSO user id. Token is necessary to login to smartedit
 * application
 */
public interface SSOAuthenticationFacade
{
	/**
	 * Method used to generate OAuth2 authentication token basing on SSO user id.
	 *
	 * @param userId
	 * @return DTO with OAuth2 token
	 */
	OAuth2AccessToken generateOAuthTokenForUser(String clientId, String userId);
}
