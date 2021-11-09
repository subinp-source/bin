/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class NullAttributeValueAccessorUnitTest extends Specification {

    def accessor = new NullAttributeValueAccessor()

    @Test
    @Unroll
    def "get value with model #model returns null"() {
        expect:
        !accessor.getValue(model)

        where:
        model << [new Object(), null]
    }

    @Test
    def 'get value with model #model and locale returns null'() {
        expect:
        !accessor.getValue(model, Locale.ENGLISH)

        where:
        model << [new Object(), null]
    }
}
