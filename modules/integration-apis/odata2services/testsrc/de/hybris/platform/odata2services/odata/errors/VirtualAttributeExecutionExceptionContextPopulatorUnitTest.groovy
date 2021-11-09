/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutorContext
import de.hybris.platform.integrationservices.virtualattributes.LogicParams
import de.hybris.platform.integrationservices.virtualattributes.VirtualAttributeExecutionException
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class VirtualAttributeExecutionExceptionContextPopulatorUnitTest extends Specification {
    def populator = new VirtualAttributeExecutionExceptionContextPopulator()

    @Test
    def 'does not populate context values if context does not contain VirtualAttributeExecutionException'() {
        given:
        def context = new ODataErrorContext(exception: new InvalidAttributeValueException('value', Stub(TypeAttributeDescriptor)))

        when:
        populator.populate context

        then:
        with(context) {
            !httpStatus
            !errorCode
            !message
            !locale
        }
    }

    @Test
    def 'populates error context values'() {
        given:
        def logicContext = Stub(LogicExecutorContext)
        def params = Stub(LogicParams)
        def runtime = Stub(RuntimeException)
        def context = new ODataErrorContext(exception: new VirtualAttributeExecutionException(logicContext, params, runtime))

        when:
        populator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            errorCode == 'runtime_error'
            message == "There was an unexpected error encountered during the retrieval of the [] property for EntityType []."
            locale == Locale.ENGLISH
        }
    }

    @Test
    def 'handles VirtualAttributeExecutionException'() {
        expect:
        populator.exceptionClass == VirtualAttributeExecutionException
    }
}
