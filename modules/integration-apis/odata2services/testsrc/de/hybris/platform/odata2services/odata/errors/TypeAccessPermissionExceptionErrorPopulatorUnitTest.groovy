/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.security.TypeAccessPermissionException
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class TypeAccessPermissionExceptionErrorPopulatorUnitTest extends Specification {

    def contextPopulator = new TypeAccessPermissionExceptionErrorPopulator()

    @Test
    def "provides error context values for TypeAccessPermissionException"() {
        given:
        def type = 'Catalog'
        def permission = 'erase'
        def ex = new TypeAccessPermissionException(type, permission)
        def context = new ODataErrorContext(exception: ex)

        when:
        contextPopulator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.FORBIDDEN
            locale == Locale.ENGLISH
            errorCode == 'forbidden'
            message == "The user lacks $permission privileges for accessing the $type type."
        }
    }

    @Test
    def "does not populate context when context exception is not an instanceof TypeAccessPermissionException"() {
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
    def 'handles TypeAccessPermissionException'() {
        expect:
        contextPopulator.exceptionClass == TypeAccessPermissionException
    }
}
