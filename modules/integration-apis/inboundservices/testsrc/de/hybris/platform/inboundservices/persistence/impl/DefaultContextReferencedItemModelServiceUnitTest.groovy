/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.CannotCreateReferencedItemException
import de.hybris.platform.inboundservices.persistence.ContextItemModelService
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultContextReferencedItemModelServiceUnitTest extends Specification {
    def itemModelService = Stub ContextItemModelService
    def referencedItemModelService = new DefaultContextReferencedItemModelService(contextItemModelService: itemModelService)
    def attributeName = "someName"
    def itemCode = "someItemCode"
    def typeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
        getAttributeName() >> attributeName
        getTypeDescriptor() >> Stub(TypeDescriptor) {
            getItemCode() >> itemCode
        }
    }

    @Test
    def "deriveReferencedItemModel throws exception when contextItemModelService cannot find or create an item"() {
        given:
        def storageRequest = Stub(PersistenceContext)

        and: 'the item is not found'
        itemModelService.findOrCreateItem(storageRequest) >> null

        when:
        referencedItemModelService.deriveReferencedItemModel(typeAttributeDescriptor, storageRequest)

        then:
        def e = thrown CannotCreateReferencedItemException
        e.attributeDescriptor == typeAttributeDescriptor
        e.persistenceContext == storageRequest
        e.message.contains(attributeName)
        e.message.contains(itemCode)
    }

    @Test
    def "deriveItemsReferencedInAttributeValue throws exception when contextItemModelService cannot find or create an item for an innerContext"() {
        given:
        def innerContext = Stub(PersistenceContext)
        def context = Stub(PersistenceContext) {
            getReferencedContexts(typeAttributeDescriptor) >> [innerContext]
        }

        and: 'the item is not found'
        itemModelService.findOrCreateItem(innerContext) >> null

        when:
        referencedItemModelService.deriveItemsReferencedInAttributeValue(context, typeAttributeDescriptor)

        then:
        def e = thrown CannotCreateReferencedItemException
        e.attributeDescriptor == typeAttributeDescriptor
        e.persistenceContext == innerContext
        e.message.contains(attributeName)
        e.message.contains(itemCode)
    }

    @Test
    def "deriveItemsReferencedInAttributeValue returns an empty list when outerContext has no referenced contexts for the attribute"() {
        given:
        def context = Stub(PersistenceContext) {
            getReferencedContexts(typeAttributeDescriptor) >> []
        }

        expect:
        referencedItemModelService.deriveItemsReferencedInAttributeValue(context, typeAttributeDescriptor) == []
    }

    @Test
    def "deriveReferencedItemModel returns the same item found by contextItemModelService"() {
        given:
        def context = Stub(PersistenceContext)

        and: 'an item is found for both innerContexts'
        def item = Stub(ItemModel)
        itemModelService.findOrCreateItem(context) >> item

        expect:
        referencedItemModelService.deriveReferencedItemModel(typeAttributeDescriptor, context) == item
    }

    @Test
    def "deriveItemsReferencedInAttributeValue returns list of the items found by contextItemModelService"() {
        given:
        def innerContext1 = Stub(PersistenceContext)
        def innerContext2 = Stub(PersistenceContext)
        def context = Stub(PersistenceContext) {
            getReferencedContexts(typeAttributeDescriptor) >> [innerContext1, innerContext2]
        }

        and: 'an item is found for both innerContexts'
        def item1 = Stub(ItemModel)
        def item2 = Stub(ItemModel)
        itemModelService.findOrCreateItem(innerContext1) >> item1
        itemModelService.findOrCreateItem(innerContext2) >> item2

        expect:
        referencedItemModelService.deriveItemsReferencedInAttributeValue(context, typeAttributeDescriptor) == [item1, item2]
    }
}
