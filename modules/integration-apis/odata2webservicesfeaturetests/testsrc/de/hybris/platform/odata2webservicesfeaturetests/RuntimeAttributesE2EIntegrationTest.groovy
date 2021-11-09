/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservicesfeaturetests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.SearchRestrictionModel
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.core.model.user.UserGroupModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.IntegrationObjectModelBuilder
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.odata2webservicesfeaturetests.ws.BasicAuthRequestBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.junit.Test
import spock.lang.Issue

import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.givenUserExistsWithUidAndGroups

@NeedsEmbeddedServer(webExtensions = Odata2webservicesConstants.EXTENSIONNAME)
@IntegrationTest
@Issue("https://cxjira.sap.com/browse/IAPI-4833")
class RuntimeAttributesE2EIntegrationTest extends ServicelayerSpockSpecification {
    private static final String TEST_NAME = "RuntimeAttributesE2E"
    private static final String TEST_IO = "${TEST_NAME}_IO"
    private static final String RUNTIME_ATTRIBUTE_META_IO = "RuntimeAttributeService"
    private static final String INTEGRATION_SERVICE_META_IO = "IntegrationService"
    private static final String USER = "${TEST_NAME}_User"
    private static final String PASSWORD = 'retset'
    private static final String RUNTIME_ATTRIBUTE_NAME = "${TEST_NAME}_runtimeAttribute"
    private static final String RUNTIME_ATTRIBUTE_VALUE = "runtimeValue"
    private static final String CATALOG_ID = "${TEST_NAME}_myTestCatalog"
    private static final Collection CREATED_IOS = [TEST_IO, RUNTIME_ATTRIBUTE_META_IO, INTEGRATION_SERVICE_META_IO]
    private static final Collection userGroups = ["integrationdeletegroup",
                                                  "integrationviewgroup",
                                                  "integrationcreategroup",
                                                  "integrationadmingroup",
                                                  "integrationusergroup",
                                                  "integrationmonitoringgroup",
                                                  "integrationservicegroup",
                                                  "outboundsyncgroup",
                                                  "scriptservicegroup",
                                                  "webhookservicegroup"]
    private static final Collection searchRestrictions = ['inboundMonitoringIntegrationVisibility',
                                                          'outboundMonitoringIntegrationVisibility',
                                                          'integrationServiceVisibility',
                                                          'outboundChannelConfigVisibility',
                                                          'scriptServiceVisibility',
                                                          'webhookServiceVisibility',
                                                          'runtimeAttributeVisibility']

    def setup() {
        importCsv '/impex/essentialdata-runtimeattributes.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2services.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2webservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-integrationservices.impex', 'UTF-8'
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationadmingroup, integrationservicegroup")
    }

    def cleanup() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { icc -> CREATED_IOS.contains icc?.integrationObject?.code }
        IntegrationTestUtil.remove IntegrationObjectModel, { it -> CREATED_IOS.contains it.code }
        IntegrationTestUtil.findAny(EmployeeModel, { it.uid == USER }).ifPresent { IntegrationTestUtil.remove it }
        userGroups.each { user -> IntegrationTestUtil.remove UserGroupModel, { userGroups.contains it.uid } }
        searchRestrictions.each { searchRestriction -> IntegrationTestUtil.remove SearchRestrictionModel, { searchRestrictions.contains it.code } }
    }

    @Test
    def "can POST an IO with a runtime attribute created with the META API"() {
        when: "a runtime attribute is created via the RuntimeAttributeService META API"
        def attributeDescriptorJson = runtimeAttributeDescriptorPosted(RUNTIME_ATTRIBUTE_NAME)

        then:
        attributeDescriptorJson.getString('d.qualifier') == RUNTIME_ATTRIBUTE_NAME

        when: 'the same runtimeAttribute is exposed via the IntegrationService META API'
        def integrationServiceResponse = basicAuthRequest(INTEGRATION_SERVICE_META_IO)
                .path("InboundChannelConfigurations")
                .build()
                .post(inboundChannelConfig(RUNTIME_ATTRIBUTE_NAME))

        then:
        integrationServiceResponse.status == 201

        when: 'we POST to the IO including a runtime attribute value in the payload'
        def testIOResponse = basicAuthRequest(TEST_IO)
                .path('Catalogs')
                .build()
                .post(catalogPayload())

        then: 'the item is successfully created and includes the runtime attribute value'
        testIOResponse.status == 201
        def json = extractBody testIOResponse
        json.getString('d.id') == CATALOG_ID
        json.getString("d.$RUNTIME_ATTRIBUTE_NAME") == RUNTIME_ATTRIBUTE_VALUE

        cleanup:
        IntegrationTestUtil.removeSafely(AttributeDescriptorModel) { it.qualifier == RUNTIME_ATTRIBUTE_NAME }
        IntegrationTestUtil.remove(CatalogModel, { it -> it.id == CATALOG_ID })
    }

    private JsonObject runtimeAttributeDescriptorPosted(String attrName) {
        def response = basicAuthRequest(RUNTIME_ATTRIBUTE_META_IO)
                .path("AttributeDescriptors")
                .accept("application/json")
                .build()
                .post(catalogRuntimeAttributeDescriptor(attrName))
        return extractBody(response)
    }

    def integrationObjectMetaPayload() {
        IntegrationObjectModelBuilder.integrationObject().withCode(TEST_IO)

    }

    def inboundChannelConfig(String attributeName) {
        Entity.json(json()
                .withField("integrationObject",
                        json().withCode(TEST_IO).withFieldValues("items",
                                json().withCode('Catalog').withField("root", true)
                                        .withField("type", json().withCode('Catalog'))
                                        .withFieldValues("attributes",
                                                json().withField("attributeName", 'id')
                                                        .withField('unique', true)
                                                        .withField("attributeDescriptor",
                                                                json().withField("qualifier", 'id')
                                                                        .withField("enclosingType", json()
                                                                                .withCode('Catalog'))),
                                                json().withField("attributeName", attributeName)
                                                        .withField("attributeDescriptor",
                                                                json().withField("qualifier", attributeName)
                                                                        .withField("enclosingType", json()
                                                                                .withCode('Catalog')))
                                        )))
                .build())
    }

    def catalogRuntimeAttributeDescriptor(String attributeName) {
        Entity.json(json()
                .withField("qualifier", attributeName)
                .withField("attributeType", json().withCode("java.lang.String"))
                .withField("enclosingType", json().withCode("Catalog"))
                .withField("unique", false)
                .withField("optional", true)
                .withField("localized", false)
                .withField("partOf", false)
                .withField("generate", false).build())
    }

    def catalogPayload() {
        Entity.json(json()
                .withField("id", CATALOG_ID)
                .withField(RUNTIME_ATTRIBUTE_NAME, RUNTIME_ATTRIBUTE_VALUE).build())
    }

    BasicAuthRequestBuilder basicAuthRequest(String serviceName) {
        new BasicAuthRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .accept("application/json")
                .credentials(USER, PASSWORD) // defined inside setup()
                .path(serviceName)
    }

    JsonObject extractBody(final Response response) {
        JsonObject.createFrom((InputStream) response.getEntity())
    }
}