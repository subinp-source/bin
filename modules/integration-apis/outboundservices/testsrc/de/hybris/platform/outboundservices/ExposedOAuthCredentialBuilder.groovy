/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices

import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static de.hybris.platform.outboundservices.OAuthClientDetailsBuilder.oAuthClientDetailsBuilder

class ExposedOAuthCredentialBuilder extends AbstractCredentialBuilder<ExposedOAuthCredentialBuilder, ExposedOAuthCredentialModel> {

	private static final String DEFAULT_ID = 'testOauthCredential'
	private static final String DEFAULT_PASSWORD = 'testSecret'

	private OAuthClientDetailsModel clientDetails

	static ExposedOAuthCredentialBuilder exposedOAuthCredentialBuilder() {
		new ExposedOAuthCredentialBuilder()
	}

	ExposedOAuthCredentialBuilder withClientDetails(OAuthClientDetailsBuilder builder) {
		withClientDetails builder.build()
	}

	ExposedOAuthCredentialBuilder withClientDetails(OAuthClientDetailsModel clientDetails) {
		this.clientDetails = clientDetails
		this
	}

	ExposedOAuthCredentialModel build() {
		exposedOAuthCredential(id, clientDetails, password)
	}

	private static ExposedOAuthCredentialModel exposedOAuthCredential(String id, OAuthClientDetailsModel clientDetails, String password) {
		def idVal = deriveId(id)
		importImpEx(
				'INSERT_UPDATE ExposedOAuthCredential; id[unique = true]; oAuthClientDetails                      ; password',
				"                                    ; $idVal           ; ${deriveClientDetails(clientDetails).pk}; ${derivePassword(password)}")
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

	private static ExposedOAuthCredentialModel getOAuthCredentialById(String id) {
		IntegrationTestUtil.findAny(ExposedOAuthCredentialModel, { it.id == id }).orElse(null)
	}
}
