/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.IntegrationAttributeException
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class IntegrationAttributeExceptionContextPopulatorUnitTest extends Specification {
    private static final String ERROR_CODE = 'misconfigured_attribute'
    def contextPopulator = new IntegrationAttributeExceptionContextPopulator()

    @Test
    def 'populates error context'() {
        given:
        def exception = Stub(IntegrationAttributeException) {
            getMessage() >> 'error message'
        }
        def errorContext = new ODataErrorContext(exception: exception)

        when:
        contextPopulator.populate errorContext

        then:
        with(errorContext) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            errorCode == ERROR_CODE
            message == exception.message
        }
    }

    @Test
    def 'handles IntegrationAttributeException'() {
        expect:
        contextPopulator.exceptionClass == IntegrationAttributeException
    }
}
