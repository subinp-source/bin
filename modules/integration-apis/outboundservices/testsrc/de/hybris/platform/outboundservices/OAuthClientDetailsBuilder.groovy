/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices


import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx

class OAuthClientDetailsBuilder {
	public static final String DEFAULT_CLIENT_ID = 'testOauthClient'
	public static final String DEFAULT_OAUTH_URL = 'https://oauth.url.for.test/oauth2/api/v1/token'

	private String clientId
	private String url

	static OAuthClientDetailsBuilder oAuthClientDetailsBuilder() {
		new OAuthClientDetailsBuilder()
	}

	OAuthClientDetailsBuilder withClientId(String clientId) {
		this.clientId = clientId
		this
	}

	OAuthClientDetailsBuilder withOAuthUrl(String url) {
		this.url = url
		this
	}

	OAuthClientDetailsModel build() {
		oAuthClientDetails(clientId, url)
	}

	private static OAuthClientDetailsModel oAuthClientDetails(String clientId, String url) {
		def clientIdVal = deriveClientId(clientId)
		importImpEx(
				'INSERT_UPDATE OAuthClientDetails; clientId[unique = true]; oAuthUrl         ; scope',
				"                                ; $clientIdVal           ; ${deriveUrl(url)};      ")
		getOAuthClientDetailsById(clientIdVal)
	}

	private static String deriveClientId(String clientId) {
		clientId ?: DEFAULT_CLIENT_ID
	}

	private static String deriveUrl(String url) {
		url ?: DEFAULT_OAUTH_URL
	}

	private static OAuthClientDetailsModel getOAuthClientDetailsById(String clientId) {
		IntegrationTestUtil.findAny(OAuthClientDetailsModel, { it.clientId == clientId }).orElse(null)
	}
}
