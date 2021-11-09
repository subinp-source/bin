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
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItem2Model
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import spock.lang.Issue
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
@Issue('https://jira.hybris.com/browse/STOUT-3197')
class ODataPostMapTypeAttributesIntegrationTest extends ServicelayerSpockSpecification {

    private static final String TEST_IO = 'SchemaWithMapsIO'
    private static final def CATALOG = 'Test'
    private static final def VERSION = 'V1'
    private static final def PRODUCT_CODE = 'testProdA'
    private static final def TEST_ITEM_CODE = 'itemCode'
    private static final def KEY = 'key'
    private static final def VALUE = 'value'

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade


    def setupSpec() {
        /* Create IO Definition where a Product IOI Definition exists with a MapType attribute "specialTreatmentClasses" with primitive key/value */
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $TEST_IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $TEST_IO                              ; Product              ; Product",
                "                                   ; $TEST_IO                              ; CatalogVersion       ; CatalogVersion",
                "                                   ; $TEST_IO                              ; Catalog              ; Catalog",
                "                                   ; $TEST_IO                              ; TestIntegrationItem2 ; TestIntegrationItem2",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]          ; attributeName[unique = true]; $attributeDescriptor  ; returnIntegrationObjectItem(integrationObject(code), code)',
                "                                            ; $TEST_IO:Catalog              ; id                          ; Catalog:id",
                "                                            ; $TEST_IO:CatalogVersion       ; version                     ; CatalogVersion:version",
                "                                            ; $TEST_IO:CatalogVersion       ; catalog                     ; CatalogVersion:catalog; $TEST_IO:Catalog",
                "                                            ; $TEST_IO:Product              ; code                        ; Product:code",
                "                                            ; $TEST_IO:Product              ; catalogVersion              ; Product:catalogVersion; $TEST_IO:CatalogVersion",
                "                                            ; $TEST_IO:Product              ; specialTreatmentClasses     ; Product:specialTreatmentClasses",
                "                                            ; $TEST_IO:TestIntegrationItem2 ; code                        ; TestIntegrationItem2:code",
                "                                            ; $TEST_IO:TestIntegrationItem2 ; requiredName                ; TestIntegrationItem2:requiredName",
                "                                            ; $TEST_IO:TestIntegrationItem2 ; requiredStringMap           ; TestIntegrationItem2:requiredStringMap",
                "                                            ; $TEST_IO:TestIntegrationItem2 ; characterMap                ; TestIntegrationItem2:characterMap"
        )
        // Create Catalog and CatalogVersion to be used by persisted products
        IntegrationTestUtil.createCatalogWithId(CATALOG)
        IntegrationTestUtil.importCatalogVersion(VERSION, CATALOG, false)
    }

    def cleanup() {
        IntegrationTestUtil.removeAll ProductModel
        IntegrationTestUtil.removeAll TestIntegrationItem2Model
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version == VERSION }
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == CATALOG }
    }

    @Test
    @Unroll
    def "POST creates item when optional map attribute is #condition in the payload"() {
        given: 'a request with body'
        def request = postRequest().withBody requestBody

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response body contains created product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.code') == PRODUCT_CODE
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the map attribute is assigned in the created product'
        def createdProduct = IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).get()
        createdProduct.getSpecialTreatmentClasses() == [:]

        where:
        condition | requestBody
        'null'    | product(PRODUCT_CODE).withField('specialTreatmentClasses', null as List)
        'empty'   | product(PRODUCT_CODE).withField('specialTreatmentClasses', [])
        'absent'  | product(PRODUCT_CODE)
    }

    @Test
    @Unroll
    def "POST does not update mapType attribute when payload value is #condition"() {
        given: 'the TestIntegrationItem2 exists with a characterMap'
        Character existingKey1 = 'k'
        Character existingValue1 = 'v'
        importImpEx(
                'INSERT_UPDATE TestIntegrationItem2; code[unique = true]; requiredName  ; requiredStringMap(key, value)[map-delimiter = |]; characterMap(key, value)[map-delimiter = |]',
                "                                  ; $TEST_ITEM_CODE    ; nameNameValue ; stringK1->stringV1                              ; $existingKey1->$existingValue1"
        )


        and: 'a request with body'
        def request = postRequest('TestIntegrationItem2s').withBody requestBody

        when: 'a post request is made for a TestIntegrationItem2s'
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'item in the database did not change'
        IntegrationTestUtil.findAny(TestIntegrationItem2Model, { it.code == TEST_ITEM_CODE })
                .map({ it.characterMap.collect({ mapAttr -> [(mapAttr.key): mapAttr.value] }) })
                .orElse([]) == [[(existingKey1): existingValue1]]

        cleanup:
        IntegrationTestUtil.removeAll TestIntegrationItem2Model

        where:
        condition | requestBody
        'null'    | testIntegrationItem2().withField('characterMap', null as List)
        'empty'   | testIntegrationItem2().withField('characterMap', [])
        'missing' | testIntegrationItem2()
    }

    @Test
    @Unroll
    def "POST fails to create item when its required mapType attribute is #condition in the payload"() {
        given: 'a request with body'
        def request = postRequest('TestIntegrationItem2s').withBody requestBody

        when: 'a post request is made for a TestIntegrationItem2s'
        def response = facade.handleRequest createContext(request)

        then: 'an error response is returned'
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains 'requiredStringMap'
        and: 'the item is not created'
        IntegrationTestUtil.findAny(TestIntegrationItem2Model, { it.code == TEST_ITEM_CODE }).empty

        where:
        condition | requestBody
        'null'    | testIntegrationItem2().withField('requiredStringMap', null as List)
        'missing' | testIntegrationItem2()
    }

    @Test
    def 'POST creates item when its required mapType attribute is empty in the payload'() {
        given: 'request with payload'
        def request = postRequest('TestIntegrationItem2s').withBody testIntegrationItem2().withField('requiredStringMap', [])

        when: 'a post request is made for a TestIntegrationItem2s'
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response body contains created item'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.code') == TEST_ITEM_CODE
        and: 'the item was created'
        IntegrationTestUtil.findAny(TestIntegrationItem2Model, { it.code == TEST_ITEM_CODE }).present
        and: 'the map attribute is assigned in the created item'
        def createdTestItem = IntegrationTestUtil.findAny(TestIntegrationItem2Model, {
            it.code == TEST_ITEM_CODE
        }).get()
        createdTestItem.getRequiredStringMap() == [:]

        cleanup:
        IntegrationTestUtil.removeAll TestIntegrationItem2Model
    }

    @Test
    @Unroll
    def "POST updates existing item when its required mapType attribute is #condition in the payload"() {
        given: 'the TestIntegrationItem2 exists with a requiredStringMap'
        def existingKey1 = 'k1'
        def existingValue1 = 'v1'
        importImpEx(
                'INSERT_UPDATE TestIntegrationItem2; code[unique = true]; requiredName  ; requiredStringMap(key, value)[map-delimiter = |]',
                "                                  ; $TEST_ITEM_CODE    ; nameNameValue ; $existingKey1->$existingValue1"
        )

        and: 'request with payload'
        def request = postRequest('TestIntegrationItem2s').withBody(requestBody)

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the item is still associated with its detail'
        IntegrationTestUtil.findAny(TestIntegrationItem2Model, { it.code == TEST_ITEM_CODE })
                .map({ it.requiredStringMap.collect({ mapAttr -> [(mapAttr.key): mapAttr.value] }) })
                .orElse([]) == [[(existingKey1): existingValue1]]

        where:
        condition | requestBody
        'null'    | testIntegrationItem2().withField('requiredStringMap', null as List)
        'empty'   | testIntegrationItem2().withField('requiredStringMap', [])
        'missing' | testIntegrationItem2()
    }

    @Test
    @Unroll
    def "POST updates map attribute by merging existing entries with #newValue in the payload"() {
        given: "a product exists with an existing map value for its MapType attribute 'specialTreatmentClasses'"
        def existingKey1 = 'k1'
        def existingValue1 = 'v1'
        def existingKey2 = 'k2'
        def existingValue2 = 'v2'
        def newKey3 = 'k3'
        def newValue3 = 'v3'
        importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); specialTreatmentClasses(key, value)[map-delimiter = |]',
                "                     ; $PRODUCT_CODE      ; $CATALOG:$VERSION                   ; $existingKey1->$existingValue1|$existingKey2->$existingValue2"
        )

        def request = postRequest().withBody product(PRODUCT_CODE)
                .withFieldValues('specialTreatmentClasses', mapEntry(existingKey2, newValue), mapEntry(newKey3, newValue3))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response body contains created product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.code') == PRODUCT_CODE
        and: 'the product specialTreatmentClasses attribute was updated with  a new value for key2'
        def createdProduct = IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).get()
        createdProduct.getSpecialTreatmentClasses().size() == 3
        createdProduct.getSpecialTreatmentClasses().entrySet().containsAll(
                [
                        new MapEntry(existingKey1, existingValue1),
                        new MapEntry(existingKey2, newValue),
                        new MapEntry(newKey3, newValue3)
                ])

        where:
        newValue << [null, 'newV2']
    }

    @Test
    def 'succeeds when an Item is created with non-null key and values provided for a MapType attribute'() {
        given: 'a request with body'
        def key1 = 'k1'
        def value1 = 'v1'
        def key2 = 'k2'
        def value2 = 'v2'

        def request = postRequest().withBody product(PRODUCT_CODE)
                .withFieldValues('specialTreatmentClasses', mapEntry(key1, value1), mapEntry(key2, value2))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response body contains created product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.code') == PRODUCT_CODE
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the map attribute is assigned in the created product'
        def createdProduct = IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).get()
        createdProduct.getSpecialTreatmentClasses().size() == 2
        createdProduct.getSpecialTreatmentClasses().entrySet().containsAll([new MapEntry(key1, value1), new MapEntry(key2, value2)])
    }

    @Test
    @Unroll
    def "succeeds when an Item is created with a non-null key and #valueDesc value provided for a MapType attribute"() {
        given: 'a request with body'
        def request = postRequest().withBody product(PRODUCT_CODE)
                .withFieldValues('specialTreatmentClasses', payload)

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response body contains created product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.code') == PRODUCT_CODE
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the map attribute is assigned in the created product'
        def createdProduct = IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).get()
        createdProduct.getSpecialTreatmentClasses() == [k1: null]

        where:
        valueDesc | payload
        'null'    | mapEntry('k1', null)
        'no'      | JsonBuilder.json().withField('key', 'k1')
    }

    @Test
    @Unroll
    def "fails when an Item is created #keyCondition for a MapType attribute"() {
        given: 'a request with body'
        def request = postRequest().withBody product(PRODUCT_CODE)
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
        and: 'the product was not created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).empty

        where:
        keyCondition | payload
        'with a null key' | mapEntry(null, 'v1')
        'without a key' | JsonBuilder.json().withField('value', 'v1')
    }

    private static JsonBuilder testIntegrationItem2() {
        json()
                .withCode(TEST_ITEM_CODE)
                .withField("requiredName", "reqName")
    }

    private static JsonBuilder product(String code) {
        json()
                .withCode(code)
                .withField('catalogVersion', json()
                        .withField('version', VERSION)
                        .withField('catalog', json().withId(CATALOG)))
    }

    private static JsonBuilder mapEntry(def key, def value) {
        json().withField(KEY, key).withField(VALUE, value)
    }

    private static ODataRequestBuilder postRequest(String entitySetName) {
        ODataRequestBuilder.oDataPostRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(TEST_IO)
                        .withEntitySet(entitySetName))
                .withContentType(APPLICATION_JSON_VALUE)
                .withAccepts(APPLICATION_JSON_VALUE)
    }

    private static ODataRequestBuilder postRequest() {
        postRequest('Products')
    }
}
