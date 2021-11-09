/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.test.TestEmployeeModel
import de.hybris.platform.core.model.user.UserGroupModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class AttributeReadabilityIntegrationTest extends ServicelayerTransactionalSpockSpecification {
	private static final String IO_NAME = 'EmployeeIO'
	private static final String EMPLOYEE_UID = 'testIntegrationEmployee'
	private static final String GROUP_UID = 'testIntegrationGroup'

	@Resource(name = 'oDataContextGenerator')
	private ODataContextGenerator contextGenerator
	@Resource(name = "defaultODataFacade")
	private ODataFacade facade

	def setup() {
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
				"                               ; $IO_NAME         ; INBOUND",
				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)   ; root[default = false]",
				"                                   ; $IO_NAME                              ; TestEmployee       ; TestEmployee ; true",
				"                                   ; $IO_NAME                              ; UserGroup          ; UserGroup    ;",
				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
				"                                            ; $IO_NAME:UserGroup                                                 ; uid                         ; UserGroup:uid                                      ;                                                           ; true",
				"                                            ; $IO_NAME:TestEmployee                                              ; uid                         ; TestEmployee:uid                                   ;                                                           ; true",
				"                                            ; $IO_NAME:TestEmployee                                              ; groups                      ; TestEmployee:groups                                ; $IO_NAME:UserGroup                                        ;                          ; true",
				"                                            ; $IO_NAME:TestEmployee                                              ; name                        ; TestEmployee:name                                  ;                                                           ;",
				"                                            ; $IO_NAME:TestEmployee                                              ; password                    ; TestEmployee:password                              ;                                                           ;"
		)
	}

	def cleanup() {
		IntegrationTestUtil.removeAll IntegrationObjectModel
		IntegrationTestUtil.removeAll TestEmployeeModel
		IntegrationTestUtil.remove UserGroupModel, { group -> (group.uid == GROUP_UID) }
	}

	@Test
	def "GET on an integration object item with a non-readable attribute returns 200 and the non-readable attribute is not provided in the payload"() {
		given:
		importImpEx(
				"INSERT_UPDATE UserGroup; uid[unique = true]",
				"                              ; $GROUP_UID",
				"INSERT_UPDATE TestEmployee; uid[unique = true]  ; name            ; password   ; groups(uid)",
				"                          ; $EMPLOYEE_UID       ; test employee 1 ; welcome123 ; $GROUP_UID",
		)
		def context = oDataGetContext('TestEmployees', [:], EMPLOYEE_UID)

		when:
		ODataResponse response = facade.handleRequest(context)
		def getBody = extractBody response as IntegrationODataResponse

		then:
		response.status == HttpStatusCodes.OK
		with(getBody)
				{
					getString('d.uid') == EMPLOYEE_UID
					getString('d.name') == 'test employee 1'
					!getString('d').contains('password')
				}
	}

	@Test
	def "POST on an integration object item with a non-readable attribute returns 201 and the non-readable attribute value show as null"() {
		given:
		def postBody = json()
				.withField('uid', EMPLOYEE_UID)
				.withField('password', 'welcome123')
				.withFieldValues("groups", json().withField('uid', GROUP_UID).build())
				.build()
		def context = oDataPostContext 'TestEmployees', postBody

		when:
		ODataResponse response = facade.handleRequest(context)
		def getBody = extractBody response as IntegrationODataResponse

		then:
		response.status == HttpStatusCodes.CREATED
		with(getBody)
				{
					getString('d.uid') == EMPLOYEE_UID
					!getString('d.name')
					!getString('d.password')
				}
	}

	ODataContext oDataGetContext(String entitySetName) {
		oDataGetContext(entitySetName, [:])
	}

	JsonObject extractBody(IntegrationODataResponse response) {
		JsonObject.createFrom response.entityAsStream
	}

	ODataContext oDataGetContext(String entitySetName, Map params, String... keys) {
		def pathInfo = PathInfoBuilder.pathInfo()
				.withServiceName(IO_NAME)
				.withEntitySet(entitySetName)

		if (keys.length > 0) {
			pathInfo.withEntityKeys(keys)
		}

		def request = ODataRequestBuilder.oDataGetRequest()
				.withAccepts(APPLICATION_JSON_VALUE)
				.withPathInfo(pathInfo)
				.withParameters(params)
				.build()

		contextGenerator.generate request
	}

	ODataContext oDataPostContext(String entitySetName, String content) {
		def request = ODataFacadeTestUtils
				.oDataPostRequest(IO_NAME, entitySetName, content, APPLICATION_JSON_VALUE)

		contextGenerator.generate request
	}
}
