/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.classification.*
import de.hybris.platform.core.model.enumeration.EnumerationValueModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ODataReadClassificationAttributesIntegrationTest extends ServicelayerTransactionalSpockSpecification {
    private static final def CLASS_SYSTEM = 'Sports'
    private static final def CLASS_VERSION = 'Cycling'
    private static final def CLASS_SYSTEM_VERSION = "$CLASS_SYSTEM:$CLASS_VERSION"
    private static final def CLASSIFICATION_CLASS = "bicycle"
    private static final def BIKE_IO = 'BikeIO'
    private static final def PRODUCT1 = 'Default:Staged:prod1'
    private static final def PRODUCT2 = 'Default:Staged:prod2'
    private static final def PRODUCT3 = 'Default:Staged:prod3'
    private static final def CATALOG = 'Default'
    private static final def VERSION = 'Staged'
    private static final def CATALOG_VERSION = "$CATALOG:$VERSION"
    private static final def QUALIFIER_PREFIX = "$CLASS_SYSTEM/$CLASS_VERSION"

    /* Impex header variables */
    private static final def CATALOG_VERSION_HEADER = 'catalogVersion(catalog(id), version)'
    private static final def SYSTEM_VERSION_HEADER = 'systemVersion(catalog(id), version)'
    private static final def CLASS_HEADER = "classificationClass($CATALOG_VERSION_HEADER, code)"
    private static final def CLASS_ATTRIBUTE_HEADER = "classificationAttribute($SYSTEM_VERSION_HEADER, code)"
    private static final def CLASS_ASSIGNMENT_HEADER = "classAttributeAssignment($CLASS_HEADER, $CLASS_ATTRIBUTE_HEADER)"
    private static final def CLASS_ATTR_ASSIGNMENT_HEADER = "classificationAttributeAssignment($CLASS_HEADER, $CLASS_ATTRIBUTE_HEADER)"
    private static final def ITEM_HEADER = 'integrationObjectItem(integrationObject(code), code)'

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setup() {
        IntegrationTestUtil.importImpEx(
                /* Create IO Definitions */
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $BIKE_IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]  ; type(code)',
                "                                   ; $BIKE_IO                              ; Product              ; Product",
                "                                   ; $BIKE_IO                              ; CatalogVersion       ; CatalogVersion",
                "                                   ; $BIKE_IO                              ; Catalog              ; Catalog",
                "                                   ; $BIKE_IO                              ; SyncItemStatus       ; SyncItemStatus",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $BIKE_IO:Catalog                                                   ; id                          ; Catalog:id",
                "                                            ; $BIKE_IO:Product                                                   ; code                        ; Product:code",
                "                                            ; $BIKE_IO:Product                                                   ; catalogVersion              ; Product:catalogVersion                             ; $BIKE_IO:CatalogVersion",
                "                                            ; $BIKE_IO:CatalogVersion                                            ; version                     ; CatalogVersion:version",
                "                                            ; $BIKE_IO:CatalogVersion                                            ; catalog                     ; CatalogVersion:catalog                             ; $BIKE_IO:Catalog",
                "                                            ; $BIKE_IO:SyncItemStatus                                            ; code                        ; SyncItemStatus:code",
                /* Create 1 Catalog, 1 CatalogVersion & 3 Products */
                'INSERT_UPDATE Catalog; id[unique = true]; name[lang = en]; defaultCatalog;',
                "; $CATALOG ; $CATALOG ; true",
                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;',
                "; $CATALOG ; $VERSION ; true",

                "INSERT_UPDATE Product	; code[unique = true]; $CATALOG_VERSION_HEADER; name",
                "                       ; prod1              ; $CATALOG_VERSION       ; Canyon Ultimate",
                "                       ; prod2              ; $CATALOG_VERSION       ; Parlee Chebacco",
                "                       ; prod3              ; $CATALOG_VERSION       ; Giant Tcr",
                /* Create Classification Attribute Objects */
                'INSERT_UPDATE ClassificationSystem; id[unique = true]',
                "                                  ; $CLASS_SYSTEM",
                'INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version[unique = true]',
                "                                         ; $CLASS_SYSTEM             ; $CLASS_VERSION",
                "INSERT_UPDATE ClassificationClass; code[unique = true]; $CATALOG_VERSION_HEADER[unique = true]; products($CATALOG_VERSION_HEADER, code)",
                "                                 ; $CLASSIFICATION_CLASS         ; $CLASS_SYSTEM_VERSION      ; $PRODUCT1, $PRODUCT2, $PRODUCT3",
                "INSERT_UPDATE ClassificationAttributeUnit; $SYSTEM_VERSION_HEADER[unique = true]; code[unique = true]; symbol; unitType",
                "                                         ; $CLASS_SYSTEM_VERSION                ; centimeters        ; cm    ; measurement",
                "                                         ; $CLASS_SYSTEM_VERSION                ; kilograms          ; kg    ; measurement",
                "INSERT_UPDATE ClassificationAttribute; code[unique = true]; $SYSTEM_VERSION_HEADER[unique = true]",
                "                                     ; availableSizes     ; $CLASS_SYSTEM_VERSION",
                "                                     ; weight             ; $CLASS_SYSTEM_VERSION",
                "                                     ; extras             ; $CLASS_SYSTEM_VERSION",
                "INSERT_UPDATE ClassAttributeAssignment; $CLASS_HEADER[unique = true]                ; $CLASS_ATTRIBUTE_HEADER[unique = true]   ; unit($SYSTEM_VERSION_HEADER, code); attributeType(code); mandatory[default = false]; multiValued[default=false]",
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS ; $CLASS_SYSTEM_VERSION:availableSizes     ; $CLASS_SYSTEM_VERSION:centimeters ; number             ; true ; true",
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS ; $CLASS_SYSTEM_VERSION:weight 	        ; $CLASS_SYSTEM_VERSION:kilograms   ; number             ; true",
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS ; $CLASS_SYSTEM_VERSION:extras 	        ;                                   ; string             ; ",

                "INSERT_UPDATE ProductFeature; product($CATALOG_VERSION_HEADER, code)[unique = true]; $CLASS_ATTR_ASSIGNMENT_HEADER[unique = true]                                      ; qualifier 						; value[mode=append, translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]; valuePosition[unique=true, default=0]; unit($SYSTEM_VERSION_HEADER, code)",
                "                            ; $PRODUCT1                                            ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:availableSizes  ; $QUALIFIER_PREFIX/bicycle.availableSizes  ; \"number, java.lang.Double, 56.0\"; 0                                                  ; $CLASS_SYSTEM_VERSION:centimeters",
                "                            ; $PRODUCT1                                            ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:availableSizes  ; $QUALIFIER_PREFIX/bicycle.availableSizes  ; \"number, java.lang.Double, 58.0\"; 1                                                  ; $CLASS_SYSTEM_VERSION:centimeters",
                "                            ; $PRODUCT1                                            ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:availableSizes  ; $QUALIFIER_PREFIX/bicycle.availableSizes  ; \"number, java.lang.Double, 61.0\"; 2                                                  ; $CLASS_SYSTEM_VERSION:centimeters",
                "                            ; $PRODUCT1                                           	; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:weight          ; $QUALIFIER_PREFIX/bicycle.weight          ; \"number, java.lang.Double, 7.3\";                                                     ; $CLASS_SYSTEM_VERSION:kilograms",
                "                            ; $PRODUCT2                                           	; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:availableSizes  ; $QUALIFIER_PREFIX/bicycle.availableSizes	; \"number, java.lang.Double, 54.0\"; 0                                                  ; $CLASS_SYSTEM_VERSION:centimeters",
                "                            ; $PRODUCT2                                           	; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:availableSizes  ; $QUALIFIER_PREFIX/bicycle.availableSizes	; \"number, java.lang.Double, 56.0\"; 1                                                  ; $CLASS_SYSTEM_VERSION:centimeters",
                "                            ; $PRODUCT2                                           	; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:availableSizes  ; $QUALIFIER_PREFIX/bicycle.availableSizes	; \"number, java.lang.Double, 61.0\"; 2                                                  ; $CLASS_SYSTEM_VERSION:centimeters",
                "                            ; $PRODUCT2                                           	; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:weight          ; $QUALIFIER_PREFIX/bicycle.weight          ; \"number, java.lang.Double, 8.4\";                                                     ; $CLASS_SYSTEM_VERSION:kilograms",
                "                            ; $PRODUCT2                                           	; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:extras          ; $QUALIFIER_PREFIX/bicycle.extras          ; \"string, Gravel!!\"",
                "                            ; $PRODUCT3                                           	; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:availableSizes  ; $QUALIFIER_PREFIX/bicycle.availableSizes  ; \"number, java.lang.Double, 52.0\"; 0                                                   ; $CLASS_SYSTEM_VERSION:centimeters",
                "                            ; $PRODUCT3                                           	; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:availableSizes  ; $QUALIFIER_PREFIX/bicycle.availableSizes  ; \"number, java.lang.Double, 54.0\"; 1                                                   ; $CLASS_SYSTEM_VERSION:centimeters",
                "                            ; $PRODUCT3                                           	; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:weight          ; $QUALIFIER_PREFIX/bicycle.weight          ; \"number, java.lang.Double, 6.8\";                                                                       ; $CLASS_SYSTEM_VERSION:kilograms",

                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $BIKE_IO:Product           ; bicycleSizes                ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:availableSizes",
                "                                                          ; $BIKE_IO:Product           ; bicycleWeight               ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:weight",
                "                                                          ; $BIKE_IO:Product           ; bicycleExtras               ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:extras",
        )
    }

    def cleanup() {
        IntegrationTestUtil.removeAll ClassAttributeAssignmentModel
        IntegrationTestUtil.removeAll ClassificationAttributeModel
        IntegrationTestUtil.removeAll ClassificationAttributeUnitModel
        IntegrationTestUtil.removeAll ClassificationClassModel
        IntegrationTestUtil.removeAll ClassificationSystemModel
        IntegrationTestUtil.removeAll ProductModel
    }

    @Test
    def 'classification attributes show in a GET request and are no different from standard attributes'() {
        when:
        def response = facade.handleRequest oDataGetContext('Products', [:])
        def getBody = extractBody response as IntegrationODataResponse

        then:
        with(getBody) {
            getCollection("\$.d.results").size() == 3
            getCollectionOfObjects("d.results[*].code").containsAll("prod1", "prod2", "prod3")
            getCollectionOfObjects("d.results[*].bicycleWeight").containsAll("7.3", "8.4", "6.8")
            getCollectionOfObjects("d.results[*].bicycleExtras").containsAll("Gravel!!")
        }
    }

    @Test
    def 'one product with classification attributes is returned when a GET request is made for a specific integration key'() {
        when:
        def response = facade.handleRequest oDataGetContext('Products', [:], 'Staged|Default|prod3')
        def getBody = extractBody response as IntegrationODataResponse

        then:
        with(getBody) {
            getString("d.code") == 'prod3'
            getObject("d.bicycleWeight") == '6.8'
        }
    }

    @Test
    def 'not_visible classification attributes are not returned in the payload'() {
        given:
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE ClassificationAttribute; code[unique = true]; $SYSTEM_VERSION_HEADER[unique = true]",
                "                                     ; secretAttribute    ; $CLASS_SYSTEM_VERSION",
                "INSERT_UPDATE ClassAttributeAssignment; $CLASS_HEADER[unique = true]                ; $CLASS_ATTRIBUTE_HEADER[unique = true]   ; unit($SYSTEM_VERSION_HEADER, code); attributeType(code); mandatory[default = false]; multiValued[default=false]; visibility(code)",
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS ; $CLASS_SYSTEM_VERSION:secretAttribute    ;                                   ; string             ;                           ;                           ; not_visible",
                "INSERT_UPDATE ProductFeature; product($CATALOG_VERSION_HEADER, code)[unique = true]; $CLASS_ATTR_ASSIGNMENT_HEADER[unique = true]                                      ; qualifier                                 ; value[mode=append, translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]; valuePosition[unique=true, default=0]; unit($SYSTEM_VERSION_HEADER, code)",
                "                            ; $PRODUCT3                                           	; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:secretAttribute ; $QUALIFIER_PREFIX/bicycle.secretAttribute ; \"string, This is a secret value\";",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $BIKE_IO:Product           ; secretAttribute             ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:secretAttribute"
        )

        when:
        def response = facade.handleRequest oDataGetContext('Products', [:], 'Staged|Default|prod3')
        def getBody = extractBody response as IntegrationODataResponse

        then:
        response.status.is HttpStatusCodes.OK
        with(getBody) {
            !getString('d').contains('secretAttribute')
        }
    }

    @Unroll
    @Test
    def "classification attribute with visibility #visibility is returned in the payload"() {
        given:
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE ClassificationAttribute; code[unique = true]; $SYSTEM_VERSION_HEADER[unique = true]",
                "                                     ; $attribute         ; $CLASS_SYSTEM_VERSION",
                "INSERT_UPDATE ClassAttributeAssignment; $CLASS_HEADER[unique = true]                ; $CLASS_ATTRIBUTE_HEADER[unique = true]   ; unit($SYSTEM_VERSION_HEADER, code); attributeType(code); mandatory[default = false]; multiValued[default=false]; visibility(code)",
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS ; $CLASS_SYSTEM_VERSION:$attribute         ;                                   ; string             ;                           ;                           ; $visibility",
                "INSERT_UPDATE ProductFeature; product($CATALOG_VERSION_HEADER, code)[unique = true]; $CLASS_ATTR_ASSIGNMENT_HEADER[unique = true]                                      ; qualifier 						   ; value[mode=append, translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]; valuePosition[unique=true, default=0]; unit($SYSTEM_VERSION_HEADER, code)",
                "                            ; $PRODUCT3                                           	; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:$attribute      ; $QUALIFIER_PREFIX/bicycle.$attribute ; \"string, $value\";",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $BIKE_IO:Product           ; $attribute                  ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:$attribute"
        )

        when:
        def response = facade.handleRequest oDataGetContext('Products', [:], 'Staged|Default|prod3')
        def getBody = extractBody response as IntegrationODataResponse

        then:
        response.status.is HttpStatusCodes.OK
        with(getBody) {
            getString("d.$attribute") == value
        }

        where:
        visibility           | attribute                | value
        'visible'            | 'visibleAttribute'       | 'visibleAttributeValue'
        'visible_in_base'    | 'visibleInBaseAttribute' | 'visibleInBaseAttributeValue'
        'visible_in_variant' | 'visibleInVariant'       | 'visibleInVariantValue'
    }

    @Unroll
    @Test
    def 'expand on bicycleSizes multivalued classification attribute for product with key #key retrieves all available sizes #sizes'() {
        when:
        def response = facade.handleRequest oDataGetContext('Products', ['$expand': 'bicycleSizes'], key)
        def getBody = extractBody response as IntegrationODataResponse

        then:
        with(getBody) {
            getCollection("d.bicycleSizes.results[*].value").containsAll(sizes)
        }

        where:
        key                    | sizes
        'Staged|Default|prod1' | ['56.0', '58.0', '61.0']
        'Staged|Default|prod2' | ['54.0', '56.0', '61.0']
        'Staged|Default|prod3' | ['52.0', '54.0']
    }

    @Test
    def 'expand on classification attribute for product shows referenced item details'() {
        given: 'a classification attribute of a referenced Product item exists'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE ClassificationAttribute; code[unique = true]; $SYSTEM_VERSION_HEADER[unique = true]",
                "                                     ; relatedProduct     ; $CLASS_SYSTEM_VERSION",
                "INSERT_UPDATE ClassAttributeAssignment; $CLASS_HEADER[unique = true]               ; $CLASS_ATTRIBUTE_HEADER[unique = true]; attributeType(code); referenceType(code)[unique = true]",
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS; $CLASS_SYSTEM_VERSION:relatedProduct  ; reference          ; Product",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER                                                         ; returnIntegrationObjectItem(integrationObject(code), code)",
                "                                                          ; $BIKE_IO:Product           ; relatedProduct              ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:relatedProduct ; $BIKE_IO:Product"
        )

        and: 'prod1 has relatedProduct attribute set to prod2'
        def product2ItemModel = IntegrationTestUtil.findAny(ProductModel, { it.getCode() == 'prod2' }).orElse(null)

        IntegrationTestUtil.importImpEx(
                "\$product = product($CATALOG_VERSION_HEADER, code)",
                "INSERT_UPDATE ProductFeature; \$product[unique = true]; $CLASS_ATTR_ASSIGNMENT_HEADER[unique = true]                                    ; qualifier                               ; value[translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]",
                "                            ; $PRODUCT1               ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:relatedProduct; $QUALIFIER_PREFIX/bicycle.relatedProduct; \"reference, ${product2ItemModel.getPk().getLongValue()}\""
        )

        when:
        def response = facade.handleRequest oDataGetContext('Products', ['$expand': 'relatedProduct'], 'Staged|Default|prod1')
        def getBody = extractBody response as IntegrationODataResponse

        then:
        with(getBody) {
            getObject("d.relatedProduct.code") == 'prod2'
            getObject("d.relatedProduct.bicycleExtras") == 'Gravel!!'
            getObject("d.relatedProduct.bicycleWeight") == '8.4'
        }
    }

    @Test
    def 'expand on multivalued reference classification attribute for product shows referenced items details'() {
        given: 'a multivalued classification attribute of a referenced Product item exists'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE ClassificationAttribute; code[unique = true]; $SYSTEM_VERSION_HEADER[unique = true]",
                "                                     ; relatedProducts     ; $CLASS_SYSTEM_VERSION",
                "INSERT_UPDATE ClassAttributeAssignment; $CLASS_HEADER[unique = true]               ; $CLASS_ATTRIBUTE_HEADER[unique = true]; attributeType(code); referenceType(code)[unique = true]; multiValued",
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS; $CLASS_SYSTEM_VERSION:relatedProducts  ; reference          ; Product                          ; true",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER                                                         ; returnIntegrationObjectItem(integrationObject(code), code)",
                "                                                          ; $BIKE_IO:Product           ; relatedProducts              ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:relatedProducts ; $BIKE_IO:Product"
        )

        and: 'prod1 has relatedProduct attribute set to prod2 and prod3'
        def product2 = IntegrationTestUtil.findAny(ProductModel, { it.getCode() == 'prod2' }).orElse(null)
        def product3 = IntegrationTestUtil.findAny(ProductModel, { it.getCode() == 'prod3' }).orElse(null)

        IntegrationTestUtil.importImpEx(
                "\$product = product($CATALOG_VERSION_HEADER, code)",
                "INSERT_UPDATE ProductFeature; \$product[unique = true]; $CLASS_ATTR_ASSIGNMENT_HEADER[unique = true]                                     ; qualifier                                ; valuePosition[unique=true, default=0]; value[mode=append, translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]",
                "                            ; $PRODUCT1               ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:relatedProducts; $QUALIFIER_PREFIX/bicycle.relatedProducts; 0                                    ;\"reference, ${product2.getPk().getLongValue()}\"",
                "                            ; $PRODUCT1               ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:relatedProducts; $QUALIFIER_PREFIX/bicycle.relatedProducts; 1                                    ;\"reference, ${product3.getPk().getLongValue()}\""
        )

        when:
        def response = facade.handleRequest oDataGetContext('Products', ['$expand': 'relatedProducts'], 'Staged|Default|prod1')
        def getBody = extractBody response as IntegrationODataResponse

        then:
        with(getBody) {
            getCollection("\$.d.relatedProducts.results").size() == 2
            getCollectionOfObjects("d.relatedProducts.results[*].code").containsAll('prod2', 'prod3')
            getCollectionOfObjects("d.relatedProducts.results[*].bicycleExtras").containsAll('Gravel!!')
            getCollectionOfObjects("d.relatedProducts.results[*].bicycleWeight").containsAll('8.4', '6.8')
        }
    }

    @Test
    def 'expand on classification attribute for product shows static enum details'() {
        given: 'a classification attribute of a enum type exists'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE ClassificationAttribute; code[unique = true]; $SYSTEM_VERSION_HEADER[unique = true]",
                "                                     ; syncItemStatus; $CLASS_SYSTEM_VERSION",
                "INSERT_UPDATE ClassAttributeAssignment; $CLASS_HEADER[unique = true]                ; $CLASS_ATTRIBUTE_HEADER[unique = true]   ; attributeType(code); referenceType(code)[unique = true]",
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS ; $CLASS_SYSTEM_VERSION:syncItemStatus; reference          ; SyncItemStatus",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER                                                             ; returnIntegrationObjectItem(integrationObject(code), code)",
                "                                                          ; $BIKE_IO:Product           ; syncItemStatus         ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:syncItemStatus; $BIKE_IO:SyncItemStatus"
        )

        and: 'prod1 has syncItemStatus attribute set'
        def enumValueModel = IntegrationTestUtil.getModelByExample new EnumerationValueModel('COUNTERPART_MISSING')

        IntegrationTestUtil.importImpEx(
                "\$product = product($CATALOG_VERSION_HEADER, code)",
                "INSERT_UPDATE ProductFeature; \$product[unique = true]; $CLASS_ATTR_ASSIGNMENT_HEADER[unique = true]                                         ; qualifier                                    ; value[translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]",
                "                            ; $PRODUCT1               ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:syncItemStatus; $QUALIFIER_PREFIX/bicycle.syncItemStatus; \"reference, ${enumValueModel.getPk().getLongValue()}\""
        )

        when:
        def response = facade.handleRequest oDataGetContext('Products', ['$expand': 'syncItemStatus'], 'Staged|Default|prod1')
        def getBody = extractBody response as IntegrationODataResponse

        then:
        with(getBody) {
            getObject("d.syncItemStatus.code") == 'COUNTERPART_MISSING'
        }
    }

    @Test
    def 'expand on classification attribute for product shows dynamic enum details'() {
        given: 'a classification attribute of a dynamic enum type exists'
        def dynamicEnumValue = "actualEnumValue"
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE ClassificationAttributeValue; $SYSTEM_VERSION_HEADER[unique = true]; code[unique = true]",
                "                                          ; $CLASS_SYSTEM_VERSION                ; $dynamicEnumValue",
                "INSERT_UPDATE ClassificationAttribute; code[unique = true]; $SYSTEM_VERSION_HEADER[unique = true]",
                "                                     ; color              ; $CLASS_SYSTEM_VERSION",
                "INSERT_UPDATE ClassAttributeAssignment; $CLASS_HEADER[unique = true]                ; $CLASS_ATTRIBUTE_HEADER[unique = true]; attributeType(code); attributeValues(code, systemVersion(catalog(id), version))",
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS ; $CLASS_SYSTEM_VERSION:color           ; enum               ; $dynamicEnumValue:$CLASS_SYSTEM_VERSION",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $BIKE_IO:Product           ; integrationColorEnum        ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:color"
        )

        IntegrationTestUtil.importImpEx(
                "\$product = product($CATALOG_VERSION_HEADER, code)",
                "INSERT_UPDATE ProductFeature; \$product[unique = true]; $CLASS_ATTR_ASSIGNMENT_HEADER[unique = true]                            ; qualifier                     ; value[translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]",
                "                            ; $PRODUCT1               ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:color; $QUALIFIER_PREFIX/bicycle.color; \"enum, $CLASS_SYSTEM, $CLASS_VERSION, $dynamicEnumValue\""
        )

        when:
        def response = facade.handleRequest oDataGetContext('Products', [:], 'Staged|Default|prod1')
        def getBody = extractBody response as IntegrationODataResponse

        then:
        with(getBody) {
            getObject("d.integrationColorEnum") == dynamicEnumValue
        }
    }

    @Test
    def 'expand on classification attribute for product shows multivalue dynamic enum details'() {
        given: 'a classification attribute of a dynamic enum type exists'
        def dynamicEnumValue1 = "actualEnumValue1"
        def dynamicEnumValue2 = "actualEnumValue2"
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE ClassificationAttributeValue; $SYSTEM_VERSION_HEADER[unique = true]; code[unique = true]",
                "                                          ; $CLASS_SYSTEM_VERSION                ; $dynamicEnumValue1",
                "                                          ; $CLASS_SYSTEM_VERSION                ; $dynamicEnumValue2",
                "INSERT_UPDATE ClassificationAttribute; code[unique = true]; $SYSTEM_VERSION_HEADER[unique = true]",
                "                                     ; colors             ; $CLASS_SYSTEM_VERSION",
                "INSERT_UPDATE ClassAttributeAssignment; $CLASS_HEADER[unique = true]                ; $CLASS_ATTRIBUTE_HEADER[unique = true]; attributeType(code); multiValued; attributeValues(code, systemVersion(catalog(id), version))",
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS ; $CLASS_SYSTEM_VERSION:colors          ; enum               ; true       ; $dynamicEnumValue1:$CLASS_SYSTEM_VERSION, $dynamicEnumValue2:$CLASS_SYSTEM_VERSION",
                "INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $ITEM_HEADER[unique = true]; attributeName[unique = true]; $CLASS_ASSIGNMENT_HEADER",
                "                                                          ; $BIKE_IO:Product           ; integrationColors           ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:colors"
        )

        IntegrationTestUtil.importImpEx(
                "\$product = product($CATALOG_VERSION_HEADER, code)",
                "INSERT_UPDATE ProductFeature; \$product[unique = true]; $CLASS_ATTR_ASSIGNMENT_HEADER[unique = true]                            ; qualifier                       ; value[translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]; valuePosition[unique=true]",
                "                            ; $PRODUCT1               ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:colors; $QUALIFIER_PREFIX/bicycle.colors; \"enum, $CLASS_SYSTEM, $CLASS_VERSION, $dynamicEnumValue1\"                                           ; 0",
                "                            ; $PRODUCT1               ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:colors; $QUALIFIER_PREFIX/bicycle.colors; \"enum, $CLASS_SYSTEM, $CLASS_VERSION, $dynamicEnumValue2\"                                           ; 1"
        )

        when:
        def response = facade.handleRequest oDataGetContext('Products', ['$expand': 'integrationColors'], 'Staged|Default|prod1')
        def getBody = extractBody response as IntegrationODataResponse

        then:
        with(getBody) {
            getCollectionOfObjects("d.integrationColors.results[*].value").containsAll(dynamicEnumValue1, dynamicEnumValue2)
        }
    }

    @Test
    def 'a classification attribute is not shown when it is not assigned to the product'() {
        when:
        def response = facade.handleRequest oDataGetContext('Products', [:], 'Staged|Default|prod1')
        def getBody = extractBody response as IntegrationODataResponse

        then:
        !getBody.exists('d.bicycleExtras')
    }

    @Test
    def 'cannot filter by classification attributes in GET request'() {
        when:
        def response = facade.handleRequest oDataGetContext('Products', ['$filter': 'bicycleWeight eq 8'])
        def getBody = extractBody response as IntegrationODataResponse

        then:
        with(getBody) {
            response.getStatus() == HttpStatusCodes.NOT_IMPLEMENTED
            getString('\$.error.code') == 'filter_not_supported'
        }
    }

    JsonObject extractBody(IntegrationODataResponse response) {
        JsonObject.createFrom response.entityAsStream
    }

    ODataContext oDataGetContext(String entitySetName, Map params, String... keys) {
        def pathInfo = PathInfoBuilder.pathInfo()
                .withServiceName(BIKE_IO)
                .withEntitySet(entitySetName)

        if (keys.length > 0) {
            pathInfo.withEntityKeys(keys)
        }

        def request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(pathInfo)
                .withParameters(params)
                .build()

        contextGenerator.generate request
    }
}