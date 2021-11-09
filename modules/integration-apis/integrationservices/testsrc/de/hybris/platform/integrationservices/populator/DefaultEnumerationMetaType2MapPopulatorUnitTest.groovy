/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.HybrisEnumValue
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultEnumerationMetaType2MapPopulatorUnitTest extends Specification {
    private static final String QUALIFIER = "code"
    private static final String ATTR_NAME = "someAttrName"
    def populator = new DefaultEnumerationMetaType2MapPopulator()

    def modelService = Stub(ModelService)

    private Map<String, Object> targetMap = [:]

    @Test
    def "does not populate when attribute type is not an enumeration"() {
        given:
        def attributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeType() >> nonEnumTypeDescriptor()
        }

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        targetMap.isEmpty()
    }

    @Test
    def "does not populate when attribute type is an enumeration and a collection"() {
        given:
        def attributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeType() >> enumTypeDescriptor()
        }
        and:
        attributeDescriptor.isCollection() >> true

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        targetMap.isEmpty()
    }

    @Test
    def "does not populate when attribute value is null"() {
        given:
        def typeDescriptor = enumTypeDescriptor()
        def attributeDescriptor = attributeWithValue(null)
        attributeDescriptor.getAttributeType() >> typeDescriptor
        typeDescriptor.getAttributes() >> [attributeDescriptor]

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        targetMap.isEmpty()
    }

    private TypeAttributeDescriptor attributeWithValue(Object value) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> ATTR_NAME
            getQualifier() >> QUALIFIER
            accessor() >> getterWithValue(value)
        }
    }

    @Test
    def "populates map when attribute value is an enumeration"() {
        given:
        def enumCodeValue = "enumValueString"
        def typeDescriptor = enumTypeDescriptor()
        def attributeDescriptor = attributeWithValue(enumValueWithCode(enumCodeValue))
        attributeDescriptor.getAttributeType() >> typeDescriptor
        typeDescriptor.getAttributes() >> [attributeDescriptor]

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        targetMap.get(ATTR_NAME) == [(ATTR_NAME): enumCodeValue]
    }

    @Test
    def "populates empty map when attribute is an enumeration but does not have a qualifier = 'code'"() {
        given:
        def qualifier = "codeNOOOTTT"
        def enumCodeValue = "enumValueString"
        def typeDescriptor = enumTypeDescriptor()

        def attributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> ATTR_NAME
            getQualifier() >> qualifier
            getAttributeType() >> typeDescriptor
            accessor() >> getterWithValue(enumValueWithCode(enumCodeValue))
        }

        typeDescriptor.getAttributes() >> [attributeDescriptor]

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        targetMap.get(ATTR_NAME) == [:]
    }

    private AttributeValueAccessor getterWithValue(enumVal) {
        Stub(AttributeValueAccessor) {
            getValue(_ as ItemModel) >> enumVal
        }
    }

    private HybrisEnumValue enumValueWithCode(enumCodeValue) {
        Stub(HybrisEnumValue) {
            getCode() >> enumCodeValue
        }
    }

    private ItemToMapConversionContext conversionContext(TypeAttributeDescriptor attribute) {
        Stub(ItemToMapConversionContext) {
            getItemModel() >> Stub(ItemModel)
            getTypeDescriptor() >>
                    Stub(TypeDescriptor) {
                        getAttributes() >> [attribute]
                    }
        }
    }

    private TypeDescriptor enumTypeDescriptor() {
        Stub(TypeDescriptor) {
            isEnumeration() >> true
        }
    }

    private TypeDescriptor nonEnumTypeDescriptor() {
        Stub(TypeDescriptor) {
            isEnumeration() >> false
        }
    }
}
