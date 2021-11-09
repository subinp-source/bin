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

package de.hybris.platform.integrationservices.item


import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class LocalizedValueUnitTest extends Specification {
    def localizedValue = LocalizedValue.EMPTY

    @Test
    def "cannot be created with null Locale"() {
        when:
        LocalizedValue.of(null, 'value')

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "reads null for a locale that has not been set yet"() {
        expect:
        !localizedValue.get(Locale.ENGLISH)
    }

    @Test
    def "combine() throws exception when locale is null"() {
        when:
        localizedValue.combine(null, 'some value')

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "set value can be read back"() {
        given:
        def updated = localizedValue.combine(Locale.ENGLISH, 'some text')

        expect:
        updated.get(Locale.ENGLISH) == 'some text'
    }

    @Test
    def "distinguishes between the country specific values"() {
        given:
        def updated = localizedValue
                .combine(Locale.ENGLISH, 'general english')
                .combine(Locale.CANADA, 'canadian english')

        expect:
        updated.get(Locale.ENGLISH) == 'general english'
        updated.get(Locale.CANADA) == 'canadian english'
    }

    @Test
    def "is immutable"() {
        given:
        def afterValueSet = localizedValue.combine(Locale.ENGLISH, 'value')

        expect:
        !localizedValue.is(afterValueSet)
        !localizedValue.get(Locale.ENGLISH)
        afterValueSet.get(Locale.ENGLISH)
    }

    @Test
    @Unroll
    def "can combine values from another LocalizedValue when value is #value"() {
        given:
        def value1 = LocalizedValue.of(Locale.ENGLISH, value)
        def value2 = LocalizedValue.of(Locale.GERMAN, 'german')

        when:
        def combined = value1.combine(value2)

        then:
        combined.get(Locale.ENGLISH) == value
        combined.get(Locale.GERMAN) == 'german'

        where:
        value << ['english', null]
    }

    @Test
    @Unroll
    def "previously set value #value can be reset"() {
        given:
        def original = LocalizedValue.of(Locale.ENGLISH, value)

        when:
        def reset = original.combine(Locale.ENGLISH, 'two')

        then:
        reset.get(Locale.ENGLISH) == 'two'

        where:
        value << ['one', null]
    }

    @Test
    @Unroll
    def "not equal when the other localized value #condition"() {
        given:
        def self = LocalizedValue.of(Locale.ENGLISH, 'value')

        expect:
        self != other

        where:
        condition                                      | other
        'is null'                                      | null
        'is empty'                                     | LocalizedValue.EMPTY
        'has same locale with null value'              | LocalizedValue.of(Locale.ENGLISH, null)
        'has different locale set with the same value' | LocalizedValue.of(Locale.CANADA, 'value')
        'has different value set for the same locale'  | LocalizedValue.of(Locale.ENGLISH, 'different')
        'has different number of locales set'          | LocalizedValue.of(Locale.ENGLISH, 'value').combine(Locale.CANADA, 'value')
        'is not a LocalizedValue'                      | [(Locale.ENGLISH): 'value']
    }

    @Test
    @Unroll
    def "equal when both localized values #condition "() {
        expect:
        self == other

        where:
        condition                                  | self                                       | other
        'are empty'                                | LocalizedValue.EMPTY                       | LocalizedValue.EMPTY
        'have same locales set to the same values' | LocalizedValue.of(Locale.ENGLISH, 'value') | LocalizedValue.of(Locale.ENGLISH, 'value')
        'have same locales set to null values'     | LocalizedValue.of(Locale.ENGLISH, null)    | LocalizedValue.of(Locale.ENGLISH, null)
    }

    @Test
    @Unroll
    def "hashCode() not equal when the other localized value #condition"() {
        given:
        def self = LocalizedValue.of(Locale.ENGLISH, 'value')

        expect:
        self.hashCode() != other.hashCode()

        where:
        condition                                      | other
        'is empty'                                     | LocalizedValue.EMPTY
        'has same locale with null value'              | LocalizedValue.of(Locale.ENGLISH, null)
        'has different locale set with the same value' | LocalizedValue.of(Locale.CANADA, 'value')
        'has different value set for the same locale'  | LocalizedValue.of(Locale.ENGLISH, 'different')
        'has different number of locales set'          | LocalizedValue.of(Locale.ENGLISH, 'value').combine(Locale.CANADA, 'value')
    }

    @Test
    @Unroll
    def "hashCode() equal when both localized values #condition "() {
        expect:
        self.hashCode() == other.hashCode()

        where:
        condition                                  | self                                       | other
        'are empty'                                | LocalizedValue.EMPTY                       | LocalizedValue.EMPTY
        'have same locales set to the same values' | LocalizedValue.of(Locale.ENGLISH, 'value') | LocalizedValue.of(Locale.ENGLISH, 'value')
        'have same locales set to null values'     | LocalizedValue.of(Locale.ENGLISH, null)    | LocalizedValue.of(Locale.ENGLISH, null)
    }

    @Test
    def "forEach() applies the lambda to each locale and value"() {
        given:
        def localizedValue = LocalizedValue.of(Locale.ENGLISH, 'en-value').combine(Locale.JAPANESE, 'jp-value')

        when:
        def map = [:]
        localizedValue.forEach({ l, v -> map.put(l, v) })

        then:
        map == [(Locale.ENGLISH): 'en-value', (Locale.JAPANESE): 'jp-value']
    }
}
