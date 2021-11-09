/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.ClassificationBuilder
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.VirtualAttributeBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.junit.Test
import spock.lang.Issue


@Issue('https://jira.hybris.com/browse/IAPI-4084')
@IntegrationTest
class VirtualAttributeDescriptorModelingIntegrationTest extends ServicelayerSpockSpecification {
    private static final def IO = 'VirtualAttributeDescriptorModelingIntegrationTestIO'
    private static final def UNIT_IOI_CODE = 'UnitItemCode'
    private static final def LOGIC_LOCATION = 'model://logLoc'
    private static final def DESCRIPTOR_TYPE = 'java.lang.String'

    def setupSpec() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; $UNIT_IOI_CODE     ; Unit"
        )
    }

    def cleanup() {
        VirtualAttributeBuilder.cleanup()
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    @Test
    def 'VirtualAttributeDescriptor cannot have an unsupported primitive type'() {
        when:
        def unsupportedType = "java.lang.Object"
        def descriptorCode = "NewVirtualDescriptor"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectVirtualAttributeDescriptor; code[unique = true]; logicLocation  ; type(code)',
                "                                                         ; $descriptorCode    ; $LOGIC_LOCATION; $unsupportedType")

        then:
        def e = thrown AssertionError
        e.message.contains "Invalid [$unsupportedType] type for virtual attribute descriptor with code [$descriptorCode]."
        e.message.contains 'java.lang.Integer'
        e.message.contains 'java.lang.Boolean'
        e.message.contains 'java.lang.Byte'
        e.message.contains 'java.lang.Double'
        e.message.contains 'java.lang.Float'
        e.message.contains 'java.lang.Long'
        e.message.contains 'java.lang.Short'
        e.message.contains 'java.lang.String'
        e.message.contains 'java.lang.Character'
        e.message.contains 'java.util.Date'
        e.message.contains 'java.math.BigInteger'
        e.message.contains 'java.math.BigDecimal'
    }

    @Test
    def 'VirtualAttributeDescriptor cannot have a returnIntegrationObjectItem'() {
        given: "a virtual attribute descriptor exists"
        def retrievalDescCode = 'retrieveVirtualBatman'
        virtualAttributeDescriptorExists(retrievalDescCode)

        when: 'a virtual attribute is imported with a non-null returnIntegrationObjectItem'
        def virtualAttributeName = 'virtualAttr'
        IntegrationTestUtil.importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute; $item[unique = true]; attributeName[unique = true]; retrievalDescriptor(code); returnIntegrationObjectItem(integrationObject(code), code)',
                "                                                   ; $IO:$UNIT_IOI_CODE  ; $virtualAttributeName       ; $retrievalDescCode       ; $IO:$UNIT_IOI_CODE"
        )

        then:
        def e = thrown AssertionError
        e.message.contains "Found [$virtualAttributeName] attribute with a returnIntegrationObjectItem set to [$UNIT_IOI_CODE]."
    }

    @Test
    def 'VirtualAttributeDescriptor can have an empty persistenceDescriptor'() {
        given: "a virtual attribute descriptor exists"
        def retrievalDescCode = 'retrieveVirtualBatman'
        virtualAttributeDescriptorExists(retrievalDescCode)

        when: 'a virtual attribute is imported without persistenceDescriptor'
        def virtualAttributeName = 'virtualAttr'
        IntegrationTestUtil.importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute; $item[unique = true]; attributeName[unique = true]; retrievalDescriptor(code);',
                "                                                   ; $IO:$UNIT_IOI_CODE  ; $virtualAttributeName       ; $retrievalDescCode       ;"
        )

        then:
        noExceptionThrown()
    }

    @Test
    def 'When type is not provided, descriptor defaults type to java.lang.String'() {
        given: "a virtual attribute descriptor with no type provided"
        def retrievalDescCode = 'retrieveVirtualBatman'
        virtualAttributeDescriptorNoTypeExists(retrievalDescCode)

        when: 'a virtual attribute is imported with a VirtualAttributeDescriptor containing no type'
        def virtualAttributeName = 'virtualAttr'
        IntegrationTestUtil.importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute; $item[unique = true]; attributeName[unique = true]; retrievalDescriptor(code);',
                "                                                   ; $IO:$UNIT_IOI_CODE  ; $virtualAttributeName       ; $retrievalDescCode       ;"
        )
        def virtualAttribute = IntegrationTestUtil.findAny(IntegrationObjectItemVirtualAttributeModel, {
            it.attributeName == virtualAttributeName
        }).orElse(null)

        then:
        noExceptionThrown()
        and:
        virtualAttribute.getRetrievalDescriptor().getType().getCode() == "java.lang.String"
    }

    @Test
    def 'the last configuration is used when virtual attribute name duplicates an existing virtual attribute name'() {
        given: "2 virtual attribute descriptors exists"
        def retrievalDescCode1 = 'retrieveVirtualBatman'
        def retrievalDescCode2 = 'retrieveVirtualRobin'
        virtualAttributeDescriptorExists(retrievalDescCode1)
        virtualAttributeDescriptorExists(retrievalDescCode2)

        when:
        def duplicatedAttributeName = 'name'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute; $item[unique = true]; attributeName[unique = true]; retrievalDescriptor(code)',
                "                                                   ; $IO:$UNIT_IOI_CODE  ; $duplicatedAttributeName    ; $retrievalDescCode1      ",
                "                                                   ; $IO:$UNIT_IOI_CODE  ; $duplicatedAttributeName    ; $retrievalDescCode2      "
        )
        def virtualAttribute = IntegrationTestUtil.findAny(IntegrationObjectItemVirtualAttributeModel, {
            it.attributeName == duplicatedAttributeName
        }).orElse(null)

        then:
        virtualAttribute.getRetrievalDescriptor().getCode() == retrievalDescCode2
    }

    @Test
    def 'virtual attribute is updated when a new INSERT_UPDATE is made for an existing virtual attribute name for the same IOI'() {
        given: "2 virtual attribute descriptors exists"
        def retrievalDescCode1 = 'retrieveVirtualBatman'
        def retrievalDescCode2 = 'retrieveVirtualRobin'
        virtualAttributeDescriptorExists(retrievalDescCode1)
        virtualAttributeDescriptorExists(retrievalDescCode2)

        and: "a virtual attribute exists"
        def duplicatedAttributeName = 'name'
        VirtualAttributeBuilder.attribute()
                .withName(duplicatedAttributeName)
                .forItem("$IO:$UNIT_IOI_CODE")
                .withRetrievalDescriptor(retrievalDescCode1)
                .setup()

        when:
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute; $item[unique = true]; attributeName[unique = true]; retrievalDescriptor(code);',
                "                                                   ; $IO:$UNIT_IOI_CODE  ; $duplicatedAttributeName    ; $retrievalDescCode2      ;"
        )
        def virtualAttribute = IntegrationTestUtil.findAny(IntegrationObjectItemVirtualAttributeModel, {
            it.attributeName == duplicatedAttributeName
        }).orElse(null)

        then:
        virtualAttribute.getRetrievalDescriptor().getCode() == retrievalDescCode2
    }

    @Test
    def 'exception is thrown when a new virtual attribute is imported using INSERT with a duplicate virtual attribute name for the same IOI'() {
        given: "an IntegrationObjectItemVirtualAttribute exists"
        def retrievalDescCode1 = 'retrieveVirtualBatman'
        def retrievalDescCode2 = 'retrieveVirtualRobin'
        virtualAttributeDescriptorExists(retrievalDescCode1)
        virtualAttributeDescriptorExists(retrievalDescCode2)

        and: "a virtual attribute exists"
        def duplicatedAttributeName = 'name'
        VirtualAttributeBuilder.attribute()
                .withName(duplicatedAttributeName)
                .forItem("$IO:$UNIT_IOI_CODE")
                .withRetrievalDescriptor(retrievalDescCode1)
                .setup()

        when: 'a virtual attribute is imported with a duplicated attribute name'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$name=attributeName',
                'INSERT IntegrationObjectItemVirtualAttribute; $item[unique = true]; $name[unique = true]    ; retrievalDescriptor(code);',
                "                                            ; $IO:$UNIT_IOI_CODE  ; $duplicatedAttributeName; $retrievalDescCode2;"
        )

        then:
        def e = thrown AssertionError
        e.message.contains 'Cannot insert. Item exists'
    }

    @Test
    def 'an exception is thrown when the name of a virtual attribute duplicates a standard attribute name'() {
        given: "an IntegrationObjectItem and IntegrationObjectVirtualAttributeDescriptor exists"
        def retrievalDescCode = 'retrieveVirtualBatman'
        virtualAttributeDescriptorExists(retrievalDescCode)

        and: "a standard attribute exists"
        def duplicatedAttributeName = 'code'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$name=attributeName',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; $name[unique = true]    ; $attributeDescriptor',
                "                                            ; $IO:$UNIT_IOI_CODE  ; $duplicatedAttributeName; Unit:$duplicatedAttributeName"
        )

        when: 'a virtual attribute is imported with a duplicated attribute name'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute; $item[unique = true]; attributeName[unique = true]; retrievalDescriptor(code);',
                "                                                   ; $IO:$UNIT_IOI_CODE  ; $duplicatedAttributeName    ; $retrievalDescCode    ;"
        )

        then:
        def e = thrown AssertionError
        e.message.contains "The attribute [$duplicatedAttributeName] already exists in this integration object item"
    }

    @Test
    def 'an exception is thrown when the name of a virtual attribute duplicates a classification attribute name'() {
        given: "an IntegrationObjectItem and IntegrationObjectVirtualAttributeDescriptor exists"
        def retrievalDescCode = 'retrieveVirtualBatman'
        virtualAttributeDescriptorExists(retrievalDescCode)

        def itemCode = 'ProductItemCode'
        def duplicatedAttributeName = 'height'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; $itemCode          ; Product"
        )
        and: 'a classification attribute exists'
        def system = 'Electronics'
        def version = 'Test'
        def systemVersion = "$system:$version"
        def classificationClass = 'dimensions'
        def classification = ClassificationBuilder.classification()
                .withSystem(system)
                .withVersion(version)
                .withClassificationClass(classificationClass)
        classification
                .withAttribute(ClassificationBuilder.attribute().withName(duplicatedAttributeName).string()).setup()
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $IO:$itemCode       ; $duplicatedAttributeName    ; $systemVersion:$classificationClass:$systemVersion:$duplicatedAttributeName"
        )

        when: 'a virtual attribute is imported with a duplicated attribute name'
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute; $item[unique = true]; attributeName[unique = true]; retrievalDescriptor(code);',
                "                                                   ; $IO:$itemCode       ; $duplicatedAttributeName    ; $retrievalDescCode    ;"
        )

        then:
        def e = thrown AssertionError
        e.message.contains "The attribute [$duplicatedAttributeName] already exists in this integration object item"
    }

    def virtualAttributeDescriptorExists(final String code) {
        VirtualAttributeBuilder.logicDescriptor()
                .withLogicLocation(LOGIC_LOCATION)
                .withType(DESCRIPTOR_TYPE)
                .withCode(code)
                .setup()
    }

    def virtualAttributeDescriptorNoTypeExists(final String code) {
        VirtualAttributeBuilder.logicDescriptor()
                .withLogicLocation(LOGIC_LOCATION)
                .withCode(code)
                .setup()
    }
}