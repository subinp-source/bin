/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices


import de.hybris.platform.apiregistryservices.model.BasicCredentialModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx

class BasicCredentialBuilder extends AbstractCredentialBuilder<BasicCredentialBuilder, BasicCredentialModel> {

	private static final String DEFAULT_ID = 'testBasicCredential'
	private static final String DEFAULT_USERNAME = 'test'
	private static final String DEFAULT_PASSWORD = 'test'

	private String username

	static BasicCredentialBuilder basicCredentialBuilder() {
		new BasicCredentialBuilder()
	}

	BasicCredentialBuilder withUsername(String username) {
		this.username = username
		this
	}

	BasicCredentialModel build() {
		basicCredentialModel(id, username, password)
	}

	private static BasicCredentialModel basicCredentialModel(String id, String username, String password) {
		def idVal = deriveId(id)
		importImpEx(
				'INSERT_UPDATE BasicCredential; id[unique = true]; username                   ; password',
				"                             ; $idVal           ; ${deriveUsername(username)}; ${derivePassword(password)}")
		getBasicCredentialById(idVal)
	}

	private static String deriveId(String id) {
		id ?: DEFAULT_ID
	}

	private static String deriveUsername(String name) {
		name ?: DEFAULT_USERNAME
	}

	private static String derivePassword(String pwd) {
		pwd ?: DEFAULT_PASSWORD
	}

	private static BasicCredentialModel getBasicCredentialById(String id) {
		IntegrationTestUtil.findAny(BasicCredentialModel, { it.id == id }).orElse(null)
	}
}
