/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices

import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static de.hybris.platform.outboundservices.OAuthClientDetailsBuilder.oAuthClientDetailsBuilder

class ConsumedOAuthCredentialBuilder extends AbstractCredentialBuilder<ConsumedOAuthCredentialBuilder, ConsumedOAuthCredentialModel> {

	private static final String DEFAULT_ID = 'testOauthCredential'
	private static final String DEFAULT_PASSWORD = 'testSecret'

	private OAuthClientDetailsModel clientDetails

	static ConsumedOAuthCredentialBuilder consumedOAuthCredentialBuilder() {
		new ConsumedOAuthCredentialBuilder()
	}

	ConsumedOAuthCredentialBuilder withClientDetails(OAuthClientDetailsBuilder builder) {
		withClientDetails builder.build()
	}

	ConsumedOAuthCredentialBuilder withClientDetails(OAuthClientDetailsModel clientDetails) {
		this.clientDetails = clientDetails
		this
	}

	ConsumedOAuthCredentialModel build() {
		consumedOAuthCredential(id, clientDetails, password)
	}

	private static ConsumedOAuthCredentialModel consumedOAuthCredential(String id, OAuthClientDetailsModel clientDetails, String password) {
		def idVal = deriveId(id)
		importImpEx(
				'INSERT_UPDATE ConsumedOAuthCredential	; id[unique = true]    	; clientId     										; oAuthUrl                                         	; clientSecret ',
				"     											; $idVal           		; ${deriveClientDetails(clientDetails).clientId}	; ${deriveClientDetails(clientDetails).OAuthUrl}	;${derivePassword(password)}")
		getOAuthCredentialById(idVal)
	}

	private static String deriveId(String id) {
		id ?: DEFAULT_ID
	}

	private static OAuthClientDetailsModel deriveClientDetails(OAuthClientDetailsModel clientDetails) {
		clientDetails ?: oAuthClientDetailsBuilder().build()
	}

	private static String derivePassword(String secret) {
		secret ?: DEFAULT_PASSWORD
	}

	private static ConsumedOAuthCredentialModel getOAuthCredentialById(String id) {
		IntegrationTestUtil.findAny(ConsumedOAuthCredentialModel, { it.id == id }).orElse(null)
	}
}
