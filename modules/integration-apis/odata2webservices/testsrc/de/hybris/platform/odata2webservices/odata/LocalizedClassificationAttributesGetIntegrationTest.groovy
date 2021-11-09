/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.product.UnitModel
import de.hybris.platform.core.model.user.CustomerModel
import de.hybris.platform.inboundservices.util.LocalizationRule
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
import org.junit.Rule
import org.junit.Test

import javax.annotation.Resource

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class LocalizedClassificationAttributesGetIntegrationTest extends ServicelayerTransactionalSpockSpecification {
    private static final def CLASS_SYSTEM = 'Sports'
    private static final def CLASS_VERSION = 'Cycling'
    private static final def CLASS_SYSTEM_VERSION = "$CLASS_SYSTEM:$CLASS_VERSION"
    private static final String PRODUCTS = 'Products'
    private static final String PRODUCT_CODE_ENCODED = 'prod1'
    private static final String PRODUCT1_INTEGRATION_KEY = "$CLASS_VERSION|$CLASS_SYSTEM|$PRODUCT_CODE_ENCODED"
    private static final def CLASSIFICATION_CLASS = 'bicycle'
    private static final def BIKE_IO = 'BikeIO'
    private static final def PRODUCT1 = "$CLASS_SYSTEM_VERSION:$PRODUCT_CODE_ENCODED"
    private static final String LOCALIZED_ATTRIBUTES = 'localizedAttributes'
    private static final def QUALIFIER_PREFIX = "$CLASS_SYSTEM/$CLASS_VERSION"
    private static final String EN_EXTRAS = 'Gravel!!'
    private static final String DE_EXTRAS = 'Kies!!'

    @Resource(name = "defaultODataFacade")
    ODataFacade facade
    @Resource
    ODataContextGenerator oDataContextGenerator
    @Rule
    LocalizationRule localizationRule = LocalizationRule.initialize()

    def setup() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Language; isocode[unique=true]',
                '                      ; de',
                '                      ; fr')
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE ClassificationSystem; id[unique = true]',
                "                                  ; $CLASS_SYSTEM",
                'INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version[unique = true]',
                "                                         ; $CLASS_SYSTEM             ; $CLASS_VERSION",
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                'INSERT_UPDATE ClassificationClass; code[unique = true]  ; $catalogVersionHeader[unique = true]',
                "                                 ; $CLASSIFICATION_CLASS; $CLASS_SYSTEM_VERSION",
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                'INSERT_UPDATE ClassificationAttribute; code[unique = true]; $systemVersionHeader[unique = true]',
                "                                     ; extras             ; $CLASS_SYSTEM_VERSION",
                '$class = classificationClass($catalogVersionHeader, code)',
                '$attribute = classificationAttribute($systemVersionHeader, code)',
                'INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]                      ; $attribute[unique = true]   ; attributeType(code); localized',
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS; $CLASS_SYSTEM_VERSION:extras; string             ; true")
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $BIKE_IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $BIKE_IO                              ; Product            ; Product",
                "                                   ; $BIKE_IO                              ; CatalogVersion     ; CatalogVersion",
                "                                   ; $BIKE_IO                              ; Catalog            ; Catalog",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $BIKE_IO:Catalog                                                   ; id                          ; Catalog:id",
                "                                            ; $BIKE_IO:Product                                                   ; code                        ; Product:code",
                "                                            ; $BIKE_IO:Product                                                   ; catalogVersion              ; Product:catalogVersion                             ; $BIKE_IO:CatalogVersion",
                "                                            ; $BIKE_IO:CatalogVersion                                            ; version                     ; CatalogVersion:version",
                "                                            ; $BIKE_IO:CatalogVersion                                            ; catalog                     ; CatalogVersion:catalog                             ; $BIKE_IO:Catalog",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $BIKE_IO:Product    ; bicycleExtras               ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:extras")
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)[unique = true]',
                'INSERT_UPDATE Product; code[unique = true]; $catalogVersionHeader[unique = true]',
                "                     ; prod1              ; $CLASS_SYSTEM_VERSION",
                'UPDATE ClassificationClass; code[unique = true]  ; $catalogVersionHeader; products($catalogVersionHeader, code)',
                "                          ; $CLASSIFICATION_CLASS; $CLASS_SYSTEM_VERSION; $PRODUCT1",
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$productHeader=product($catalogVersionHeader, code)',
                '$attributeAssignmentHeader=classificationAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                '$valueHeader=value[translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]',
                'INSERT_UPDATE ProductFeature; $productHeader[unique = true]; $attributeAssignmentHeader[unique = true]                               ; qualifier                       ; $valueHeader          ; language(isocode); valuePosition[unique = true]',
                "                            ; $PRODUCT1                    ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:extras; $QUALIFIER_PREFIX/bicycle.extras; \"string, $EN_EXTRAS\"; en               ; 0",
                "                            ; $PRODUCT1                    ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:extras; $QUALIFIER_PREFIX/bicycle.extras; \"string, $DE_EXTRAS\"; de               ; 1")
    }

    def cleanup() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll ProductModel
        IntegrationTestUtil.remove LanguageModel, { it.isocode == 'de' || it.isocode == 'fr' }
        IntegrationTestUtil.removeAll ClassAttributeAssignmentModel
        IntegrationTestUtil.removeAll ClassificationAttributeModel
        IntegrationTestUtil.removeAll ClassificationAttributeUnitModel
        IntegrationTestUtil.removeAll ClassificationClassModel
        IntegrationTestUtil.removeAll ClassificationSystemModel
    }

    @Test
    def "all values for classification localizedAttributes returned when requesting product's localizedAttributes navigation segment"() {
        when:
        def response = facade.handleRequest oDataContext(PRODUCT1_INTEGRATION_KEY, LOCALIZED_ATTRIBUTES)

        then:
        response.status == HttpStatusCodes.OK
        def json = extractBody response as IntegrationODataResponse
        json.getCollectionOfObjects('d.results[*].language') == ['de', 'en']
        json.getCollectionOfObjects('d.results[*].bicycleExtras') == [DE_EXTRAS, EN_EXTRAS]
    }

    @Test
    def "classification localizedAttributes are returned in the platform's default language"() {
        given:
        localizationRule.setSessionLanguage("de")

        when:
        def response = facade.handleRequest oDataContext(PRODUCT1_INTEGRATION_KEY)

        then:
        def json = extractBody response as IntegrationODataResponse
        json.getString('d.bicycleExtras') == DE_EXTRAS

        cleanup:
        localizationRule.sessionLanguage = 'en'
    }

    @Test
    def "classification localizedAttributes of type ValueList are returned"() {
        given: 'an enum classification attribute exists and it is exposed in the IO'
        final String productColor = "black"
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                'INSERT_UPDATE ClassificationAttribute; code[unique = true]; $systemVersionHeader[unique = true]',
                "                                     ; color             ; $CLASS_SYSTEM_VERSION",

                '$systemVersionHeader = systemVersion(catalog(id), version)',
                'INSERT_UPDATE ClassificationAttributeValue; $systemVersionHeader[unique = true]; code[unique = true]; name',
                "; $CLASS_SYSTEM_VERSION         ; $productColor               ; Black",

                '$class = classificationClass($catalogVersionHeader, code)',
                '$attribute = classificationAttribute($systemVersionHeader, code)',
                'INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]                      ; $attribute[unique = true]  ; attributeType(code); localized; attributeValues(code, systemVersion(catalog(id), version));',
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS; $CLASS_SYSTEM_VERSION:color; enum               ; true     ; $productColor:$CLASS_SYSTEM_VERSION",

                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $BIKE_IO:Product    ; color               ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:color")
        and: "an existing product has values for the enum classification attribute"
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$productHeader=product($catalogVersionHeader, code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$attributeAssignmentHeader=classificationAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$valueHeader=value[translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]',
                'INSERT_UPDATE ProductFeature; $productHeader[unique = true]; $attributeAssignmentHeader[unique = true]                              ; qualifier                      ; $valueHeader                                          ; language(isocode); valuePosition[unique = true]',
                "                            ; $PRODUCT1                    ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:color; $QUALIFIER_PREFIX/bicycle.color; \"enum, $CLASS_SYSTEM, $CLASS_VERSION, $productColor\"; en               ; 0")

        when:
        def response = facade.handleRequest oDataContext(PRODUCT1_INTEGRATION_KEY, ['$expand': 'localizedAttributes'])

        then:
        response.status == HttpStatusCodes.OK
        def json = extractBody response as IntegrationODataResponse
        json.getString('d.color') == productColor
        json.getString('d.localizedAttributes.results[*].color').contains(productColor)
    }

    @Test
    def "all supported languages are provided in the response when \$expand=localizedAttributes"() {
        given:
        def context = oDataContext(PRODUCT1_INTEGRATION_KEY, ['$expand': 'localizedAttributes'])

        when:
        def response = facade.handleRequest(context)

        then:
        response.status == HttpStatusCodes.OK
        def json = extractBody response as IntegrationODataResponse
        json.getCollectionOfObjects('d.localizedAttributes.results[*].language') == ['de', 'en']
        json.getCollectionOfObjects('d.localizedAttributes.results[*].bicycleExtras') == [DE_EXTRAS, EN_EXTRAS]
    }

    @Test
    def "localized classification attribute with null value is not returned in response body"() {
        given: 'requests French locale, for which data were not set in the setup'
        def request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withAcceptLanguage(Locale.FRENCH)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(BIKE_IO)
                        .withEntitySet(PRODUCTS)
                        .withEntityKeys(PRODUCT1_INTEGRATION_KEY))

        when:
        def response = facade.handleRequest oDataContext(request)

        then:
        response.status == HttpStatusCodes.OK
        def json = extractBody response as IntegrationODataResponse
        !json.exists('d.results[*].bicycleExtras')
    }

    @Test
    def "includes deferred classification localizedAttributes for item with localizedAttributes"() {
        given:
        def request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(BIKE_IO)
                        .withEntitySet(PRODUCTS))

        when:
        def response = facade.handleRequest oDataContext(request)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = extractBody response as IntegrationODataResponse
        json.exists 'd.results[*].localizedAttributes.__deferred.uri'
    }

    @Test
    def "Expands nested classification localizedAttributes"() {
        given:
        def customer = 'ob1'
        def usd = 'us'
        def unit = 'pieces'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $BIKE_IO                              ; Order              ; Order     ; true",
                "                                   ; $BIKE_IO                              ; OrderEntry         ; OrderEntry;",
                "                                   ; $BIKE_IO                              ; Customer           ; Customer  ;",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor           ; $attributeType     ; unique[default = false]',
                "                                            ; $BIKE_IO:Order      ; code                        ; Order:code            ;                    ;",
                "                                            ; $BIKE_IO:Order      ; user                        ; Order:user            ; $BIKE_IO:Customer  ;",
                "                                            ; $BIKE_IO:Order      ; entries                     ; Order:entries         ; $BIKE_IO:OrderEntry;",
                "                                            ; $BIKE_IO:Customer   ; uid                         ; User:uid              ;                    ;",
                "                                            ; $BIKE_IO:OrderEntry ; entryNumber                 ; OrderEntry:entryNumber;                    ; true",
                "                                            ; $BIKE_IO:OrderEntry ; order                       ; OrderEntry:order      ; $BIKE_IO:Order     ; true",
                "                                            ; $BIKE_IO:OrderEntry ; product                     ; OrderEntry:product    ; $BIKE_IO:Product   ;",
                'INSERT_UPDATE Customer; uid[unique = true]',
                "                      ; $customer",
                'INSERT_UPDATE Currency; isocode[unique = true]; symbol',
                "                      ; $usd                  ; \$",
                'INSERT_UPDATE Unit; code[unique = true]; unitType',
                "                  ; $unit              ; $unit"
        )


        def orderCode = 'order2'
        and: "an order exists with a product"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Order; code[unique = true]; user(uid); &orderPk; currency(isocode); date[dateformat=MM/dd/yyyy]',
                "                   ; $orderCode         ; $customer; theOrder; $usd             ; 02/20/2020",
                'INSERT_UPDATE OrderEntry; order(&orderPk)[unique = true]; entryNumber[unique = true]; product(code); unit(code); quantity',
                "                        ; theOrder                      ; 1                         ; prod1        ; $unit     ; 1")


        def context = oDataContext(null, ['$expand': 'product/localizedAttributes'], 'OrderEntries')

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = extractBody response as IntegrationODataResponse
        json.getCollection("d.results").size() == 1
        json.getCollectionOfObjects("d.results[*].product.localizedAttributes.results[*].language") == ['de', 'en']
        json.getCollectionOfObjects("d.results[*].product.localizedAttributes.results[*].bicycleExtras") == [DE_EXTRAS, EN_EXTRAS]

        cleanup:
        IntegrationTestUtil.removeAll OrderModel
        IntegrationTestUtil.removeAll UnitModel
        IntegrationTestUtil.remove CurrencyModel, { it.isocode == usd }
        IntegrationTestUtil.remove CustomerModel, { it.uid == customer }
    }

    JsonObject extractBody(IntegrationODataResponse response) {
        JsonObject.createFrom response.entityAsStream
    }

    ODataContext oDataContext(final String integrationKey) {
        oDataContext(integrationKey, "", [:], null)
    }

    ODataContext oDataContext(final String integrationKey, final String navigationSegment) {
        oDataContext(integrationKey, navigationSegment, [:], null)
    }

    ODataContext oDataContext(final String integrationKey, Map params) {
        oDataContext(integrationKey, "", params, null)
    }

    ODataContext oDataContext(final String integrationKey, Map params, String entitySet) {
        oDataContext(integrationKey, "", params, null, entitySet)
    }

    ODataContext oDataContext(String integrationKey, String navigationSegment, Map params, Locale locale, String entitySet = PRODUCTS) {
        oDataContext ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withAcceptLanguage(locale)
                .withParameters(params)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(BIKE_IO)
                        .withEntitySet(entitySet)
                        .withEntityKeys(integrationKey)
                        .withNavigationSegment(navigationSegment))
    }

    ODataContext oDataContext(ODataRequestBuilder builder) {
        oDataContextGenerator.generate builder.build()
    }
}
