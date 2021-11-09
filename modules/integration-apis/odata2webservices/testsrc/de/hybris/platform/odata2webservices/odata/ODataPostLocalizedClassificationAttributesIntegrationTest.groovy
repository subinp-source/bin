/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.ClassificationBuilder
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2services.odata.asserts.ODataAssertions
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import spock.lang.Issue
import spock.lang.Unroll

import javax.annotation.Resource
import java.util.stream.Collectors

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.assertModelExists
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.getClassificationAttributeCollectionValue
import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
@Issue('https://jira.hybris.com/browse/STOUT-3062')
class ODataPostLocalizedClassificationAttributesIntegrationTest extends ServicelayerSpockSpecification {
    private static final def CLASS_SYSTEM = 'Sports'
    private static final def CLASS_VERSION = 'Cycling'
    private static final def CLASS_SYSTEM_VERSION = "$CLASS_SYSTEM:$CLASS_VERSION"
    private static final def CLASSIFICATION_CLASS = "Bicycle"
    private static final def IO = 'BikeIO'
    private static final def PRODUCT_CODE = 'bike1'
    private static final def CATALOG = 'Test'
    private static final def VERSION = 'V1'
    private static final def CLASS_ASSIGNMENT_PREFIX = "$CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION"

    public static final String CONTENT_LANGUAGE = "Content-Language"

    /* Impex header variables */
    private static final def CATALOG_VERSION_HEADER = 'catalogVersion(catalog(id), version)'
    private static final def SYSTEM_VERSION_HEADER = 'systemVersion(catalog(id), version)'
    private static final def CLASS_HEADER = "classificationClass($CATALOG_VERSION_HEADER, code)"
    private static final def CLASS_ATTRIBUTE_HEADER = "classificationAttribute($SYSTEM_VERSION_HEADER, code)"
    private static final def CLASS_ASSIGNMENT_HEADER = "classAttributeAssignment($CLASS_HEADER, $CLASS_ATTRIBUTE_HEADER)"
    private static final def ITEM_HEADER = 'integrationObjectItem(integrationObject(code), code)'

