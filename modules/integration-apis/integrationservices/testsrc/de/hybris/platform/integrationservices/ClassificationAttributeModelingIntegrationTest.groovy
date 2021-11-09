/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.ClassificationBuilder
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.junit.Test
import spock.lang.Issue

@Issue('https://jira.hybris.com/browse/STOUT-2773')
@IntegrationTest
class ClassificationAttributeModelingIntegrationTest extends ServicelayerSpockSpecification {
    private static final def SYSTEM = 'Electronics'
    private static final def VERSION = 'Test'
    private static final def SYSTEM_VERSION = "$SYSTEM:$VERSION"
    private static final def IO = 'ClassifiedIO'

    private static final def classification = ClassificationBuilder.classification()
            .withSystem(SYSTEM)
            .withVersion(VERSION)
            .withClassificationClass('dimensions')

    def importClassifications() {
        classification
                .withAttribute(ClassificationBuilder.attribute().withName('height').number())
                .withAttribute(ClassificationBuilder.attribute().withName('width').number())
                .withAttribute(ClassificationBuilder.attribute().withName('depth').number())
                .withAttribute(ClassificationBuilder.attribute().withName('description').string().localized())
                .withAttribute(ClassificationBuilder.attribute().withName('status').valueList([]))
                .withAttribute(ClassificationBuilder.attribute().withName('relatedProduct').references('Product'))
                .setup()
    }

