/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.odata2services.odata.monitoring

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.user.UserModel
import de.hybris.platform.inboundservices.model.InboundRequestErrorModel
import de.hybris.platform.inboundservices.model.InboundRequestMediaModel
import de.hybris.platform.integrationservices.enums.HttpMethod
import de.hybris.platform.integrationservices.enums.IntegrationRequestStatus
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.odata.monitoring.RequestBatchEntityBuilder.requestBatchEntity
import static de.hybris.platform.odata2services.odata.monitoring.ResponseChangeSetEntityBuilder.responseChangeSetEntity

@UnitTest
class InboundRequestBuilderUnitTest extends Specification {

	private static final String INTEGRATION_KEY = 'key'
	private static final String REQUEST_INTEGRATION_KEY = "Request$INTEGRATION_KEY"

	@Shared
	def error = Stub(InboundRequestErrorModel)

	@Test
	@Unroll
	def "request cannot be null"() {
		when:
		InboundRequestBuilder.builder().withRequest(null).build()

		then:
		thrown IllegalArgumentException
	}

	@Test
	def "build inbound request with #condition"() {
		given:
		def request = requestBatchEntity()
						.withMessageId('msgId')
						.withIntegrationKey(requestIntKey)
						.withIntegrationObjectType('IOType')
						.build()

		def media = Stub(InboundRequestMediaModel)
		def user = Stub(UserModel)

		when:
		def inboundRequest = InboundRequestBuilder.builder()
								.withRequest(request)
								.withResponse(response)
								.withHttpMethod(HttpMethod.DELETE)
								.withMedia(media)
								.withUser(user)
								.withSapPassport('my-passport')
								.build()

		then:
		with(inboundRequest) {
			messageId == 'msgId'
			type == 'IOType'
			media == media
			httpMethod == HttpMethod.DELETE
			integrationKey == expected['integrationKey']
			user == user
			status == expected['status']
			errors == expected['errors']
			sapPassport == 'my-passport'

		}

		where:
		condition 								| requestIntKey 			| response 							| expected
		'all fields but response'				| REQUEST_INTEGRATION_KEY 	| null								| [integrationKey:REQUEST_INTEGRATION_KEY, status:null, errors:[].toSet()]
		'all fields and successful response'	| null						| successResponse(INTEGRATION_KEY) 	| [integrationKey:INTEGRATION_KEY, status: IntegrationRequestStatus.SUCCESS, errors:[].toSet()]
		'all fields and error response'			| REQUEST_INTEGRATION_KEY 	| errorResponse()					| [integrationKey:REQUEST_INTEGRATION_KEY, status: IntegrationRequestStatus.ERROR, errors:[error].toSet()]
	}

	def successResponse(String key = null) {
		responseChangeSetEntity()
			.withIntegrationKey(key)
			.withStatusCode(201)
			.build()
	}

	def errorResponse(String key = null) {
		responseChangeSetEntity()
			.withIntegrationKey(key)
			.withStatusCode(400)
			.withRequestError(error)
			.build()
	}
}
