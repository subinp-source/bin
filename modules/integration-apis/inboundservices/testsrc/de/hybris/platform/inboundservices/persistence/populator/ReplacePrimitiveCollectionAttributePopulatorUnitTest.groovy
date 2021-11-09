/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.inboundservices.persistence.PrimitiveValueHandler
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.AttributeValueSetter
import de.hybris.platform.integrationservices.model.CollectionDescriptor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ReplacePrimitiveCollectionAttributePopulatorUnitTest extends Specification {
    def attributeValueAccessor = Mock AttributeValueAccessor
    def populator = new ReplacePrimitiveCollectionAttributePopulator()

    @Test
    @Unroll
    def "populate() ignores an attribute that is #condition"() {
        given: 'a non-applicable attribute'
        def nonApplicable = Stub(TypeAttributeDescriptor) {
            isCollection() >> collection
            isPrimitive() >> primitive
            accessor() >> attributeValueAccessor
            isSettable(_) >> true
        }
        and: 'a context that has a primitive collection for the attribute value in the payload'
        def item = Stub(ItemModel)
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(nonApplicable) >> ['one', 'two']
                getAttributes() >> [nonApplicable]
            }
        }

        when:
        populator.populate item, context

        then:
        0 * attributeValueAccessor.setValue(_, _)

        where:
        condition                      | collection | primitive | applicable
        'collection of non-primitives' | true       | false     | false
        'singular primitive'           | false      | true      | false
        'singular non-primitive'       | false      | false     | false
    }

    @Test
    def 'populate() ignores the attribute when persistence context does not specify attribute replacement'() {
        given: 'an primitive collection attribute'
        def descriptor = applicableAttribute()
        and: 'a persistence context that contains primitive collection in the payload but does not specify attribute replacement'
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(descriptor) >> [1, 2, 3]
                getAttributes() >> [descriptor]
            }
            isReplaceAttributes() >> false
        }

        when:
        populator.populate Stub(ItemModel), context

        then:
        0 * attributeValueAccessor.setValue(_, _)
    }

    @Test
    def 'populate() ignores a non-settable attribute for an existing item update'() {
        given: 'an primitive collection attribute'
        def descriptor = Stub(TypeAttributeDescriptor) {
            isCollection() >> true
            isPrimitive() >> true
            accessor() >> attributeValueAccessor
            isSettable(_) >> false
        }

        and: 'a persistence context that contains primitive collection in the payload but does not specify attribute replacement'
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(descriptor) >> [1, 2, 3]
                getAttributes() >> [descriptor]
            }
            isReplaceAttributes() >> false
        }
        and:
        def item = Stub ItemModel

        when:
        populator.populate item, context

        then:
        0 * attributeValueAccessor.setValue(_, _)
    }

    @Test
    @Unroll
    def "populate() reset existing item attribute values with new #colType"() {
        given: "an applicable attribute of #colType type"
        def descriptor = applicableAttribute()
        descriptor.getCollectionDescriptor() >> Stub(CollectionDescriptor) {
            newCollection() >> collection
        }

        and: 'a persistence context with new values for the attribute'
        def context = contextWithAttributeValues descriptor, newValues

        and:
        def item = Stub(ItemModel)

        when:
        populator.populate item, context

        then:
        1 * attributeValueAccessor.setValue(item, values)

        where:
        colType           | collection | newValues             | values
        'list of values'  | []         | [1, 2, 3]             | [1, 2, 3]
        'set of values'   | [] as Set  | [1, 2]                | [1, 2] as Set
        'empty list'      | []         | []                    | []
        'empty set'       | [] as Set  | []                    | [] as Set
        'null value'      | []         | null                  | null
        'list with nulls' | []         | ['a', null, 'b', 'c'] | ['a', 'b', 'c']
    }

    @Test
    def 'populate throws exception when setting an attribute'() {
        given: 'an applicable attribute descriptor'
        def descriptor = applicableAttribute()
        and: 'a persistence context containing new values in the paylaod'
        def context = contextWithAttributeValues descriptor, [1, 2]
        and: 'the attribute is not settable in the item'
        def item = Stub ItemModel
        attributeValueAccessor.setValue(item, [1, 2]) >> {
            throw new AttributeNotSupportedException('IGNORE - testing exception', descriptor.qualifier)
        }

        when:
        populator.populate item, context

        then:
        def e = thrown UnmodifiableAttributeException
        e.attributeDescriptor == descriptor
        e.persistenceContext == context
    }

    @Test
    def 'populate sets all applicable attributes'() {
        given: 'an applicable attribute'
        def attributeOne = applicableAttribute()
        and: 'another applicable attribute'
        def valueAccessor2 = Mock AttributeValueAccessor
        def attributeTwo = applicableAttribute(valueAccessor2)
        and: 'a context for the item to populate'
        def item = Stub(ItemModel)
        def context = contextWithAttributeValues([(attributeOne): [1, 2], (attributeTwo): ['a', 'b']])

        when:
        populator.populate item, context

        then:
        1 * attributeValueAccessor.setValue(item, [1, 2])
        1 * valueAccessor2.setValue(item, ['a', 'b'])
    }

    @Test
    def "replace converts each collection value before they are set"() {
        given:
        def item = Stub ItemModel
        and:
        def descriptor = applicableAttribute()
        and:
        def values = [1, 2, 3]
        def context = Stub(PersistenceContext) {
            isReplaceAttributes() >> true
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(descriptor) >> values
                getAttributes() >> [descriptor]
            }
        }
        and: 'each value is converted to string'
        def handler = Mock(PrimitiveValueHandler) {
            convert(descriptor, _) >> { _, value -> value as String }
        }
        and:
        populator.setPrimitiveValueHandler(handler)

        when:
        populator.populate item, context

        then: 'converted values are set on item'
        1 * attributeValueAccessor.setValue(item, ['1', '2', '3'])
    }

    TypeAttributeDescriptor applicableAttribute(AttributeValueSetter valueAccessor = attributeValueAccessor) {
        Stub(TypeAttributeDescriptor) {
            isCollection() >> true
            isPrimitive() >> true
            accessor() >> valueAccessor
            isSettable(_) >> true
        }
    }

    PersistenceContext contextWithAttributeValues(TypeAttributeDescriptor attribute, def value) {
        contextWithAttributeValues([(attribute): value])
    }

    PersistenceContext contextWithAttributeValues(Map<TypeAttributeDescriptor, ?> attributes) {
        def item = Stub(IntegrationItem) {
            getAttributes() >> attributes.keySet()
        }
        attributes.entrySet().each { item.getAttribute(it.getKey()) >> it.getValue() }
        Stub(PersistenceContext) {
            getIntegrationItem() >> item
            isReplaceAttributes() >> true
        }
    }
}
