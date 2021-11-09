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
import de.hybris.platform.integrationservices.service.IntegrationObjectConversionService
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultCollectionType2MapPopulatorUnitTest extends Specification {

    private static final def ATTR_NAME = "testAttribute"
    private static final def QUALIFIER = "qualifier"

    def targetMap = [:]

    private def attributeDescriptor = Stub(TypeAttributeDescriptor) {
        getAttributeName() >> ATTR_NAME
        getQualifier() >> QUALIFIER
    }
    private def itemModel = Stub(ItemModel)
    private def conversionService = Mock(IntegrationObjectConversionService)
    private def converter = Stub(AtomicTypeValueConverter)

    def populator = new DefaultCollectionType2MapPopulator(conversionService: conversionService, atomicTypeValueConverter: converter)

    @Test
    def "populates to map when attribute's value is a collection of ItemModel"() {
        given:
        attributeIsCollection()
        and:
        attributeHasValue([itemModel, itemModel])
        and:
        def convertedValue = ['convertedCode': 'convertedValue']
        conversionServiceReturnsConvertedItem(convertedValue)

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        targetMap.get(ATTR_NAME) == [convertedValue, convertedValue]
    }

    @Test
    def "populates to map when attribute's value is a collection of primitives"() {
        given:
        attributeIsCollection()
        and:
        attributeHasValue(['value1', 'value2'])
        and:
        converter.convert('value1') >> 'value1'
        converter.convert('value2') >> 'value2'

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        targetMap.get(ATTR_NAME) == [PrimitiveCollectionElement.create('value1'), PrimitiveCollectionElement.create('value2')]
    }

    def "populates to map when attribute's value is a collection of enumerations"() {
        given:
        attributeIsCollection()
        and:
        def enumVal = createHybrisEnumValue("ENUM_VAL")
        attributeHasValue([enumVal])

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        targetMap.get(ATTR_NAME) == [Collections.singletonMap("code", "ENUM_VAL")]
    }

    @Test
    def "does not populate to map when attribute's value is an empty collection"() {
        given:
        attributeIsCollection()
        and:
        attributeHasValue([])

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        0 * conversionService.convert(_)
        targetMap.isEmpty()
    }

    @Test
    def "does not populate to map when attribute is not a collection"() {
        given:
        attributeIsNotCollection()

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        0 * conversionService.convert(_)
        targetMap.isEmpty()
    }

    private ItemToMapConversionContext conversionContext(TypeAttributeDescriptor attribute) {
        Stub(ItemToMapConversionContext) {
            getItemModel() >> itemModel
            getTypeDescriptor() >>
                    Stub(TypeDescriptor) {
                        getAttributes() >> [attribute]
                    }
        }
    }

    def attributeHasValue(Object value) {
        attributeDescriptor.accessor() >> Stub(AttributeValueAccessor) {
            getValue(itemModel) >> value
        }
    }

    def attributeIsCollection() {
        attributeDescriptor.isCollection() >> true
    }

    def attributeIsNotCollection() {
        attributeDescriptor.isCollection() >> false
    }

    private Map<String, Object> conversionServiceReturnsConvertedItem(Map<String, Object> convertedValue) {
        conversionService.convert(_ as ItemToMapConversionContext) >> convertedValue
    }

    def createHybrisEnumValue(final String code) {
        Stub(HybrisEnumValue) {
            getCode() >> code
        }
    }
}