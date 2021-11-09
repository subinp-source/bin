/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.core.model.c2l.CountryModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.ClassificationBuilder
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

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.getClassificationAttributeCollectionValue
import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
@Issue('https://jira.hybris.com/browse/STOUT-2428')
class ODataPostClassificationAttributesIntegrationTest extends ServicelayerSpockSpecification {
    private static final def CLASS_SYSTEM = 'Sports'
    private static final def CLASS_VERSION = 'Cycling'
    private static final def CLASS_SYSTEM_VERSION = "$CLASS_SYSTEM:$CLASS_VERSION"
    private static final def CLASSIFICATION_CLASS = "Bicycle"
    private static final def IO = 'BikeIO'
    private static final def PRODUCT_CODE = 'bike1'
    private static final def CATALOG = 'Test'
    private static final def VERSION = 'V1'
    private static final def CLASS_ASSIGNMENT_PREFIX = "$CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION"

    /* Impex header variables */
    private static final def CATALOG_VERSION_HEADER = 'catalogVersion(catalog(id), version)'
    private static final def SYSTEM_VERSION_HEADER = 'systemVersion(catalog(id), version)'
    private static final def CLASS_HEADER = "classificationClass($CATALOG_VERSION_HEADER, code)"
    private static final def CLASS_ATTRIBUTE_HEADER = "classificationAttribute($SYSTEM_VERSION_HEADER, code)"
    private static final def CLASS_ASSIGNMENT_HEADER = "classAttributeAssignment($CLASS_HEADER, $CLASS_ATTRIBUTE_HEADER)"
    private static final def ITEM_HEADER = 'integrationObjectItem(integrationObject(code), code)'
    private static final def RETURN_ITEM_HEADER = 'returnIntegrationObjectItem(integrationObject(code), code)'

