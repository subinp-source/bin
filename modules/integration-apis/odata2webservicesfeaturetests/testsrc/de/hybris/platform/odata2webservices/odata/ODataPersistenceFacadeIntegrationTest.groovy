/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel
import de.hybris.platform.category.model.CategoryModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.test.TestItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItemModel
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpHeaders
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.*
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

/**
 * Tests for entity persistence feature scenarios.
 */
@IntegrationTest
class ODataPersistenceFacadeIntegrationTest extends ServicelayerTransactionalSpockSpecification {
    private static final def ERROR_CODE_PATH = 'error.code'
    private static final def ERROR_MSG_PATH = 'error.message.value'
    private static final def ENTITYSET = 'Products'
    private static final def PRODUCT_CODE = 'Test'
    private static final def SERVICE_NAME = 'ODataPersistenceFacadeIntegrationTestIO'
    private static final def PRODUCT_NAME_ENGLISH = 'EnglishProduct'
    private static final def PRODUCT_NAME_GERMAN = 'GermanProduct'
    private static final def CATALOG_ID = 'Default'
    private static final def CATALOG_INVALID_ID = 'Invalid'
    private static final def CATALOG_VERSION = 'Staged'
    private static final def INTEGRATION_KEY = "$CATALOG_VERSION|$CATALOG_ID|$PRODUCT_CODE"
    private static final String IO = "IO1"
    private static final String IO_ATTR = "uid"
    private static final String IO_ATTR_VALUE = "adminGroup"
    private static final String IO_ITEM = "PrincipalGroups"

    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setup() {
        createCoreData()
        // create product metadata
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $SERVICE_NAME      ",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $SERVICE_NAME                         ; Product            ; Product",
                "                                   ; $SERVICE_NAME                         ; Catalog            ; Catalog",
                "                                   ; $SERVICE_NAME                         ; CatalogVersion     ; CatalogVersion",
                "                                   ; $SERVICE_NAME                         ; Category           ; Category",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$name=attributeName',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]        ; $name[unique = true]; $attributeDescriptor    ; $attributeType',
                "                                            ; $SERVICE_NAME:Product       ; code                ; Product:code",
                "                                            ; $SERVICE_NAME:Product       ; catalogVersion      ; Product:catalogVersion;  $SERVICE_NAME:CatalogVersion",
                "                                            ; $SERVICE_NAME:Product       ; name                ; Product:name",
                "                                            ; $SERVICE_NAME:Product       ; description         ; Product:description",
                "                                            ; $SERVICE_NAME:Product       ; supercategories     ; Product:supercategories; $SERVICE_NAME:Category",
                "                                            ; $SERVICE_NAME:Catalog       ; id                  ; Catalog:id",
                "                                            ; $SERVICE_NAME:Catalog       ; version             ; Catalog:version",
                "                                            ; $SERVICE_NAME:CatalogVersion; catalog             ; CatalogVersion:catalog;  $SERVICE_NAME:Catalog;",
                "                                            ; $SERVICE_NAME:CatalogVersion; version             ; CatalogVersion:version",
                "                                            ; $SERVICE_NAME:Category      ; code                ; Category:code",
                "                                            ; $SERVICE_NAME:Category      ; catalogVersion      ; Category:catalogVersion; $SERVICE_NAME:CatalogVersion",
        )
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Catalog; id[unique = true]; name[lang = en]; defaultCatalog',
                '                     ; Default          ; Default        ; true',
                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active',
                '                            ; Default                   ; Staged                ; true')
    }

    def cleanup() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll ProductModel
        IntegrationTestUtil.removeAll TestItemModel
    }

    @Test
    def 'reports error when navigation property refers non-existent item'() {
        given: '"catalog" attribute in the payload refer value that does not exist in the platform'
        def content = json()
                .withCode(PRODUCT_CODE)
                .withField("name", PRODUCT_NAME_ENGLISH)
                .withField("catalogVersion", json()
                        .withField("version", "Staged")
                        .withField("catalog", json().withId(CATALOG_INVALID_ID)))
                .build()

        when: 'the payload is sent for processing'
        def response = handleReq(content)

        then: 'the response reports missing_nav_property error'
        response.status == HttpStatusCodes.BAD_REQUEST
        def json = JsonObject.createFrom response.entityAsStream
        json.getString(ERROR_CODE_PATH) == 'missing_nav_property'
        and: 'payload item was not persisted'
        !getPersistedProduct()
    }

    @Test
    def 'persists new item with valid payload'() {
        given:
        def content = json()
                .withCode(PRODUCT_CODE)
                .withField("catalogVersion", json()
                        .withField("version", CATALOG_VERSION)
                        .withField("catalog", json().withId(CATALOG_ID)))
                .withField("name", PRODUCT_NAME_ENGLISH)
                .build()

        when:
        def response = handleReq(content)

        then:
        response.status == HttpStatusCodes.CREATED
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.name') == PRODUCT_NAME_ENGLISH
        and:
        def persistedModel = getPersistedProduct()
        persistedModel.getName(Locale.ENGLISH) == PRODUCT_NAME_ENGLISH
    }

    @Test
    def 'ignores integration key value in the payload'() {
        given: 'payload contains integrationKey attribute with some value'
        def content = json()
                .withCode(PRODUCT_CODE)
                .withField("catalogVersion", json()
                        .withField("version", CATALOG_VERSION)
                        .withField("catalog", json().withId(CATALOG_ID)))
                .withField("integrationKey", 'does|not|matter')
                .build()

        when:
        def response = handleReq(content)

        then: 'item is successfully persisted'
        response.status == HttpStatusCodes.CREATED
        and: 'the persisted item contains correct integrationKey instead of the submitted in the request'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.integrationKey') == INTEGRATION_KEY
        and: 'item model is saved'
        getPersistedProduct()
    }

    @Test
    def 'item is updated when it already exists'() {
        given:
        "item with code=$PRODUCT_CODE already exists in the system"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; description',
                "; $PRODUCT_CODE; Stored in the database")
        and:
        "payload contains item with code=$PRODUCT_CODE"
        def payload = json()
                .withCode(PRODUCT_CODE)
                .withField('catalogVersion', json()
                        .withField('version', CATALOG_VERSION)
                        .withField('catalog', json().withId(CATALOG_ID)))
                .withField('name', 'Name From Request')
                .build()

        when:
        def response = handleRequest facade, postRequest().withBody(payload).build()

        then: 'response indicates success'
        response.status == HttpStatusCodes.CREATED
        and: 'response body contains updated item'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.name') == 'Name From Request'
        json.getString('d.description') == 'Stored in the database'
        and: 'updated item is stored in the database'
        def persistedModel = getPersistedProduct()
        persistedModel.name == 'Name From Request'
        persistedModel.description == 'Stored in the database'
    }

    @Test
    def 'post of a primitive type not possible'() {
        given: '"entryGroupNumbers" attribute creates a primitive type Integer in metadata'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true];',
                '; PrimitiveTypesIntegrationObject',
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                '; PrimitiveTypesIntegrationObject ; Order      ; Order',
                '; PrimitiveTypesIntegrationObject ; OrderEntry ; OrderEntry',

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                '; PrimitiveTypesIntegrationObject:Order      ; code              ; Order:code                   ;',
                '; PrimitiveTypesIntegrationObject:OrderEntry ; order             ; OrderEntry:order             ; PrimitiveTypesIntegrationObject:Order',
                '; PrimitiveTypesIntegrationObject:OrderEntry ; entryGroupNumbers ; OrderEntry:entryGroupNumbers ;')
        and: 'payload is for the primitive type Integer'
        def request = postRequestBuilder("PrimitiveTypesIntegrationObject", "Integer", APPLICATION_JSON_VALUE)
                .withBody(json().withField("value", 3))
                .build()

        when:
        final ODataResponse response = handleRequest facade, request

        then: 'response indicates not_found error'
        response.status == HttpStatusCodes.NOT_FOUND
        def json = JsonObject.createFrom response.entityAsStream
        json.getString(ERROR_CODE_PATH) == null
        json.getString(ERROR_MSG_PATH).contains 'Integer'
    }

    @Test
    def 'persists item when Accept-Language differs from Content-Language'() {
        given: 'an item with English content exists in the system'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE Product; code[unique = true]; name[lang = 'en']",
                "                     ; $PRODUCT_CODE      ; $PRODUCT_NAME_ENGLISH")

        when: 'request is made with different Content-Language and Accept-Language values'
        def request = postRequest()
                .withContentLanguage(Locale.GERMAN)
                .withAcceptLanguage(Locale.ENGLISH)
                .withBody(product().withField("name", PRODUCT_NAME_GERMAN))
                .build()
        def response = handleRequest facade, request

        then: 'response contains values in Accept-Language locale'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.name') == PRODUCT_NAME_ENGLISH
        and: 'Content-Language value is persisted in the database'
        def model = getPersistedProduct()
        model.getName(Locale.GERMAN) == PRODUCT_NAME_GERMAN
    }

    @Test
    def "responds with the Content-Language including a region when only Content-Language is specified"() {
        given: 'English with US region language exists'
        def region = 'en_US'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Language; isocode[unique=true]',
                "                      ; $region"
        )
        when: 'en_US content is posted and Accept-Language is not specified'
        def request = postRequest()
                .withHeader(HttpHeaders.CONTENT_LANGUAGE, region)
                .withBody(product().withField("name", PRODUCT_NAME_ENGLISH))
                .build()
        def response = handleRequest facade, request

        then: 'the response contains values of the Content-Language'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.name') == PRODUCT_NAME_ENGLISH
        then: 'request is handled successfully'
        response.status == HttpStatusCodes.CREATED
        and: 'the response header for Content-Language contains the language with region'
        response.getHeader('Content-Language') == region

        IntegrationTestUtil.remove LanguageModel, { it.isocode == region }
    }

    @Test
    def 'responds with Accept-Language when only Accept-Language is specified'() {
        given: 'a product with English content exists in the system'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE Product; code[unique = true]; name[lang = 'en']",
                "                     ; $PRODUCT_CODE      ; $PRODUCT_NAME_ENGLISH")

        when: 'German content is posted but only Accept-Language is specified'
        def request = postRequest()
                .withAcceptLanguage(Locale.ENGLISH)
                .withBody(product().withField("name", PRODUCT_NAME_GERMAN))
                .build()
        def response = handleRequest facade, request

        then: 'response contains values in Accept-Language'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.name') == PRODUCT_NAME_ENGLISH
    }

    @Test
    @Unroll
    def "item cannot be persisted with unsupported #header header value"() {
        given: 'payload contains values in a language not supported by the platform'
        def request = postRequest()
                .withContentLanguage(content)
                .withAcceptLanguage(accept)
                .withBody(product().withField("name", "Name in Korean"))
                .build()

        when:
        def response = handleRequest facade, request

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        def json = JsonObject.createFrom response.entityAsStream
        json.getString(ERROR_CODE_PATH) == 'invalid_language'
        json.getString(ERROR_MSG_PATH).contains Locale.KOREA.language
        and: 'the payload item is not saved'
        !getPersistedProduct()

        where:
        header             | content        | accept
        'Content-Language' | Locale.KOREA   | Locale.ENGLISH
        'Accept-Language'  | Locale.ENGLISH | Locale.KOREA
    }

    @Test
    def 'persists item referencing item(s) of the same type in their payload'() {
        given: 'IO item metadata has self-referencing attributes: "supercategories" and "category"'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true];',
                '                               ; InboundCategory',

                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                '                                   ; InboundCategory                       ; Category           ; Category',

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code)',
                '; InboundCategory:Category      ; code              ; Category:code              ;',
                '; InboundCategory:Category      ; supercategories   ; Category:supercategories   ; InboundCategory:Category',
                '; InboundCategory:Category      ; categories        ; Category:categories        ; InboundCategory:Category')
        and: 'a category exists in the system'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]',
                '                      ; test_category1')

        when: 'item is posted with references to the existing category'
        def request = postRequestBuilder("InboundCategory", "Categories", APPLICATION_JSON_VALUE)
                .withBody(json()
                        .withCode("test_category2")
                        .withFieldValues("supercategories", json().withCode("test_category1")))
                .build()
        def response = handleRequest facade, request

        then: 'the new item is created'
        response.status == HttpStatusCodes.CREATED
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.code') == 'test_category2'
        json.getString('d.supercategories').contains '__deferred'
        json.getString('d.categories').contains '__deferred'
        and: 'the categories stored in the syatem are correctly connected'
        def category1 = getPersistedCategory 'test_category1'
        def category2 = getPersistedCategory 'test_category2'
        category2.supercategories == [category1]
        category1.categories == [category2]

        cleanup:
        IntegrationTestUtil.removeAll CategoryModel
    }

    @Test
    def 'persists an item with all possible primitive attribute types'() {
        def itemCode = 'new-item'
        def longVal = Long.valueOf(92233720)
        def bigDecimalVal = new BigDecimal(854.775807)
        def bigIntegerVal = new BigInteger('-922337203685477')
        given: 'test item integration object exists'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                '; TestObject; INBOUND',
                "INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique=true]; code[unique = true]; type(code)",
                '; TestObject	; TestIntegrationItem	; TestIntegrationItem',
                "INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
                '                                            ; TestObject:TestIntegrationItem                                     ; code                        ; TestIntegrationItem:code',
                '                                            ; TestObject:TestIntegrationItem                                     ; string          	        ; TestIntegrationItem:string',
                '                                            ; TestObject:TestIntegrationItem	                                  ; primitiveBoolean	        ; TestIntegrationItem:primitiveBoolean',
                '                                            ; TestObject:TestIntegrationItem	                                  ; primitiveShort  	        ; TestIntegrationItem:primitiveShort',
                '                                            ; TestObject:TestIntegrationItem	                                  ; primitiveChar   	        ; TestIntegrationItem:primitiveChar',
                '                                            ; TestObject:TestIntegrationItem	                                  ; primitiveInteger	        ; TestIntegrationItem:primitiveInteger',
                '                                            ; TestObject:TestIntegrationItem	                                  ; primitiveByte   	        ; TestIntegrationItem:primitiveByte',
                '                                            ; TestObject:TestIntegrationItem	                                  ; primitiveFloat  	        ; TestIntegrationItem:primitiveFloat',
                '                                            ; TestObject:TestIntegrationItem	                                  ; primitiveDouble 	        ; TestIntegrationItem:primitiveDouble',
                '                                            ; TestObject:TestIntegrationItem	                                  ; date            	        ; TestIntegrationItem:date',
                '                                            ; TestObject:TestIntegrationItem	                                  ; long            	        ; TestIntegrationItem:long',
                '                                            ; TestObject:TestIntegrationItem	                                  ; bigDecimal                  ; TestIntegrationItem:bigDecimal',
                '                                            ; TestObject:TestIntegrationItem	                                  ; bigInteger                  ; TestIntegrationItem:bigInteger')
        and: 'the payload item has attributes of all possible primitive types set'
        def request = postRequestBuilder("TestObject", "TestIntegrationItems", APPLICATION_JSON_VALUE)
                .withBody(json()
                        .withField("code", itemCode)
                        .withField("string", "new-item")
                        .withField("primitiveShort", Short.MAX_VALUE)
                        .withField("primitiveChar", 'b')
                        .withField("primitiveInteger", Integer.MAX_VALUE)
                        .withField("primitiveByte", Byte.MAX_VALUE)
                        .withField("primitiveBoolean", Boolean.TRUE)
                        .withField("primitiveFloat", 3.4028234f)
                        .withField("primitiveDouble", 1.7976931348623157d)
                        .withField("date", new Date(1574665200000L))
                        .withField("long", longVal.toString())
                        .withField("bigDecimal", bigDecimalVal.toString())
                        .withField("bigInteger", bigIntegerVal.toString()))
                .build()

        when:
        def response = handleRequest facade, request

        then: 'response is successful'
        response.status == HttpStatusCodes.CREATED
        and: 'the response body contains all correct primitive attribute values'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.code') == itemCode
        json.getString('d.string') == 'new-item'
        json.getObject('d.primitiveShort') == 32767
        json.getString('d.primitiveChar') == 'b'
        json.getObject('d.primitiveInteger') == 2147483647
        json.getObject('d.primitiveByte') == 127
        json.getBoolean('d.primitiveBoolean')
        json.getObject('d.primitiveFloat') == '3.4028234'
        json.getObject('d.primitiveDouble') == '1.7976931348623157'
        json.getString('d.date') == '/Date(1574665200000)/'
        json.getString('d.long') == longVal.toString()
        json.getString('d.bigDecimal') == bigDecimalVal.toString()
        json.getString('d.bigInteger') == bigIntegerVal.toString()
        and: 'the primitive attributes are persited in the database'
        def persistedItem = IntegrationTestUtil.findAny(TestIntegrationItemModel, { it.code == itemCode })
                .orElse(null)
        persistedItem != null
        persistedItem.string == 'new-item'
        persistedItem.primitiveShort == Short.MAX_VALUE
        persistedItem.primitiveChar == 'b'
        persistedItem.primitiveInteger == Integer.MAX_VALUE
        persistedItem.primitiveByte == Byte.MAX_VALUE
        persistedItem.primitiveBoolean
        persistedItem.primitiveFloat == 3.4028235f
        persistedItem.primitiveDouble == 1.7976931348623157d
        persistedItem.date == new Date(1574665200000L)
        persistedItem.long == longVal
        persistedItem.bigDecimal == bigDecimalVal
        persistedItem.bigInteger == bigIntegerVal
    }

    @Test
    @Unroll
    def "persists #desc item when its payload contains a read only attribute"() {
        given: 'payload contains read-only "version" attribute in Catalog item'
        def request = postRequest(entitySet)
                .withBody(payload)
                .build()

        when:
        def response = handleRequest facade, request

        then:
        response.status == HttpStatusCodes.CREATED

        where:
        desc       | entitySet  | payload
        'the root' | 'Catalogs' | catalog(version: 'read-only')
        'a nested' | 'Products' | product(catalogVersion: catalogVersion(catalog: catalog(version: 'read-only')))
    }

    @Test
    def "persisting subtype is possible even when the attribute in the IO definition returns the supertype"() {
        given: "Classification Class, a subtype of Category"
        def classSysId = 'MyTestClassificationCatalog'
        def classSysVersion = 'MyTestClassificationSystemVersion'
        def classCode = 'MyTestClassificationClass'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE ClassificationSystem; id[unique = true]',
                "                                  ; $classSysId",
                'INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version[unique = true]; active',
                "                                         ; $classSysId               ; $classSysVersion      ; false",
                'INSERT_UPDATE ClassificationClass; code[unique = true]; catalogVersion(version, catalog(id))[unique = true]',
                "                                 ; $classCode         ; $classSysVersion:$classSysId")

        and:
        def content = json()
                .withCode(PRODUCT_CODE)
                .withField("catalogVersion", json()
                        .withField("version", CATALOG_VERSION)
                        .withField("catalog", json().withId(CATALOG_ID)))
                .withFieldValues('supercategories', json()
                        .withCode(classCode)
                        .withField("catalogVersion", json()
                                .withField("version", classSysVersion)
                                .withField("catalog", json().withId(classSysId))))
                .build()

        when:
        def response = handleReq(content)

        then:
        response.status == HttpStatusCodes.CREATED

        cleanup:
        IntegrationTestUtil.remove ClassificationClassModel, {
            it.code == classCode && it.catalogVersion.version == classSysVersion && it.catalogVersion.catalog.id == classSysId
        }
        IntegrationTestUtil.remove ClassificationSystemModel, { it.id == classSysId }
        IntegrationTestUtil.remove ClassificationSystemVersionModel, {
            it.version == classSysVersion && it.catalog.id == classSysId
        }
    }

    @Test
    @Unroll
    def "read-only attribute is ignored when #condition"() {
        given:
        def content = json()
                .withId(catalogId)
                .withField('version', 'non-writable-field-ignored')
                .build()

        def request = oDataPostRequest SERVICE_NAME, 'Catalogs', content, APPLICATION_JSON_VALUE

        when:
        def response = handleRequest facade, request

        then:
        response.status == HttpStatusCodes.CREATED

        and:
        def catalog = IntegrationTestUtil.findAny CatalogModel, { it.id == catalogId }
        catalog.isPresent()
        catalog.get().version == version

        where:
        condition                   | catalogId            | version
        'creating a new item'       | 'myNewTestCatalogId' | null
        'updating an existing item' | CATALOG_ID           | 'Staged'
    }

    @Test
    def "an error reported when POST payload refers an attribute not declared in the integration object"() {
        given: 'payload contains attribute "name" that exists in the type but not declared in the IO'
        def content = json()
                .withCode('some-category')
                .withField('name', 'my_category')
                .build()

        def request = oDataPostRequest SERVICE_NAME, 'Categories', content, APPLICATION_JSON_VALUE

        when:
        def response = handleRequest facade, request

        then: 'error is reported'
        response.status == HttpStatusCodes.BAD_REQUEST
        def json = JsonObject.createFrom response.entityAsStream
        json.getString(ERROR_CODE_PATH) == 'invalid_property'
        json.getString(ERROR_MSG_PATH).contains 'name'

        and: 'category is not created'
        IntegrationTestUtil.findAny(CategoryModel, { it.code == 'some-category' }).empty
    }

    @Test
    def "Error status returned when creating an instance of abstract type"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "; $IO                ; INBOUND",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true];type(code)     ;root[default = false]',
                "; $IO                                   ; PrincipalGroup     ;PrincipalGroup ; true",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "; $IO:PrincipalGroup                                                 ; uid                         ; PrincipalGroup:uid                                 ;                                                           ; true",
        )
        def request = postRequestAbstractType().withBody(
                json()
                        .withField(IO_ATTR, IO_ATTR_VALUE))
        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'invalid_type'
        json.getString('error.message.value') == "The type PrincipalGroup cannot be persisted because it is an abstract type."
    }

    private static ODataRequestBuilder postRequestAbstractType() {
        ODataRequestBuilder.oDataPostRequest()
                .withContentType(APPLICATION_JSON_VALUE)
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(IO)
                        .withEntitySet(IO_ITEM))
    }

    private static ProductModel getPersistedProduct(String code = PRODUCT_CODE) {
        IntegrationTestUtil.getModelByExample new ProductModel(code: code)
    }

    private static CategoryModel getPersistedCategory(String code) {
        IntegrationTestUtil.getModelByExample new CategoryModel(code: code)
    }

    private static JsonBuilder product(Map<String, ?> attributes = [:]) {
        def json = json()
                .withCode(PRODUCT_CODE)
                .withField('catalogVersion', catalogVersion())
        attributes.forEach({ k, v -> json = json.withField(k, v) })
        json
    }

    private static JsonBuilder catalogVersion(Map<String, ?> attributes = [:]) {
        def json = json()
                .withField('version', CATALOG_VERSION)
                .withField('catalog', catalog())
        attributes.forEach({ k, v -> json = json.withField(k, v) })
        json
    }

    private static JsonBuilder catalog(Map<String, ?> attributes = [:]) {
        def json = json().withId(CATALOG_ID)
        attributes.forEach({ k, v -> json = json.withField(k, v) })
        json
    }

    private static ODataRequestBuilder postRequest(String entities = ENTITYSET) {
        postRequestBuilder SERVICE_NAME, entities, APPLICATION_JSON_VALUE
    }

    private ODataResponse handleReq(String content) {
        handleRequest(facade, oDataPostRequest(SERVICE_NAME, PRODUCTS_ENTITYSET, content, Locale.ENGLISH, APPLICATION_JSON_VALUE))
    }
}
