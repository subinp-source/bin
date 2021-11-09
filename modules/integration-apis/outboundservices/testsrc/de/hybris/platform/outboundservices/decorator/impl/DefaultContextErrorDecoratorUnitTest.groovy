/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.decorator.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.outboundservices.decorator.DecoratorContext
import de.hybris.platform.outboundservices.decorator.DecoratorContextErrorException
import de.hybris.platform.outboundservices.decorator.DecoratorExecution
import org.junit.Test
import org.springframework.http.HttpHeaders
import spock.lang.Specification

@UnitTest
class DefaultContextErrorDecoratorUnitTest extends Specification {

    private static final String ERROR_MESSAGE = 'an error message for the exception in the test'
    private static final String ANOTHER_ERROR_MESSAGE = 'another error message for the exception in the test'

    def contextErrorDecorator = new DefaultContextErrorDecorator()
    def decoratorExecution = Mock(DecoratorExecution)

    @Test
    def 'an exception is thrown when errors are present in the context'() {
        given:
        def context = decoratorContextWithErrors()

        when:
        contextErrorDecorator.decorate(new HttpHeaders(), [:], context, decoratorExecution)

        then:
        def e = thrown(DecoratorContextErrorException)
        with(e) {
            message.contains("Errors found in the DecoratorContext")
            message.contains(ERROR_MESSAGE)
            message.contains(ANOTHER_ERROR_MESSAGE)
        }
    }

    @Test
    def 'no exception is thrown when no errors are present in the context'() {
        given:
        def context = decoratorContextNoErrors()

        when:
        contextErrorDecorator.decorate(new HttpHeaders(), [:], context, decoratorExecution)

        then:
        noExceptionThrown()
        1 * decoratorExecution.createHttpEntity(_ as HttpHeaders, [:], context)
    }

    private DecoratorContext decoratorContextWithErrors() {
        Stub(DecoratorContext) {
            hasErrors() >> true
            getErrors() >> [ERROR_MESSAGE, ANOTHER_ERROR_MESSAGE]
        }
    }

    private DecoratorContext decoratorContextNoErrors() {
        Stub(DecoratorContext) {
            hasErrors() >> false
        }
    }
}
