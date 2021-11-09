/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util.timeout

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class IntegrationExecutionExceptionUnitTest extends Specification {

    private static final Exception EXCEPTION = new Exception("IGNORED - Testing exception")

    @Test
    @Unroll
    def "Exception is created with the cause #cause"() {
        when:
        def e = new IntegrationExecutionException(cause)

        then:
        e.message == 'The execution encountered an exception.'
        e.cause == expectedCause

        where:
        cause                                        | expectedCause
        EXCEPTION                                    | EXCEPTION
        new IntegrationExecutionException(EXCEPTION) | EXCEPTION
    }

    @Test
    @Unroll
    def "Exception is created with the cause #cause and message"() {
        when:
        def e = new IntegrationExecutionException('An exception occurred', cause)

        then:
        e.message == 'An exception occurred'
        e.cause == expectedCause

        where:
        cause                                        | expectedCause
        EXCEPTION                                    | EXCEPTION
        new IntegrationExecutionException(EXCEPTION) | EXCEPTION
    }
}
