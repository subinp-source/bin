/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import spock.lang.Issue
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
@Issue('https://jira.hybris.com/browse/STOUT-3197')
class ODataPatchMapTypeAttributesIntegrationTest extends ServicelayerSpockSpecification {

    private static final String TEST_IO = 'SchemaWithMapsIO'
    private static final def CATALOG = 'Test'
    private static final def VERSION = 'V1'
    private static final def PRODUCT_CODE = 'testProdA'
    private static final def KEY = 'key'
    private static final def VALUE = 'value'
    private static final def KEY1 = 'k1'
    private static final def VAL1= 'v1'
    private static final def KEY2 = 'k2'
    private static final def VAL2 = 'v2'

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade
    
    def setupSpec() {
        /* Create IO Definition where a Product IOI Definition exists with a MapType attribute "specialTreatmentClasses" with primitive key/value */
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $TEST_IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $TEST_IO                                   ; Product              ; Product",
                "                                   ; $TEST_IO                                   ; CatalogVersion       ; CatalogVersion",
                "                                   ; $TEST_IO                                   ; Catalog              ; Catalog",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]   ; attributeName[unique = true]; $attributeDescriptor  ; returnIntegrationObjectItem(integrationObject(code), code)',
                "                                            ; $TEST_IO:Catalog       ; id                          ; Catalog:id",
                "                                            ; $TEST_IO:CatalogVersion; version                     ; CatalogVersion:version",
                "                                            ; $TEST_IO:CatalogVersion; catalog                     ; CatalogVersion:catalog; $TEST_IO:Catalog",
                "                                            ; $TEST_IO:Product       ; code                        ; Product:code",
                "                                            ; $TEST_IO:Product       ; catalogVersion              ; Product:catalogVersion; $TEST_IO:CatalogVersion",
                "                                            ; $TEST_IO:Product       ; specialTreatmentClasses     ; Product:specialTreatmentClasses")
        // Create Catalog and CatalogVersion to be used by persisted products
        IntegrationTestUtil.createCatalogWithId(CATALOG)
        IntegrationTestUtil.importCatalogVersion(VERSION, CATALOG, false)
    }

    def setup() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); specialTreatmentClasses(key, value)[map-delimiter = |]',
                "                     ; $PRODUCT_CODE      ; $CATALOG:$VERSION                   ; $KEY1->$VAL1|$KEY2->$VAL2")
    }

    def cleanup() {
        IntegrationTestUtil.removeAll ProductModel
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version == VERSION }
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == CATALOG }
    }

    @Test
    def "patching map attribute with new values replaces the existing values"() {
        given: 'a request to replace the existing map value with a new one'
        def key = 'newKey'
        def value = 'newValue'

        def request = patchRequest().withBody json()
                .withFieldValues('specialTreatmentClasses', mapEntry(key, value))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.OK
        and: 'response body contains patched product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.code') == PRODUCT_CODE
        and: 'the map attribute is updated to the new value'
        def product = IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).get()
        product.getSpecialTreatmentClasses().size() == 1
        product.getSpecialTreatmentClasses() == [(key):value]
    }

    @Test
    def "patching map attribute with an empty collection removes existing values"() {
        given: 'a request to remove existing map values'
        def request = patchRequest().withBody json()
                .withField('specialTreatmentClasses', [])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.OK
        and: 'response body contains patched product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.code') == PRODUCT_CODE
        and: 'the map attribute is now empty'
        def product = IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).get()
        product.getSpecialTreatmentClasses().isEmpty()
    }

    @Test
    def "patching map attribute with a null does not change the existing value"() {
        given: 'a request to set map attribute to null'
        def request = patchRequest().withBody json()
                .withField('specialTreatmentClasses', null)

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.OK
        and: 'response body contains patched product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.code') == PRODUCT_CODE
        and: 'the map attribute is not changed'
        def product = IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).get()
        product.getSpecialTreatmentClasses().size() == 2
        product.getSpecialTreatmentClasses().entrySet().containsAll([new MapEntry(KEY1, VAL1), new MapEntry(KEY2, VAL2)])
    }

    @Test
    @Unroll
    def "succeeds when an Item is patched with a non-null key and #valueDesc value provided for a MapType attribute"() {
        given: 'a request with body'
        def request = patchRequest().withBody json()
                .withFieldValues('specialTreatmentClasses', payload)

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.OK
        and: 'response body contains patched product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.code') == PRODUCT_CODE
        and: 'the map attribute is updated'
        def product = IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).get()
        product.getSpecialTreatmentClasses() == [k1: null]

        where:
        valueDesc | payload
        'null'    | mapEntry('k1', null)
        'no'      | JsonBuilder.json().withField('key', 'k1')
    }

    @Test
    @Unroll
    def "fails when an Item is patched #keyCondition for a MapType attribute"() {
        given: 'a request to patch map attribute without key'
        def request = patchRequest().withBody json()
                .withFieldValues('specialTreatmentClasses', payload)

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is correctly reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_property'
        json.getString('error.message.value').contains 'key'
        json.getString('error.innererror') == "$VERSION|$CATALOG|$PRODUCT_CODE"
        and: 'the map attribute is not changed'
        def product = IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).get()
        product.getSpecialTreatmentClasses().size() == 2
        product.getSpecialTreatmentClasses().entrySet().containsAll([new MapEntry(KEY1, VAL1), new MapEntry(KEY2, VAL2)])

        where:
        keyCondition      | payload
        'with a null key' | mapEntry(null, 'v1')
        'without a key'   | JsonBuilder.json().withField('value', 'v1')
    }

    private static JsonBuilder mapEntry(def key, def value) {
        json().withField(KEY, key).withField(VALUE, value)
    }

    private static ODataRequestBuilder patchRequest() {
        ODataRequestBuilder.oDataPatchRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(TEST_IO)
                        .withRequestPath("Products('$VERSION%7C$CATALOG%7C$PRODUCT_CODE')"))
                .withContentType(APPLICATION_JSON_VALUE)
                .withAccepts(APPLICATION_JSON_VALUE)
    }
}
