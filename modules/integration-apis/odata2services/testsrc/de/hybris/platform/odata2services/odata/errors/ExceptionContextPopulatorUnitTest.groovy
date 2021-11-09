/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ExceptionContextPopulatorUnitTest extends Specification {
    def contextPopulator = new ExceptionContextPopulator()

    @Test
    @Unroll
    def "provides context values for #ex"() {
        given:
        def context = new ODataErrorContext()

        when:
        contextPopulator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.INTERNAL_SERVER_ERROR
            errorCode == 'unknown_error'
            message == 'An unexpected error occurred. See the log for details'
            locale == Locale.ENGLISH
        }

        where:
        ex << [new Exception(), null]
    }

    @Test
    def 'handles Exception'() {
        expect:
        contextPopulator.exceptionClass == Exception
    }
}
