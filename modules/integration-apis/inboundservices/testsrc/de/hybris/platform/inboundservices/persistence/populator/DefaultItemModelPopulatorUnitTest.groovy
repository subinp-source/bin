/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.AttributePopulator
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultItemModelPopulatorUnitTest extends Specification {
    def itemPopulator = new DefaultItemModelPopulator()

    @Test
    @Unroll
    def "populate() throws exception when #condition is null"() {
        when:
        itemPopulator.populate item, context

        then:
        thrown IllegalArgumentException

        where:
        condition             | item            | context
        'item model'          | null            | Stub(PersistenceContext)
        'persistence context' | Stub(ItemModel) | null
    }

    @Test
    def 'invokes property populators in the order they are configured'() {
        given:
        def populator1 = Mock(AttributePopulator)
        def populator2 = Mock(AttributePopulator)
        def populator3 = Mock(AttributePopulator)
        itemPopulator.attributePopulators = [populator1, populator2, populator3]
        and:
        def item = Stub ItemModel
        def context = Stub PersistenceContext

        when:
        itemPopulator.populate item, context

        then:
        1 * populator1.populate(item, context)
        then:
        1 * populator2.populate(item, context)
        then:
        1 * populator3.populate(item, context)
    }

    @Test
    def 'setPropertyPopulators() handles null'() {
        given:
        itemPopulator.attributePopulators = null

        when:
        itemPopulator.populate Stub(ItemModel), Stub(PersistenceContext)

        then:
        noExceptionThrown()
    }
}
