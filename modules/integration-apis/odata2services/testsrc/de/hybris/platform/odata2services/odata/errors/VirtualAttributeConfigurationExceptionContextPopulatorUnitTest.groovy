/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutorContext
import de.hybris.platform.integrationservices.virtualattributes.VirtualAttributeConfigurationException
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class VirtualAttributeConfigurationExceptionContextPopulatorUnitTest extends Specification {

    def populator = new VirtualAttributeConfigurationExceptionContextPopulator()

    @Test
    def 'does not populate context values if context does not contain VirtualAttributeConfigurationException'() {
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
        def logicContext = Stub(LogicExecutorContext){
            getDescriptor() >> Stub(TypeAttributeDescriptor){
                getAttributeName() >> "SomeName"
                getTypeDescriptor() >> Stub(TypeDescriptor){
                    getItemCode() >> "Product"
                }
            }
        }
        def runtime = Stub(RuntimeException)
        def context = new ODataErrorContext(exception: new VirtualAttributeConfigurationException(logicContext, runtime))

        when:
        populator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            errorCode == 'misconfigured_attribute'
            message == "There was an error reading the attribute [SomeName] for EntityType [Product]."
            locale == Locale.ENGLISH
        }
    }

    @Test
    def 'handles VirtualAttributeConfigurationException'() {
        expect:
        populator.exceptionClass == VirtualAttributeConfigurationException
    }
}

