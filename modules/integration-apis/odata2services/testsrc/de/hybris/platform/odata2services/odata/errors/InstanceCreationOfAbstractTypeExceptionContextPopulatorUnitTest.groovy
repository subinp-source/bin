/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.persistence.validation.InstanceCreationOfAbstractTypeException
import de.hybris.platform.odata2services.odata.errors.InstanceCreationOfAbstractTypeExceptionContextPopulator
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class InstanceCreationOfAbstractTypeExceptionContextPopulatorUnitTest extends Specification {
    private static final String ERROR_CODE = "invalid_type"
    def contextPopulator = new InstanceCreationOfAbstractTypeExceptionContextPopulator()

    @Test
    def 'populates error context'() {
        given:
        def exception = Stub(InstanceCreationOfAbstractTypeException) {
            getMessage() >> 'error message'
        }
        def errorContext = new ODataErrorContext(exception: exception)

        when:
        contextPopulator.populate errorContext

        then:
        with(errorContext) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            errorCode == ERROR_CODE
            locale == Locale.ENGLISH
            message == exception.message
        }
    }

    @Test
    def 'context is not populated when Exception is not InstanceCreationOfAbstractTypeException'() {
        given:
        def exception = Stub(RuntimeException)
        def errorContext = new ODataErrorContext(exception: exception)

        when:
        contextPopulator.populate errorContext

        then:
        with(errorContext) {
            httpStatus == null
            errorCode == null
            locale == null
            message == null
        }
    }

    @Test
    def 'handles InstanceCreationOfAbstractTypeException'() {
        expect:
        contextPopulator.exceptionClass == InstanceCreationOfAbstractTypeException
    }

}
