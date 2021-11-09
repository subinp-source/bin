/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutor
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutorContext
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutorFactory
import de.hybris.platform.integrationservices.virtualattributes.LogicParams
import de.hybris.platform.integrationservices.virtualattributes.VirtualAttributeConfigurationException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class VirtualAttributeValueGetterUnitTest extends Specification {

    private static final String TEST_LOGIC_LOCATION = "model://someScriptName"
    def logicExecutor = Stub(LogicExecutor)

    @Test
    @Unroll
    def "exception is thrown when #parameter is not provided"() {
        when:
        new VirtualAttributeValueGetter(descriptor, logicExecutorFactory)

        then:
        def e = thrown IllegalArgumentException
        e.message.toLowerCase().contains parameter

        where:
        parameter                | descriptor                           | logicExecutorFactory
        'attribute descriptor'   | null                                 | Stub(LogicExecutorFactory)
        'logic executor factory' | Stub(VirtualTypeAttributeDescriptor) | null
    }

    @Test
    def "throws exception if logic location cannot be created when VirtualAttributeValueGetter is instantiated"() {
        given:
        def descriptor = typeAttributeDescriptor(null)

        when:
        new VirtualAttributeValueGetter(descriptor, Stub(LogicExecutorFactory))

        then:
        thrown VirtualAttributeConfigurationException
    }

    @Test
    def "getValue returns the value from the logicExecutor.execute call"() {
        given:
        def virtualAttributeGetter = new VirtualAttributeValueGetter(typeAttributeDescriptor(TEST_LOGIC_LOCATION), logicExecutorFactory(logicExecutor))
        def itemModel = Stub(ItemModel)
        def expectedValue = "expectedValue"
        logicExecutor.execute(LogicParams.create(itemModel)) >> expectedValue

        when:
        def value = virtualAttributeGetter.getValue(itemModel)

        then:
        value == expectedValue
    }


    def typeAttributeDescriptor(String logicLocation) {
        Stub(VirtualTypeAttributeDescriptor) {
            getLogicLocation() >> logicLocation
        }
    }

    def logicExecutorFactory(LogicExecutor logicExecutor) {
        Stub(LogicExecutorFactory) {
            create(_ as LogicExecutorContext) >> logicExecutor
        }
    }
}
