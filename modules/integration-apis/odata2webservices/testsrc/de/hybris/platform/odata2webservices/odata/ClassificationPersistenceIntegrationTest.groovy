/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.ProductFeatureModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.assertModelDoesNotExist
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importCatalogVersion
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ClassificationAttributeUnitBuilder.classificationAttributeUnit
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.oDataGetRequest
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.oDataPostRequest
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ClassificationPersistenceIntegrationTest extends ServicelayerTransactionalSpockSpecification {

    private static final String CATALOG_VERSION_VERSION = 'testCatalogVersion'
    private static final String CATALOG_ID = 'testCatalog'
    private static final String PRODUCT_CODE = 'testProduct'
    private static final String PRODUCT_INTEGRATION_KEY = "$CATALOG_VERSION_VERSION|$CATALOG_ID|$PRODUCT_CODE"
    private static final String PRODUCT_FEATURE_QUALIFIER = 'testProductFeatureQualifier'
    private static final String PRODUCTS = "Products"
    private static final String PRODUCT_IO = 'InboundProductFeature'
    private static final int PRODUCT_FEATURE_VALUE_POSITION = 1

    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setupSpec() {
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]   ; integrationType(code)',
                "                               ; $PRODUCT_IO           ; INBOUND",

                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]         ; type(code)                  ; root[default = false]',
                "                                   ; $PRODUCT_IO                           ; Catalog                     ; Catalog                     ;                      ",
                "                                   ; $PRODUCT_IO                           ; Product                     ; Product                     ; true                 ",
                "                                   ; $PRODUCT_IO                           ; CatalogVersion              ; CatalogVersion              ;                      ",
                "                                   ; $PRODUCT_IO                           ; ProductFeature              ; ProductFeature              ;                      ",
                "                                   ; $PRODUCT_IO                           ; ClassificationAttributeUnit ; ClassificationAttributeUnit ;                      ",
                "                                   ; $PRODUCT_IO                           ; ClassificationSystemVersion ; ClassificationSystemVersion ;                      ",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]',
                "                                            ; $PRODUCT_IO:Catalog                                                ; id                          ; Catalog:id                                         ;                                                           ;                        ;                            ",
                "                                            ; $PRODUCT_IO:Product                                                ; code                        ; Product:code                                       ;                                                           ;                        ;                            ",
                "                                            ; $PRODUCT_IO:Product                                                ; features                    ; Product:features                                   ; $PRODUCT_IO:ProductFeature                                ;                        ;                            ",
                "                                            ; $PRODUCT_IO:Product                                                ; catalogVersion              ; Product:catalogVersion                             ; $PRODUCT_IO:CatalogVersion                                ;                        ;                            ",
                "                                            ; $PRODUCT_IO:CatalogVersion                                         ; version                     ; CatalogVersion:version                             ;                                                           ;                        ;                            ",
                "                                            ; $PRODUCT_IO:CatalogVersion                                         ; active                      ; CatalogVersion:active                              ;                                                           ;                        ;                            ",
                "                                            ; $PRODUCT_IO:CatalogVersion                                         ; catalog                     ; CatalogVersion:catalog                             ; $PRODUCT_IO:Catalog                                       ;                        ;                            ",
                "                                            ; $PRODUCT_IO:ProductFeature                                         ; qualifier                   ; ProductFeature:qualifier                           ;                                                           ;                        ;                            ",
                "                                            ; $PRODUCT_IO:ProductFeature                                         ; valuePosition               ; ProductFeature:valuePosition                       ;                                                           ;                        ;                            ",
                "                                            ; $PRODUCT_IO:ProductFeature                                         ; value                       ; ProductFeature:value                               ;                                                           ;                        ;                            ",
                "                                            ; $PRODUCT_IO:ProductFeature                                         ; unit                        ; ProductFeature:unit                                ; $PRODUCT_IO:ClassificationAttributeUnit                   ;                        ;                            ",
                "                                            ; $PRODUCT_IO:ProductFeature                                         ; product                     ; ProductFeature:product                             ; $PRODUCT_IO:Product                                       ;                        ;                            ",
                "                                            ; $PRODUCT_IO:ClassificationAttributeUnit                            ; code                        ; ClassificationAttributeUnit:code                   ;                                                           ; true                   ;                            ",
                "                                            ; $PRODUCT_IO:ClassificationAttributeUnit                            ; systemVersion               ; ClassificationAttributeUnit:systemVersion          ; $PRODUCT_IO:ClassificationSystemVersion                   ; true                   ;                            ",
                "                                            ; $PRODUCT_IO:ClassificationSystemVersion                            ; version                     ; ClassificationSystemVersion:version                ;                                                           ; true                   ;                            ",
                "                                            ; $PRODUCT_IO:ClassificationSystemVersion                            ; catalog                     ; ClassificationSystemVersion:catalog                ; $PRODUCT_IO:Catalog                                       ; true                   ;                            "
        )
    }

    def cleanup() {
        IntegrationTestUtil.removeSafely ProductFeatureModel, { it.qualifier == PRODUCT_FEATURE_QUALIFIER }
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    @Test
    def "product feature with unit classification is created"() {
        given:
        importClassificationAttributeUnit()
        def catalogVersion = importCatalogVersion(CATALOG_VERSION_VERSION, CATALOG_ID, true)

        and:
        def product = new ProductModel()
        product.setCode(PRODUCT_CODE)
        product.setCatalogVersion(catalogVersion)
        assertModelDoesNotExist(product)

        when:
        def postResponse = postProduct(PRODUCT_IO, productFeatureWithUnitContent())

        then:
        postResponse.getStatus() == HttpStatusCodes.CREATED
        and:
        def getResponse = getProductFeatures(PRODUCT_IO, PRODUCT_INTEGRATION_KEY)
        getResponse.getStatus() == HttpStatusCodes.OK
        numberOfFeatures(getResponse) == 1
        featureIntegrationKeyAt(0, getResponse) == "$CATALOG_VERSION_VERSION|${ClassificationSystemBuilder.ID}|$CATALOG_ID|${ClassificationAttributeUnitBuilder.CODE}|${ClassificationSystemVersionBuilder.VERSION}|$PRODUCT_FEATURE_QUALIFIER|$PRODUCT_FEATURE_VALUE_POSITION|$PRODUCT_CODE"
    }

    private static ClassificationAttributeUnitModel importClassificationAttributeUnit() {
        classificationAttributeUnit().build()
    }

    private static String productFeatureWithUnitContent() {
        product()
                .withField('features', [feature(unit())])
                .build()
    }

    private static JsonBuilder product() {
        json()
                .withCode(PRODUCT_CODE)
                .withField("catalogVersion", catalogVersion(CATALOG_VERSION_VERSION, CATALOG_ID))
    }

    private static JsonBuilder catalogVersion(String version, String catalogId) {
        json()
                .withField("version", version)
                .withField("catalog", catalog(catalogId))
    }

    private static JsonBuilder catalog(String id) {
        json()
                .withId(id)
    }

    private static JsonBuilder feature(JsonBuilder unit) {
        json()
                .withField('qualifier', PRODUCT_FEATURE_QUALIFIER)
                .withField('valuePosition', PRODUCT_FEATURE_VALUE_POSITION)
                .withField('value', 'testProductFeatureValue')
                .withField('product', product())
                .withField("unit", unit)
    }

    private static JsonBuilder unit() {
        json()
                .withCode(ClassificationAttributeUnitBuilder.CODE)
                .withField('systemVersion', catalogVersion(ClassificationSystemVersionBuilder.VERSION, ClassificationSystemVersionBuilder.CLASSIFICATION_SYSTEM))
    }

    private ODataResponse postProduct(String service, String content) {
        def request = oDataPostRequest(service, PRODUCTS, content, Locale.ENGLISH, APPLICATION_JSON_VALUE)
        facade.handleRequest(createContext(request))
    }

    private ODataResponse getProductFeatures(String service, String integrationKey) {
        def getRequest = oDataGetRequest(service, PRODUCTS, Locale.ENGLISH, "features", integrationKey)
        facade.handleRequest(createContext(getRequest))
    }

    private static int numberOfFeatures(ODataResponse response) {
        JsonObject.createFrom(response.getEntityAsStream())
                .getCollectionOfObjects('d.results')
                .size()
    }

    private static String featureIntegrationKeyAt(int index, ODataResponse response) {
        JsonObject.createFrom(response.getEntityAsStream())
                .getString("d.results[$index].integrationKey")
    }
}
