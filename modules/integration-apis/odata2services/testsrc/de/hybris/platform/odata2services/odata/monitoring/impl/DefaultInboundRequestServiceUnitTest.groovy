/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.odata2services.odata.monitoring.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.user.UserModel
import de.hybris.platform.inboundservices.model.InboundRequestErrorModel
import de.hybris.platform.inboundservices.model.InboundRequestMediaModel
import de.hybris.platform.inboundservices.model.InboundRequestModel
import de.hybris.platform.integrationservices.enums.IntegrationRequestStatus
import de.hybris.platform.integrationservices.enums.HttpMethod
import de.hybris.platform.odata2services.odata.monitoring.ResponseChangeSetEntity
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.user.UserService
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.odata.monitoring.InboundRequestServiceParameter.inboundRequestServiceParameter
import static de.hybris.platform.odata2services.odata.monitoring.RequestBatchEntityBuilder.requestBatchEntity
import static de.hybris.platform.odata2services.odata.monitoring.ResponseChangeSetEntityBuilder.responseChangeSetEntity

@UnitTest
class DefaultInboundRequestServiceUnitTest extends Specification {

	private static final def INTEGRATION_OBJECT_TYPE = 'IOType'
	private static final def INTEGRATION_KEY = 'key'
	
	@Shared
	def error = Stub(InboundRequestErrorModel)
	def modelService = Mock(ModelService)
	def userService = Mock(UserService)

	def inboundRequestService = new DefaultInboundRequestService()

	def setup() {
		inboundRequestService.setModelService(modelService)
		inboundRequestService.setUserService(userService)
	}

	@Test
	@Unroll
	def "register #condition"() {
		given:
		def requests = requests(msgIdResponsesMap)
		def responses = msgIdResponsesMap.values().flatten().toList() as List<ResponseChangeSetEntity>
		def param = inboundRequestServiceParameter()
				.withRequests(requests)
				.withResponses(responses)
				.withMedias(payloads)
				.withHttpMethod(HttpMethod.POST)
				.build()

		when:
		inboundRequestService.register(param)

		then:
		modelService.saveAll(_) >> { args ->
			def inboundRequests = args[0] as List<InboundRequestModel>
			assert inboundRequests.size() == expectedResults.size()
			expectedResults.eachWithIndex { expected, i ->
				def inboundRequest = inboundRequests.get(i)
				with(inboundRequest) {
					messageId == expected['msgId']
					type == INTEGRATION_OBJECT_TYPE
					integrationKey == INTEGRATION_KEY
					status == expected['status']
					payload == expected['payload']
					httpMethod == HttpMethod.POST
					errors == expected['error']
				}
			}
		}

		where:
		condition 													| msgIdResponsesMap 																				| payloads 		| expectedResults
		"one request with one success response" 					| ['1':[successResponse()]] 																		| [media()]		| [[msgId:'1', status:IntegrationRequestStatus.SUCCESS, payload:payloads[0], error:[].toSet()]]
		"one request with one error response"						| ['1':[errorResponse()]]																			| [media()]		| [[msgId:'1', status:IntegrationRequestStatus.ERROR, payload:payloads[0], error:[error].toSet()]]
		"one request with multiple success reponses" 				| ['1':[successResponse()] * 2]																		| [media()]		| [[msgId:'1', status:IntegrationRequestStatus.SUCCESS, payload:payloads[0], error:[].toSet()]]
		"one request with no payload"								| ['1':[successResponse()]] 																		| [null]		| [[msgId:'1', status:IntegrationRequestStatus.SUCCESS, payload:null, error:[].toSet()]]
		"multiple requests with one error response" 				| ['1':[errorResponse()], '2':[], '3':[]] 															| [media()] * 3	| [[msgId:'1', status:IntegrationRequestStatus.ERROR, payload:payloads[0], error:[error].toSet()]]
		"multiple requests with mix of success and error responses"	| ['1':[successResponse()] * 3, '2':[errorResponse(), successResponse()], '3':[successResponse()]] 	| [media()] * 3 | [[msgId:'1', status:IntegrationRequestStatus.SUCCESS, payload:payloads[0], error:[].toSet()], [msgId:'2', status:IntegrationRequestStatus.ERROR, payload:payloads[1], error:[error].toSet()], [msgId:'3', status:IntegrationRequestStatus.SUCCESS, payload:payloads[2], error:[].toSet()]]
	}

	@Test
	@Unroll
	def "register with #user user when #condition"() {
		given:
		def responses = [successResponse()]
		def requests = requests(['1':responses])
		def param = inboundRequestServiceParameter()
				.withRequests(requests)
				.withResponses(responses)
				.withUserId(user)
				.build()

		when:
		inboundRequestService.register(param)

		then:
		modelService.saveAll(_) >> { args ->
			def inboundRequests = args[0]
			assert inboundRequests.size() == 1
			def inboundRequest = inboundRequests.get(0)
			assert (inboundRequest.user == null) == userModelNull
		}

		and:
		times * userService.getUserForUID(user) >> Stub(UserModel)

		where:
		condition 		| user 		| times | userModelNull
		'user provided'	| 'admin'	| 1		| false
		'user is empty'	| '' 		| 0		| true
		'user is null' 	| null 		| 0		| true
	}

	def media() {
		Stub(InboundRequestMediaModel)
	}

	def requests(Map<String, List<ResponseChangeSetEntity>> msgIdNumChangeSetMap) {
		msgIdNumChangeSetMap.collect { id, responses ->
			requestBatchEntity()
				.withMessageId(id)
				.withIntegrationObjectType(INTEGRATION_OBJECT_TYPE)
				.withNumberOfChangeSets(responses.size() > 0 ? responses.size() : 1)
				.build()
		}.toList()
	}

	def successResponse() {
		responseChangeSetEntity()
			.withIntegrationKey(INTEGRATION_KEY)
			.withStatusCode(201)
			.build()
	}

	def errorResponse() {
		responseChangeSetEntity()
			.withIntegrationKey(INTEGRATION_KEY)
			.withStatusCode(400)
			.withRequestError(error)
			.build()
	}
}
