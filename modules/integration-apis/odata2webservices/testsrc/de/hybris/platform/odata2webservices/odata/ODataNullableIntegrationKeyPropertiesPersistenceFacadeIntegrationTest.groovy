/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.ProductFeatureModel
import de.hybris.platform.core.model.order.OrderEntryModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import org.springframework.http.MediaType

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.oDataPostRequest

@IntegrationTest
class ODataNullableIntegrationKeyPropertiesPersistenceFacadeIntegrationTest extends ServicelayerTransactionalSpockSpecification {

    def ORDER_IO = "OrderEntryExample"
    def PRODUCT_IO = "TestForProductFeatureIO"
    def CATALOG_VERSION = "Default:Staged";

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setup() {
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $ORDER_IO",
                '$obj = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $obj[unique = true]; code[unique = true]; type(code)',
                "                                   ; $ORDER_IO          ; Order              ; Order",
                "                                   ; $ORDER_IO          ; OrderEntry         ; OrderEntry",
                "                                   ; $ORDER_IO          ; CatalogVersion     ; CatalogVersion",
                "                                   ; $ORDER_IO          ; Catalog            ; Catalog",
                "                                   ; $ORDER_IO          ; Product            ; Product",
                "                                   ; $ORDER_IO          ; Unit               ; Unit",
                "                                   ; $ORDER_IO          ; Currency           ; Currency",
                "                                   ; $ORDER_IO          ; User               ; User",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]     ; attributeName[unique = true]; $descriptor                  ; $refType; unique[default = false]; autoCreate[default = false]',
                "                                            ; $ORDER_IO:Order          ; code                        ; Order:code                   ;",
                "                                            ; $ORDER_IO:Order          ; currency                    ; Order:currency               ; $ORDER_IO:Currency",
                "                                            ; $ORDER_IO:Order          ; user                        ; Order:user                   ; $ORDER_IO:User    ;",
                "                                            ; $ORDER_IO:Order          ; date                        ; Order:date                   ;",

                "                                            ; $ORDER_IO:Currency       ; isocode                     ; Currency:isocode             ;",

                "                                            ; $ORDER_IO:User           ; uid                         ; User:uid                     ;                           ;",

                "                                            ; $ORDER_IO:Catalog        ; id                          ; Catalog:id                   ;",

                "                                            ; $ORDER_IO:CatalogVersion ; catalog                     ; CatalogVersion:catalog       ; $ORDER_IO:Catalog",
                "                                            ; $ORDER_IO:CatalogVersion ; version                     ; CatalogVersion:version       ;",

                "                                            ; $ORDER_IO:Product        ; code                        ; Product:code                 ;",
                "                                            ; $ORDER_IO:Product        ; catalogVersion              ; Product:catalogVersion       ; $ORDER_IO:CatalogVersion",
                "                                            ; $ORDER_IO:Product        ; name                        ; Product:name                 ;",

                "                                            ; $ORDER_IO:Unit           ; code                        ; Unit:code                    ;",
                "                                            ; $ORDER_IO:Unit           ; name                        ; Unit:name                    ;",
                "                                            ; $ORDER_IO:Unit           ; unitType                    ; Unit:unitType                ;",

