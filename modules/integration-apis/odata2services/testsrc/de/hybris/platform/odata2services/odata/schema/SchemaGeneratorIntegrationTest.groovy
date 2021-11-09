/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2services.odata.ODataSchema
import de.hybris.platform.scripting.model.ScriptModel
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.edm.EdmMultiplicity
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind
import org.apache.olingo.odata2.api.edm.FullQualifiedName
import org.junit.Test

import javax.annotation.Resource

@IntegrationTest
class SchemaGeneratorIntegrationTest extends ServicelayerSpockSpecification {

    private static final String SCHEMA_NAME = 'HybrisCommerceOData'
    private static final String PART_OF = 's:IsPartOf'
    private static final String IS_UNIQUE = 's:IsUnique'
    private static final String NULLABLE = 'Nullable'
    private static final String TEST_IO = 'SchemaGenerationTestIO'
    private static final def SYSTEM = 'Electronics'
    private static final def VERSION = 'Test'
    private static final def SYSTEM_VERSION = "$SYSTEM:$VERSION"
    private static final def CLASSIFICATION_CLASS = 'dimensions'

    @Resource(name = 'oDataSchemaGenerator')
    SchemaGenerator generator

    def setup() {
        // Test IO
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $TEST_IO")
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
                'INSERT_UPDATE ClassificationClass; code[unique = true];  $catalogVersionHeader[unique = true]',
                "                                 ; $CLASSIFICATION_CLASS ; $SYSTEM_VERSION",
                'INSERT_UPDATE ClassificationAttribute; code[unique = true]; $systemVersionHeader[unique = true]',
                "                                     ; relatedProduct     ; $SYSTEM_VERSION",
                "                                     ; classApprovalStatus; $SYSTEM_VERSION",
                "                                     ; height             ; $SYSTEM_VERSION",
                "                                     ; description        ; $SYSTEM_VERSION")
    }

    def cleanup() {
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll ClassificationAttributeUnitModel
        IntegrationTestUtil.removeAll ClassificationAttributeModel
        IntegrationTestUtil.removeAll ClassificationClassModel
        IntegrationTestUtil.removeAll ClassificationSystemModel
        IntegrationTestUtil.removeAll IntegrationObjectVirtualAttributeDescriptorModel
        IntegrationTestUtil.removeAll ScriptModel
    }

    @Test
    def 'schema is empty when no integration object items are provided'() {
        when: 'an empty item list is provided'
        def generatedSchema = generator.generateSchema([])

        then:
        generatedSchema.namespace == SCHEMA_NAME
        ODataSchema.from(generatedSchema).empty
    }

    @Test
    def 'schema contains entity type'() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Unit               ; Unit",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Unit                                                      ; code                        ; Unit:code")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def schema = ODataSchema.from generatedSchema
        with(schema) {
            entityTypeNames == ['Unit']
            defaultEntityContainer.entitySetTypes == ['Unit']
        }
    }

    @Test
    def 'schema contains integration key'() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Unit               ; Unit",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Unit                                                      ; code                        ; Unit:code")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def unit = ODataSchema.from(generatedSchema).getEntityType 'Unit'
        with(unit) {
            keyProperties.contains 'integrationKey'
            propertyNames.contains 'integrationKey'
        }
    }

    @Test
    def 'schema contains properties'() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Unit               ; Unit",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Unit                                                     ; code                         ; Unit:code",
                "                                            ; $TEST_IO:Unit                                                     ; name                         ; Unit:name")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def unit = ODataSchema.from(generatedSchema).getEntityType 'Unit'
        unit.propertyNames.containsAll(['code', 'name'])
    }

    @Test
    def 'schema contains associations'() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Product            ; Product",
                "                                   ; $TEST_IO                              ; Unit               ; Unit",
                "                                   ; $TEST_IO                              ; Address            ; Address",
                "                                   ; $TEST_IO                              ; Company            ; Company",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Unit                                                      ; code                        ; Unit:code",
                "                                            ; $TEST_IO:Unit                                                      ; name                        ; Unit:name",
                "                                            ; $TEST_IO:Product                                                   ; code                        ; Product:code",
                "                                            ; $TEST_IO:Product                                                   ; unit                        ; Product:unit                                       ; $TEST_IO:Unit",
                "                                            ; $TEST_IO:Address                                                   ; publicKey                   ; Address:publicKey                                  ;                                                           ; true",
                "                                            ; $TEST_IO:Company                                                   ; uid                         ; Company:uid",
                "                                            ; $TEST_IO:Company                                                   ; addresses                   ; Company:addresses                                  ; $TEST_IO:Address")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then: 'Unit associates with Product'
        def schema = ODataSchema.from generatedSchema
        with(schema) {
            containsAssociationBetween('Product', 'Unit')
            getEntityType('Product').navigationPropertyNames == ['unit']
            defaultEntityContainer.containsAssociationSetBetween('Product', 'Unit')
        }

        and: 'Address associates with Company'
        with(schema) {
            containsAssociationBetween('Company', 'Address')
            getEntityType('Company').navigationPropertyNames == ['addresses']
            defaultEntityContainer.containsAssociationSetBetween('Company', 'Address')
        }
    }

    @Test
    def 'schema contains nullable and unique attribute annotations'() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Address            ; Address",
                "                                   ; $TEST_IO                              ; Company            ; Company",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Address                                                   ; publicKey                   ; Address:publicKey                                  ;                                                           ; true",
                "                                            ; $TEST_IO:Company                                                   ; uid                         ; Company:uid",
                "                                            ; $TEST_IO:Company                                                   ; addresses                   ; Company:addresses                                  ; $TEST_IO:Address")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def company = ODataSchema.from(generatedSchema).getEntityType 'Company'
        with(company.getAnnotatableProperty('uid')) {
            annotationNames.containsAll([IS_UNIQUE, NULLABLE])
            getAnnotation(IS_UNIQUE).get().text == 'true'
            getAnnotation(NULLABLE).get().text == 'false'
        }
    }

    @Test
    def 'schema contains primitive type properties'() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique=true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                            ; TestItem           ; TestItemType2",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); unique[default = false]',
                "                                            ; $TEST_IO:TestItem                                                  ; string                      ; TestItemType2:string                               ; true",
                "                                            ; $TEST_IO:TestItem                                                  ; boolean                     ; TestItemType2:boolean",
                "                                            ; $TEST_IO:TestItem                                                  ; primitiveBoolean            ; TestItemType2:primitiveBoolean",
                "                                            ; $TEST_IO:TestItem                                                  ; primitiveShort              ; TestItemType2:primitiveShort",
                "                                            ; $TEST_IO:TestItem                                                  ; character                   ; TestItemType2:character",
                "                                            ; $TEST_IO:TestItem                                                  ; primitiveChar               ; TestItemType2:primitiveChar",
                "                                            ; $TEST_IO:TestItem                                                  ; integer                     ; TestItemType2:integer",
                "                                            ; $TEST_IO:TestItem                                                  ; primitiveInteger            ; TestItemType2:primitiveInteger",
                "                                            ; $TEST_IO:TestItem                                                  ; byte                        ; TestItemType2:byte",
                "                                            ; $TEST_IO:TestItem                                                  ; primitiveByte               ; TestItemType2:primitiveByte",
                "                                            ; $TEST_IO:TestItem                                                  ; long                        ; TestItemType2:long",
                "                                            ; $TEST_IO:TestItem                                                  ; primitiveLong               ; TestItemType2:primitiveLong",
                "                                            ; $TEST_IO:TestItem                                                  ; float                       ; TestItemType2:float",
                "                                            ; $TEST_IO:TestItem                                                  ; primitiveFloat              ; TestItemType2:primitiveFloat",
                "                                            ; $TEST_IO:TestItem                                                  ; double                      ; TestItemType2:double",
                "                                            ; $TEST_IO:TestItem                                                  ; primitiveDouble             ; TestItemType2:primitiveDouble",
                "                                            ; $TEST_IO:TestItem                                                  ; date                        ; TestItemType2:date")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def testItem = ODataSchema.from(generatedSchema).getEntityType 'TestItem'
        testItem.propertyNames.containsAll([
                'string',
                'boolean',
                'primitiveBoolean',
                'primitiveShort',
                'character',
                'primitiveChar',
                'integer',
                'primitiveInteger',
                'byte',
                'primitiveByte',
                'long',
                'primitiveLong',
                'float',
                'primitiveFloat',
                'double',
                'primitiveDouble',
                'date'
        ])
    }

    @Test
    def 'schema contains primitive collections'() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Order              ; Order",
                "                                   ; $TEST_IO                              ; OrderEntry         ; OrderEntry",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Order                                                     ; code                        ; Order:code",
                "                                            ; $TEST_IO:OrderEntry                                                ; order                       ; OrderEntry:order                                   ; $TEST_IO:Order",
                "                                            ; $TEST_IO:OrderEntry                                                ; entryGroupNumbers           ; OrderEntry:entryGroupNumbers")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def schema = ODataSchema.from generatedSchema
        with(schema) {
            entityTypeNames.containsAll(['Order', 'OrderEntry', 'Integer'])
            containsAssociationBetween 'OrderEntry', 'Integer'
            defaultEntityContainer.entitySetTypes.contains 'Integer'
            defaultEntityContainer.associationSetNames.contains 'OrderEntry_Integers'
            associations.collect { it.name }.contains 'FK_OrderEntry_entryGroupNumbers'
        }
    }

    @Test
    def 'schema contains referenced item collections'() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; USAddress          ; Address",
                "                                   ; $TEST_IO                              ; SAPCompany         ; Company",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:USAddress                                                 ; publicKey                   ; Address:publicKey                                  ;                                                           ; true",
                "                                            ; $TEST_IO:SAPCompany                                                ; uid                         ; Company:uid",
                "                                            ; $TEST_IO:SAPCompany                                                ; unit                        ; Company:addresses                                  ; $TEST_IO:USAddress")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def schema = ODataSchema.from generatedSchema
        with(schema) {
            entityTypeNames.containsAll(['USAddress', 'SAPCompany'])
            containsAssociationBetween 'SAPCompany', 'USAddress'
            defaultEntityContainer.entitySetTypes.contains 'USAddress'
            defaultEntityContainer.associationSetNames.contains 'SAPCompany_USAddresses'
            associations.collect { it.name }.contains 'FK_SAPCompany_unit'
        }
    }

    @Test
    def 'schema contains enum type standard attributes'() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]   ; type(code)',
                "                                   ; $TEST_IO                ; Product               ; Product",
                "                                   ; $TEST_IO                ; ArticleApprovalStatus ; ArticleApprovalStatus",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Product                                                   ; code                        ; Product:code",
                "                                            ; $TEST_IO:Product                                                   ; approvalStatus              ; Product:approvalStatus                             ; $TEST_IO:ArticleApprovalStatus",
                "                                            ; $TEST_IO:ArticleApprovalStatus                                     ; code                        ; ArticleApprovalStatus:code")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def schema = ODataSchema.from generatedSchema
        with(schema) {
            entityTypeNames.containsAll(['Product', 'ArticleApprovalStatus'])
            containsAssociationBetween('Product', 'ArticleApprovalStatus')
            defaultEntityContainer.entitySetTypes.containsAll(['Product', 'ArticleApprovalStatus'])
            defaultEntityContainer.associationSetNames.contains 'Product_ArticleApprovalStatuses'
            associations.collect { it.name }.contains 'FK_Product_approvalStatus'
        }
    }

    @Test
    def 'schema contains classification attribute that references a standard enum'() {
        given: 'A ClassAttributeAssignment for a item of type enum exists'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$class = classificationClass($catalogVersionHeader, code)',
                '$attribute = classificationAttribute($systemVersionHeader, code)',
                'INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]                ; $attribute[unique = true]           ; attributeType(code); mandatory; referenceType(code)[unique = true]',
                "                                      ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS; $SYSTEM_VERSION:classApprovalStatus ; reference          ; false    ; ArticleApprovalStatus")
        and: 'Integration Object with classification attribute that references a standard enum'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]   ; type(code)',
                "                                   ; $TEST_IO                ; Product               ; Product",
                "                                   ; $TEST_IO                ; ArticleApprovalStatus ; ArticleApprovalStatus",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Product                                                   ; code                        ; Product:code",
                "                                            ; $TEST_IO:ArticleApprovalStatus                                     ; code                        ; ArticleApprovalStatus:code",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment                                                 ; returnIntegrationObjectItem(integrationObject(code), code)',
                "                                                          ; $TEST_IO:Product    ; classApprovalStatusAttrName ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS:$SYSTEM_VERSION:classApprovalStatus ; $TEST_IO:ArticleApprovalStatus")
        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def schema = ODataSchema.from generatedSchema
        with(schema) {
            entityTypeNames.containsAll(['Product', 'ArticleApprovalStatus'])
            containsAssociationBetween('Product', 'ArticleApprovalStatus')
            defaultEntityContainer.entitySetTypes.containsAll(['Product', 'ArticleApprovalStatus'])
            defaultEntityContainer.associationSetNames.contains 'Product_ArticleApprovalStatuses'
            associations.collect { it.name }.contains 'FK_Product_classApprovalStatusAttrName'
        }
    }

    @Test
    def 'schema contains classification attribute for dynamic enum'() {
        given: 'A ClassAttributeAssignment for a item of type enum exists'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$class = classificationClass($catalogVersionHeader, code)',
                '$attribute = classificationAttribute($systemVersionHeader, code)',
                'INSERT_UPDATE ClassificationAttribute; code[unique = true]; $systemVersionHeader[unique = true]',
                "                                     ; someEnumAttr       ; $SYSTEM_VERSION",
                'INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]                ; $attribute[unique = true]           ; attributeType(code); mandatory',
                "                                      ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS; $SYSTEM_VERSION:someEnumAttr        ; enum          ; false")
        and: 'Integration Object with classification attribute that references a standard enum'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]   ; type(code)',
                "                                   ; $TEST_IO                ; Product               ; Product",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Product                                                   ; code                        ; Product:code",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $TEST_IO:Product    ; someEnumAttrName            ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS:$SYSTEM_VERSION:someEnumAttr")
        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        with(ODataSchema.from(generatedSchema)) {
            entityTypeNames.size() == 1
            with(getEntityType('Product')) {
                with(getAnnotatableProperty('someEnumAttrName')) {
                    !annotationNames.contains(IS_UNIQUE)
                    annotationNames.contains(NULLABLE)
                    getAnnotation(NULLABLE).get().text == 'true'
                }
            }
        }
    }

    @Test
    def 'schema contains classification attribute for multivalue dynamic enum'() {
        given: 'A ClassAttributeAssignment for a item of type enum exists'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$class = classificationClass($catalogVersionHeader, code)',
                '$attribute = classificationAttribute($systemVersionHeader, code)',
                'INSERT_UPDATE ClassificationAttribute; code[unique = true]; $systemVersionHeader[unique = true]',
                "                                     ; myEnums            ; $SYSTEM_VERSION",
                'INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]                ; $attribute[unique = true]; attributeType(code); mandatory ; multiValued',
                "                                      ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS; $SYSTEM_VERSION:myEnums  ; enum               ; false     ; true")
        and: 'Integration Object with classification attribute that references a standard enum'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]   ; type(code)',
                "                                   ; $TEST_IO                ; Product               ; Product",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Product                                                   ; code                        ; Product:code",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $TEST_IO:Product    ; myIntegrationEnums          ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS:$SYSTEM_VERSION:myEnums")
        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        with(ODataSchema.from(generatedSchema)) {
            entityTypeNames.size() == 2
            with(getEntityType('Product')) {
                with(getAnnotatableProperty('myIntegrationEnums')) {
                    !annotationNames.contains(IS_UNIQUE)
                    annotationNames.contains(NULLABLE)
                    getAnnotation(NULLABLE).get().text == 'true'
                }
            }
            with(getEntityType('String')) {
                with(getAnnotatableProperty('value')) {
                    !annotationNames.contains(IS_UNIQUE)
                    getAnnotation(NULLABLE).get().text == 'false'
                }
            }
            containsAssociationBetween('Product', 'String')
            defaultEntityContainer.entitySetTypes.containsAll(['Product', 'String'])
            defaultEntityContainer.associationSetNames.contains 'Product_Strings'
            associations.collect { it.name }.contains 'FK_Product_myIntegrationEnums'
        }
    }

    @Test
    def 'schema contains partOf attribute annotation when partOf is true in type system'() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; CatalogVersion     ; CatalogVersion",
                "                                   ; $TEST_IO                              ; Category           ; Category",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]',
                "                                            ; $TEST_IO:CatalogVersion                                            ; version                     ; CatalogVersion:version",
                "                                            ; $TEST_IO:CatalogVersion                                            ; active                      ; CatalogVersion:active",
                "                                            ; $TEST_IO:CatalogVersion                                            ; rootCategories              ; CatalogVersion:rootCategories                      ; $TEST_IO:Category                                         ;                        ; false",
                "                                            ; $TEST_IO:Category                                                  ; code                        ; Category:code")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def schema = ODataSchema.from generatedSchema
        schema.entityTypeNames.containsAll(['CatalogVersion', 'Category'])
        schema.getEntityType('CatalogVersion')
                .getAnnotatableProperty('rootCategories')
                .getAnnotation(PART_OF).get().text == 'true'
    }

    @Test
    def 'schema does not contain partOf attribute annotation when partOf is false'() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Address            ; Address",
                "                                   ; $TEST_IO                              ; Country            ; Country ",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]',
                "                                            ; $TEST_IO:Address                                                   ; publicKey                   ; Address:publicKey                                  ;                                                           ; true",
                "                                            ; $TEST_IO:Address                                                   ; country                     ; Address:country                                    ; $TEST_IO:Country",
                "                                            ; $TEST_IO:Country                                                   ; isocode                     ; Country:isocode")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def address = ODataSchema.from(generatedSchema).getEntityType 'Address'
        address.getAnnotatableProperty('country').getAnnotation(PART_OF).empty
    }

    @Test
    def "schema contains standard attribute's association of the same type"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Category           ; Category",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Category                                                  ; code                        ; Category:code",
                "                                            ; $TEST_IO:Category                                                  ; supercategories             ; Category:supercategories                           ; $TEST_IO:Category")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        generatedSchema.entityTypes[0].navigationProperties[0].toRole == 'Supercategories'

        def association = ODataSchema.from(generatedSchema).getAssociation 'FK_Category_supercategories'
        with(association.end1) {
            type == new FullQualifiedName(SCHEMA_NAME, 'Category')
            role == 'Category'
        }

        with(association.end2) {
            type == new FullQualifiedName(SCHEMA_NAME, 'Category')
            role == 'Supercategories'
        }
    }

    @Test
    def "schema contains classification attribute's association of the same type"() {
        given: 'A Classification Attribute Assignment for a referenced attribute exists'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$class = classificationClass($catalogVersionHeader, code)',
                '$attribute = classificationAttribute($systemVersionHeader, code)',
                'INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]                ; $attribute[unique = true]      ; attributeType(code); mandatory ; referenceType(code)[unique = true]',
                "                                      ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS; $SYSTEM_VERSION:relatedProduct ; reference          ; false     ; Product")
        and: 'Integration Object with classification attributes'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Product            ; Product",
                "                                   ; $TEST_IO                              ; CatalogVersion     ; CatalogVersion",
                "                                   ; $TEST_IO                              ; Catalog            ; Catalog",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Catalog                                                   ; id                          ; Catalog:id",
                "                                            ; $TEST_IO:Product                                                   ; code                        ; Product:code",
                "                                            ; $TEST_IO:Product                                                   ; name                        ; Product:name",
                "                                            ; $TEST_IO:Product                                                   ; catalogVersion              ; Product:catalogVersion                             ; $TEST_IO:CatalogVersion",
                "                                            ; $TEST_IO:CatalogVersion                                            ; version                     ; CatalogVersion:version",
                "                                            ; $TEST_IO:CatalogVersion                                            ; catalog                     ; CatalogVersion:catalog                             ; $TEST_IO:Catalog",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment                                            ; returnIntegrationObjectItem(integrationObject(code), code)',
                "                                                          ; $TEST_IO:Product    ; relatedProduct              ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS:$SYSTEM_VERSION:relatedProduct ; $TEST_IO:Product")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def schema = ODataSchema.from generatedSchema
        def product = schema.getEntityType 'Product'
        product.navigationPropertyNames.contains('relatedProduct')
        with(product.getAnnotatableProperty('relatedProduct')) {
            !annotationNames.contains(IS_UNIQUE)
            annotationNames.contains(NULLABLE)
            getAnnotation(NULLABLE).get().text == 'true'
        }

        def association = schema.getAssociation 'FK_Product_relatedProduct'
        with(association.end1) {
            type == new FullQualifiedName(SCHEMA_NAME, 'Product')
            role == 'Product'
        }

        with(association.end2) {
            type == new FullQualifiedName(SCHEMA_NAME, 'Product')
            role == 'RelatedProduct'
        }
    }

    @Test
    def 'schema contains simple classification attributes as properties'() {
        given: 'Classifications with different types and mandatory specifications'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$class = classificationClass($catalogVersionHeader, code)',
                '$attribute = classificationAttribute($systemVersionHeader, code)',
                'INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]                ; $attribute[unique = true]   ; unit($systemVersionHeader, code); attributeType(code); mandatory',
                "                                      ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS; $SYSTEM_VERSION:height      ; $SYSTEM_VERSION:centimeters     ; number             ; false",
                "                                      ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS; $SYSTEM_VERSION:description ; $SYSTEM_VERSION:centimeters     ; string             ; true")

        and: 'Integration Object with classification attributes'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Product            ; Product",
                "                                   ; $TEST_IO                              ; CatalogVersion     ; CatalogVersion",
                "                                   ; $TEST_IO                              ; Catalog            ; Catalog",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Catalog                                                   ; id                          ; Catalog:id",
                "                                            ; $TEST_IO:Product                                                   ; code                        ; Product:code",
                "                                            ; $TEST_IO:Product                                                   ; name                        ; Product:name",
                "                                            ; $TEST_IO:Product                                                   ; catalogVersion              ; Product:catalogVersion                             ; $TEST_IO:CatalogVersion",
                "                                            ; $TEST_IO:CatalogVersion                                            ; version                     ; CatalogVersion:version",
                "                                            ; $TEST_IO:CatalogVersion                                            ; catalog                     ; CatalogVersion:catalog                             ; $TEST_IO:Catalog",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $TEST_IO:Product    ; height                      ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS:$SYSTEM_VERSION:height",
                "                                                          ; $TEST_IO:Product    ; description                 ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS:$SYSTEM_VERSION:description")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def schema = ODataSchema.from generatedSchema
        def product = schema.getEntityType 'Product'
        product.propertyNames.containsAll(['height', 'description'])

        and: 'height attribute annotations set'
        product.getSimpleProperty('height').type == EdmSimpleTypeKind.Double
        with(product.getAnnotatableProperty('height')) {
            annotationNames == [NULLABLE]
            getAnnotation(NULLABLE).get().text == 'true'
        }

        and: 'description attribute annotations set'
        product.getSimpleProperty('description').type == EdmSimpleTypeKind.String
        with(product.getAnnotatableProperty('description')) {
            annotationNames == [NULLABLE]
            getAnnotation(NULLABLE).get().text == 'false'
        }
    }

    @Test
    def "schema contains classification attribute that is a primitive collection"() {
        given: 'Classification with a collection of primitive values'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$class = classificationClass($catalogVersionHeader, code)',
                '$attribute = classificationAttribute($systemVersionHeader, code)',
                'INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]                ; $attribute[unique = true]   ; unit($systemVersionHeader, code); attributeType(code); multiValued',
                "                                      ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS; $SYSTEM_VERSION:height      ; $SYSTEM_VERSION:centimeters     ; number             ; true")

        and: 'Integration Object with classification attributes'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Product            ; Product",
                "                                   ; $TEST_IO                              ; CatalogVersion     ; CatalogVersion",
                "                                   ; $TEST_IO                              ; Catalog            ; Catalog",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Catalog                                                   ; id                          ; Catalog:id",
                "                                            ; $TEST_IO:Product                                                   ; code                        ; Product:code",
                "                                            ; $TEST_IO:Product                                                   ; name                        ; Product:name",
                "                                            ; $TEST_IO:Product                                                   ; catalogVersion              ; Product:catalogVersion                             ; $TEST_IO:CatalogVersion",
                "                                            ; $TEST_IO:CatalogVersion                                            ; version                     ; CatalogVersion:version",
                "                                            ; $TEST_IO:CatalogVersion                                            ; catalog                     ; CatalogVersion:catalog                             ; $TEST_IO:Catalog",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $TEST_IO:Product    ; heights                     ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS:$SYSTEM_VERSION:height")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def schema = ODataSchema.from generatedSchema
        def product = schema.getEntityType 'Product'
        product.navigationPropertyNames.contains 'heights'
        with(product.getAnnotatableProperty('heights')) {
            !annotationNames.contains(IS_UNIQUE)
            annotationNames.contains(NULLABLE)
            getAnnotation(NULLABLE).get().text == 'true'
        }

        and: 'Product and heights are associated'
        def association = schema.getAssociation 'FK_Product_heights'
        with(association.end1) {
            type == new FullQualifiedName(SCHEMA_NAME, 'Product')
            role == 'Product'
            multiplicity == EdmMultiplicity.ZERO_TO_ONE
        }
        with(association.end2) {
            type == new FullQualifiedName(SCHEMA_NAME, 'Double')
            role == 'java.lang.Double'
            multiplicity == EdmMultiplicity.MANY
        }
    }

    @Test
    def "schema contains classification attribute that is an entity collection"() {
        given: 'Classification with a collection of references'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$class = classificationClass($catalogVersionHeader, code)',
                '$attribute = classificationAttribute($systemVersionHeader, code)',
                'INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]                ; $attribute[unique = true]           ; attributeType(code); multiValued; referenceType(code)[unique = true]',
                "                                      ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS; $SYSTEM_VERSION:classApprovalStatus ; reference          ; true       ; ArticleApprovalStatus")
        and: 'Integration Object with classification attribute that references a collection of standard enum'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]   ; type(code)',
                "                                   ; $TEST_IO                ; Product               ; Product",
                "                                   ; $TEST_IO                ; ArticleApprovalStatus ; ArticleApprovalStatus",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:Product                                                   ; code                        ; Product:code",
                "                                            ; $TEST_IO:ArticleApprovalStatus                                     ; code                        ; ArticleApprovalStatus:code",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment                                                 ; returnIntegrationObjectItem(integrationObject(code), code)',
                "                                                          ; $TEST_IO:Product    ; statuses                    ; $SYSTEM_VERSION:$CLASSIFICATION_CLASS:$SYSTEM_VERSION:classApprovalStatus ; $TEST_IO:ArticleApprovalStatus")

        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def schema = ODataSchema.from generatedSchema
        def product = schema.getEntityType 'Product'
        product.navigationPropertyNames.contains 'statuses'
        with(product.getAnnotatableProperty('statuses')) {
            !annotationNames.contains(IS_UNIQUE)
            annotationNames.contains(NULLABLE)
            getAnnotation(NULLABLE).get().text == 'true'
        }

        and: 'Product and statuses are associated'
        def association = schema.getAssociation 'FK_Product_statuses'
        with(association.end1) {
            type == new FullQualifiedName(SCHEMA_NAME, 'Product')
            role == 'Product'
            multiplicity == EdmMultiplicity.ZERO_TO_ONE
        }
        with(association.end2) {
            type == new FullQualifiedName(SCHEMA_NAME, 'ArticleApprovalStatus')
            role == 'ArticleApprovalStatus'
            multiplicity == EdmMultiplicity.MANY
        }
    }

    @Test
    def "schema contains a primitive virtual attribute"() {
        given: "a item exists with a virtual and standard attribute"
        def standardAttributeName = 'code'
        def virtualAttributeName = 'virtualBatman'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; Unit               ; Unit",
                '$item=integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)',
                "                                            ; $TEST_IO:Unit       ; $standardAttributeName      ; Unit:$standardAttributeName",
                "INSERT_UPDATE Script; code[unique = true]; scriptType(code); content",
                "                    ; modelScript        ; GROOVY          ; 'hello world from model'",
                "INSERT_UPDATE IntegrationObjectVirtualAttributeDescriptor; code[unique = true]     ; logicLocation       ; type(code)",
                "                                                         ; retrieveVirtualBatman   ; model://modelScript ; java.lang.String",
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute; $item[unique = true]; attributeName[unique = true]; retrievalDescriptor(code)',
                "                                                   ; $TEST_IO:Unit       ; $virtualAttributeName       ; retrieveVirtualBatman"
        )


        when:
        def generatedSchema = generator.generateSchema integrationObjectItems()

        then:
        def schema = ODataSchema.from generatedSchema
        def unit = schema.getEntityType 'Unit'
        unit.propertyNames.containsAll(standardAttributeName, virtualAttributeName)
        def virtualAttrProperty = unit.getAnnotatableProperty(virtualAttributeName)
        and: "the virtual attribute always generates as non unique, nullable properties"
        with(virtualAttrProperty) {
            annotationNames == [NULLABLE]
            getAnnotation(NULLABLE).get().text == 'true'
        }
        and: "the virtual attribute property type is generated from the retrievalDescriptor script type"
        unit.getSimpleProperty(virtualAttributeName).getType().name() == "String"

        and: 'Schema has no associations'
        schema.getAssociations().isEmpty()
    }

    def integrationObjectItems() {
        IntegrationTestUtil.findAll(IntegrationObjectItemModel).collect { (IntegrationObjectItemModel) it }
    }
}
