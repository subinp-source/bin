/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.decorator

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DecoratorContextErrorExceptionUnitTest extends Specification {

    private static final String ERROR1 = 'error 1'
    private static final String ERROR2 = 'error 2'

    @Test
    def 'exception contains the message in the context and the context is accessible'() {
        given:
        def context = decoratorContext()

        when:
        def e = new DecoratorContextErrorException(context)

        then:
        with(e) {
            message.contains("Errors found in the DecoratorContext")
            message.contains(ERROR1)
            message.contains(ERROR2)
            getContext() == context
        }
    }

    private DecoratorContext decoratorContext() {
        Stub(DecoratorContext) {
            getErrors() >> [ERROR1, ERROR2]
        }
    }

}
