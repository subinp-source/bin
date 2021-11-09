/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util.timeout.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.util.timeout.IntegrationExecutionException
import de.hybris.platform.integrationservices.util.timeout.IntegrationTimeoutException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.Callable

@UnitTest
class DefaultTimeoutServiceUnitTest extends Specification {

    private static final long TIMEOUT = 10

    def timeoutService = new DefaultTimeoutService()

    @Test
    @Unroll
    def "Execute throws exception during instantiation when callable is #callable and timeout is #timeout"() {
        when:
        timeoutService.execute callable, timeout

        then:
        def e = thrown exception
        e.message.contains msg
        
        where:
        callable       | timeout | exception                   | msg
        null           | 1       | IllegalArgumentException    | "Callable must be provided"
        Stub(Callable) | 0       | IntegrationTimeoutException | "$timeout"
        Stub(Callable) | -1      | IntegrationTimeoutException | "$timeout"
    }

    @Test
    def "Execution exception is thrown when the callable causes an exception"() {
        given: 'Callable throws an exception when call is invoked'
        def exception = new Exception('IGNORE - testing callable exception')
        def callable = Stub(Callable) {
            call() >> { throw exception }
        }

        when:
        timeoutService.execute callable, TIMEOUT

        then:
        def e = thrown IntegrationExecutionException
        e.cause.is exception
    }

    @Test
    def "Timeout exception is thrown when the callable execution exceeds timeout limit"() {
        given: 'Long callable execution'
        def timeout = 5
        def callableExecutionTime = 10
        def callable = Stub(Callable) {
            call() >> { Thread.sleep(callableExecutionTime) }
        }

        when:
        timeoutService.execute callable, timeout

        then:
        def e = thrown IntegrationTimeoutException
        e.message.contains "$timeout"
    }

    @Test
    def "Execution completes within timeout limit"() {
        given: 'Callable execution within time limit'
        def callableReturn = 'I finished within time!'
        def callable = Stub(Callable) {
            call() >> callableReturn
        }

        when:
        def value = timeoutService.execute callable, TIMEOUT

        then:
        noExceptionThrown()
        value == callableReturn
    }
}
