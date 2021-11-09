/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.inboundservices.persistence.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ReplaceMapAttributePopulatorTest extends Specification {

    def item = Stub ItemModel
    def valueAccessor = Mock AttributeValueAccessor
    def populator = new ReplaceMapAttributePopulator()

    @Test
    @Unroll
    def "populator does not set value when #condition"() {
        given:
        def attribute = Stub(TypeAttributeDescriptor) {
            isMap() >> map
            isLocalized() >> localized
            isSettable(item) >> settable
        }

        def context = Stub(PersistenceContext) {
            isReplaceAttributes() >> replace
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> [attribute]
            }
        }

        when:
        populator.populate item, context

        then:
        0 * valueAccessor.setValue(item, _)

        where:
        condition                   | map   | localized | settable | replace
        'attribute is not a map'    | false | false     | true     | true
        'attribute is localized'    | true  | true      | true     | true
        'attribute is not settable' | true  | false     | false    | true
        'context is not replace'    | true  | false     | true     | false
    }

    @Test
    @Unroll
    def "populator sets map attribute #count times when the value is #condition"() {
        given: 'an attribute'
        def attribute = Stub(TypeAttributeDescriptor) {
            accessor() >> valueAccessor
            isMap() >> true
            isLocalized() >> false
            isSettable(item) >> true
        }

        and: 'context providing the attribute value'
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(attribute) >> map
                getAttributes() >> [attribute]
            }
            isReplaceAttributes() >> true
        }

        when:
        populator.populate item, context

        then:
        count * valueAccessor.setValue(item, map)

        where:
        condition   | map  | count
        'a map'     | [:]  | 1
        'not a map' | []   | 0
        'null'      | null | 0
    }

    @Test
    def "map entry must contain a key when populating a map attribute"() {
        given: 'an attribute'
        def descriptor = Stub(TypeDescriptor) {
            getTypeCode() >> 'FailingTypeCode'
            getItemCode() >> 'FailingItemCode'
        }
        def attribute = Stub(TypeAttributeDescriptor) {
            accessor() >> valueAccessor
            isMap() >> true
            isLocalized() >> false
            isSettable(item) >> true
            getAttributeType() >> descriptor
            getTypeDescriptor() >> descriptor
            getAttributeName() >> 'FailingAttributeName'
        }

        and: 'context providing the attribute value'
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(attribute) >> [(null):'value']
                getAttributes() >> [attribute]
            }
            isReplaceAttributes() >> true
        }

        when:
        populator.populate item, context

        then:
        def e = thrown MissingRequiredMapKeyValueException
        e.message == 'Property [FailingAttributeName.key] of type FailingTypeCode is required for EntityType [FailingItemCode].'
    }
}
