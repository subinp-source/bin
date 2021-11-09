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
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.odata2services.odata.asserts.ODataAssertions
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import de.hybris.platform.servicelayer.model.ModelService
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import org.springframework.http.MediaType
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.assertModelExists
import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext

@IntegrationTest
class LocalizedAttributesPersistenceIntegrationTest extends ServicelayerTransactionalSpockSpecification {
    private static final String SERVICE = "LocalizedAttributesPersistenceIntegrationTestIO"
    private static final String PRODUCTS = "Products"
    private static final String PRODUCT_CODE = "orange_product"
    private static final String ORIG_EN_PRODUCT_NAME = "UPhone 11"
    private static final String ORIG_EN_PRODUCT_DESCRIPTION = "Latest version of the UPhone"
    private static final String NEW_EN_PRODUCT_NAME = "UPhone XI"
    private static final String NEW_EN_PRODUCT_DESCRIPTION = "The newest and fastest UPhone"
    private static final String NEW_DE_PRODUCT_NAME = "UPhone XI.de"
    private static final String NEW_DE_PRODUCT_DESCRIPTION = "Das neueste und schnellste UPhone"
    private static final String NEW_ES_PRODUCT_NAME = "UPhone XI.es"
    private static final String NEW_ES_PRODUCT_DESCRIPTION = "El último y más rápido UPhone es"
    private static final String NEW_ES_CO_PRODUCT_NAME = "UPhone XI.es_CO"
    private static final String NEW_ES_CO_PRODUCT_DESCRIPTION = "El último y más rápido UPhone es_CO"
    private static final String CATALOG_ID = "Default"
    private static final String CATALOG_VERSION_VERSION = "Staged"
    private static final String NEW_EN_CATALOG_VERSION_CATEGORY_SYSTEM_NAME = "New tech of the year"
    private static final String INTEGRATION_KEY = "$CATALOG_VERSION_VERSION|$CATALOG_ID|$PRODUCT_CODE"

    private static final String[] impEx = [
            "INSERT_UPDATE IntegrationObject; code[unique = true];",
            "; $SERVICE",
            "INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique=true]; code[unique = true]; type(code)",
            "; $SERVICE                         ; Product        ; Product",
            "; $SERVICE                         ; Catalog        ; Catalog",
            "; $SERVICE                         ; CatalogVersion ; CatalogVersion",
            '$item = integrationObjectItem(integrationObject(code), code)',
            'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]     ; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
            "; $SERVICE:Product                          ; code                ; Product:code                     ;",
            "; $SERVICE:Product                          ; name                ; Product:name                     ;",
            "; $SERVICE:Product                          ; description         ; Product:description              ;",
            "; $SERVICE:Product                          ; catalogVersion      ; Product:catalogVersion           ; $SERVICE:CatalogVersion",
            "; $SERVICE:Catalog                          ; id                  ; Catalog:id                       ;",
            "; $SERVICE:CatalogVersion                   ; catalog             ; CatalogVersion:catalog           ; $SERVICE:Catalog",
            "; $SERVICE:CatalogVersion                   ; version             ; CatalogVersion:version           ;",
            "; $SERVICE:CatalogVersion                   ; categorySystemName  ; CatalogVersion:categorySystemName;",
            "INSERT_UPDATE Catalog;id[unique=true];name[lang=en];defaultCatalog;",
            ";Default;Default;true",
            "INSERT_UPDATE CatalogVersion; catalog(id)[unique=true]; version[unique=true];active;",
            ";Default;Staged;true",
            "INSERT_UPDATE Language;isocode[unique=true];name[lang=de];name[lang=en]",
            ";de;Deutsch;German",
            ";es;Spanisch;Spanish",
            ";es_CO;Spanisch Columbia;Spanish Columbia",
    ]
    public static final String CONTENT_LANGUAGE = "Content-Language"

    @Resource(name = "defaultODataFacade")
    ODataFacade facade

    @Resource
    ModelService modelService

    def setupSpec() {
        IntegrationTestUtil.importImpEx(impEx)
    }

