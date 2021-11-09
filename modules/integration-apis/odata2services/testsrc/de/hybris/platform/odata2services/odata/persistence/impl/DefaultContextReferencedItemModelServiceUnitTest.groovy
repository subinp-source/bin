/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.CannotCreateReferencedItemException
import de.hybris.platform.inboundservices.persistence.ContextItemModelService
import de.hybris.platform.inboundservices.persistence.ItemModelPopulator
import de.hybris.platform.inboundservices.persistence.populator.ContextReferencedItemModelService
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import de.hybris.platform.integrationservices.search.ItemSearchService
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest
import de.hybris.platform.odata2services.odata.persistence.StorageRequest
import de.hybris.platform.odata2services.odata.persistence.creation.CreateItemStrategy
import de.hybris.platform.odata2services.odata.persistence.populator.EntityModelPopulator
import de.hybris.platform.odata2services.odata.persistence.validator.CreateItemValidator
import de.hybris.platform.servicelayer.model.ModelService
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultContextReferencedItemModelServiceUnitTest extends Specification {
    def entityModelPopulator = Mock EntityModelPopulator
    def itemModelPopulator = Mock ItemModelPopulator
    def itemSearchService = Stub ItemSearchService
    def modelService = Stub ModelService
    def createItemStrategy = Stub CreateItemStrategy
    def referencedItemModelService = Mock ContextReferencedItemModelService
    def modelEntityService = new DefaultModelEntityService()

    def setup() {
        modelEntityService.entityModelPopulator = entityModelPopulator
        modelEntityService.itemModelPopulator = itemModelPopulator
        modelEntityService.searchService = itemSearchService
        modelEntityService.modelService = modelService
        modelEntityService.createItemStrategy = createItemStrategy
        modelEntityService.createItemValidators = []
    }

    @Test
    def "deriveReferencedItemModel delegates to the referencedItemModelService when it is set"() {
        given: "the referencedItemModelService is set with the odata independent service"
        def typeAttributeDescriptor = typeAttributeDescriptor()
        def context = storageRequest()
        modelEntityService.setContextReferencedItemModelService(referencedItemModelService)

        when:
        modelEntityService.deriveReferencedItemModel(typeAttributeDescriptor, context)

        then:
        1 * referencedItemModelService.deriveReferencedItemModel(typeAttributeDescriptor, context)
    }

    @Test
    def "deriveReferencedItemModel throws exception when item cannot be created and no existing item found"() {
        given: 'canCreateReferencedItem >> false'
        def typeAttributeDescriptor = typeAttributeDescriptor(partof: false, autocreate: false)

        and: 'request does not contain an item in the context'
        def lookupRequest = Stub ItemLookupRequest
        def storageRequest = Stub(StorageRequest) {
            getContextItem() >> Optional.empty()
            toLookupRequest() >> lookupRequest
            isItemCanBeCreated() >> false
        }

        and: 'the item is not found'
        itemSearchService.findUniqueItem(lookupRequest) >> Optional.empty()

        when:
        modelEntityService.deriveReferencedItemModel(typeAttributeDescriptor, storageRequest)

        then:
        def e = thrown CannotCreateReferencedItemException
        e.attributeDescriptor == typeAttributeDescriptor
        e.persistenceContext == storageRequest
    }

    @Test
    def 'deriveReferencedItemModel prefers item found in the context to an item existing in the platform'() {
        given: 'canCreateReferencedItem >> false'
        def typeAttributeDescriptor = typeAttributeDescriptor(partof: false, autocreate: false)

        and: 'context contains an item'
        def contextItem = Stub(ItemModel)
        def context = storageRequest(contextItem)

        and: 'a matching item exists in the platform'
        def existingItem = Stub ItemModel

        itemSearchService.findUniqueItem(_ as ItemSearchRequest) >> Optional.of(existingItem)

        when:
        def result = modelEntityService.deriveReferencedItemModel(typeAttributeDescriptor, context)

        then:
        result == contextItem
        0 * context.putItem(_)
        0 * entityModelPopulator.populateItem(contextItem, _)
        0 * itemModelPopulator.populate(contextItem, context)
    }

    @Test
    def 'deriveReferencedItemModel returns an item existing in the platform when there is no item in the context'() {
        given: 'canCreateReferencedItem >> false'
        def typeAttributeDescriptor = typeAttributeDescriptor(partof: false, autocreate: false)

        and: 'context does not contain an item'
        def context = storageRequest(null)

        and: 'a matching item exists in the platform'
        def existingReferencedItem = Stub ItemModel
        itemSearchService.findUniqueItem(_ as ItemSearchRequest) >> Optional.of(existingReferencedItem)


        when:
        def result = modelEntityService.deriveReferencedItemModel(typeAttributeDescriptor, context)

        then:
        result == existingReferencedItem
        1 * context.putItem(existingReferencedItem)
        1 * entityModelPopulator.populateItem(existingReferencedItem, _)
        1 * itemModelPopulator.populate(existingReferencedItem, context)
    }

    @Test
    @Unroll
    def "deriveReferencedItemModel creates new item when item is not found and the attribute has #attr=true"() {
        given: 'item does not exist'
        def request = storageRequest(null)
        itemSearchService.findUniqueItem(_ as ItemSearchRequest) >> Optional.empty()
        and: "attribute has #attr=true"
        def attribute = typeAttributeDescriptor(partof: partOf, autocreate: autoCreate)
        and: 'create strategy creates an item'
        def item = Stub ItemModel
        createItemStrategy.createItem(request) >> item

        when:
        modelEntityService.deriveReferencedItemModel attribute, request

        then:
        1 * entityModelPopulator.populateItem(item, request)
        1 * itemModelPopulator.populate(item, request)
        1 * request.putItem(item)

        where:
        attr         | partOf | autoCreate
        'partOf'     | true   | false
        'autoCreate' | false  | true
    }

    @Test
    def 'deriveReferencedItemModel throws exception when context item retrieval throws exception'() {
        given: 'canCreateReferencedItem >> false'
        def typeAttributeDescriptor = typeAttributeDescriptor(partof: false, autocreate: false)

        and: 'context does not contain an item'
        def context = Stub(StorageRequest) {
            getContextItem() >> { throw new EdmException(EdmException.COMMON) }
        }

        when:
        modelEntityService.deriveReferencedItemModel(typeAttributeDescriptor, context)

        then:
        thrown InternalProcessingException
    }

    @Test
    def 'deriveReferencedItemModel throws exception when found item cannot be set in the context'() {
        given: 'canCreateReferencedItem >> false'
        def typeAttributeDescriptor = typeAttributeDescriptor(partof: false, autocreate: false)

        and: 'context does not contain an item'
        def context = storageRequest(null)

        context.putItem(_ as ItemModel) >> { throw new EdmException(EdmException.COMMON) }

        when:
        modelEntityService.deriveReferencedItemModel(typeAttributeDescriptor, context)

        then:
        thrown InternalProcessingException
    }

    @Test
    def 'deriveReferencedItemModel throws exception when lookup validator throws exception'() {
        given: 'canCreateReferencedItem >> false'
        def typeAttributeDescriptor = typeAttributeDescriptor(partof: false, autocreate: false)

        and:
        modelEntityService.createItemValidators = [failingLookupValidator()]

        when:
        modelEntityService.deriveReferencedItemModel(typeAttributeDescriptor, storageRequest())

        then:
        thrown InternalProcessingException
    }

    @Test
    def 'deriveReferencedItemModel throws exception when populate validator throws exception'() {
        given: 'attribute is autoCreate so the item will be created/populated'
        def attribute = typeAttributeDescriptor(autocreate: true)
        and: 'populate validator fails'
        modelEntityService.createItemValidators = [failingPopulateValidator()]

        when:
        modelEntityService.deriveReferencedItemModel attribute, storageRequest()

        then:
        thrown InternalProcessingException
        0 * entityModelPopulator._
        0 * itemModelPopulator._
    }

    @Test
    def 'deriveReferencedItemModel throws exception when a create validator throws exception'() {
        given: 'item does not exit'
        itemSearchService.findUniqueItem(_ as ItemSearchRequest) >> Optional.empty()
        and: 'the attribute is partOf'
        def attribute = typeAttributeDescriptor(partof: true)
        and: 'create validator fails'
        modelEntityService.createItemValidators = [failingCreateValidator()]

        when:
        modelEntityService.deriveReferencedItemModel attribute, storageRequest()

        then:
        thrown InternalProcessingException
        0 * entityModelPopulator._
        0 * itemModelPopulator._
    }

    @Test
    def "deriveItemsReferencedInAttributeValue delegates to the referencedItemModelService when it is set"() {
        given: "the referencedItemModelService is set with the odata independent service"
        def typeAttributeDescriptor = typeAttributeDescriptor()
        def context = storageRequest()
        modelEntityService.setContextReferencedItemModelService(referencedItemModelService)

        when:
        modelEntityService.deriveItemsReferencedInAttributeValue(context, typeAttributeDescriptor)

        then:
        1 * referencedItemModelService.deriveItemsReferencedInAttributeValue(context, typeAttributeDescriptor)
    }

    @Test
    def 'deriveItemsReferencedInAttributeValue returns empty collection when the attribute value in the payload is empty'() {
        given: 'an attribute descriptor'
        def descriptor = typeAttributeDescriptor()

        and: 'the persistence context has empty attribute value'
        def context = storageRequest()
        context.getReferencedContexts(descriptor) >> []

        expect:
        modelEntityService.deriveItemsReferencedInAttributeValue(context, descriptor).empty
    }

    @Test
    def "deriveItemsReferencedInAttributeValue throws exception when autocreate=false and partof=false and no existing item can be found"() {
        given: 'canCreateReferencedItem >> false'
        def descriptor = typeAttributeDescriptor(partof: false, autocreate: false)

        and: 'request does not contain an item in the context'
        def context = storageRequest(null)
        and: 'request contains a collection referring to an item that does not exist in the system'
        def subContext = Stub StorageRequest
        context.getReferencedContexts(descriptor) >> [subContext]
        itemSearchService.findUniqueItem(_ as ItemLookupRequest) >> Optional.empty()

        when:
        modelEntityService.deriveItemsReferencedInAttributeValue context, descriptor

        then:
        def e = thrown CannotCreateReferencedItemException
        e.attributeDescriptor == descriptor
        e.persistenceContext == subContext
    }

    @Test
    def 'deriveItemsReferencedInAttributeValue prefers items found in the context to an item existing in the platform'() {
        given: 'an attribute descriptor'
        def attributeDescriptor = typeAttributeDescriptor()

        and: 'context contains an item'
        def contextItem = Stub ItemModel
        def subContext = storageRequest(contextItem)
        def context = storageRequest()
        context.getReferencedContexts(attributeDescriptor) >> [subContext]

        and: 'a matching item exists in the platform'
        def existingItem = Stub ItemModel
        itemSearchService.findUniqueItem(_ as ItemSearchRequest) >> Optional.of(existingItem)

        when:
        def result = modelEntityService.deriveItemsReferencedInAttributeValue context, attributeDescriptor

        then:
        result == [contextItem]
        0 * subContext.putItem(_)
        0 * itemModelPopulator.populate(contextItem, subContext)
    }

    @Test
    def 'deriveItemsReferencedInAttributeValue returns an item existing in the platform when there is no item in the context'() {
        given: 'an attribute descriptor'
        def attribute = typeAttributeDescriptor()

        and: 'a context with referenced contexts, which do not contain items'
        def subContext1 = storageRequest(null)
        def subContext2 = storageRequest(null)
        def context = storageRequest()
        context.getReferencedContexts(attribute) >> [subContext1, subContext2]

        and: 'matching items exist in the persistent storage'
        def existingReferencedItem1 = Stub ItemModel
        itemSearchService.findUniqueItem(subContext1.toLookupRequest()) >> Optional.of(existingReferencedItem1)
        def existingReferencedItem2 = Stub ItemModel
        itemSearchService.findUniqueItem(subContext2.toLookupRequest()) >> Optional.of(existingReferencedItem2)

        when:
        def result = modelEntityService.deriveItemsReferencedInAttributeValue context, attribute

        then:
        result == [existingReferencedItem1, existingReferencedItem2]
        1 * subContext1.putItem(existingReferencedItem1)
        1 * subContext2.putItem(existingReferencedItem2)
        1 * itemModelPopulator.populate(existingReferencedItem1, subContext1)
        1 * itemModelPopulator.populate(existingReferencedItem2, subContext2)
    }

    @Test
    @Unroll
    def "deriveItemsReferencedInAttributeValue creates new item when item is not found and the attribute has #attr=true"() {
        given: "attribute has #attr=true"
        def attribute = typeAttributeDescriptor(partof: partOf, autocreate: autoCreate)

        and: 'items do not exist for the referenced sub contexts'
        def subContext1 = storageRequest(null)
        def subContext2 = storageRequest(null)
        def request = storageRequest()
        request.getReferencedContexts(attribute) >> [subContext1, subContext2]
        itemSearchService.findUniqueItem(_ as ItemSearchRequest) >> Optional.empty()

        and: 'create strategy creates items when called'
        def item1 = Stub ItemModel
        createItemStrategy.createItem(subContext1) >> item1
        def item2 = Stub ItemModel
        createItemStrategy.createItem(subContext2) >> item2

        when:
        def res = modelEntityService.deriveItemsReferencedInAttributeValue request, attribute

        then:
        res == [item1, item2]
        1 * subContext1.putItem(item1)
        1 * subContext2.putItem(item2)
        1 * itemModelPopulator.populate(item1, subContext1)
        1 * itemModelPopulator.populate(item2, subContext2)

        where:
        attr         | partOf | autoCreate
        'partOf'     | true   | false
        'autoCreate' | false  | true
    }

    @Test
    def 'deriveItemsReferencedInAttributeValue throws exception when context item retrieval throws exception'() {
        given: 'an attribute descriptor'
        def attribute = typeAttributeDescriptor()

        and: 'one of the subcontexts throws exception when context item checked'
        def subContextWithItem = storageRequest Stub(ItemModel)
        def failingSubContext = Stub(StorageRequest) {
            getContextItem() >> { throw new EdmException(EdmException.COMMON) }
        }
        def context = storageRequest()
        context.getReferencedContexts(attribute) >> [subContextWithItem, failingSubContext]

        when:
        modelEntityService.deriveItemsReferencedInAttributeValue context, attribute

        then:
        thrown InternalProcessingException
    }

    @Test
    def 'deriveItemsReferencedInAttributeValue throws exception when found item cannot be set in the context'() {
        given: 'an attribute descriptor'
        def descriptor = typeAttributeDescriptor()

        and: 'one of the sub-contexts does not contain an item and fails to set it'
        def subContext1 = storageRequest Stub(ItemModel)
        def subContext2 = storageRequest(null)
        subContext2.putItem(_ as ItemModel) >> { throw new EdmException(EdmException.COMMON) }
        def context = storageRequest()
        context.getReferencedContexts(descriptor) >> [subContext1, subContext2]

        when:
        modelEntityService.deriveItemsReferencedInAttributeValue context, descriptor

        then:
        thrown InternalProcessingException
    }

    private CreateItemValidator failingLookupValidator() {
        Stub(CreateItemValidator) {
            beforeItemLookup(_ as EdmEntityType, _ as ODataEntry) >> { throw new EdmException(EdmException.COMMON) }
        }
    }

    private CreateItemValidator failingCreateValidator() {
        Stub(CreateItemValidator) {
            beforeCreateItem(_, _) >> { throw new EdmException(EdmException.COMMON) }
        }
    }

    private CreateItemValidator failingPopulateValidator() {
        Stub(CreateItemValidator) {
            beforePopulateItem(_, _) >> { throw new EdmException(EdmException.COMMON) }
        }
    }

    def typeAttributeDescriptor(Map<String, Boolean> params = [:]) {
        Stub(TypeAttributeDescriptor) {
            isAutoCreate() >> (params['autocreate'] ?: false)
            isPartOf() >> (params['partof'] ?: false)
        }
    }

    private StorageRequest storageRequest(ItemModel itemModel = null) {
        Mock(StorageRequest) {
            getContextItem() >> Optional.ofNullable(itemModel)
            toLookupRequest() >> Stub(ItemLookupRequest)
            isItemCanBeCreated() >> true
        }
    }
}
