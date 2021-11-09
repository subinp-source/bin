/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.populator

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification

@UnitTest
class NullPrimitiveValueHandlerUnitTest extends Specification {
    def handler = new NullPrimitiveValueHandler()

    @Test
    def "convert returns value that is passed in"() {
        expect:
        handler.convert(null, val) == val

        where:
        val << [null, 0, 'str', new Date(), 'c' as Character, 5 as BigInteger]
    }
}
