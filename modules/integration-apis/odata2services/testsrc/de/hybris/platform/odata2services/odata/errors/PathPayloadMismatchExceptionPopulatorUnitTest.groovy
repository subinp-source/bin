/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.odata.processor.handler.persistence.PathPayloadKeyMismatchException
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class PathPayloadMismatchExceptionPopulatorUnitTest extends Specification {

    def contextPopulator = new PathPayloadMismatchExceptionPopulator()

    @Test
    def "provides error context values for PathPayloadMismatchException"() {
        given:
        def ex = new PathPayloadKeyMismatchException('pathIntKey', 'payloadIntKey')
        def context = new ODataErrorContext(exception: ex)

        when:
        contextPopulator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            locale == Locale.ENGLISH
            errorCode == 'invalid_key'
            message == 'Key [payloadIntKey] in the payload does not match the key [pathIntKey] in the path'
        }
    }

    @Test
    def "does not populate context when context exception is not an instanceof PathPayloadKeyMismatchException"() {
        given:
        def ex = new Exception('IGNORE - test exception')
        def context = new ODataErrorContext(exception: ex)

        when:
        contextPopulator.populate context

        then:
        with(context) {
            ! httpStatus
            ! locale
            ! errorCode
            ! message
        }
    }

    @Test
    def 'handles PathPayloadKeyMismatchException'() {
        expect:
        contextPopulator.exceptionClass == PathPayloadKeyMismatchException
    }
}
