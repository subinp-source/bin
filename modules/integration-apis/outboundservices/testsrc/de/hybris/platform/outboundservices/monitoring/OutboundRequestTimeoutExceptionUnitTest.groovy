/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.monitoring

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification

@UnitTest
class OutboundRequestTimeoutExceptionUnitTest extends Specification {

    @Test
    def 'Exception is created with the timeout value'() {
        when:
        def e = new OutboundRequestTimeoutException(Long.MAX_VALUE)

        then:
        e.message == "Request timed out after $Long.MAX_VALUE ms."
        e.timeout == Long.MAX_VALUE
    }
}
