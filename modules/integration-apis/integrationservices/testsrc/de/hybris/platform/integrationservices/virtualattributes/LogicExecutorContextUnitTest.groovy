/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.virtualattributes

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.scripting.LogicLocation
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class LogicExecutorContextUnitTest extends Specification {

    @Shared
    def logicLocation = Stub LogicLocation
    @Shared
    def descriptor = Stub TypeAttributeDescriptor

    @Test
    @Unroll
    def "#param must be provided to create a LogicExecutorContext"() {
        when:
        LogicExecutorContext.create logicLoc, attrDescriptor

        then:
        def e = thrown IllegalArgumentException
        e.message == "$param must be provided"

        where:
        param                  | logicLoc      | attrDescriptor
        'Logic location'       | null          | descriptor
        'Attribute descriptor' | logicLocation | null
    }

    @Test
    def 'LogicExecutorContext is created when required fields are provided'() {
        when:
        def context = LogicExecutorContext.create logicLocation, descriptor

        then:
        with(context) {
            logicLocation == logicLocation
            descriptor == descriptor
        }
    }

    @Test
    @Unroll
    def "two contexts are equal when they #condition"() {
        expect:
        context1 == context2

        where:
        condition               | context1                                               | context2
        'are the same object'   | LogicExecutorContext.create(logicLocation, descriptor) | context1
        'have the same content' | LogicExecutorContext.create(logicLocation, descriptor) | LogicExecutorContext.create(logicLocation, descriptor)
    }

    @Test
    @Unroll
    def "two contexts are different when context2 #condition"() {
        given:
        def context1 = LogicExecutorContext.create logicLocation, descriptor

        expect:
        context1 != context2

        where:
        condition               | context2
        'is null'               | null
        'has different content' | LogicExecutorContext.create(Stub(LogicLocation), Stub(TypeAttributeDescriptor))
        'is a different type'   | 2
    }

    @Test
    @Unroll
    def "two context hashCodes are equal when they #condition"() {
        expect:
        context1.hashCode() == context2.hashCode()

        where:
        condition               | context1                                               | context2
        'are the same object'   | LogicExecutorContext.create(logicLocation, descriptor) | context1
        'have the same content' | LogicExecutorContext.create(logicLocation, descriptor) | LogicExecutorContext.create(logicLocation, descriptor)
    }

    @Test
    @Unroll
    def "two context hashCodes are different when context2 #condition"() {
        given:
        def context1 = LogicExecutorContext.create logicLocation, descriptor

        expect:
        context1.hashCode() != context2.hashCode()

        where:
        condition               | context2
        'has different content' | LogicExecutorContext.create(Stub(LogicLocation), Stub(TypeAttributeDescriptor))
        'is a different type'   | 2
    }
}
