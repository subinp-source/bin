/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.EndpointModel
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.category.model.CategoryModel
import de.hybris.platform.core.model.product.ProductModel
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ODataExpandEntityIntegrationTest extends ServicelayerTransactionalSpockSpecification {

    private static final String TEST_IO = 'ExpandTest'
    private static final String TEST_IO_MAP = 'ExpandTestMap'
    private static final def CATALOG = 'Expand'
    private static final def VERSION = 'Test'

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setupSpec() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "                               ; $TEST_IO; INBOUND",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Product            ; Product",
                "                                   ; $TEST_IO                              ; Catalog            ; Catalog",
                "                                   ; $TEST_IO                              ; CatalogVersion     ; CatalogVersion",
                "                                   ; $TEST_IO                              ; Category           ; Category",
                "                                   ; $TEST_IO                              ; ClassificationClass ; ClassificationClass",
                "                                   ; $TEST_IO	                            ; ClassificationSystem  ; ClassificationSystem",
                "                                   ; $TEST_IO                              ; ClassificationSystemVersion; ClassificationSystemVersion",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]    ; attributeName[unique = true]; $attributeDescriptor            ; returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Catalog        ; id                          ; Catalog:id                      ;",
                "                                            ; $TEST_IO:CatalogVersion ; catalog                     ; CatalogVersion:catalog          ; $TEST_IO:Catalog",
                "                                            ; $TEST_IO:CatalogVersion ; version                     ; CatalogVersion:version          ;",
                "                                            ; $TEST_IO:Product        ; code                        ; Product:code                    ;",
                "                                            ; $TEST_IO:Product        ; catalogVersion              ; Product:catalogVersion          ; $TEST_IO:CatalogVersion",
                "                                            ; $TEST_IO:Product        ; supercategories             ; Product:supercategories         ; $TEST_IO:Category",
                "                                            ; $TEST_IO:Product        ; specialTreatmentClasses     ; Product:specialTreatmentClasses",
                "                                            ; $TEST_IO:Category       ; code                        ; Category:code                   ;",
                "                                            ; $TEST_IO:Category       ; products                    ; Category:products               ; $TEST_IO:Product",
                "                                            ; $TEST_IO:Category       ; catalogVersion              ; Category:catalogVersion         ; $TEST_IO:CatalogVersion",
                "                                            ; $TEST_IO:Product        ; classificationClasses       ; Product:classificationClasses   ; $TEST_IO:ClassificationClass",
                "                                            ; $TEST_IO:Category       ; allSupercategories          ; Category:allSupercategories     ; $TEST_IO:Category",
                "                                            ; $TEST_IO:ClassificationClass         ; catalogVersion ; ClassificationClass:catalogVersion  ; $TEST_IO:ClassificationSystemVersion ; true",
                "                                            ; $TEST_IO:ClassificationClass         ; code           ; ClassificationClass:code            ;                                      ; true",
                "                                            ; $TEST_IO:ClassificationSystem        ; id             ; ClassificationSystem:id             ;                                      ; true",
                "                                            ; $TEST_IO:ClassificationSystemVersion ; version        ; ClassificationSystemVersion:version ;                                      ; true",
                "                                            ; $TEST_IO:ClassificationSystemVersion ; catalog        ; ClassificationSystemVersion:catalog ; $TEST_IO:ClassificationSystem        ; true",
                'INSERT_UPDATE Catalog; id[unique = true]',
                "                     ; $CATALOG",
                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]',
                "                            ; $CATALOG                  ; $VERSION"
        )
    }

    def cleanup() {
        IntegrationTestUtil.removeAll ClassificationClassModel
        IntegrationTestUtil.removeAll ProductModel
        IntegrationTestUtil.removeAll CategoryModel
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version == VERSION }
        IntegrationTestUtil.removeSafely CatalogModel, { it.version == CATALOG }
    }

    @Test
    def "Expands nested navigation properties"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                "                     ; prod1              ; $CATALOG:$VERSION"
        )
        def context = oDataContext("Products", ['$expand': 'catalogVersion/catalog'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getCollection("d.results").size() == 1
        json.getString("d.results[0].catalogVersion.version") == VERSION
        json.getString("d.results[0].catalogVersion.catalog.id") == CATALOG
    }

    @Test
    def "Expands map navigation properties from ConsumedDestination"() {
        given:
        def key1 = 'key1'
        def val1 = 'value1'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]   ; integrationType(code)',
                "                                      ; $TEST_IO_MAP          ; ",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)           ; root[default = false]; itemTypeMatch(code)',
                "                                   ; $TEST_IO_MAP                          ; Endpoint           ; Endpoint             ;                      ; ;",
                "                                   ; $TEST_IO_MAP                          ; ConsumedDestination; ConsumedDestination  ; true                 ; ;",
                "                                   ; $TEST_IO_MAP                          ; DestinationTarget  ; DestinationTarget    ; ; ;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]',
                "                                            ; $TEST_IO_MAP:Endpoint                                              ; id                          ; Endpoint:id                                        ;                                                           ; true                   ;",
                "                                            ; $TEST_IO_MAP:Endpoint                                              ; name                        ; Endpoint:name                                      ;                                                           ; true                   ;",
                "                                            ; $TEST_IO_MAP:Endpoint                                              ; version                     ; Endpoint:version                                   ;                                                           ; true                   ;",
                "                                            ; $TEST_IO_MAP:Endpoint                                              ; specUrl                     ; Endpoint:specUrl                                   ;                                                           ; true                   ;",
                "                                            ; $TEST_IO_MAP:ConsumedDestination                                   ; endpoint                    ; ConsumedDestination:endpoint                       ; $TEST_IO_MAP:Endpoint                                     ; true                   ; true",
                "                                            ; $TEST_IO_MAP:ConsumedDestination                                   ; url                         ; ConsumedDestination:url                            ;                                                           ; true                   ;",
                "                                            ; $TEST_IO_MAP:ConsumedDestination                                   ; destinationTarget           ; ConsumedDestination:destinationTarget              ; $TEST_IO_MAP:DestinationTarget                            ; true                   ; true",
                "                                            ; $TEST_IO_MAP:ConsumedDestination                                   ; id                          ; ConsumedDestination:id                             ;                                                           ; true                   ;",
                "                                            ; $TEST_IO_MAP:ConsumedDestination                                   ; additionalProperties        ; ConsumedDestination:additionalProperties           ;                                                           ;                        ;",
                "                                            ; $TEST_IO_MAP:DestinationTarget                                     ; id                          ; DestinationTarget:id                               ;                                                           ; true                   ;",
        )
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Endpoint; id[unique=true] ; name          ; version[unique=true] ; description                ; specUrl                ; specData ; extensionName',
                "                             ; testEndpoint1   ; testEndpoint1 ; testEndpoint1        ; Integration api endpoint   ; https://www.schema.com ;          ;",
                'INSERT_UPDATE DestinationTarget; id[unique=true] ;destinationChannel(code)[default=DEFAULT];template',
                "                               ; Default_Template;                                         ;true",
                'INSERT_UPDATE ConsumedDestination;id[unique=true]; url                 ; endpoint(id)[unique=true]; additionalProperties(key,value)[map-delimiter=|] ; destinationTarget(id)[default=Default_Template] ; active[default=true]',
                "                                 ; testCD        ; https://www.sap.com ; testEndpoint1            ; $key1->$val1                                     ; Default_Template                                ;"
        )
        def context = oDataContext("ConsumedDestinations", ['$expand': 'additionalProperties'], TEST_IO_MAP)

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getString("d.results[0].additionalProperties.results[0]key").contains(key1)
        json.getString("d.results[0].additionalProperties.results[0].value").contains(val1)

        cleanup:
        IntegrationTestUtil.removeAll ConsumedDestinationModel
        IntegrationTestUtil.removeAll EndpointModel
        IntegrationTestUtil.removeAll DestinationTargetModel
    }

    @Test
    def "Expands nested navigation properties when ClassificationClass instance is used as Category for the product"() {
        def CATALOG1 = "Catalog01"
        def SUPERCATEGORY = "rootCategory"
        def SUBCATEGORY = "childCategory"
        def CATALOGVERSIONHEADER = "catalogversion(catalog(id[default = $CATALOG1]), version[default = $VERSION])[unique = true, default = $CATALOG1:$VERSION]"
        def SUPERCATEGORIESHEADER = "supercategories(code, $CATALOGVERSIONHEADER)"

        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE ClassificationSystem; id[unique = true]; name[lang = en]',
                "                                         ; $CATALOG1        ; $CATALOG1       ",

                'INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version[unique = true]',
                "                                         ; $CATALOG1                 ; $VERSION",

                "INSERT_UPDATE ClassificationClass; code[unique = true]; name[lang = en]    ; $CATALOGVERSIONHEADER       ; $SUPERCATEGORIESHEADER",
                "                                 ; $SUPERCATEGORY     ; $SUPERCATEGORY     ;                             ; ",
                "                                 ; $SUBCATEGORY       ; $SUBCATEGORY       ;                             ; $SUPERCATEGORY",

                "INSERT_UPDATE Product; code[unique = true]  ; name  ; $SUPERCATEGORIESHEADER ; $CATALOGVERSIONHEADER",
                "                     ; pr_1                 ; pr_1  ; $SUBCATEGORY           ; "

        )

        def context = oDataContext("Products", ['$expand': 'supercategories/allSupercategories'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getCollection("d.results").size() == 1
        json.exists("d.results[0].supercategories.results[?(@.code == 'childCategory')]")
        json.getCollection("d.results[0].supercategories.results[?(@.code == 'childCategory')]").size() == 1
        json.exists("d.results[0].supercategories.results[?(@.code == 'childCategory')].allSupercategories.results[?(@.code == 'rootCategory')]]")
        json.getCollection("d.results[0].supercategories.results[?(@.code == 'childCategory')].allSupercategories.results[?(@.code == 'rootCategory')]]").size() == 1
    }

    @Test
    def "Expands multiple navigation properties at a time"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)',
                "                      ; test               ; $CATALOG:$VERSION",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); supercategories(code)',
                "                     ; pr-1               ; $CATALOG:$VERSION                   ; test"
        )
        def context = oDataContext("Products", ['$expand': 'catalogVersion,supercategories'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getCollection("d.results").size() == 1
        json.getString("d.results[0].catalogVersion.version") == VERSION
        !json.exists("d.results[0].supercategories.results[?(@.code == 'test')].catalogVersion.version")
        json.exists "d.results[0].supercategories.results[?(@.code == 'test')].catalogVersion.__deferred"
    }

    @Test
    def "Expands map navigation properties for all Products"() {
        given:
        def key1 = 'k1'
        def val1 = 'v1'
        def key2 = 'k2'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)',
                "                      ; test               ; $CATALOG:$VERSION",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); supercategories(code); specialTreatmentClasses(key, value)[map-delimiter = |]',
                "                     ; pr-1               ; $CATALOG:$VERSION                   ; test                 ; $key1->$val1",
                "                     ; pr-2               ; $CATALOG:$VERSION                   ; test                 ; $key2->"
        )
        def context = oDataContext("Products", ['$expand': 'specialTreatmentClasses'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getCollection("d.results").size() == 2
        json.getCollectionOfObjects("d.results[?(@.code == 'pr-1')].specialTreatmentClasses.results").size() == 1
        json.getString("d.results[?(@.code == 'pr-1')].specialTreatmentClasses.results[0].key").contains(key1)
        json.getString("d.results[?(@.code == 'pr-1')].specialTreatmentClasses.results[0].value").contains(val1)
        json.getCollectionOfObjects("d.results[?(@.code == 'pr-2')].specialTreatmentClasses.results").size() == 1
        json.getString("d.results[?(@.code == 'pr-2')].specialTreatmentClasses.results[0].key").contains(key2)
        json.getString("d.results[?(@.code == 'pr-2')].specialTreatmentClasses.results[0].value").contains('null')
    }

    @Test
    def "Expanded property is empty when no referenced items exist"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)',
                "                      ; someCategory       ; $CATALOG:$VERSION "
        )
        def context = oDataContext("Categories", ['$expand': 'products'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getCollection("d.results").size() == 1
        json.getCollection("d.results[0].products.results").isEmpty()
    }

    @Test
    def "Expanded map navigation property is empty when property is null"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                "                     ; pr-1               ; $CATALOG:$VERSION")
        def context = oDataContext("Products", ['$expand': 'specialTreatmentClasses'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getCollection("d.results").size() == 1
        json.getCollectionOfObjects("d.results[0].specialTreatmentClasses.results").isEmpty()
    }

    @Test
    def "Expand collection navigation property for GET for integration object by integration key"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)',
                "                      ; test               ; $CATALOG:$VERSION",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); supercategories(code)',
                "                     ; pr-1               ; $CATALOG:$VERSION                   ; test",
                "                     ; pr-2               ; $CATALOG:$VERSION                   ; test")
        def context = oDataContext("Categories('$VERSION%7C$CATALOG%7Ctest')", ['$expand': 'products'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getString("d.integrationKey") == "$VERSION|$CATALOG|test"
        json.getCollection("d.products.results").size() == 2
    }

    @Test
    def "Expand map navigation property for GET for integration object by integration key"() {
        given:
        def key1 = 'k1'
        def val1 = 'v1'
        def key2 = 'k2'
        def val2 = 'v2'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); specialTreatmentClasses(key, value)[map-delimiter = |]',
                "                     ; pr-1               ; $CATALOG:$VERSION                   ; $key1->$val1|$key2->$val2")
        def context = oDataContext("Products", ['$expand': 'specialTreatmentClasses'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getCollection("d.results").size() == 1
        json.getString("d.results[0].integrationKey") == "$VERSION|$CATALOG|pr-1"
        json.getCollection("d.results[0].specialTreatmentClasses.results").size() == 2
        json.getCollectionOfObjects("d.results[0].specialTreatmentClasses.results[*].key").containsAll(key1, key2)
        json.getCollectionOfObjects("d.results[0].specialTreatmentClasses.results[*].value").containsAll(val1, val2)
    }

    @Test
    def "Expand single navigation property for GET for integration object by integration key"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)',
                "                      ; test               ; $CATALOG:$VERSION",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); supercategories(code)',
                "                     ; pr-1               ; $CATALOG:$VERSION                   ; test")
        def context = oDataContext("Categories('$VERSION%7C$CATALOG%7Ctest')", ['$expand': 'catalogVersion'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getString("d.integrationKey") == "$VERSION|$CATALOG|test"
        json.getString("d.catalogVersion.version") == VERSION
    }

    @Test
    def "Expand nested single navigation property for GET for integration object by integration key"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)',
                "                      ; test               ; $CATALOG:$VERSION",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); supercategories(code)',
                "                     ; pr-1               ; $CATALOG:$VERSION                   ; test")
        def context = oDataContext("Categories('$VERSION%7C$CATALOG%7Ctest')", ['$expand': 'catalogVersion/catalog'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getString("d.integrationKey") == "$VERSION|$CATALOG|test"
        json.getString("d.catalogVersion.version") == VERSION
        json.getString("d.catalogVersion.catalog.id") == CATALOG
    }

    @Test
    def "Expand nested collection navigation property for GET for integration object by integration key"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)',
                "                      ; test               ; $CATALOG:$VERSION",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); supercategories(code)',
                "                     ; pr-1               ; $CATALOG:$VERSION                   ; test")
        def context = oDataContext("Categories('$VERSION%7C$CATALOG%7Ctest')", ['$expand': 'products/catalogVersion'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getString("d.integrationKey") == "$VERSION|$CATALOG|test"
        json.getCollection("d.products.results").size() == 1
        json.getString("d.products.results[0].catalogVersion.version") == VERSION
    }

    @Test
    def "Recursive access to properties for GET for integration object by integration key"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)',
                "                      ; test               ; $CATALOG:$VERSION",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); supercategories(code)',
                "                     ; pr-1               ; $CATALOG:$VERSION                   ; test")
        def context = oDataContext("Categories('$VERSION%7C$CATALOG%7Ctest')", ['$expand': 'products/supercategories'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getString("d.integrationKey") == "$VERSION|$CATALOG|test"
        json.getCollectionOfObjects("d.products.results[*].code") == ['pr-1']
        json.getCollectionOfObjects("d.products.results[0].supercategories.results[*].code") == ['test']
    }

    @Test
    def "Recursive access to properties for GET for multiple Integration Objects"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)',
                "                      ; test               ; $CATALOG:$VERSION",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); supercategories(code)',
                "                     ; pr-1               ; $CATALOG:$VERSION                   ; test")
        def context = oDataContext("Categories", ['$expand': 'products/supercategories'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getCollectionOfObjects("d.results[*].products.results[*].code") == ['pr-1']
        json.getCollectionOfObjects("d.results[0].products.results[0].supercategories.results[*].code") == ['test']
    }

    ODataContext oDataContext(String entitySetName, Map params) {
        def request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withParameters(params)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(TEST_IO)
                        .withEntitySet(entitySetName))
                .build()

        contextGenerator.generate request
    }

    ODataContext oDataContext(String entitySetName, Map params, String serviceName) {
        def request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withParameters(params)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(serviceName)
                        .withEntitySet(entitySetName))
                .build()

        contextGenerator.generate request
    }
}
