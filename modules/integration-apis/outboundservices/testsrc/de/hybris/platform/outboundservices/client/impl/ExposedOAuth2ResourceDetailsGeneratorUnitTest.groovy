/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.client.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ExposedOAuth2ResourceDetailsGeneratorUnitTest extends Specification {

	private static final String TEST_CLIENT = "test_client"
	private static final String TEST_PASSWORD = "test_password"
	private static final String TEST_URL = "test_url"
	def exposedOAuth2ResourceDetailsGenerator = new ExposedOAuth2ResourceDetailsGenerator()

	@Test
	@Unroll
	def "isApplicable should be #applicable when credential is of type #credentialType"() {

		expect:
		exposedOAuth2ResourceDetailsGenerator.isApplicable(credentialType) == applicable

		where:
		credentialType                     | applicable
		new ConsumedOAuthCredentialModel() | false
		new ExposedOAuthCredentialModel()  | true
	}

	@Test
	def "createResourceDetails returns a correctly formed resource details model"() {

		when:
		def details = exposedOAuth2ResourceDetailsGenerator.createResourceDetails(consumedDestination())

		then:
		with(details) {
			getClientId() == TEST_CLIENT
			getClientSecret() == TEST_PASSWORD
			getAccessTokenUri() == TEST_URL
		}
	}

	def consumedDestination() {
		def consumedDestination = new ConsumedDestinationModel()
		def credentialModel = new ExposedOAuthCredentialModel()
		def detailsModel = new OAuthClientDetailsModel()
		detailsModel.setClientId(TEST_CLIENT)
		detailsModel.setOAuthUrl(TEST_URL)
		credentialModel.setOAuthClientDetails(detailsModel)
		credentialModel.setPassword(TEST_PASSWORD)
		consumedDestination.setCredential(credentialModel)
		consumedDestination
	}
}
