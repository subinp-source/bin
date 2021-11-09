/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.populator

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class PrimitiveCollectionElementUnitTest extends Specification {

    @Shared
    def elem1 = PrimitiveCollectionElement.create(123)

    @Test
    def '#value is wrapped in the PrimitiveCollectionElement'() {
        expect:
        PrimitiveCollectionElement.create(value).getValue() == value

        where:
        value << ['test', null]
    }

    @Test
    @Unroll
    def "equals() is #result when second element #condition"() {
        expect:
        (elem1 == elem2) == result

        where:
        condition             | elem2                                     | result
        'is the same element' | elem1                                     | true
        'has the same value'  | PrimitiveCollectionElement.create(123)    | true
        'has different value' | PrimitiveCollectionElement.create('test') | false
        'is null'             | null                                      | false
        'is different type'   | new Object()                              | false
    }

    @Test
    @Unroll
    def "hashcode() equal is #result when second element #condition"() {
        expect:
        (elem1.hashCode() == elem2.hashCode()) == result

        where:
        condition             | elem2                                     | result
        'is the same element' | elem1                                     | true
        'has the same value'  | PrimitiveCollectionElement.create(123)    | true
        'has different value' | PrimitiveCollectionElement.create('test') | false
        'is different type'   | new Object()                              | false
    }
}