    private static final def SPANISH = new Locale("es")
    private static final def SPANISH_CO = new Locale.Builder().setLanguage("es").setRegion("CO").build()
    private static final def DEFAULT_NAME = "Road Bike default"
    private static final def ENGLISH_NAME = "Road Bike"
    private static final def ENGLISH_DESCRIPTION = "Built for racing"
    private static final def SPANISH_NAME = "Bicicleta de carretera"
    private static final def SPANISH_DESCRIPTION = "Especial para competición"
    private static final def ENGLISH_SIZE = "Medium"
    private static final def SPANISH_SIZE = "Mediana"

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
                'INSERT_UPDATE Language; isocode[unique=true]',
                '                      ; es',
                '                      ; es_CO')
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
        ClassificationBuilder.cleanup()
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version == VERSION }
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == CATALOG }
        IntegrationTestUtil.removeSafely LanguageModel, { it.isocode == 'es' || it.isocode == 'es_CO' }
    }

    @Test
    @Unroll
    def "localized classification attributes are persisted as provided in localized attributes and returned in #languageReturned when content language is #contentLanguage and accept language is #acceptLanguage"() {
        given: 'primitive classification attributes'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('alternativeName').string().localized())
                .setup()
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('alternativeDescription').string().localized())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; alternativeName               ; $CLASS_ASSIGNMENT_PREFIX:alternativeName",
                "                                                          ; $IO:Product                ; alternativeDescription        ; $CLASS_ASSIGNMENT_PREFIX:alternativeDescription")
        and: "A request with localized attributes in the body"
        def request = postRequest()
                .withContentLanguage(contentLanguage)
                .withAcceptLanguage(acceptLanguage)
                .withBody(product(PRODUCT_CODE)
                        .withField("alternativeName", DEFAULT_NAME)
                        .withLocalizedAttributes(
                                [language: "en", alternativeName: ENGLISH_NAME, alternativeDescription: ENGLISH_DESCRIPTION],
                                [language: "es", alternativeName: SPANISH_NAME, alternativeDescription: SPANISH_DESCRIPTION]))

        when: "The request is processed"
        def oDataResponse = facade.handleRequest(createContext(request))

        then: "Response body returns the accept language provided."
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.CREATED)
                .jsonBody()
                .hasPathWithValue("d.alternativeName", bodyValues["alternativeName"])
                .hasPathWithValue("d.alternativeDescription", bodyValues["alternativeDescription"])
        and: "Localized attributes are persisted at the item level."
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'alternativeName').containsAll(ENGLISH_NAME, SPANISH_NAME)
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'alternativeDescription').containsAll(ENGLISH_DESCRIPTION, SPANISH_DESCRIPTION)
        and: "Response contains Content-Language header with the same locale as body"
        def header = oDataResponse.getHeader(CONTENT_LANGUAGE)
        if (acceptLanguage == null) {
            header == null
        } else {
            header == acceptLanguage.language
        }

        where:
        languageReturned            | contentLanguage | acceptLanguage | bodyValues
        'en'                        | Locale.ENGLISH  | Locale.ENGLISH | [alternativeName: ENGLISH_NAME, alternativeDescription: ENGLISH_DESCRIPTION]
        'es'                        | Locale.ENGLISH  | SPANISH        | [alternativeName: SPANISH_NAME, alternativeDescription: SPANISH_DESCRIPTION]
        'en'                        | Locale.ENGLISH  | null           | [alternativeName: ENGLISH_NAME, alternativeDescription: ENGLISH_DESCRIPTION]
        'es'                        | null            | SPANISH        | [alternativeName: SPANISH_NAME, alternativeDescription: SPANISH_DESCRIPTION]
        'default platform language' | null            | null           | [alternativeName: ENGLISH_NAME, alternativeDescription: ENGLISH_DESCRIPTION]
    }

    @Test
    @Unroll
    def "localized classification attributes and localized properties are merged, and localized attributes take precedence"() {
        given: 'primitive classification attributes'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('alternativeName').string().localized())
                .setup()
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('alternativeDescription').string().localized())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; alternativeName               ; $CLASS_ASSIGNMENT_PREFIX:alternativeName",
                "                                                          ; $IO:Product                ; alternativeDescription        ; $CLASS_ASSIGNMENT_PREFIX:alternativeDescription")
        and: "A request with localized attributes in the body"
        def request = postRequest()
                .withContentLanguage(contentLanguage)
                .withAcceptLanguage(acceptLanguage)
                .withBody(product(PRODUCT_CODE)
                        .withField("alternativeName", DEFAULT_NAME)
                        .withLocalizedAttributes(
                                [language: "en", alternativeDescription: ENGLISH_DESCRIPTION],
                                [language: "es", alternativeName: SPANISH_NAME, alternativeDescription: SPANISH_DESCRIPTION]))
        when: "The request is processed"
        def oDataResponse = facade.handleRequest(createContext(request))

        then: "Response body returns the accept language provided."
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.CREATED)
                .jsonBody()
                .hasPathWithValue("d.alternativeName", bodyValues["alternativeName"])
                .hasPathWithValue("d.alternativeDescription", bodyValues["alternativeDescription"])
        and: "Localized attributes are persisted at the item level."
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'alternativeName').containsAll(persistedNames)
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'alternativeDescription').containsAll(persistedDescriptions)
        and: "Response contains Content-Language header with the same locale as body"
        def header = oDataResponse.getHeader(CONTENT_LANGUAGE)
        if (acceptLanguage == null) {
            header == null
        } else {
            header == acceptLanguage.language
        }

        where:
        contentLanguage | acceptLanguage | bodyValues                                                                   | persistedNames               | persistedDescriptions
        Locale.ENGLISH  | Locale.ENGLISH | [alternativeName: DEFAULT_NAME, alternativeDescription: ENGLISH_DESCRIPTION] | [DEFAULT_NAME, SPANISH_NAME] | [ENGLISH_DESCRIPTION, SPANISH_DESCRIPTION]
        SPANISH         | SPANISH        | [alternativeName: SPANISH_NAME, alternativeDescription: SPANISH_DESCRIPTION] | SPANISH_NAME                 | [ENGLISH_DESCRIPTION, SPANISH_DESCRIPTION]
    }

    @Test
    def "stored localized properties in the SPANISH when content language is SPANISH and localized attributes are not provided"() {
        given: 'primitive classification attributes'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('alternativeName').string().localized())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; alternativeName               ; $CLASS_ASSIGNMENT_PREFIX:alternativeName")
        and: "A request without localized attributes in the body"
        def request = postRequest()
                .withContentLanguage(SPANISH)
                .withAcceptLanguage(SPANISH)
                .withBody(product(PRODUCT_CODE)
                        .withField("alternativeName", SPANISH_NAME))

        when: "The request is processed"
        def oDataResponse = facade.handleRequest(createContext(request))

        then: "Response body returns the accept language provided."
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.CREATED)
                .jsonBody()
                .hasPathWithValue("d.alternativeName", SPANISH_NAME)
        and:
        "Localized attributes are persisted at the item level."
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'alternativeName') == [SPANISH_NAME]
        and: "Response contains Content-Language header with the same locale as body"
        def header = oDataResponse.getHeader(CONTENT_LANGUAGE)
        header == SPANISH.language
    }

    @Test
    def "default language is persisted for localized properties when content language is null and localized attributes are not provided"() {
        given: 'primitive classification attributes'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('alternativeName').string().localized())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; alternativeName               ; $CLASS_ASSIGNMENT_PREFIX:alternativeName")
        and: "A request without localized attributes in the body"
        def request = postRequest()
                .withAcceptLanguage(SPANISH)
                .withBody(product(PRODUCT_CODE)
                        .withField("alternativeName", DEFAULT_NAME))

        when: "The request is processed"
        def oDataResponse = facade.handleRequest(createContext(request))

        then: "Response body returns the accept language provided."
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.CREATED)
                .jsonBody()
                .doesNotHavePath("d.alternativeName")
        and:
        "Localized attributes are persisted at the item level."
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'alternativeName') == [DEFAULT_NAME]

        and: "Response contains Content-Language header with the same locale as body"
        def header = oDataResponse.getHeader(CONTENT_LANGUAGE)
        header == SPANISH.language
    }

    @Test
    def "stored localized properties of type enum when localized attributes are provided"() {
        given: 'primitive classification attributes'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('size').valueList(['S', 'M', 'L']).localized())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; size               ; $CLASS_ASSIGNMENT_PREFIX:size")
        and: "A request with localized attributes in the body"
        def request = postRequest()
                .withAcceptLanguage(SPANISH)
                .withBody(product(PRODUCT_CODE)
                        .withField("size", ENGLISH_NAME)
                        .withLocalizedAttributes(
                                [language: "en", size: ENGLISH_SIZE],
                                [language: "es", size: SPANISH_SIZE]))

        when: "The request is processed"
        def oDataResponse = facade.handleRequest(createContext(request))

        then:
        "Response body returns the accept language provided."
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.CREATED)
                .jsonBody()
                .hasPathWithValue("d.size", SPANISH_SIZE)

        and: "Localized attributes are persisted at the item level."
        getClassificationAttributeCollectionValue(PRODUCT_CODE, 'size').stream()
                .map({ v -> ((ClassificationAttributeValueModel) v).getCode() }).collect(Collectors.toList())
                .containsAll(ENGLISH_SIZE, SPANISH_SIZE)

        and: "Response contains Content-Language header with the same locale as body"
        def header = oDataResponse.getHeader(CONTENT_LANGUAGE)
        header == SPANISH.language
    }

    @Test
    def "localized classification attributes are saved under the correct locale when using a composed locale in the content language"() {
        given: 'primitive classification attributes'
        classification.withAttribute(
                ClassificationBuilder.attribute().withName('altName').string().localized())
                .setup()
        and: 'it is exposed in the IO'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $IO:Product                ; altName               ; $CLASS_ASSIGNMENT_PREFIX:altName")
        and: "A request with localized attributes in the body"
        def request = postRequest()
                .withContentLanguage(SPANISH_CO)
                .withAcceptLanguage(SPANISH_CO)
                .withBody(product(PRODUCT_CODE)
                        .withField("altName", SPANISH_NAME))

        when: "The request is processed"
        def oDataResponse = facade.handleRequest(createContext(request))

        then:
        "Response body returns the accept language provided."
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.CREATED)
                .jsonBody()
                .hasPathWithValue("d.altName", SPANISH_NAME)
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

    ProductModel retrieveProduct() {
        ProductModel productModel = new ProductModel()
        productModel.setCode(PRODUCT_CODE)
        assertModelExists(productModel)
    }
}