    def cleanup() {
        IntegrationTestUtil.removeAll ProductModel
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeSafely LanguageModel, { it.isocode == 'de' || it.isocode == 'es' || it.isocode == 'es_CO' }
    }

    @Test
    @Unroll
    def "localizedAttributes are persisted with content language '#contentLanguage' and accept language '#acceptLanguage'"() {
        given: "A request with localized attributes in the body"
        def request = postRequest()
                .withContentLanguage(contentLanguage)
                .withAcceptLanguage(acceptLanguage)
                .withBody(product()
                        .withField("name", ORIG_EN_PRODUCT_NAME)
                        .withField("description", ORIG_EN_PRODUCT_DESCRIPTION)
                        .withLocalizedAttributes(
                                [language: "en", name: NEW_EN_PRODUCT_NAME, description: NEW_EN_PRODUCT_DESCRIPTION],
                                [language: "de", name: NEW_DE_PRODUCT_NAME, description: NEW_DE_PRODUCT_DESCRIPTION]))

        when: "The request is processed"
        def oDataResponse = facade.handleRequest(createContext(request))

        then: "Response body contains data in Accept-Language locale"
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.CREATED)
                .jsonBody()
                .hasPathWithValue("d.name", bodyValues["name"])
                .hasPathWithValue("d.description", bodyValues["description"])

        and: "Localized attributes are persisted at the item level."
        ProductModel productModel = retrieveProduct()
        NEW_EN_PRODUCT_NAME == productModel.getProperty("name", Locale.ENGLISH)
        NEW_EN_PRODUCT_DESCRIPTION == productModel.getProperty("description", Locale.ENGLISH)
        NEW_DE_PRODUCT_NAME == productModel.getProperty("name", Locale.GERMAN)
        NEW_DE_PRODUCT_DESCRIPTION == productModel.getProperty("description", Locale.GERMAN)

        and: "Localized attributes are persisted at a nested level."
        CatalogVersionModel catalogVersionModel = retrieveCatalogVersion()
        NEW_EN_CATALOG_VERSION_CATEGORY_SYSTEM_NAME == catalogVersionModel.getProperty("categorySystemName", Locale.ENGLISH)

        and: "Response contains Content-Language header with the same locale as body"
        def header = oDataResponse.getHeader(CONTENT_LANGUAGE)
        header == acceptLanguage.language

