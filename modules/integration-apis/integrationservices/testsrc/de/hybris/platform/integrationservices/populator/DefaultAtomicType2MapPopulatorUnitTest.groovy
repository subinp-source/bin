/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.rowset.RowSetWarning

import static de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModelUtils.falseIfNull

@UnitTest
class DefaultAtomicType2MapPopulatorUnitTest extends Specification {
    private static final def ATTR_NAME = "codex"
    private static final def QUALIFIER = "qualifier"
    private static final def LOCALE = Locale.getDefault()
    private static final def TEST_VALUE = "testValue"

    def converter = Stub(Converter)
    def populator = new DefaultAtomicType2MapPopulator(converter: converter);

    private Map<String, Object> targetMap = [:]
    private def attributeDescriptor = Stub(TypeAttributeDescriptor) {
        getAttributeName() >> ATTR_NAME
        getQualifier() >> QUALIFIER
    }
    private def itemModel = Stub(ItemModel)

    def cleanup() {
        Locale.setDefault(LOCALE)
    }

    @Test
    def "populates to map for primitive attribute that is not a collection for type #attributeType"() {
        given:
        attributeIsNonCollectionPrimitive((['primitive': true, 'collection': false]))
        and:
        attributeHasValue(TEST_VALUE)
        and:
        converter.convert(TEST_VALUE) >> TEST_VALUE

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        targetMap == [(ATTR_NAME): TEST_VALUE]
    }

    @Test
    def "does not populate when attribute value is null"() {
        given:
        attributeIsNonCollectionPrimitive((['primitive': true, 'collection': false]))
        and:
        attributeHasValue(null)

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        targetMap.isEmpty()
    }

    @Test
    @Unroll
    def "does not populate when collection is #collection and primitive is #primitive"() {
        given:
        attributeDescriptor.isPrimitive() >> primitive
        attributeDescriptor.isCollection() >> collection

        when:
        populator.populate(conversionContext(attributeDescriptor), targetMap)

        then:
        targetMap.isEmpty()

        where:
        primitive | collection
        true      | true
        false     | false
        false     | true
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

    def attributeIsNonCollectionPrimitive(final Map<String, Boolean> params) {
        attributeDescriptor.isPrimitive() >> falseIfNull(params['primitive'])
        attributeDescriptor.isCollection() >> falseIfNull(params['collection'])
    }

    def attribute(Boolean primitive, Boolean collection) {
        attributeDescriptor.isPrimitive() >> primitive
        attributeDescriptor.isCollection() >> collection
    }

    def attributeHasValue(Object value) {
        attributeDescriptor.accessor() >> Stub(AttributeValueAccessor) {
            getValue(itemModel) >> value
        }
    }
}