    private static final def classification = ClassificationBuilder.classification()
            .withSystem(CLASS_SYSTEM)
            .withVersion(CLASS_VERSION)
            .withClassificationClass(CLASSIFICATION_CLASS)

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setupSpec() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Catalog; id[unique = true]',
                "                     ; $CATALOG",
                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]',
                "                            ; $CATALOG                  ; $VERSION")
    }

    def setup() {
        // Create IO Definition
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Product              ; Product",
                "                                   ; $IO                                   ; CatalogVersion       ; CatalogVersion",
                "                                   ; $IO                                   ; Catalog              ; Catalog",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $attributeDescriptor  ; returnIntegrationObjectItem(integrationObject(code), code)',
                "                                            ; $IO:Catalog         ; id                          ; Catalog:id",
                "                                            ; $IO:Product         ; code                        ; Product:code",
                "                                            ; $IO:Product         ; catalogVersion              ; Product:catalogVersion; $IO:CatalogVersion",
                "                                            ; $IO:CatalogVersion  ; version                     ; CatalogVersion:version",
                "                                            ; $IO:CatalogVersion  ; catalog                     ; CatalogVersion:catalog; $IO:Catalog")
    }

    def cleanup() {
        IntegrationTestUtil.removeAll ProductModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll CountryModel
        ClassificationBuilder.cleanup()
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version == VERSION }
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == CATALOG }
    }

    @Test
    @Unroll
    def "fails when a required primitive attribute value is #condition in the payload"() {
        given: 'a mandatory primitive classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('weight').number().mandatory())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; bicycleWeight               ; $CLASS_ASSIGNMENT_PREFIX:weight")
        and: 'a request with body'
        def request = postRequest().withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is correctly reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_property'
        json.getString('error.message.value').contains 'bicycleWeight'
        json.getString('error.innererror') == "$VERSION|$CATALOG|$PRODUCT_CODE"
        and: 'the product was not created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).empty

        where:
        condition | payload
        'null'    | product(PRODUCT_CODE).withField('bicycleWeight', null)
        'missing' | product(PRODUCT_CODE)
    }

    @Test
    def 'succeeds when a required primitive attribute value is present in the payload'() {
        given: 'a mandatory primitive classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('weight').number().mandatory())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; bicycleWeight               ; $CLASS_ASSIGNMENT_PREFIX:weight")
        and: 'a request with body'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('bicycleWeight', "7.3")

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response body contains created product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.bicycleWeight') == '7.3'
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the classification attribute is assigned in the created product'
        IntegrationTestUtil.getClassificationAttributeValue(PRODUCT_CODE, 'weight') == 7.3
    }

    @Test
    @Unroll
    def "succeeds when optional primitive attribute value is #condition in the payload"() {
        given: 'an optional primitive classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('weight').number())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; weight                      ; $CLASS_ASSIGNMENT_PREFIX:weight")
        and: 'a request with body'
        def request = postRequest().withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present

        where:
        condition | payload
        'null'    | product(PRODUCT_CODE).withField('weight', null)
        'missing' | product(PRODUCT_CODE)
    }

    @Test
    def "attribute value is cleared when value is null in the payload"() {
        given: 'an optional primitive classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('weight').number())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; weight                      ; $CLASS_ASSIGNMENT_PREFIX:weight")
        and: 'a request with body'
        def firstRequest = postRequest().withBody product(PRODUCT_CODE).withField('weight', 7.6)

        when:
        def firstResponse = facade.handleRequest createContext(firstRequest)

        then: 'product contains feature value'
        firstResponse.status == HttpStatusCodes.CREATED
        def prod = IntegrationTestUtil.findAny ProductModel, { it.code == PRODUCT_CODE }
        prod.isPresent()
        prod.get().features.first().value == 7.6

        when: 'same product is posted with a null value for attribute that contains a value'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('weight', null)
        def response = facade.handleRequest createContext(request)

        then: 'the value is cleared'
        response.status == HttpStatusCodes.CREATED
        and: 'the product was created'
        def product = IntegrationTestUtil.findAny ProductModel, { it.code == PRODUCT_CODE }
        product.isPresent()
        product.get().features.isEmpty()
    }

    @Test
    @Unroll
    def "fails when a required primitive collection attribute value is #condition in the payload"() {
        given: 'a mandatory primitive collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('sizes').number().mandatory().multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; availableSizes              ; $CLASS_ASSIGNMENT_PREFIX:sizes")
        and: 'a request with body'
        def request = postRequest().withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is correctly reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains 'availableSizes'
        json.getString('error.innererror') == "$VERSION|$CATALOG|$PRODUCT_CODE"
        and: 'the product was not created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).empty

        where:
        condition | payload
        'null'    | product(PRODUCT_CODE).withField('availableSizes', null as List)
        'missing' | product(PRODUCT_CODE)
    }

    @Test
    def "succeeds when a required primitive collection attribute value is empty in the payload"() {
        given: 'a mandatory primitive collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('sizes').number().mandatory().multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; availableSizes              ; $CLASS_ASSIGNMENT_PREFIX:sizes")
        and: 'a request with body'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('availableSizes', [])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
    }

    @Test
    def "succeeds when a required primitive collection attribute has values in the payload"() {
        given: 'a mandatory primitive collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('sizes').number().mandatory().multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; availableSizes              ; $CLASS_ASSIGNMENT_PREFIX:sizes")
        and: 'a request with body'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('availableSizes', [json().withField('value', '56'), json().withField('value', '58'), json().withField('value', '61')])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response body contains created product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.availableSizes.__deferred')
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the classification attribute is assigned in the created product'
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'sizes') == [56.0d, 58.0d, 61.0d]
    }

    @Test
    def "succeeds when optional collection of dates attribute has values in the payload"() {
        given: 'an optional collection of dates classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('dates').date().multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; notableDates                ; $CLASS_ASSIGNMENT_PREFIX:dates")
        and: 'a request with body'
        def date1 = new Date(1985, 04, 01)
        def date2 = new Date(2009, 04, 01)
        def request = postRequest().withBody product(PRODUCT_CODE).withField('notableDates',
                [json().withField('value', date1), json().withField('value', date2)])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response body contains created product and attribute is deferred'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.notableDates.__deferred')
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the classification attribute is assigned in the created product'
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'dates') == [date1, date2]
    }

    @Test
    @Unroll
    def "succeeds when a required primitive collection attribute has values and it's updated with #condition in the payload"() {
        given: 'a mandatory primitive collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('sizes').number().mandatory().multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; availableSizes              ; $CLASS_ASSIGNMENT_PREFIX:sizes")
        and: 'a request with body'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('availableSizes',
                [JsonBuilder.json().withField('value', '56'),
                 JsonBuilder.json().withField('value', '58'),
                 JsonBuilder.json().withField('value', '61')])
        facade.handleRequest createContext(request)
        and: 'another request is made with the same code'
        def updateRequest = postRequest().withBody body

        when:
        def response = facade.handleRequest createContext(updateRequest)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response body contains created product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.availableSizes.__deferred')
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the classification attribute is assigned in the created product'
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'sizes').containsAll(expectedSizes)

        where:
        condition                | body                                                                                             | expectedSizes
        'null'                   | product(PRODUCT_CODE).withField('availableSizes', null)                                          | [56.0d, 58.0d, 61.0d]
        'empty collection'       | product(PRODUCT_CODE).withField('availableSizes', [])                                            | [56.0d, 58.0d, 61.0d]
        'collection not present' | product(PRODUCT_CODE)                                                                            | [56.0d, 58.0d, 61.0d]
        'new values'             | product(PRODUCT_CODE).withField('availableSizes', [JsonBuilder.json().withField('value', '52')]) | [56.0d, 58.0d, 61.0d, 52.0d]
    }

    @Test
    @Unroll
    def "succeeds when an optional primitive collection attribute value is #condition in the payload"() {
        given: 'an optional primitive collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('sizes').number().multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; sizes                       ; $CLASS_ASSIGNMENT_PREFIX:sizes")
        and: 'a request with body'
        def request = postRequest().withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present

        where:
        condition | payload
        'null'    | product(PRODUCT_CODE).withField('sizes', null as List)
        'empty'   | product(PRODUCT_CODE).withField('sizes', [])
        'missing' | product(PRODUCT_CODE)
    }

    @Test
    @Unroll
    def "fails when a required reference attribute value is #condition in the payload"() {
        given: 'a mandatory reference classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('country').references('Country').mandatory())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Country              ; Country",
                "INSERT_UPDATE IntegrationObjectItemAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                "                                            ; $IO:Country                ; code                        ; Country:isocode",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER        ; $RETURN_ITEM_HEADER",
                "                                                          ; $IO:Product                ; madeIn                      ; $CLASS_ASSIGNMENT_PREFIX:country; $IO:Country")
        and: 'a request with body'
        def request = postRequest().withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is correctly reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains 'madeIn'
        json.getString('error.innererror') == "$VERSION|$CATALOG|$PRODUCT_CODE"
        and: 'the product was not created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).empty

        where:
        condition | payload
        'null'    | product(PRODUCT_CODE).withField('madeIn', null)
        'missing' | product(PRODUCT_CODE)
    }

    @Test
    def 'fails when the attribute value references a non-existent item'() {
        given: 'a mandatory reference classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('country').references('Country'))
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Country              ; Country",
                "INSERT_UPDATE IntegrationObjectItemAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                "                                            ; $IO:Country                ; code                        ; Country:isocode",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER        ; $RETURN_ITEM_HEADER",
                "                                                          ; $IO:Product                ; country                     ; $CLASS_ASSIGNMENT_PREFIX:country; $IO:Country")
        and: 'an existing country'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Country; isocode[unique = true]',
                '                     ; de')
        and: 'a request with body containing reference to a country other than existing "de"'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('country', json().withCode('fr'))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is correctly reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains 'country'
        json.getString('error.innererror') == "$VERSION|$CATALOG|$PRODUCT_CODE"
        and: 'the product was not created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).empty
        and: 'the country was not created'
        IntegrationTestUtil.findAny(CountryModel, { it.isocode == 'fr' }).empty
    }

    @Test
    def 'succeeds when the attribute value references an existing item'() {
        given: 'a mandatory reference classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('country').references('Country').mandatory())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Country              ; Country",
                "INSERT_UPDATE IntegrationObjectItemAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                "                                            ; $IO:Country                ; code                        ; Country:isocode",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER        ; $RETURN_ITEM_HEADER",
                "                                                          ; $IO:Product                ; country                     ; $CLASS_ASSIGNMENT_PREFIX:country; $IO:Country")
        and: 'an existing country'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Country; isocode[unique = true]',
                '                     ; de')
        and: 'a request with body containing reference to the existing country "de"'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('country', json().withCode('de'))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response body contains created product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.country.__deferred')
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the classification attribute is assigned in the created product'
        IntegrationTestUtil.getClassificationAttributeValue(PRODUCT_CODE, 'country').isocode == 'de'
    }

    @Test
    def 'succeeds when the attribute value references a non-existent item but autoCreate is true'() {
        given: 'a mandatory reference classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('country').references('Country').mandatory())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Country              ; Country",
                "INSERT_UPDATE IntegrationObjectItemAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                "                                            ; $IO:Country                ; code                        ; Country:isocode",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER        ; $RETURN_ITEM_HEADER; autoCreate",
                "                                                          ; $IO:Product                ; madeIn                      ; $CLASS_ASSIGNMENT_PREFIX:country; $IO:Country        ; true")
        and: 'a request with body containing reference to a non-existing country'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('madeIn', json().withCode('fr'))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response body contains created product'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.madeIn.__deferred')
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the country was created'
        IntegrationTestUtil.findAny(CountryModel, { it.isocode == 'fr' }).present
        and: 'the classification attribute is assigned in the created product'
        IntegrationTestUtil.getClassificationAttributeValue(PRODUCT_CODE, 'country').isocode == 'fr'
    }

    @Test
    @Unroll
    def "succeeds when an optional reference classification attribute value is #condition in the payload"() {
        given: 'an optional reference classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('country').references('Country'))
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Country              ; Country",
                "INSERT_UPDATE IntegrationObjectItemAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                "                                            ; $IO:Country                ; code                        ; Country:isocode",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER        ; $RETURN_ITEM_HEADER",
                "                                                          ; $IO:Product                ; madeIn                      ; $CLASS_ASSIGNMENT_PREFIX:country; $IO:Country")
        and: 'a request with body'
        def request = postRequest().withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present

        where:
        condition | payload
        'null'    | product(PRODUCT_CODE).withField('madeIn', null)
        'missing' | product(PRODUCT_CODE)
    }

    @Test
    @Unroll
    def "fails when a required reference collection attribute value is #condition in the payload"() {
        given: 'a mandatory reference collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('countries').references('Country').mandatory().multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Country              ; Country",
                "INSERT_UPDATE IntegrationObjectItemAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                "                                            ; $IO:Country                ; code                        ; Country:isocode",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER",
                "                                                          ; $IO:Product                ; availableIn                 ; $CLASS_ASSIGNMENT_PREFIX:countries; $IO:Country")
        and: 'a request with body'
        def request = postRequest().withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is correctly reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains 'availableIn'
        json.getString('error.innererror') == "$VERSION|$CATALOG|$PRODUCT_CODE"
        and: 'the product was not created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).empty

        where:
        condition | payload
        'null'    | product(PRODUCT_CODE).withField('availableIn', null as List)
        'missing' | product(PRODUCT_CODE)
    }

    @Test
    def 'succeeds when a required reference collection attribute value is empty in the payload'() {
        given: 'a mandatory reference collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('countries').references('Country').mandatory().multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Country              ; Country",
                "INSERT_UPDATE IntegrationObjectItemAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                "                                            ; $IO:Country                ; code                        ; Country:isocode",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER",
                "                                                          ; $IO:Product                ; availableIn                 ; $CLASS_ASSIGNMENT_PREFIX:countries; $IO:Country")
        and: 'a request with body'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('availableIn', [])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
    }

    @Test
    @Unroll
    def "succeeds when an optional reference collection attribute value is #condition in the payload"() {
        given: 'an optional reference collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('countries').references('Country').multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Country              ; Country",
                "INSERT_UPDATE IntegrationObjectItemAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                "                                            ; $IO:Country                ; code                        ; Country:isocode",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER",
                "                                                          ; $IO:Product                ; availableIn                 ; $CLASS_ASSIGNMENT_PREFIX:countries; $IO:Country")
        and: 'a request with body'
        def request = postRequest().withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the product created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present

        where:
        condition | payload
        'null'    | product(PRODUCT_CODE).withField('availableIn', null as List)
        'empty'   | product(PRODUCT_CODE).withField('availableIn', [])
        'missing' | product(PRODUCT_CODE)
    }

    @Test
    def 'fails when at least one reference collection attribute value refers a non-existent item in the payload'() {
        given: 'a reference collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('countries').references('Country').multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Country              ; Country",
                "INSERT_UPDATE IntegrationObjectItemAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                "                                            ; $IO:Country                ; code                        ; Country:isocode",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER",
                "                                                          ; $IO:Product                ; countries                   ; $CLASS_ASSIGNMENT_PREFIX:countries; $IO:Country")
        and: 'one country exists'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Country; isocode[unique = true]',
                '                     ; de')
        and: 'the request body refers an existing and non-existent country'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('countries', [
                json().withCode('de'),
                json().withCode('fr')])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is correctly reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains 'countries'
        json.getString('error.innererror') == "$VERSION|$CATALOG|$PRODUCT_CODE"
        and: 'the product not created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).empty
        and: 'the missing country not created'
        IntegrationTestUtil.findAny(CountryModel, { it.isocode == 'fr' }).empty
    }

    @Test
    def 'succeeds when all reference collection attribute values refer existing items in the payload'() {
        given: 'a reference collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('countries').references('Country').multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Country              ; Country",
                "INSERT_UPDATE IntegrationObjectItemAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                "                                            ; $IO:Country                ; code                        ; Country:isocode",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER",
                "                                                          ; $IO:Product                ; countries                   ; $CLASS_ASSIGNMENT_PREFIX:countries; $IO:Country")
        and: 'two countries exists'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Country; isocode[unique = true]',
                '                     ; fr',
                '                     ; de')
        and: 'the request body refers an existing and non-existent country'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('countries', [
                json().withCode('de'),
                json().withCode('fr')])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.countries.__deferred')
        and: 'the product is created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'countries are associated with the created product'
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'countries').collect({ it.isocode }) == ['de', 'fr']
    }

    @Test
    def 'succeeds when a reference collection attribute value refers a non-existent item in the payload but the attribute has autoCreate=true'() {
        given: 'a reference collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('countries').references('Country').mandatory().multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Country              ; Country",
                "INSERT_UPDATE IntegrationObjectItemAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                "                                            ; $IO:Country                ; code                        ; Country:isocode",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER; autoCreate",
                "                                                          ; $IO:Product                ; countries                   ; $CLASS_ASSIGNMENT_PREFIX:countries; $IO:Country        ; true")
        and: 'one country exists'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Country; isocode[unique = true]',
                '                     ; de')
        and: 'the request body refers a not existing country "fr"'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('countries', [
                json().withCode('de'),
                json().withCode('fr')])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.countries.__deferred')
        and: 'the product is created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the missing country is created'
        IntegrationTestUtil.findAny(CountryModel, { it.isocode == 'fr' }).present
        and: 'countries are associated with the created product'
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'countries').collect({ it.isocode }) == ['de', 'fr']
    }

    @Test
    def 'succeeds when a required reference collection attribute value references existing items'() {
        given: 'a mandatory reference collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('countries').references('Country').multiValue().mandatory())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Country              ; Country",
                "INSERT_UPDATE IntegrationObjectItemAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                "                                            ; $IO:Country                ; code                        ; Country:isocode",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER; autoCreate",
                "                                                          ; $IO:Product                ; countries                   ; $CLASS_ASSIGNMENT_PREFIX:countries; $IO:Country        ; true")
        and: 'one country exists'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Country; isocode[unique = true]',
                '                     ; de')
        and: 'the request body refers existing country'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('countries', [json().withCode('de')])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.countries.__deferred')
        and: 'the product is created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'countries are associated with the created product'
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'countries').collect({ it.isocode }) == ['de']
    }

    @Test
    @Unroll
    def "fails when a required value list attribute is #condition in the payload"() {
        given: 'a mandatory enum classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('color').valueList(['Black', 'Silver']).mandatory())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; color                       ; $CLASS_ASSIGNMENT_PREFIX:color")
        and: 'a request with body'
        def request = postRequest().withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is correctly reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_property'
        json.getString('error.message.value').contains 'color'
        json.getString('error.innererror') == "$VERSION|$CATALOG|$PRODUCT_CODE"
        and: 'the product was not created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).empty

        where:
        condition | payload
        'null'    | product(PRODUCT_CODE).withField('color', null)
        'missing' | product(PRODUCT_CODE)
    }

    @Test
    def 'succeeds when payload contains a value absent in the attribute value list'() {
        given: 'an enum classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('color').valueList(['Titanium']))
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; finish                      ; $CLASS_ASSIGNMENT_PREFIX:color")
        and: 'a request with body containing value different from what is defined in the value list'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('finish', 'Chrome')

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.finish') == 'Chrome'
        and: 'the product created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the product has attribute assigned'
        IntegrationTestUtil.getClassificationAttributeValue(PRODUCT_CODE, 'color').code == 'Chrome'
    }

    @Test
    @Unroll
    def "succeeds when an optional value list attribute value is #condition in the payload"() {
        given: 'an optional enum classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('color').valueList(['Red']))
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; finish                      ; $CLASS_ASSIGNMENT_PREFIX:color")
        and: 'a request with body'
        def request = postRequest().withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the product is created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present

        where:
        condition | payload
        'null'    | product(PRODUCT_CODE).withField('finish', null)
        'missing' | product(PRODUCT_CODE)
    }

    @Test
    def 'succeeds when payload attribute value is from the value list for that attribute'() {
        given: 'an enum classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('color').valueList(['Black', 'Silver']).mandatory())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; color                       ; $CLASS_ASSIGNMENT_PREFIX:color")
        and: 'a request with body containing a value from the value list'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('color', 'Silver')

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.color') == 'Silver'
        and: 'the product created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the product has attribute assigned'
        IntegrationTestUtil.getClassificationAttributeValue(PRODUCT_CODE, 'color').code == 'Silver'
    }

    @Test
    @Unroll
    def "fails when a required value list collection attribute is #condition in the payload"() {
        given: 'a mandatory enum collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('color').valueList(['Blue']).mandatory().multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; color                       ; $CLASS_ASSIGNMENT_PREFIX:color")
        and: 'a request with body'
        def request = postRequest().withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is correctly reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains 'color'
        json.getString('error.innererror') == "$VERSION|$CATALOG|$PRODUCT_CODE"
        and: 'the product was not created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).empty

        where:
        condition | payload
        'null'    | product(PRODUCT_CODE).withField('color', null as List)
        'missing' | product(PRODUCT_CODE)
    }

    @Test
    def 'succeeds when a required value list collection attribute is empty in the payload'() {
        given: 'a mandatory enum collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('color').valueList(['Blue']).mandatory().multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; color                       ; $CLASS_ASSIGNMENT_PREFIX:color")
        and: 'a request with body'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('color', [])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the product was created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
    }

    @Test
    def 'succeeds when payload collection contains at least one value that is not in the attribute value list'() {
        given: 'an enum collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('color').valueList(['Black', 'White']).multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; finish                      ; $CLASS_ASSIGNMENT_PREFIX:color")
        and: 'a request with body containing value different from what is defined in the value list'
        def request = postRequest().withBody product(PRODUCT_CODE).withFieldValues('finish',
                json().withField('value', 'White'),
                json().withField('value', 'Transparent'))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.finish.__deferred')
        and: 'the product created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the product has attribute assigned'
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'color').collect({
            it.code
        }).containsAll(['White', 'Transparent'])
    }

    @Test
    @Unroll
    def "succeeds when an optional value list collection attribute value is #condition in the payload"() {
        given: 'an optional enum collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('color').valueList(['Red']).multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; finish                      ; $CLASS_ASSIGNMENT_PREFIX:color")
        and: 'a request with body'
        def request = postRequest().withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the product is created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present

        where:
        condition | payload
        'null'    | product(PRODUCT_CODE).withField('finish', null as List)
        'empty'   | product(PRODUCT_CODE).withField('finish', [])
        'missing' | product(PRODUCT_CODE)
    }

    @Test
    def 'succeeds when payload attribute collection contains only values from the attribute value list'() {
        given: 'an enum collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('color').valueList(['Chrome', 'Silver']).mandatory().multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; color                       ; $CLASS_ASSIGNMENT_PREFIX:color")
        and: 'a request with body containing a value from the value list'
        def request = postRequest().withBody product(PRODUCT_CODE).withFieldValues('color',
                json().withField('value', 'Silver'),
                json().withField('value', 'Chrome'))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.color.__deferred')
        and: 'the product created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the product has attribute assigned'
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'color').collect({
            it.code
        }).containsAll(['Silver', 'Chrome'])
    }

    @Test
    def 'succeeds when a non-existing reference collection attribute value refers back to the parent item when autocreate=true'() {
        given: 'a reference collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('products').references('Product').multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $attributeDescriptor',
                "                                            ; $IO:Product         ; name                        ; Product:name",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER; autoCreate",
                "                                                          ; $IO:Product                ; products                    ; $CLASS_ASSIGNMENT_PREFIX:products ; $IO:Product        ; true")
        and: 'the request body refers a not existing country "fr"'
        def prodName = 'outerProductName'
        def request = postRequest().withBody product(PRODUCT_CODE)
                .withField('name', prodName)
                .withField('products', [
                        product(PRODUCT_CODE)
                ])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.products.__deferred')
        and: 'all expected products are created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).get().name == prodName
        and: 'products are associated with the created product'
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'products').collect({ it.code }) == [PRODUCT_CODE]
    }

    @Test
    def 'succeeds when a reference attribute value refers a non-existent item with autoCreate=true with a nested reference value attribute referring to another non-existent item with autoCreate=true'() {
        given: 'a reference collection classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('product').references('Product'))
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER        ; $RETURN_ITEM_HEADER; autoCreate",
                "                                                          ; $IO:Product                ; product                     ; $CLASS_ASSIGNMENT_PREFIX:product; $IO:Product        ; true")
        and: 'the request body refers back to the same product code'
        def nestedProdBCode = 'productB'
        def nestedProdCCode = 'productC'
        def request = postRequest().withBody product(PRODUCT_CODE)
                .withField('product',
                        product(nestedProdBCode)
                                .withField('product', product(nestedProdCCode))
                )

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.product.__deferred')
        and: 'all expected products are created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        IntegrationTestUtil.findAny(ProductModel, { it.code == nestedProdBCode }).present
        IntegrationTestUtil.findAny(ProductModel, { it.code == nestedProdCCode }).present
        and: 'products are associated with the created product'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).get().getFeatures().size() == 1
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).get().getFeatures().first().value.getCode() == nestedProdBCode
        IntegrationTestUtil.findAny(ProductModel, { it.code == nestedProdBCode }).get().getFeatures().size() == 1
        IntegrationTestUtil.findAny(ProductModel, { it.code == nestedProdBCode }).get().getFeatures().first().value.getCode() == nestedProdCCode
        IntegrationTestUtil.findAny(ProductModel, { it.code == nestedProdCCode }).get().getFeatures() == []
    }

    @Test
    def 'succeeds when posting to an existing product with a an existing referenced classification attribute'() {
        given: 'a reference classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('product').references('Product'))
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER; autoCreate",
                "                                                          ; $IO:Product                ; product                     ; $CLASS_ASSIGNMENT_PREFIX:product  ; $IO:Product        ; true"
        )
        and: 'prod1 & prod2 exist'
        def prod1 = 'prod1'
        def prod2 = 'prod2'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE Product;code[unique=true];catalogVersion(catalog(id),version)[unique=true];",
                "                     ;$prod1           ; $CATALOG:$VERSION",
                "                     ;$prod2           ; $CATALOG:$VERSION"
        )
        def product2 = IntegrationTestUtil.findAny(ProductModel, { it.getCode() == 'prod2' }).orElse(null)

        IntegrationTestUtil.importImpEx(
                '$CATALOG_VERSION_HEADER = catalogVersion(catalog(id), version)',
                '$SYSTEM_VERSION_HEADER = systemVersion(catalog(id), version)',
                '$CLASS_HEADER = classificationClass($CATALOG_VERSION_HEADER, code)',
                '$ATTRIBUTE_HEADER = classificationAttribute($SYSTEM_VERSION_HEADER, code)',
                "\$CLASS_ATTR_ASSIGNMENT_HEADER = classificationAttributeAssignment($CLASS_HEADER, \$ATTRIBUTE_HEADER)",
                "\$product = product($CATALOG_VERSION_HEADER, code)",
                'INSERT_UPDATE ProductFeature; $product[unique = true] ; $CLASS_ATTR_ASSIGNMENT_HEADER[unique = true]; qualifier                                   ; value[mode = append, translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]',
                "                            ; $CATALOG:$VERSION:$prod1; $CLASS_ASSIGNMENT_PREFIX:product            ; $CLASS_SYSTEM/$CLASS_VERSION/Bicycle.product; \"reference, ${product2.getPk().getLongValue()}\"",
                "INSERT_UPDATE ClassificationClass; code[unique = true]  ; $CATALOG_VERSION_HEADER[unique = true]; products($CATALOG_VERSION_HEADER, code)[append=true]",
                "                                 ; $CLASSIFICATION_CLASS; $CLASS_SYSTEM_VERSION                 ; $CATALOG:$VERSION:$prod1"
        )

        and: 'the request body refers to prod2'
        def request = postRequest().withBody product(prod1)
                .withField('product', product(prod2))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.product.__deferred')
        and: 'all expected products are created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod1 }).present
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod2 }).present
        and: 'outer product is associated with referenced product'
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod1 }).get().getFeatures().size() == 1
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod1 }).get().getFeatures().first().value.getCode() == prod2
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod2 }).get().getFeatures() == []
    }


    @Test
    def 'succeeds when posting to an existing product with a an existing referenced collection classification attribute'() {
        given: 'a reference classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('products').references('Product').multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER; autoCreate",
                "                                                          ; $IO:Product                ; products                    ; $CLASS_ASSIGNMENT_PREFIX:products; $IO:Product        ; true"
        )
        and: 'prod1 & prod2 exist'
        def prod1 = 'prod1'
        def prod2 = 'prod2'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE Product;code[unique=true];catalogVersion(catalog(id),version)[unique=true];",
                "                     ;$prod1           ; $CATALOG:$VERSION",
                "                     ;$prod2           ; $CATALOG:$VERSION"
        )
        def product2 = IntegrationTestUtil.findAny(ProductModel, { it.getCode() == 'prod2' }).orElse(null)

        IntegrationTestUtil.importImpEx(
                '$CATALOG_VERSION_HEADER = catalogVersion(catalog(id), version)',
                '$SYSTEM_VERSION_HEADER = systemVersion(catalog(id), version)',
                '$CLASS_HEADER = classificationClass($CATALOG_VERSION_HEADER, code)',
                '$ATTRIBUTE_HEADER = classificationAttribute($SYSTEM_VERSION_HEADER, code)',
                "\$CLASS_ATTR_ASSIGNMENT_HEADER = classificationAttributeAssignment($CLASS_HEADER, \$ATTRIBUTE_HEADER)",
                "\$product = product($CATALOG_VERSION_HEADER, code)",
                'INSERT_UPDATE ProductFeature; $product[unique = true]; $CLASS_ATTR_ASSIGNMENT_HEADER[unique = true]; qualifier                                    ; value[mode = append, translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]',
                "                            ; $CATALOG:$VERSION:$prod1  ; $CLASS_ASSIGNMENT_PREFIX:products           ; $CLASS_SYSTEM/$CLASS_VERSION/Bicycle.products; \"reference, ${product2.getPk().getLongValue()}\"",
                "INSERT_UPDATE ClassificationClass; code[unique = true]  ; $CATALOG_VERSION_HEADER[unique = true]; products($CATALOG_VERSION_HEADER, code)[append=true]",
                "                                 ; $CLASSIFICATION_CLASS; $CLASS_SYSTEM_VERSION                 ; $CATALOG:$VERSION:$prod1"
        )

        and: 'the request body refers to the same referenced product'
        def request = postRequest().withBody product(prod1)
                .withField('products', [product(prod2)])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.products.__deferred')
        and: 'all expected products are created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod1 }).present
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod2 }).present
        and: 'outer product is associated with referenced product'
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod1 }).get().getFeatures().size() == 1
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod1 }).get().getFeatures().first().value.getCode() == prod2
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod2 }).get().getFeatures() == []
    }

    @Test
    def 'succeeds when posting to an existing product with existing referenced collection classification attribute'() {
        given: 'a reference classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('products').references('Product').multiValue())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER; autoCreate",
                "                                                          ; $IO:Product                ; products                    ; $CLASS_ASSIGNMENT_PREFIX:products; $IO:Product         ; true"
        )
        and: 'prod1 & prod2 exist'
        def prod1 = 'prod1'
        def prod2 = 'prod2'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE Product;code[unique=true];catalogVersion(catalog(id),version)[unique=true];",
                "                     ;$prod1           ; $CATALOG:$VERSION",
                "                     ;$prod2           ; $CATALOG:$VERSION"
        )

        and: 'the request body refers to the same referenced product'
        def request = postRequest().withBody product(prod1)
                .withField('products', [product(prod2)])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.products.__deferred')
        and: 'all expected products are created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod1 }).present
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod2 }).present
        and: 'outer product is associated with referenced product'
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod1 }).get().getFeatures().size() == 1
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod1 }).get().getFeatures().first().value.getCode() == prod2
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod2 }).get().getFeatures() == []
    }

    @Test
    def 'succeeds when posting to an existing product with a different existing referenced classification attribute value'() {
        given: 'a reference classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('product').references('Product'))
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER",
                "                                                          ; $IO:Product                ; product                     ; $CLASS_ASSIGNMENT_PREFIX:product  ; $IO:Product        "
        )
        and: 'prod1, prod2 & prod3 exist'
        def prod1 = 'prod1'
        def prod2 = 'prod2'
        def prod3 = 'prod3'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE Product;code[unique=true];catalogVersion(catalog(id),version)[unique=true];",
                "                     ;$prod1           ; $CATALOG:$VERSION",
                "                     ;$prod2           ; $CATALOG:$VERSION",
                "                     ;$prod3           ; $CATALOG:$VERSION"
        )
        and: 'prod1 has relatedProduct attribute set to prod2'
        def product2 = IntegrationTestUtil.findAny(ProductModel, { it.getCode() == 'prod2' }).get()
        IntegrationTestUtil.importImpEx(
                '$CATALOG_VERSION_HEADER = catalogVersion(catalog(id), version)',
                '$SYSTEM_VERSION_HEADER = systemVersion(catalog(id), version)',
                '$ATTRIBUTE_HEADER = classificationAttribute($SYSTEM_VERSION_HEADER, code)',
                "\$CLASS_ATTR_ASSIGNMENT_HEADER = classificationAttributeAssignment($CLASS_HEADER, \$ATTRIBUTE_HEADER)",
                "\$product = product($CATALOG_VERSION_HEADER, code)",
                'INSERT_UPDATE ProductFeature; $product[unique = true] ; $CLASS_ATTR_ASSIGNMENT_HEADER[unique = true]; qualifier                                   ; value[mode = append, translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]',
                "                            ; $CATALOG:$VERSION:$prod1; $CLASS_ASSIGNMENT_PREFIX:product            ; $CLASS_SYSTEM/$CLASS_VERSION/Bicycle.product; \"reference, ${product2.getPk().getLongValue()}\"",
                "INSERT_UPDATE ClassificationClass; code[unique = true]  ; $CATALOG_VERSION_HEADER[unique = true]; products($CATALOG_VERSION_HEADER, code)[append=true]",
                "                                 ; $CLASSIFICATION_CLASS; $CLASS_SYSTEM_VERSION                 ; $CATALOG:$VERSION:$prod1"
        )
        and: 'the request body refers to the same referenced product'
        def request = postRequest().withBody product(prod1)
                .withField('product', product(prod3))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'response contains the attribute value'
        def json = JsonObject.createFrom response.entityAsStream
        json.getObject('d.product.__deferred')
        and: 'outer product is associated with referenced product'
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod1 }).get().getFeatures().size() == 1
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod1 }).get().getFeatures().first().value.getCode() == prod3
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod2 }).get().getFeatures() == []
        IntegrationTestUtil.findAny(ProductModel, { it.code == prod3 }).get().getFeatures() == []
    }

    @Test
    def 'succeeds when posting classification attributes that are in a hierarchy'() {
        given: 'color and size classification classes where color is the supercategory of size'
        ClassificationBuilder.classification()
                .withSystem(CLASS_SYSTEM)
                .withVersion(CLASS_VERSION)
                .withClassificationClass("colorClass")
                .withAttribute(
                        ClassificationBuilder.attribute().withName('color').string())
                .setup()
        ClassificationBuilder.classification()
                .withSystem(CLASS_SYSTEM)
                .withVersion(CLASS_VERSION)
                .withClassificationClass("sizeClass")
                .withAttribute(
                        ClassificationBuilder.attribute().withName('size').string())
                .setup()

        IntegrationTestUtil.importImpEx(
                "UPDATE ClassificationClass; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]; supercategories(code)",
                "                                 ; sizeClass          ; $CLASS_SYSTEM_VERSION                              ; colorClass")

        and: 'classification attributes exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; color               ; $CLASS_SYSTEM_VERSION:colorClass:$CLASS_SYSTEM_VERSION:color",
                "                                                          ; $IO:Product                ; size                ; $CLASS_SYSTEM_VERSION:sizeClass:$CLASS_SYSTEM_VERSION:size")

        and: 'a request with body'
        def request = postRequest().withBody product(PRODUCT_CODE)
                .withField('color', "red")
                .withField('size', 'small')

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).present
        and: 'the classification attribute is assigned in the created product'
        IntegrationTestUtil.getClassificationAttributeValue(PRODUCT_CODE, 'color') == 'red'
        IntegrationTestUtil.getClassificationAttributeValue(PRODUCT_CODE, 'size') == 'small'

        cleanup: 'remove sizeClass sub-category so colorClass can be remove'
        IntegrationTestUtil.remove(ClassificationClassModel, { it.code == 'sizeClass' })
    }

    @Test
    def "reference product is not persisted when request fails due to missing navigation property on the toplevel product"() {
        given: 'a reference product classification attribute'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('product').references('Product')).setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $IO                                   ; Unit                 ; Unit",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER          ; $RETURN_ITEM_HEADER; autoCreate",
                "                                                          ; $IO:Product                ; product                     ; $CLASS_ASSIGNMENT_PREFIX:product  ; $IO:Product        ; true",
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $attributeDescriptor  ; returnIntegrationObjectItem(integrationObject(code), code)',
                "                                            ; $IO:Unit            ; code                        ; Unit:code",
                "                                            ; $IO:Product         ; unit                        ; Product:unit          ; $IO:Unit"
        )
        and: 'the request body refers a non-existing reference product'
        def referencedProdCode = 'bike2'
        def request = postRequest().withBody product(PRODUCT_CODE).withField('unit', json().withCode('non-existing-unit'))
                .withField('product', product(referencedProdCode))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is correctly reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains 'unit'
        and: 'the product is not created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).empty
        and: 'the referenced product is not created'
        IntegrationTestUtil.findAny(ProductModel, { it.code == referencedProdCode }).empty
    }

    ODataRequestBuilder postRequest() {
        ODataRequestBuilder.oDataPostRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(IO)
                        .withEntitySet('Products'))
                .withContentType(APPLICATION_JSON_VALUE)
                .withAccepts(APPLICATION_JSON_VALUE)
    }

    JsonBuilder product(String code) {
        json()
                .withCode(code)
                .withField('catalogVersion', json()
                        .withField('version', VERSION)
                        .withField('catalog', json().withId(CATALOG)))
    }
}