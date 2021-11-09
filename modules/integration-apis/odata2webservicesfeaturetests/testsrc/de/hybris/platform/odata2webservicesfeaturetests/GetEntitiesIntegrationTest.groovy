/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2webservicesfeaturetests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.impex.jalo.ImpExException
import de.hybris.platform.inboundservices.enums.AuthenticationType
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.inboundservices.util.InboundMonitoringRule
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.integrationservices.util.XmlObject
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItemModel
import de.hybris.platform.odata2webservicesfeaturetests.ws.BasicAuthRequestBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.junit.Rule
import org.junit.Test

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.givenUserExistsWithUidAndGroups
import static de.hybris.platform.odata2webservicesfeaturetests.ws.InboundChannelConfigurationBuilder.inboundChannelConfigurationBuilder

@NeedsEmbeddedServer(webExtensions = Odata2webservicesConstants.EXTENSIONNAME)
@IntegrationTest
class GetEntitiesIntegrationTest extends ServicelayerSpockSpecification {

    private static final String USER = 'tester'
    private static final String PASSWORD = 'retset'
    private static final String TEST_IO = 'InboundProduct'
    private static final def CATALOG = 'TestCatalog'
    private static final def VERSION = 'V2'

    @Rule
    InboundMonitoringRule monitoring = InboundMonitoringRule.disabled()