    def importIntegrationObject() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $IO                                   ; Product            ; Product   ; true",
                "                                   ; $IO                                   ; Catalog            ; Catalog",
                "                                   ; $IO                                   ; CatalogVersion     ; CatalogVersion",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType = returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor           ; $attributeType;',
                "                                            ; $IO:Product         ; code                        ; Product:code",
                "                                            ; $IO:Product         ; catalogVersion              ; Product:catalogVersion; $IO:CatalogVersion",
                "                                            ; $IO:CatalogVersion  ; version                     ; CatalogVersion:version",
                "                                            ; $IO:CatalogVersion  ; catalog                     ; CatalogVersion:catalog; $IO:Catalog",
                "                                            ; $IO:Catalog         ; id                          ; Catalog:id")
    }

    def setup() {
        importClassifications()
        importIntegrationObject()
    }

    def cleanup() {
        ClassificationBuilder.cleanup()
        IntegrationObjectTestUtil.cleanup()
    }

    @Test
    def 'classification attribute cannot be part of integration key'() {
        when: 'unique classification attribute added to the integration object'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; unique; $classificationAssignment',
                "                                                          ; $IO:Product         ; height                      ; true  ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:height")

        then:
        def e = thrown AssertionError
        e.message.contains "unknown attribute 'unique' in header"
    }

    @Test
    def 'classification attribute can only be associated to a Product type'() {
        when: 'attributes associated to Product and Catalog'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $IO:Catalog         ; height                      ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:height",
                "                                                          ; $IO:Product         ; depth                       ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:depth")

        then: 'Attribute associated to Catalog is not imported'
        def e = thrown AssertionError
        e.message.contains 'height'
        e.message.contains 'Product or its subtype'
        IntegrationTestUtil.findAny(IntegrationObjectItemClassificationAttributeModel, { it.attributeName == 'height' }).empty

        and: 'Attribute associated to Product is imported'
        IntegrationTestUtil.findAny(IntegrationObjectItemClassificationAttributeModel, { it.attributeName == 'depth' }).present
    }

    @Test
    def 'classification attribute must exist in the system'() {
        given: 'non-existent classification attribute'
        def attribute = "$SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:weight"

        when: 'the attribute is added to the integration object'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $IO:Product         ; weight                      ; $attribute")

        then:
        def e = thrown AssertionError
        e.message.contains "cannot resolve value '$attribute' for attribute 'classAttributeAssignment'"
    }

    @Test
    def 'integration object determines whether classification attributes are present or not'() {
        when: 'integration object has no classification attributes'
        def io = IntegrationTestUtil.findAny(IntegrationObjectModel, { it.code == IO }).orElse(null)
        then: 'getClassificationAttributesPresent() is false'
        !io.getClassificationAttributesPresent()

        when: 'at least one classification attribute is added to the IO'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $IO:Product         ; width                       ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:width")
        io = IntegrationTestUtil.findAny(IntegrationObjectModel, { it.code == IO }).orElse(null)
        then: 'getClassificationAttributesPresent() is true'
        io.getClassificationAttributesPresent()
    }

    @Test
    def 'exception is thrown when classification attribute name duplicates a standard attribute name'() {
        when: 'code is repeated in the classification attribute name'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $IO:Product         ; code                        ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:depth")

        then:
        def e = thrown AssertionError
        e.message.contains 'The attribute [code] already exists in this integration object item'
    }

    @Test
    def 'the last configuration is used when classification attribute name duplicates another classification attribute name'() {
        when: 'classification attribute width exists'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment; ',
                "                                                          ; $IO:Product         ; width                        ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:depth",
                "                                                          ; $IO:Product         ; width                        ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:width")
        def classAttribute = IntegrationTestUtil.findAny(IntegrationObjectItemClassificationAttributeModel, { it.attributeName == 'width' }).orElse(null)

        then:
        'the classification attribute has the latest configuration'
        classAttribute.getClassAttributeAssignment().getClassificationAttribute().getCode() == 'width'
    }

    @Test
    def 'classification attribute is updated when a new INSERT_UPDATE is made for an existing classification attribute name for the same IOI'() {
        given: 'classification attribute width exists'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment; ',
                "                                                          ; $IO:Product         ; width                        ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:depth")
        when: 'width is repeated in the classification attribute name'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment; ',
                "                                                          ; $IO:Product         ; width                        ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:width")
        def classAttribute = IntegrationTestUtil.findAny(IntegrationObjectItemClassificationAttributeModel, { it.attributeName == 'width' }).orElse(null)

        then: 'the classification attribute has the latest configuration'
        classAttribute.getClassAttributeAssignment().getClassificationAttribute().getCode() == 'width'
    }

    @Test
    def 'exception is thrown when a new classification attribute is imported using INSERT with a duplicate classification attribute name for the same IOI'() {
        given: 'classification attribute width exists'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment; ',
                "                                                          ; $IO:Product         ; width                        ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:depth")
        when: 'width is repeated in the classification attribute name'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment; ',
                "                                                          ; $IO:Product         ; width                        ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:width")

        then: 
        def e = thrown AssertionError
        e.message.contains 'Cannot insert. Item exists'
    }

    @Test
    def 'exception is thrown when classification attribute name duplicates an existing virtual attribute name'() {
        given: 'a virtual attribute exists'
        def duplicatedAttributeName = 'duplicateName'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE Script; code[unique = true]; scriptType(code); content",
                "                           ; exScript           ; GROOVY          ; 'hello world from model'",
                'INSERT_UPDATE IntegrationObjectVirtualAttributeDescriptor; code[unique = true]  ; logicLocation ; type(code)',
                "                                                         ; retrieveVirtualBatman; model://logLoc; java.lang.String",
                '$item = integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute; $item[unique = true]; attributeName[unique = true]; retrievalDescriptor(code);',
                "                                                   ; $IO:Product         ; $duplicatedAttributeName    ; retrieveVirtualBatman    ;"
        )

        when: 'code is repeated in the classification attribute name'
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE Script; code[unique = true]; scriptType(code); content",
                "                    ; exScript           ; GROOVY          ; 'hello world from model'",
                'INSERT_UPDATE IntegrationObjectVirtualAttributeDescriptor; code[unique = true]  ; logicLocation ; type(code)',
                "                                                         ; retrieveVirtualBatman; model://logLoc; java.lang.String",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $IO:Product         ; $duplicatedAttributeName    ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:depth")

        then:
        def e = thrown AssertionError
        e.message.contains "The attribute [$duplicatedAttributeName] already exists in this integration object item"
    }

    @Test
    def 'attribute name must be different from classification attribute name'() {
        given:
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $IO:Product         ; depth                       ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:depth")

        when: 'depth is repeated in the standard attribute name'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType = returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor           ; $attributeType;',
                "                                            ; $IO:Product         ; depth                       ; Product:catalogVersion; $IO:CatalogVersion")

        then:
        def e = thrown AssertionError
        e.message.contains 'The attribute [depth] already exists in this integration object item'
    }

    @Test
    def 'cannot model a classification attribute that is a collection of localized values'() {
        given: 'class attribute assignment with multiValued and localized set to true'
        classification
                .withAttribute(ClassificationBuilder.attribute().withName('description').string().multiValue().localized())
                .setup()

        when: 'associating the classification attribute to the integration object'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $IO:Product         ; description                       ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:description")

        then:
        def e = thrown AssertionError
        e.message.contains '[description] does not support both multiValued and localized being enabled'
    }

    @Test
    def 'cannot model a classification attribute that has a range of values'() {
        given: 'class attribute assignment with range set to true'
        classification
                .withAttribute(ClassificationBuilder.attribute().withName('depth').number().range())
                .setup()

        when: 'associating the classification attribute to the integration object'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $IO:Product         ; depth                       ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:depth")

        then:
        def e = thrown AssertionError
        e.message.contains '[depth] does not support range being enabled'
    }

    @Test
    def 'cannot model a classification attribute that is a reference type without a returnIntegrationObjectItem'() {
        given: 'A Classification Attribute Assignment for a referenced attribute exists'
        classification
                .withAttribute(ClassificationBuilder.attribute().withName('relatedProduct').references('Product'))
                .setup()

        when: 'associating the classification attribute to the integration object'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment                                ',
                "                                                          ; $IO:Product         ; relatedProduct              ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:relatedProduct")

        then:
        def e = thrown AssertionError
        e.message.contains 'Missing returnIntegrationObjectItem for classification attribute [relatedProduct]'
    }

    @Test
    def 'cannot model a classification attribute that is a localized reference'() {
        given: 'A Classification Attribute Assignment for a localized referenced attribute exists'
        classification
                .withAttribute(ClassificationBuilder.attribute().withName('relatedProduct').references('Product').localized())
                .setup()

        when: 'associating the classification attribute to the integration object'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment                                 ; returnIntegrationObjectItem(integrationObject(code), code)',
                "                                                          ; $IO:Product         ; relatedProduct              ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:relatedProduct ; $IO:Product")

        then:
        def e = thrown AssertionError
        e.message.contains '[relatedProduct] does not support localized reference.'
    }
}