                "                                            ; $ORDER_IO:OrderEntry     ; entryGroupNumbers           ; OrderEntry:entryGroupNumbers ;",
                "                                            ; $ORDER_IO:OrderEntry     ; entryNumber                 ; OrderEntry:entryNumber       ;",
                "                                            ; $ORDER_IO:OrderEntry     ; quantity                    ; OrderEntry:quantity          ;",
                "                                            ; $ORDER_IO:OrderEntry     ; order                       ; OrderEntry:order             ; $ORDER_IO:Order   ; ; true",
                "                                            ; $ORDER_IO:OrderEntry     ; product                     ; OrderEntry:product           ; $ORDER_IO:Product ; ; true",
                "                                            ; $ORDER_IO:OrderEntry     ; unit                        ; OrderEntry:unit              ; $ORDER_IO:Unit    ;")

        importImpEx(
                'INSERT_UPDATE Unit; code[unique = true]; name[lang = en]; unitType;',
                '; pieces ; pieces ; pieces',
                'INSERT_UPDATE Catalog; id[unique = true]; name[lang = en]; defaultCatalog;',
                '; Default ; Default ; true',
                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;',
                '; Default ; Staged ; true',
                'INSERT_UPDATE Currency; isocode[unique = true]',
                '; EUR',
                'INSERT_UPDATE User; uid[unique = true]',
                '; anonymous')
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $PRODUCT_IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]        ; type(code)',
                "                                   ; $PRODUCT_IO                           ; ProductFeature             ; ProductFeature",
                "                                   ; $PRODUCT_IO                           ; ClassificationAttributeUnit; ClassificationAttributeUnit",
                "                                   ; $PRODUCT_IO                           ; Product                    ; Product",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attribute=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]                   ; attributeName[unique = true]; $attribute                          ; $refType                               ; unique[default = false]; autoCreate[default = false]',
                "                                            ; $PRODUCT_IO:ProductFeature             ; qualifier                   ; ProductFeature:qualifier            ;                                        ; true                   ; true",
                "                                            ; $PRODUCT_IO:ProductFeature             ; unit                        ; ProductFeature:unit                 ; $PRODUCT_IO:ClassificationAttributeUnit; true                   ; true",
                "                                            ; $PRODUCT_IO:ProductFeature             ; product                     ; ProductFeature:product              ; $PRODUCT_IO:Product                    ;",
                "                                            ; $PRODUCT_IO:ProductFeature             ; value                       ; ProductFeature:value                ;                                        ;",
                "                                            ; $PRODUCT_IO:ClassificationAttributeUnit; code                        ; ClassificationAttributeUnit:code    ;                                        ; true",
                "                                            ; $PRODUCT_IO:ClassificationAttributeUnit; unitType                    ; ClassificationAttributeUnit:unitType;                                        ; true",
                "                                            ; $PRODUCT_IO:Product                    ; code                        ; Product:code                        ;                                        ; true"
        )

    }

    def cleanup() {
        IntegrationTestUtil.removeAll OrderEntryModel
        IntegrationTestUtil.removeAll ProductFeatureModel
        IntegrationTestUtil.removeAll ProductModel
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == "OrderEntryExample" }
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == "TestForProductFeatureIO" }
    }

    @Test
    def "can persist without providing nullable integration key primitive attributes"() {
        given:
        def body = orderEntryWithoutEntryNumberJson()
        def postContext = ODataFacadeTestUtils.createContext(
                oDataPostRequest(ORDER_IO, "OrderEntries", body, Locale.ENGLISH, MediaType.APPLICATION_JSON_VALUE))
        when:
        def postResponse = facade.handleRequest(postContext)

        then:
        postResponse.getStatus() == HttpStatusCodes.CREATED
    }

    @Test
    def "can persist without providing nullable integration key referenced attributes"() {
        given:
        def productCode = "pFeature1"
        def qualifier = "pFeatureQualifier"
        def value = "pFeatureValue"
        importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                "                     ; $productCode       ; $CATALOG_VERSION"
        )

        def body = JsonBuilder.json()
                .withField("qualifier", qualifier)
                .withField("value", value)
                .withField("product", JsonBuilder.json().withField("code", productCode)).build()

        def postContext = ODataFacadeTestUtils.createContext(
                oDataPostRequest(PRODUCT_IO, "ProductFeatures", body, Locale.ENGLISH, MediaType.APPLICATION_JSON_VALUE))
        when:
        def postResponse = facade.handleRequest(postContext)

        then:
        postResponse.getStatus() == HttpStatusCodes.CREATED
        and:
        def json = JsonObject.createFrom postResponse.entityAsStream
        json.getString('d.integrationKey') == "null|null|$qualifier|$productCode"
    }

    @Test
    def "can get item with null referenced key attribute"() {
        given:
        def productCode = "pFeature1"
        def qualifier = "pFeatureQualifier"
        def value = "pFeatureValue"
        importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                "                     ; $productCode       ; $CATALOG_VERSION",
                'INSERT_UPDATE ProductFeature; product($catalogVersionHeader, code)[unique = true];qualifier ; value[mode=append, translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]; valuePosition[unique=true, default=0]',
                "                            ; $CATALOG_VERSION:$productCode                      ;$qualifier; \"string, $value\""
        )

        def getContext = get(PRODUCT_IO, "ProductFeatures", "null|null|$qualifier|$productCode")
        when:
        def postResponse = facade.handleRequest(getContext)

        then:
        postResponse.getStatus() == HttpStatusCodes.OK
    }

    static ODataContext get(String serviceName, String entitySet, String key) {
        ODataFacadeTestUtils.createContext ODataRequestBuilder.oDataGetRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(serviceName)
                        .withEntitySet(entitySet)
                        .withEntityKeys(key))
                .withContentType('application/json')
                .withAccepts('application/json')
    }

    def orderEntryWithoutEntryNumberJson() {
        JsonBuilder.json()
                .withField("order", orderWithOutAddressJson())
                .withField("product", productJson())
                .withField("unit", unitJson())
                .withField("quantity", "25").build()
    }

    def orderWithOutAddressJson() {
        JsonBuilder.json().withCode("code1")
                .withField("currency", currencyJson())
                .withField("user", userJson())
                .withField("date", "2019-04-02T08:59:04").build()
    }

    def userJson() {
        JsonBuilder.json()
                .withField("uid", "anonymous").build()
    }

    def currencyJson() {
        JsonBuilder.json()
                .withField("isocode", "EUR").build()
    }

    def productJson() {
        JsonBuilder.json()
                .withCode("test_article1")
                .withField("catalogVersion", catalogVersionJson()).build()
    }

    def catalogVersionJson() {
        JsonBuilder.json()
                .withField("catalog", catalogJson())
                .withField("version", "Staged").build()
    }

    def catalogJson() {
        JsonBuilder.json().withId("Default")
    }

    def unitJson() {
        JsonBuilder.json().withCode("pieces")
                .withField("name", "pieces")
                .withField("unitType", "pieces").build()
    }
}
