/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.populator

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class AtomicTypeValueConverterUnitTest extends Specification {

    def converter = new AtomicTypeValueConverter()

    @Test
    @Unroll
    def "converts #value to #convertedValue for type #valueType"() {
        expect:
        converter.convert(value) == convertedValue

        where:
        valueType    | value                       | convertedValue
        "string"     | "test"                      | value
        "date"       | new Date()                  | "/Date(" + ((Date) value).getTime() + ")/"
        "long"       | 1l                          | "1"
        "bigDecimal" | BigDecimal.valueOf(1234.56) | "1234.56"
    }
}
