/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.user.AddressModel
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueConverter
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueHandler
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ClassificationAttributeValueSetterUnitTest extends Specification {
    private static final def SOME_VALUE = 'testValue'
    private static def product = new ProductModel()

    @Test
    @Unroll
    def "create classification attribute value setter with null #property throws exception"() {
        when:
        new ClassificationAttributeValueSetter(descriptor, converters, handlers)

        then:
        def e = thrown IllegalArgumentException
        e.message.contains property

        where:
        property               | descriptor                                  | converters | handlers
        "Attribute descriptor" | null                                        | []         | []
        "converters"           | Stub(ClassificationTypeAttributeDescriptor) | null       | []
        "handlers"             | Stub(ClassificationTypeAttributeDescriptor) | []         | null
    }

    @Test
    def "no interactions when the model is not of type Product"() {
        given:
        def converters = Mock(List)
        def handlers = Mock(List)
        and:
        def setter = new ClassificationAttributeValueSetter(
                Stub(ClassificationTypeAttributeDescriptor),
                converters,
                handlers)

        when:
        setter.setValue(Stub(AddressModel), SOME_VALUE)

        then:
        0 * converters._
        0 * handlers._
    }

    @Test
    def "only applicable converter is used to convert value"() {
        given:
        def handler = applicableHandler()
        and:
        def setter = new ClassificationAttributeValueSetter(
                Stub(ClassificationTypeAttributeDescriptor),
                [applicableToStringConverter(), inapplicableConverter()],
                [handler])
        and:
        def inputValue = 5
        def convertedValue = 5 as String

        when:
        setter.setValue(product, inputValue)

        then: 'value converted to string is used to set on handler'
        1 * handler.set(product, _ as ClassificationTypeAttributeDescriptor, convertedValue)
    }

    @Test
    def "only applicable handlers are used to set value"() {
        given:
        def inapplicableHandler = inapplicableHandler()
        def applicableHandler = applicableHandler()

        and:
        def setter = new ClassificationAttributeValueSetter(
                Stub(ClassificationTypeAttributeDescriptor),
                [],
                [applicableHandler, inapplicableHandler])

        when:
        setter.setValue(product, SOME_VALUE)

        then:
        1 * applicableHandler.set(product, _, SOME_VALUE)
        0 * inapplicableHandler.set(product, _, SOME_VALUE)
    }

    @Test
    def "only most (first) applicable converter is used to convert value"() {
        given:
        def handler = applicableHandler()

        and:
        def setter = new ClassificationAttributeValueSetter(
                Stub(ClassificationTypeAttributeDescriptor),
                [applicableDoublingConverter(), applicableToStringConverter()],
                [handler])
        and:
        def inputValue = 5
        def convertedValue = 10

        when:
        setter.setValue(product, inputValue)

        then: 'value * 2 is set on handler'
        1 * handler.set(product, _ as ClassificationTypeAttributeDescriptor, convertedValue)
    }

    @Test
    def "only most (first) applicable handler is used to set value"() {
        given:
        def applicableHandler1 = applicableHandler()
        def applicableHandler2 = applicableHandler()
        and:
        def setter = new ClassificationAttributeValueSetter(
                Stub(ClassificationTypeAttributeDescriptor),
                [],
                [applicableHandler1, applicableHandler2])

        when:
        setter.setValue(product, SOME_VALUE)

        then:
        1 * applicableHandler1.set(product, _, SOME_VALUE)
        0 * applicableHandler2.set(product, _, SOME_VALUE)
    }

    private def applicableHandler() {
        Mock(ClassificationAttributeValueHandler) {
            isApplicable(_ as ClassificationTypeAttributeDescriptor, _) >> true
        }
    }

    private def inapplicableHandler() {
        Mock(ClassificationAttributeValueHandler) {
            isApplicable(_ as ClassificationTypeAttributeDescriptor, _) >> false
        }
    }

    private def applicableToStringConverter() {
        Stub(ClassificationAttributeValueConverter) {
            isApplicable(_ as ClassificationTypeAttributeDescriptor) >> true
            convert(_ as ClassificationTypeAttributeDescriptor, _) >> { descriptor, value -> value as String }
        }
    }
    private def applicableDoublingConverter() {
        Stub(ClassificationAttributeValueConverter) {
            isApplicable(_ as ClassificationTypeAttributeDescriptor) >> true
            convert(_ as ClassificationTypeAttributeDescriptor, _) >> { descriptor, value -> value * 2 }
        }
    }

    private def inapplicableConverter() {
        Stub(ClassificationAttributeValueConverter) {
            isApplicable(_ as ClassificationTypeAttributeDescriptor) >> false
            convert(_ as ClassificationTypeAttributeDescriptor, _) >> { descriptor, value -> value + "wrong" }
        }
    }
}
