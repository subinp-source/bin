/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util.timeout

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification

@UnitTest
class IntegrationTimeoutExceptionUnitTest extends Specification {
    
    @Test
    def 'Exception is created with the timeout value'() {
        when:
        def e = new IntegrationTimeoutException(Long.MAX_VALUE)

        then:
        e.message == "The execution timed out after $Long.MAX_VALUE ms."
        e.timeout == Long.MAX_VALUE
    }

    @Test
    def 'Exception is created with the timeout value and message'() {
        when:
        def e = new IntegrationTimeoutException("A timeout exception", Long.MAX_VALUE)

        then:
        e.message == "A timeout exception"
        e.timeout == Long.MAX_VALUE
    }
}
