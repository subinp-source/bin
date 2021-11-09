/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification

import static de.hybris.platform.odata2services.converter.ConversionParameters.ConversionParametersBuilder.conversionParametersBuilder

@UnitTest
class DefaultPayloadAttributeValueConverterUnitTest extends Specification {
    private static final def PARAMETERS = conversionParametersBuilder().withAttributeValue('some value').build()

    def converter = new DefaultPayloadAttributeValueConverter()

    @Test
    def "attribute value is returned when no value converters set"() {
        expect:
        converter.convertAttributeValue(PARAMETERS) == PARAMETERS.attributeValue
    }

    @Test
    def "attribute value is returned when only not applicable value converters set"() {
        given:
        converter.valueConverters = [notApplicableConverter('converter value')]

        expect:
        converter.convertAttributeValue(PARAMETERS) == PARAMETERS.attributeValue
    }

    @Test
    def "value conversion result is returned when at least one applicable value converters set"() {
        given:
        converter.valueConverters = [
                notApplicableConverter('wrong value'),
                applicableConverter('right value')
        ]

        expect:
        converter.convertAttributeValue(PARAMETERS) == 'right value'
    }

    @Test
    def "only first applicable value converter is used to derive the conversion result"() {
        given:
        converter.valueConverters = [
                applicableConverter('first value'),
                applicableConverter('second value')
        ]

        expect:
        converter.convertAttributeValue(PARAMETERS) == 'first value'
    }

    @Test
    def "setValueConverters resets previously set converters"() {
        given: 'an applicable converter is set'
        converter.valueConverters = [applicableConverter('converter value')]

        when: 'a list with non-applicable converters set'
        converter.valueConverters = [notApplicableConverter(null)]

        then: 'the originally set applicable converter is not used anymore'
        converter.convertAttributeValue(PARAMETERS) == PARAMETERS.attributeValue
    }

    private ValueConverter notApplicableConverter(def value) {
        Stub(ValueConverter) {
            isApplicable(PARAMETERS) >> false
            convert(PARAMETERS) >> value
        }
    }

    private ValueConverter applicableConverter(def value) {
        Stub(ValueConverter) {
            isApplicable(PARAMETERS) >> true
            convert(PARAMETERS) >> value
        }
    }
}
