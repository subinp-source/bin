/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.persistence.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.inboundservices.persistence.populator.UnmodifiableAttributeException
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.CollectionDescriptor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ReplaceItemModelCollectionAttributePopulatorUnitTest extends Specification {
    @Shared
    def payloadItem1 = Stub ItemModel
    @Shared
    def payloadItem2 = Stub ItemModel

    def attributeValueAccessor = Mock AttributeValueAccessor
    def itemModelService = Stub de.hybris.platform.inboundservices.persistence.populator.ContextReferencedItemModelService
    def attributePopulator = new ReplaceItemModelCollectionAttributePopulator(
            contextReferencedItemModelService: itemModelService)

    @Test
    @Unroll
    def "populate() ignores #attrDesc attribute"() {
        given: "an attribute: collection=#collection, primitive=#primitive"
        def inapplicableAttribute = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> primitive
            isCollection() >> collection
            accessor() >> attributeValueAccessor
            isSettable(_) >> true
        }

        and: 'persistence context that has some other attribute in the payload but not the applicable attribute'
        def context = persistenceContext inapplicableAttribute

        when:
        attributePopulator.populate Stub(ItemModel), context

        then:
        0 * attributeValueAccessor.setValue(_, _)

        where:
        attrDesc                | collection | primitive
        'primitive collection'  | true       | true
        'single primitive'      | false      | true
        'single item reference' | false      | false
    }

    @Test
    def 'populate() ignores an applicable attribute when persistence context is not for replacing attributes'() {
        given: 'an applicable attribute descriptor'
        def attributeDescriptor = applicableAttributeDescriptor()
        and: 'a persistence context that does not request attribute replacement'
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> [attributeDescriptor]
            }
            isReplaceAttributes() >> false
        }

        when:
        attributePopulator.populate Stub(ItemModel), context

        then:
        0 * attributeValueAccessor.setValue(_, _)
    }

    @Test
    def 'populate() ignores attribute value for an existing item when the attribute is not settable'() {
        given: 'an applicable but not settable attribute'
        def descriptor = Stub(TypeAttributeDescriptor) {
            isCollection() >> true
            isPrimitive() >> false
            accessor() >> attributeValueAccessor
            isSettable(_) >> false
        }

        and: 'a persistence context that resolves to new items in the payload body'
        def context = persistenceContext(descriptor)
        itemModelService.deriveItemsReferencedInAttributeValue(context, descriptor) >> [Stub(ItemModel)]
        def item = Stub ItemModel

        when:
        attributePopulator.populate item, context

        then:
        0 * attributeValueAccessor.setValue(item, _)
    }

    @Test
    @Unroll
    def "populate() resets item attribute values to #colType from the payload"() {
        given: 'an attribute '
        def descriptor = applicableAttributeDescriptor()
        descriptor.getCollectionDescriptor() >> Stub(CollectionDescriptor) {
            newCollection() >> collection
        }

        and: 'the persistence context that resolves to two new items in the payload body'
        def context = persistenceContext(descriptor)
        context.integrationItem.getAttribute(descriptor) >> payloadItems
        itemModelService.deriveItemsReferencedInAttributeValue(context, descriptor) >> resolvedItems

        and:
        def item = Stub ItemModel

        when:
        attributePopulator.populate item, context

        then:
        1 * attributeValueAccessor.setValue(item, newValue)

        where:
        colType         | collection | payloadItems                           | resolvedItems                | newValue
        'list of items' | []         | [integrationItem(), integrationItem()] | [payloadItem1, payloadItem2] | [payloadItem1, payloadItem2]
        'set of items'  | [] as Set  | [integrationItem(), integrationItem()] | [payloadItem1, payloadItem2] | [payloadItem1, payloadItem2] as Set
        'empty list'    | []         | []                                     | []                           | []
        'empty set'     | [] as Set  | []                                     | []                           | [] as Set
        'null'          | []         | null                                   | []                           | null
    }

    @Test
    def 'populate() throws exception when setting an attribute'() {
        given: 'an applicable attribute'
        def descriptor = applicableAttributeDescriptor()

        and: 'a persistence context that resolves the payload value to items'
        def context = persistenceContext(descriptor)
        itemModelService.deriveItemsReferencedInAttributeValue(context, descriptor) >> [Stub(ItemModel)]

        and: 'the payload item does not exist in the persistent storage'
        def item = Stub ItemModel

        and: 'attribute value cannot be set'
        attributeValueAccessor.setValue(item, _) >> {
            throw new AttributeNotSupportedException('IGNORE - testing exception', 'items')
        }

        when:
        attributePopulator.populate item, context

        then:
        def e = thrown UnmodifiableAttributeException
        e.attributeDescriptor == descriptor
        e.persistenceContext == context
    }

    @Test
    def 'populate sets all applicable attributes'() {
        given: 'an applicable attribute'
        def attributeOne = applicableAttributeDescriptor()

        and: 'another applicable attribute'
        def valueAccessor = Mock AttributeValueAccessor
        def attributeTwo = applicableAttributeDescriptor(valueAccessor)

        and: 'a context for the item to populate'
        def item = Stub ItemModel
        def context = persistenceContext([attributeOne, attributeTwo])

        and: 'the context resolves items for each of the attributes'
        def item1 = Stub ItemModel
        def item2 = Stub ItemModel
        def attrOneContext = Stub PersistenceContext
        def attrTwoContext = Stub PersistenceContext
        context.getReferencedContexts(attributeOne) >> [attrOneContext]
        context.getReferencedContexts(attributeTwo) >> [attrTwoContext]
        itemModelService.deriveItemsReferencedInAttributeValue(context, attributeOne) >> [item1]
        itemModelService.deriveItemsReferencedInAttributeValue(context, attributeTwo) >> [item2]

        when:
        attributePopulator.populate item, context

        then:
        1 * attributeValueAccessor.setValue(item, [item1])
        1 * valueAccessor.setValue(item, [item2])
    }

    PersistenceContext persistenceContext(TypeAttributeDescriptor descriptor) {
        persistenceContext([descriptor])
    }

    PersistenceContext persistenceContext(Collection<TypeAttributeDescriptor> descriptors) {
        Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> descriptors
            }
            isReplaceAttributes() >> true
        }
    }

    TypeAttributeDescriptor applicableAttributeDescriptor(AttributeValueAccessor valueAccessor = attributeValueAccessor) {
        Stub(TypeAttributeDescriptor) {
            isCollection() >> true
            accessor() >> valueAccessor
            isSettable(_) >> true
        }
    }

    IntegrationItem integrationItem() {
        Stub(IntegrationItem)
    }
}
