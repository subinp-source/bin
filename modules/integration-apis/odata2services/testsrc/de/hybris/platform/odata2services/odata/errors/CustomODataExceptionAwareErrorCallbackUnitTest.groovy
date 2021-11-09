/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.util.JsonObject
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import org.springframework.expression.EvaluationException
import org.springframework.expression.ExpressionException
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class CustomODataExceptionAwareErrorCallbackUnitTest extends Specification {
    private static final String ERROR_MESSAGE_VALUE_PATH = '$.error.message.value'
    private static final String ERROR_MESSAGE_LANGUAGE_PATH = '$.error.message.lang'
    private static final String ERROR_CODE_PATH = '$.error.code'

    def oDataExceptionCallback = new CustomODataExceptionAwareErrorCallback()

    def setup() {
        oDataExceptionCallback.errorContextPopulators = []
    }

    @Test
    @Unroll
    def "handles #desc as unknown error"() {
        when:
        final ODataResponse response = oDataExceptionCallback.handleError errorContextWith(ex)

        then:
        response.status == HttpStatusCodes.INTERNAL_SERVER_ERROR
        def json = extractBody(response)
        json.getString(ERROR_CODE_PATH) == 'unknown_error'
        json.getString(ERROR_MESSAGE_VALUE_PATH)
        json.getString(ERROR_MESSAGE_LANGUAGE_PATH) == 'en'

        where:
        desc                  | ex
        'non-OData exception' | new RuntimeException()
        'null exception'      | null
    }

    @Test
    def 'handles an exception even when no ErrorContextProviders added'() {
        given:
        def context = errorContextWith(new EdmException(EdmException.COMMON))

        when:
        def response = oDataExceptionCallback.handleError context

        then:
        response.status == HttpStatusCodes.INTERNAL_SERVER_ERROR
        with extractBody(response), {
            getString(ERROR_CODE_PATH) == 'unknown_error'
            getString(ERROR_MESSAGE_VALUE_PATH)
            getString(ERROR_MESSAGE_LANGUAGE_PATH) == 'en'
        }
    }

    @Test
    @Unroll
    def "handles exception by delegating it to the handler most closely matching its #kind"() {
        given: 'populators handling RuntimeException and its subclass ExpressionException'
        def runtime = populator(RuntimeException, 'runtime_code')
        def expression = populator(ExpressionException, 'expression_code')
        oDataExceptionCallback.errorContextPopulators = [runtime, expression]
        and: 'an error context with an extension extending from RuntimeException'
        def context = errorContextWith exception

        when:
        def response = oDataExceptionCallback.handleError context

        then:
        extractBody(response).getString(ERROR_CODE_PATH) == 'expression_code'

        where:
        kind        | exception
        'type'      | new ExpressionException('') // extends RuntimeExpression and has exactly matching populator
        'supertype' | new EvaluationException('') // extends ExpressionException and has supertype matching populator
    }

    @Test
    def 'default Exception populator can be overridden'() {
        given: 'a populator handling Exception that should replace default logic'
        oDataExceptionCallback.errorContextPopulators = [populator(Exception, 'overridden_code')]
        and: 'an error context with some exception'
        def context = errorContextWith new RuntimeException()

        when:
        def response = oDataExceptionCallback.handleError context

        then:
        extractBody(response).getString(ERROR_CODE_PATH) == 'overridden_code'
    }

    @Test
    def 'last populator for a given exception type overrides previously configured populators'() {
        given: 'two populators for RuntimeException'
        def one = populator(RuntimeException, 'first')
        def two = populator(RuntimeException, 'second')
        oDataExceptionCallback.errorContextPopulators = [one, two]
        and: 'an error context with RuntimeException'
        def context = errorContextWith new RuntimeException()

        when:
        def response = oDataExceptionCallback.handleError context

        then:
        extractBody(response).getString(ERROR_CODE_PATH) == 'second'
    }

    @Test
    def 'setErrorContextPopulators resets previously set populators'() {
        given: 'two populators for different exceptions to be used in different set method invocations'
        def one = populator(IllegalArgumentException, 'illegal_argument')
        def two = populator(RuntimeException, 'runtime')
        and: 'an error context with exception matching the first populator'
        def context = errorContextWith new IllegalArgumentException()

        when: 'errorContextPopulators is called twice with different populators'
        oDataExceptionCallback.errorContextPopulators = [one]
        oDataExceptionCallback.errorContextPopulators = [two]
        and: 'the context is handled'
        def response = oDataExceptionCallback.handleError context

        then: 'context is processed by the second populator because the first populator is reset by the second call'
        extractBody(response).getString(ERROR_CODE_PATH) == 'runtime'
    }

    @Test
    def 'addErrorContextPopulator does not reset previously set populators'() {
        given: 'two populators for different exceptions to be used in different add method invocations'
        def one = populator(IllegalArgumentException, 'illegal_argument')
        def two = populator(RuntimeException, 'runtime')
        and: 'an error context with exception matching the first populator'
        def context = errorContextWith new IllegalArgumentException()

        when: 'addErrorContextPopulator is called twice with different populators'
        oDataExceptionCallback.addErrorContextPopulator one
        oDataExceptionCallback.addErrorContextPopulator two
        and: 'the context is handled'
        def response = oDataExceptionCallback.handleError context

        then: 'context is processed by the first populator because it was not reset by the second call'
        extractBody(response).getString(ERROR_CODE_PATH) == 'illegal_argument'
    }

    @Test
    def 'handles Exception'() {
        expect:
        oDataExceptionCallback.exceptionClass == Exception
    }

    ODataErrorContext errorContextWith(Exception e) {
        new ODataErrorContext(exception: e, contentType: 'application/json')
    }

    ErrorContextPopulator populator(Class type, String errCode) {
        Stub(ErrorContextPopulator) {
            getExceptionClass() >> type
            populate(_ as ODataErrorContext) >> { args -> args[0].errorCode = errCode }
        }
    }

    JsonObject extractBody(final ODataResponse response) {
        JsonObject.createFrom((InputStream) response.getEntity())
    }
}