        where:
        contentLanguage | acceptLanguage | bodyValues
        Locale.ENGLISH  | Locale.ENGLISH | [name: NEW_EN_PRODUCT_NAME, description: NEW_EN_PRODUCT_DESCRIPTION]
        Locale.ENGLISH  | Locale.GERMAN  | [name: NEW_DE_PRODUCT_NAME, description: NEW_DE_PRODUCT_DESCRIPTION]
    }

    @Test
    @Unroll
    def "#localizedAttribute is persisted with '#description' description using locale #locale"() {
        given:
        def request = postRequest()
                .withContentLanguage(Locale.ENGLISH)
                .withBody(product().withLocalizedAttributes(localizedAttribute))
                .build()

        when:
        facade.handleRequest(createContext(request))

        then:
        ProductModel productModel = retrieveProduct()
        name == productModel.getProperty("name", locale)
        description == productModel.getProperty("description", locale)

        where:
        localizedAttribute                                                                            | name                   | description                   | locale
        [language: "de", name: NEW_DE_PRODUCT_NAME, description: ""]                                  | NEW_DE_PRODUCT_NAME    | ""                            | Locale.GERMAN
        [language: "de", name: NEW_DE_PRODUCT_NAME]                                                   | NEW_DE_PRODUCT_NAME    | null                          | Locale.GERMAN
        [language: "es", name: NEW_ES_PRODUCT_NAME, description: NEW_ES_PRODUCT_DESCRIPTION]          | NEW_ES_PRODUCT_NAME    | NEW_ES_PRODUCT_DESCRIPTION    | createLocale("es")
        [language: "es_CO", name: NEW_ES_CO_PRODUCT_NAME, description: NEW_ES_CO_PRODUCT_DESCRIPTION] | NEW_ES_CO_PRODUCT_NAME | NEW_ES_CO_PRODUCT_DESCRIPTION | createLocale("es", "CO")
    }

    @Test
    def "exception is thrown if the language is not provided in the localizedAttribute map entry"() {
        given:
        def request = postRequest()
                .withContentLanguage(Locale.ENGLISH)
                .withBody(product().withLocalizedAttributes([name: NEW_EN_PRODUCT_NAME, description: NEW_EN_PRODUCT_DESCRIPTION]))

        when:
        def oDataResponse = facade.handleRequest(createContext(request))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.BAD_REQUEST)
                .jsonBody()
                .hasPathWithValue("error.code", "missing_language")
    }

    @Test
    def "exception is thrown if the provided language does not match any existing platform language"() {
        given: "'zz' is a language tag that is not supported by the platform"
        def request = postRequest()
                .withContentLanguage(Locale.ENGLISH)
                .withBody(product().withLocalizedAttributes([language: "zz", name: NEW_EN_PRODUCT_NAME]))

        when:
        def oDataResponse = facade.handleRequest(createContext(request))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.BAD_REQUEST)
                .jsonBody()
                .hasPathWithValue("error.code", "invalid_language")
                .hasPathWithValue("error.innererror", INTEGRATION_KEY)
    }

    @Test
    def "Content-Language is returned when Accept-Language is omitted"() {
        given:
        def request = postRequest()
                .withContentLanguage(Locale.GERMAN)
                .withBody(product().withLocalizedAttributes(
                        [language: 'en', name: NEW_EN_PRODUCT_NAME, description: NEW_EN_PRODUCT_DESCRIPTION],
                        [language: 'de', name: NEW_DE_PRODUCT_NAME, description: NEW_DE_PRODUCT_DESCRIPTION]))

        when:
        def oDataResponse = facade.handleRequest(createContext(request))

        then: "The value of the response body correspond with the locale provided in the Content-Language request header"
        ODataAssertions.assertThat(oDataResponse).jsonBody()
                .hasPathWithValue("d.name", NEW_DE_PRODUCT_NAME)
                .hasPathWithValue("d.description", NEW_DE_PRODUCT_DESCRIPTION)

        and: "Content-Language is included in the response header"
        def header = oDataResponse.getHeader(CONTENT_LANGUAGE)
        header == Locale.GERMAN.language
    }

    ProductModel retrieveProduct() {
        ProductModel testModel = new ProductModel()
        testModel.setCode(PRODUCT_CODE)
        assertModelExists(testModel)
    }

    CatalogModel retrieveCatalog() {
        CatalogModel testModel = new CatalogModel()
        testModel.setId(CATALOG_ID)
        assertModelExists(testModel)
    }

    CatalogVersionModel retrieveCatalogVersion() {
        CatalogVersionModel testModel = new CatalogVersionModel()
        testModel.setCatalog(retrieveCatalog())
        testModel.setVersion(CATALOG_VERSION_VERSION)
        assertModelExists(testModel)
    }

    JsonBuilder product() {
        json()
                .withCode(PRODUCT_CODE)
                .withField("catalogVersion", catalogVersion())
    }

    JsonBuilder catalogVersion() {
        json()
                .withField("version", CATALOG_VERSION_VERSION)
                .withField("catalog", catalog())
                .withLocalizedAttributes([language: "en", categorySystemName: NEW_EN_CATALOG_VERSION_CATEGORY_SYSTEM_NAME])
    }

    JsonBuilder catalog() {
        json().withId(CATALOG_ID)
    }

    ODataRequestBuilder postRequest() {
        ODataFacadeTestUtils.postRequestBuilder(SERVICE, PRODUCTS, MediaType.APPLICATION_JSON_VALUE)
    }

    Locale createLocale(String language) {
        new Locale.Builder().setLanguage(language).build()
    }

    Locale createLocale(String language, String region) {
        new Locale.Builder()
                .setLanguage(language)
                .setRegion(region)
                .build()
    }
}
