/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.exception.ODataMessageException
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class AttributeNotInIntegrationObjectExceptionContextPopulatorUnitTest extends Specification {
    def populator = new AttributeNotInIntegrationObjectExceptionContextPopulator()

    @Test
    def 'does not populate exception of other than EntityProviderException type'() {
        given:
        def context = new ODataErrorContext(exception: Stub(ODataMessageException))

        when:
        populator.populate context

        then:
        with(context) {
            !httpStatus
            !errorCode
            !message
            !locale
            !innerError
        }
    }

    @Unroll
    @Test
    def "populates context for EntityProviderException with ILLEGAL_ARGUMENT message reference and #content as content"() {
        given:
        def context = new ODataErrorContext(exception: new AttributeNotInIntegrationObjectException(content))

        when:
        populator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            errorCode == 'invalid_property'
            message == "An entity contains " + expectedMessage + " that is not defined in the integration object"
            locale == Locale.ENGLISH
        }

        where:
        content                         | expectedMessage
        ['name']                        | 'property [name]'
        [5]                             | 'property [5]'
        ['testName', 'testDescription'] | 'properties [testName, testDescription]'
        []                              | "property []"
        null                            | "property []"
    }

    @Test
    def "handles EntityPersistenceException"() {
        expect:
        populator.exceptionClass == AttributeNotInIntegrationObjectException
    }
}
