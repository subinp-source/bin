/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import org.apache.commons.collections.map.UnmodifiableMap
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class MapAttributePopulatorUnitTest extends Specification {
    def populator = new MapAttributePopulator()
    def attributeAccessor = Mock AttributeValueAccessor
    def item = Stub ItemModel

    @Test
    @Unroll
    def "attribute is not set when #condition and replace attributes is #replaceAttributes"() {
        given: "replace attribute is #replaceAttributes"
        def attribute = Stub(TypeAttributeDescriptor) {
            isLocalized() >> isLocalized
            isMap() >> isMap
        }
        def context = Stub(PersistenceContext) {
            isReplaceAttributes() >> replaceAttributes
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> [attribute]
                getAttribute(attribute) >> [:]
            }
        }

        when:
        populator.populate item, context

        then:
        0 * attributeAccessor.setValue(item, _)

        where:
        condition               | replaceAttributes | isLocalized | isMap
        'localized map'         | false             | true        | true
        'not map'               | false             | false       | false
        'map and not localized' | true              | false       | true
    }

    @Unroll
    @Test
    def "setValue() is invoked #invocations time/s when #condition"() {
        given: "replace attribute is #replaceAttributes"
        def attribute = mapDescriptor()
        def context = Stub(PersistenceContext) {
            isReplaceAttributes() >> replaceAttributes
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> [attribute]
                getAttribute(attribute) >> [:]
            }
        }

        when:
        populator.populate item, context

        then:
        invocations * attributeAccessor.setValue(item, _)

        where:
        condition            | invocations | replaceAttributes
        'replace attributes' | 0           | true
        'append attributes'  | 1           | false
    }

    @Test
    def "populate ignores un-settable attribute"() {
        given: 'attribute is not settable'
        def attribute = Stub(TypeAttributeDescriptor) {
            isSettable(_) >> false
            isMap() >> true
            isLocalized() >> false
        }
        and:
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> [attribute]
            }
        }

        when:
        populator.populate item, context

        then:
        0 * attributeAccessor.setValue(item, _)
    }

    @Unroll
    @Test
    def "populate does not set values when value is #condition"() {
        given:
        def attribute = mapDescriptor()
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> [attribute]
                getAttribute(attribute) >> value
            }
        }

        when:
        populator.populate item, context

        then:
        0 * attributeAccessor.setValue(item, _)

        where:
        condition   | value
        'null'      | null
        'not a map' | 'string value'
    }

    @Test
    def "populate does not update values when new value is empty map"() {
        given:
        def attribute = mapDescriptor()
        def existingValues = [1: 'a', 2: 'b']
        attributeAccessor.getValue(item) >> existingValues
        and: 'new value is empty map'
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> [attribute]
                getAttribute(attribute) >> [:]
            }
        }

        when:
        populator.populate item, context

        then:
        1 * attributeAccessor.setValue(item, existingValues)
    }

    @Test
    def "populate replaces values already existing in map"() {
        given:
        def attribute = mapDescriptor()
        attributeAccessor.getValue(item) >> [a: 1]
        and: 'existing value is overridden'
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> [attribute]
                getAttribute(attribute) >> [a: 2]
            }
        }

        when:
        populator.populate item, context

        then:
        1 * attributeAccessor.setValue(item, [a: 2])
    }

    @Test
    def "populate appends to existing values in map"() {
        given:
        def attribute = mapDescriptor()
        attributeAccessor.getValue(item) >> UnmodifiableMap.of('a', 1)
        and:
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> [attribute]
                getAttribute(attribute) >> [b: 2]
            }
        }

        when:
        populator.populate item, context

        then:
        1 * attributeAccessor.setValue(this.item, [a: 1, b: 2])
    }

    @Test
    def "populate sets new map value when existing value is null"() {
        given:
        def attribute = mapDescriptor()
        attributeAccessor.getValue(item) >> null
        and:
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> [attribute]
                getAttribute(attribute) >> [a: 1]
            }
        }

        when:
        populator.populate item, context

        then:
        1 * attributeAccessor.setValue(item, [a: 1])
    }

    private TypeAttributeDescriptor mapDescriptor() {
        Stub(TypeAttributeDescriptor) {
            isLocalized() >> false
            isMap() >> true
            isSettable(_) >> true
            accessor() >> attributeAccessor
        }
    }
}
