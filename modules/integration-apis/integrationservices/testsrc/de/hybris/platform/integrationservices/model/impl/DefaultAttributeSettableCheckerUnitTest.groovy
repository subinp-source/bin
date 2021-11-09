/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.SettableAttributeHandler
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.servicelayer.type.TypeService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultAttributeSettableCheckerUnitTest extends Specification {

    def typeService = Stub TypeService
    def settableHandler1 = Mock(SettableAttributeHandler)
    def settableHandler2 = Mock(SettableAttributeHandler)
    def settableHandler3 = Mock(SettableAttributeHandler)
    def settableHandlers = [settableHandler1, settableHandler2, settableHandler3]
    def checker = new DefaultAttributeSettableChecker(typeService)

    def setup() {
        checker.settableHandlers = settableHandlers
    }

    @Test
    @Unroll
    def "Test isApplicable filtering"() {
        given:
        def itemType = 'String'
        def item = Stub(ItemModel) {
            getItemtype() >> itemType
        }

        and:
        def qualifier = 'Attribute'
        def attribute = Stub(TypeAttributeDescriptor) {
            getQualifier() >> qualifier
        }

        and:
        settableHandler1.isApplicable(item) >> true
        settableHandler2.isApplicable(item) >> false
        settableHandler3.isApplicable(item) >> true

        when:
        checker.isSettable(item, attribute)

        then:
        1 * settableHandler1.isSettable(_) >> true
        0 * settableHandler2.isSettable(_)
        1 * settableHandler3.isSettable(_) >> true
    }

    @Test
    @Unroll
    def "Test for unanimous true result from handler isSettable methods"() {
        given:
        def itemType = 'String'
        def item = Stub(ItemModel) {
            getItemtype() >> itemType
        }

        and:
        def qualifier = 'Attribute'
        def attribute = Stub(TypeAttributeDescriptor) {
            getQualifier() >> qualifier
        }

        and:
        settableHandler1.isApplicable(_) >> true
        settableHandler2.isApplicable(_) >> true
        settableHandler3.isApplicable(_) >> true

        and:
        settableHandler1.isSettable(_) >> handler1
        settableHandler2.isSettable(_) >> handler2
        settableHandler3.isSettable(_) >> handler3

        expect:
        checker.isSettable(item, attribute) == result

        where:
        handler1 | handler2 | handler3 | result
        true     | true     | true     | true
        true     | true     | false    | false
        true     | false    | true     | false
        true     | false    | false    | false
        false    | true     | true     | false
        false    | true     | false    | false
        false    | false    | true     | false
        false    | false    | false    | false
    }
}
