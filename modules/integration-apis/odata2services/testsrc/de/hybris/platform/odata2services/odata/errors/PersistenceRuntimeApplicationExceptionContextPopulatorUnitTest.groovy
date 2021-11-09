/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException
import de.hybris.platform.odata2services.odata.persistence.PersistenceRuntimeApplicationException
import de.hybris.platform.odata2services.odata.persistence.hook.PersistHookNotFoundException
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class PersistenceRuntimeApplicationExceptionContextPopulatorUnitTest extends Specification {
    def contextPopulator = new PersistenceRuntimeApplicationExceptionContextPopulator()

    @Test
    @Unroll
    def "populates error context values when context exception is #ex"() {
        given:
        def context = new ODataErrorContext(exception: ex)

        when:
        contextPopulator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            errorCode == ex.code
            message == 'message'
            locale == Locale.ENGLISH
            innerError == 'key'
        }

        where:
        ex << [new PersistenceRuntimeApplicationException('message', HttpStatusCodes.BAD_REQUEST, 'err-code', 'key'),
               new PersistHookNotFoundException('message', 'key')]
    }

    @Test
    @Unroll
    def "does not populate error context when context exception #ex is not instance of PersistenceRuntimeApplicationException"() {
        given:
        def context = new ODataErrorContext(exception: ex)

        when:
        contextPopulator.populate context

        then:
        with(context) {
            ! httpStatus
            ! errorCode
            ! message
            ! locale
            ! innerError
        }

        where:
        ex << [new InternalProcessingException('message'), null]
    }

    @Test
    def 'handles PersistenceRuntimeApplicationException'() {
        expect:
        contextPopulator.exceptionClass == PersistenceRuntimeApplicationException
    }
}
