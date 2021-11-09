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
import de.hybris.platform.integrationservices.model.CollectionDescriptor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class PrimitiveCollectionAttributePopulatorUnitTest extends Specification {
    def attributeValueAccessor = Mock AttributeValueAccessor
    def populator = new PrimitiveCollectionAttributePopulator()

    @Test
    @Unroll
    def "isApplicable is #applicable when attribute is a #condition"() {
        given:
        def descriptor = Stub(TypeAttributeDescriptor) {
            isCollection() >> collection
            isPrimitive() >> primitive
        }

        expect:
        populator.isApplicable(descriptor, Stub(PersistenceContext)) == applicable

        where:
        condition                      | collection | primitive | applicable
        'collection of primitives'     | true       | true      | true
        'collection of non-primitives' | true       | false     | false
        'singular primitive'           | false      | true      | false
        'singular non-primitive'       | false      | false     | false
    }

    @Test
    def 'isApplicable is false when context specifies attribute replacement'() {
        given: 'an primitive collection attribute'
        def descriptor = Stub(TypeAttributeDescriptor) {
            isCollection() >> true
            isPrimitive() >> true
        }

        and: 'a persistence context that specifies attribute replacement'
        def context = Stub(PersistenceContext) {
            isReplaceAttributes() >> true
        }

        expect:
        !populator.isApplicable(descriptor, context)
    }

    @Test
    @Unroll
    def "populate() combines existing values #existingValues and new values #newValues to produce #combinedValues"() {
        given:
        def item = Stub(ItemModel)

        and:
        def descriptor = applicableAttribute()
        descriptor.getCollectionDescriptor() >> Stub(CollectionDescriptor) {
            newCollection() >> collection
        }

        and:
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(descriptor) >> newValues
                getAttributes() >> [descriptor]
            }
        }

        and:
        attributeValueAccessor.getValue(item) >> existingValues

        when:
        populator.populate item, context

        then:
        1 * attributeValueAccessor.setValue(item, combinedValues)

        where:
        collection | existingValues   | newValues        | combinedValues
        []         | []               | []               | []
        [] as Set  | [1, 2]           | []               | [1, 2] as Set
        null       | []               | [1, 2, 3]        | [1, 2, 3]
        []         | [1, 2, 3, 5]     | [2, 3, 4] as Set | [1, 2, 3, 5, 4]
        [] as Set  | [2, 3, 4] as Set | [1, 2, 3, 5]     | [2, 3, 4, 1, 5] as Set
        null       | new Object()     | [1, 2, 3]        | [1, 2, 3]
        null       | [1, 2, 3]        | new Object()     | [1, 2, 3]
        []         | [1, 2]           | [null, 3, null]  | [1, 2, 3]
    }

    @Test
    def 'populate throws exception when setting a non settable attribute'() {
        given:
        def item = Stub ItemModel
        def descriptor = applicableAttribute()
        and:
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(descriptor) >> [1, 2]
                getAttributes() >> [descriptor]
            }
        }
        and: 'the attribute is not settable'
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
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(attributeOne) >> [1, 2]
                getAttribute(attributeTwo) >> ['a', 'b']
                getAttributes() >> [attributeOne, attributeTwo]
            }
        }

        when:
        populator.populate item, context

        then:
        1 * attributeValueAccessor.setValue(item, [1, 2])
        1 * valueAccessor2.setValue(item, ['a', 'b'])
    }

    @Test
    def 'populate ignores not applicable attributes'() {
        given: 'a non-applicable attribute'
        def nonApplicable = Stub(TypeAttributeDescriptor)
        and: 'a context for the item to populate'
        def item = Stub(ItemModel)
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(nonApplicable) >> Stub(ItemModel)
                getAttributes() >> [nonApplicable]
            }
        }

        when:
        populator.populate item, context

        then:
        0 * attributeValueAccessor.setValue(item, _)
    }


    @Test
    @Unroll
    def "populate #action #attrKind attribute for an item"() {
        given: 'a non-applicable attribute'
        def keyAttr = Stub(TypeAttributeDescriptor) {
            isCollection() >> true
            isPrimitive() >> true
            accessor() >> attributeValueAccessor
            isSettable(_) >> settable
        }
        and:
        def item = Stub(ItemModel)

        and: 'a context for the item to populate'
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(keyAttr) >> ['123', 'abc']
                getAttributes() >> [keyAttr]
            }
        }

        when:
        populator.populate item, context

        then:
        cnt * attributeValueAccessor.setValue(item, _)

        where:
        action    | attrKind    | settable | cnt
        'sets'    | 'writable'  | true     | 1
        'ignores' | 'read-only' | false    | 0
    }

    @Test
    def "populate converts each collection value before they are set"() {
        given:
        def item = Stub ItemModel
        and:
        def descriptor = applicableAttribute()
        and:
        def values = [1, 2, 3]
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(descriptor) >> values
                getAttributes() >> [descriptor]
            }
        }
        and: 'handler converts each value to string'
        def handler = Stub(PrimitiveValueHandler) {
            convert(descriptor, _) >> { d, value -> value as String }
        }
        and:
        populator.setPrimitiveValueHandler(handler)

        when:
        populator.populate item, context

        then: 'converted values are set on item'
        1 * attributeValueAccessor.setValue(item, ['1', '2', '3'])
    }

    private TypeAttributeDescriptor applicableAttribute(AttributeValueAccessor a = attributeValueAccessor) {
        Stub(TypeAttributeDescriptor) {
            isCollection() >> true
            isPrimitive() >> true
            accessor() >> a
            isSettable(_) >> true
        }
    }
}