    def setupSpec() {
        importCsv("/impex/essentialdata-odata2services.impex", "UTF-8") // for the integrationadmingroup
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $TEST_IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Product            ; Product",
                "                                   ; $TEST_IO                              ; Unit               ; Unit",
                "                                   ; $TEST_IO                              ; Catalog            ; Catalog",
                "                                   ; $TEST_IO                              ; CatalogVersion     ; CatalogVersion",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]    ; attributeName[unique = true]; $attributeDescriptor    ; returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]', "                                            ; $TEST_IO:Unit           ; code            ; Unit:code               ;",
                "                                            ; $TEST_IO:Unit           ; name                        ; Unit:name               ;",
                "                                            ; $TEST_IO:Unit           ; unitType                    ; Unit:unitType           ;",
                "                                            ; $TEST_IO:Catalog        ; id                          ; Catalog:id              ;",
                "                                            ; $TEST_IO:CatalogVersion ; catalog                     ; CatalogVersion:catalog  ; $TEST_IO:Catalog",
                "                                            ; $TEST_IO:CatalogVersion ; version                     ; CatalogVersion:version  ;",
                "                                            ; $TEST_IO:Product        ; code                        ; Product:code            ;",
                "                                            ; $TEST_IO:Product        ; unit                        ; Product:unit            ; $TEST_IO:Unit",
                "                                            ; $TEST_IO:Product        ; catalogVersion              ; Product:catalogVersion  ; $TEST_IO:CatalogVersion"
        )
        // Create Catalog and CatalogVersion to be used by persisted products
        IntegrationTestUtil.createCatalogWithId(CATALOG)
        IntegrationTestUtil.importCatalogVersion(VERSION, CATALOG, false)
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationadmingroup")
    }

    def setup() {
        inboundChannelConfigurationBuilder()
                .withCode(TEST_IO)
                .withAuthType(AuthenticationType.BASIC)
                .build()
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll ProductModel
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version == VERSION }
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == CATALOG }
        IntegrationTestUtil.findAny(EmployeeModel, { it.uid == USER }).ifPresent { IntegrationTestUtil.remove it }
    }

    def cleanup() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == TEST_IO }
    }

    @Test
    def "get all integration object items for an integration object as json"() {
        when:
        def response = basicAuthRequest(TEST_IO)
                .build()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()

        then:
        response.status == 200
        def json = extractBody(response)
        def entities = json.getCollection('d.EntitySets[*]')
        entities.containsAll(["Products", "Units", "CatalogVersions", "Catalogs"])
    }

    @Test
    def "get collection of enums"() throws ImpExException {
        given:
        final String itemKey = "testItem1Code"
        final String enumKey1 = "string"
        final String enumKey2 = "bool"
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]                         ; type(code)",
                "                                   ; $TEST_IO                              ; TestIntegrationItem                         ; TestIntegrationItem",
                "                                   ; $TEST_IO                              ; OData2webservicesFeatureTestPropertiesTypes ; OData2webservicesFeatureTestPropertiesTypes",
                "INSERT_UPDATE IntegrationObjectItemAttribute ; integrationObjectItem(integrationObject(code), code)[unique = true] ; attributeName[unique = true] ; attributeDescriptor(enclosingType(code), qualifier) ; returnIntegrationObjectItem(integrationObject(code), code)  ; unique[default = false]",
                "                                             ; $TEST_IO:TestIntegrationItem                                        ; code                         ; TestIntegrationItem:code",
                "                                             ; $TEST_IO:TestIntegrationItem                                        ; testEnums                    ; TestIntegrationItem:testEnums                       ; $TEST_IO:OData2webservicesFeatureTestPropertiesTypes  ; false",
                "                                             ; $TEST_IO:OData2webservicesFeatureTestPropertiesTypes                ; code                         ; OData2webservicesFeatureTestPropertiesTypes:code",

                "INSERT_UPDATE TestIntegrationItem ; code[unique = true] ; testEnums(code)",
                "                                  ; $itemKey            ; $enumKey1,$enumKey2"
        )

        when:
        def response = basicAuthRequest("$TEST_IO/TestIntegrationItems('$itemKey')/testEnums")
                .build()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()

        then:
        response.status == 200
        def json = extractBody(response)
        json.getCollection('d.results').size() == 2
        json.getCollectionOfObjects("d.results[*].code").containsAll(enumKey1, enumKey2)

        cleanup:
        IntegrationTestUtil.remove TestIntegrationItemModel, { it.code == 'testItem1Code' }
    }

    @Test
    def "get mapType attribute with values"() throws ImpExException {
        given:
        def key1 = 'k1'
        def val1 = 'v1'
        def key2 = 'k2'
        def val2 = 'v2'
        IntegrationTestUtil.importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]    ; attributeName[unique = true]; $attributeDescriptor',
                "                                            ; $TEST_IO:Product        ; specialTreatmentClasses     ; Product:specialTreatmentClasses",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); specialTreatmentClasses(key, value)[map-delimiter = |]',
                "                     ; pr-1               ; $CATALOG:$VERSION                      ; $key1->$val1|$key2->$val2"
        )

        when:
        def response = basicAuthRequest("$TEST_IO/Products('$VERSION|$CATALOG|pr-1')/specialTreatmentClasses")
                .build()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()

        then:
        response.status == 200
        def json = extractBody(response)
        json.getCollection('d.results').size() == 2
        json.getCollectionOfObjects("d.results[?(@.key == '$key1')].value").contains(val1)
        json.getCollectionOfObjects("d.results[?(@.key == '$key2')].value").contains(val2)
    }

    @Test
    def "get mapType attribute when null"() throws ImpExException {
        given:
        IntegrationTestUtil.importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]    ; attributeName[unique = true]; $attributeDescriptor',
                "                                            ; $TEST_IO:Product        ; specialTreatmentClasses     ; Product:specialTreatmentClasses",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                "                     ; pr-1               ; $CATALOG:$VERSION"
        )

        when:
        def response = basicAuthRequest("$TEST_IO/Products('$VERSION|$CATALOG|pr-1')/specialTreatmentClasses")
                .build()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()

        then:
        response.status == 200
        def json = extractBody(response)
        json.getCollection('d.results').isEmpty()
    }

    @Test
    def "get all integration object items for an integration object as xml"() {
        when:
        def response = basicAuthRequest(TEST_IO)
                .build()
                .accept(MediaType.APPLICATION_XML_TYPE)
                .get()

        then:
        response.status == 200
        def xml = getXml(response)
        def entities = xml.get("/service/workspace")
        entities.contains("Units")
        entities.contains("Products")
        entities.contains("CatalogVersions")
        entities.contains("Catalogs")
    }

    @Test
    def "get all integration object items for nonexistent integration object returns 404"() {
        when:
        def response = basicAuthRequest("NonExistentProduct")
                .build()
                .accept(MediaType.APPLICATION_XML_TYPE)
                .get()

        then:
        response.status == 404
    }

    BasicAuthRequestBuilder basicAuthRequest(final String path) {
        new BasicAuthRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .credentials(USER, PASSWORD) // defined in setup()
                .path(path)
    }

    private static XmlObject getXml(final Response response) {
        return XmlObject.createFrom(response.entity as InputStream)
    }

    JsonObject extractBody(final Response response) {
        JsonObject.createFrom((InputStream) response.getEntity())
    }
}
