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

package de.hybris.platform.odata2services.odata.schema

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.classification.*
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2services.odata.ODataSchema
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.junit.Test

import javax.annotation.Resource

@IntegrationTest
class SchemaGenerationLocalizationIntegrationTest extends ServicelayerSpockSpecification {
    private static final def IO = 'LocalizedSchema'
    private static final def IS_LANGUAGE_DEPENDENT = 's:IsLanguageDependent'
    private static final def SYSTEM = 'Electronics'
    private static final def VERSION = 'Test'
    private static final def SYSTEM_VERSION = "$SYSTEM:$VERSION"

    @Resource(name = "oDataSchemaGenerator")
    private SchemaGenerator generator

    def setup() {
        // Test IO
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO")
        // Test classification units, class(es) and attributes
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE ClassificationSystem; id[unique = true]',
                "                                  ; $SYSTEM",
                'INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version[unique = true]',
                "                                         ; $SYSTEM                   ; $VERSION",
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                'INSERT_UPDATE ClassificationAttributeUnit; $systemVersionHeader[unique = true]; code[unique = true]; symbol; unitType',
                "                                         ; $SYSTEM_VERSION                    ; centimeters        ; cm    ; measurement",
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                'INSERT_UPDATE ClassificationClass; code[unique = true]; $catalogVersionHeader[unique = true]',
                "                                 ; dimensions         ; $SYSTEM_VERSION",
                'INSERT_UPDATE ClassificationAttribute; code[unique = true]; $systemVersionHeader[unique = true]',
                "                                     ; height             ; $SYSTEM_VERSION",
                "                                     ; description        ; $SYSTEM_VERSION")
    }

    def cleanup() {
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll ClassAttributeAssignmentModel
        IntegrationTestUtil.removeAll ClassificationAttributeModel
        IntegrationTestUtil.removeAll ClassificationAttributeUnitModel
        IntegrationTestUtil.removeAll ClassificationClassModel
        IntegrationTestUtil.removeAll ClassificationSystemModel
    }

    @Test
    def "Schema for item with localized fields contains localized entity type, navigation property and association"() {
        given: 'integration object contains a localized attribute "name"'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; Unit               ; Unit",
                '$item=integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)',
                "                                            ; $IO:Unit            ; code                        ; Unit:code",
                "                                            ; $IO:Unit            ; name                        ; Unit:name")

        when:
        def schema = ODataSchema.from generator.generateSchema(getIntegrationObjectItemModelDefinitions())

        then: 'schema contains Localized type for the Unit type'
        schema.entityTypeNames == ['Localized___Unit', 'Unit']
        def unitType = schema.getEntityType('Unit')
        def localizedType = schema.getEntityType('Localized___Unit')

        and: 'the localized type contains all expected properties'
        localizedType.propertyNames == ['name', 'language']
        localizedType.keyProperties == ['language']

        and: 'localized properties are annotated respectively'
        unitType.getAnnotatableProperty('name').getAnnotation(IS_LANGUAGE_DEPENDENT).get().text == 'true'
        localizedType.getAnnotatableProperty('name').getAnnotation(IS_LANGUAGE_DEPENDENT).get().text == 'true'

        and: 'type Unit contains a navigation property to its localized type'
        unitType.navigationPropertyNames == ['localizedAttributes']

        and: 'the schema contains association between the Unit type and its localized type'
        schema.containsAssociationBetween('Unit', 'Localized___Unit')
        schema.defaultEntityContainer.containsAssociationSetBetween('Unit', 'Localized___Unit')
    }

    @Test
    def "Schema does not contain localized entity type when items have no localized attributes"() {
        given: 'integration object does not contain items with localized attributes'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; Unit               ; Unit",
                '$item=integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)',
                "                                            ; $IO:Unit            ; code                        ; Unit:code")

        when:
        def schema = ODataSchema.from generator.generateSchema(getIntegrationObjectItemModelDefinitions())

        then: 'schema does not contains Localized type for the Unit type'
        schema.entityTypeNames == ['Unit']

        and: 'non-localized properties are not annotated as language dependent'
        def unitType = schema.getEntityType('Unit')
        unitType.getAnnotatableProperty('code').getAnnotation(IS_LANGUAGE_DEPENDENT).empty

        and: 'type Unit has no navigation properties'
        unitType.navigationPropertyNames.empty

        and: 'the schema contains no associations'
        schema.associations.empty
        schema.defaultEntityContainer.associationSetNames.empty
    }

    @Test
    def 'Schema contains localized entity type, navigation property and association for items with localized classification attribute'() {
        given: 'Classifications with different types and mandatory specifications'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$class = classificationClass($catalogVersionHeader, code)',
                '$attribute = classificationAttribute($systemVersionHeader, code)',
                'INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]     ; $attribute[unique = true]   ; unit($systemVersionHeader, code); attributeType(code); localized',
                "                                      ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:height      ; $SYSTEM_VERSION:centimeters     ; number             ; false",
                "                                      ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:description ; $SYSTEM_VERSION:centimeters     ; string             ; true")

        and: 'Integration Object with classification attributes'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; Product            ; Product",
                "                                   ; $IO                                   ; CatalogVersion     ; CatalogVersion",
                "                                   ; $IO                                   ; Catalog            ; Catalog",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code)',
                "                                            ; $IO:Catalog                                                        ; id                          ; Catalog:id",
                "                                            ; $IO:Product                                                        ; code                        ; Product:code",
                "                                            ; $IO:Product                                                        ; catalogVersion              ; Product:catalogVersion                             ; $IO:CatalogVersion",
                "                                            ; $IO:CatalogVersion                                                 ; version                     ; CatalogVersion:version",
                "                                            ; $IO:CatalogVersion                                                 ; catalog                     ; CatalogVersion:catalog                             ; $IO:Catalog",

                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $IO:Product         ; height                      ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:height",
                "                                                          ; $IO:Product         ; description                 ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:description")

        when:
        def generatedSchema = generator.generateSchema getIntegrationObjectItemModelDefinitions()

        then: 'schema contains localized type for the Product type'
        def schema = ODataSchema.from generatedSchema
        schema.entityTypeNames.containsAll(['Product', 'Localized___Product'])
        def productType = schema.getEntityType 'Product'
        def localizedType = schema.getEntityType 'Localized___Product'

        and: 'the localized type contains all expected properties'
        localizedType.propertyNames == ['description', 'language']
        localizedType.keyProperties == ['language']

        and: 'localized properties are annotated respectively'
        productType.getAnnotatableProperty('height').getAnnotation(IS_LANGUAGE_DEPENDENT).empty
        productType.getAnnotatableProperty('description').getAnnotation(IS_LANGUAGE_DEPENDENT).get().text == 'true'
        localizedType.getAnnotatableProperty('description').getAnnotation(IS_LANGUAGE_DEPENDENT).get().text == 'true'

        and: 'type Product contains a navigation property to its localized type'
        productType.navigationPropertyNames.contains 'localizedAttributes'

        and: 'the schema contains association between the Product type and its localized type'
        schema.containsAssociationBetween('Product', 'Localized___Product')
        schema.defaultEntityContainer.containsAssociationSetBetween('Product', 'Localized___Product')
    }

    def getIntegrationObjectItemModelDefinitions() {
        IntegrationTestUtil.findAll(IntegrationObjectItemModel) as Collection<IntegrationObjectItemModel>
    }
}
