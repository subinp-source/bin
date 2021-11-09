/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.odata.persistence.exception.ItemNotFoundException
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.exception.ODataException
import org.apache.olingo.odata2.api.exception.ODataRuntimeApplicationException
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ODataRuntimeApplicationExceptionContextPopulatorUnitTest extends Specification {
    def contextPopulator = new ODataRuntimeApplicationExceptionContextPopulator()

    @Test
    @Unroll
    def "provides error context values for #ex"() {
        given:
        def context = new ODataErrorContext(exception: ex)

        when:
        contextPopulator.populate context

        then:
        with(context) {
            httpStatus == ex.httpStatus
            locale == ex.locale
            errorCode == ex.code
            message == ex.message
        }

        where:
        ex << [new ODataRuntimeApplicationException('message', Locale.FRENCH, HttpStatusCodes.BAD_REQUEST, 'err_code'),
               new ItemNotFoundException('Type', 'key')]
    }

    @Test
    @Unroll
    def "does not populate context when context exception #ex in not an instanceof ODataRuntimeApplicationException"() {
        given:
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

        where:
        ex << [new ODataException(), null]
    }

    @Test
    def 'handles ODataRuntimeApplicationException'() {
        expect:
        contextPopulator.exceptionClass == ODataRuntimeApplicationException
    }
}
