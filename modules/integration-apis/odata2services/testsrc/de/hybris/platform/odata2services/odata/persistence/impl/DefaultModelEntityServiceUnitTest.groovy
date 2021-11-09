/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.ContextItemModelService
import de.hybris.platform.inboundservices.persistence.ItemModelPopulator
import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyValueGenerator
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import de.hybris.platform.integrationservices.search.ItemSearchResult
import de.hybris.platform.integrationservices.search.ItemSearchService
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService
import de.hybris.platform.odata2services.converter.IntegrationObjectItemNotFoundException
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest
import de.hybris.platform.odata2services.odata.persistence.StorageRequest
import de.hybris.platform.odata2services.odata.persistence.creation.CreateItemStrategy
import de.hybris.platform.odata2services.odata.persistence.populator.EntityModelPopulator
import de.hybris.platform.odata2services.odata.persistence.validator.CreateItemValidator
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultModelEntityServiceUnitTest extends Specification {
    private static final def INTERNAL_INTEGRATION_KEY = 'myKey'
    private static final String EXISTING_OBJECT_CODE = 'TestObject'
    private static final String EXISTING_ITEM_TYPE_CODE = 'TestItem'

    def entityModelPopulator = Mock EntityModelPopulator
    def itemModelPopulator = Mock ItemModelPopulator
    def itemSearchService = Stub ItemSearchService
    def createItemStrategy = Stub CreateItemStrategy
    def modelEntityService = Spy(DefaultModelEntityService)
    def integrationKeyValueGenerator = Stub(IntegrationKeyValueGenerator) {
        generate(_ as TypeDescriptor, _ as ODataEntry) >> INTERNAL_INTEGRATION_KEY
    }
    def deprecatedIntegrationKeyValueGenerator = Stub(IntegrationKeyValueGenerator) {
        generate(_, _) >> INTERNAL_INTEGRATION_KEY
    }
    def itemType = Stub(TypeDescriptor)
    def typeDescriptorService = Stub(ItemTypeDescriptorService)

    def setup() {
        modelEntityService.entityModelPopulator = entityModelPopulator
        modelEntityService.itemModelPopulator = itemModelPopulator
        modelEntityService.searchService = itemSearchService
        modelEntityService.integrationKeyValueGenerator = deprecatedIntegrationKeyValueGenerator
        modelEntityService.createItemStrategy = createItemStrategy
        modelEntityService.keyValueGenerator = integrationKeyValueGenerator
        modelEntityService.itemTypeDescriptorService = typeDescriptorService
    }

    @Test
    def 'creates new item successfully'() {
        def request = storageRequest()

        given: 'item does not exist'
        itemSearchService.findUniqueItem(_ as ItemLookupRequest) >> Optional.empty()
        and: 'the service has validators configured'
        modelEntityService.createItemValidators = [passingValidator(), passingValidator()]
        and: 'the create item strategy creates an item'
        def item = Stub ItemModel
        createItemStrategy.createItem(request) >> item

        when:
        modelEntityService.createOrUpdateItem request, createItemStrategy

        then:
        1 * entityModelPopulator.populateItem(item, request)
        1 * itemModelPopulator.populate(item, request)
        1 * request.putItem(item)
        modelEntityService.createItemValidators.forEach {
            1 * it.beforeItemLookup(_, _)
            1 * it.beforeCreateItem(_, _)
            1 * it.beforePopulateItem(_, _)
        }
    }

    @Test
    def 'updates an existing item successfully'() {
        def request = storageRequest()

        given: 'item exists'
        def item = Stub ItemModel
        itemSearchService.findUniqueItem(_ as ItemLookupRequest) >> Optional.of(item)
        and: 'the service has validators configured'
        modelEntityService.createItemValidators = [passingValidator(), passingValidator()]

        when:
        def itemReturned = modelEntityService.createOrUpdateItem request, createItemStrategy

        then:
        itemReturned.is item
        0 * createItemStrategy._
        1 * entityModelPopulator.populateItem(item, request)
        1 * itemModelPopulator.populate(item, request)
        1 * request.putItem(item)
        modelEntityService.createItemValidators.forEach {
            1 * it.beforeItemLookup(_, _)
            0 * it.beforeCreateItem(_, _)
            1 * it.beforePopulateItem(_, _)
        }
    }

    @Test
    def 'item is not created when creation is not permitted'() {
        given: 'a request that does not allow item creation'
        def lookupRequest = Stub ItemLookupRequest
        def storageRequest = Stub(StorageRequest) {
            getContextItem() >> Optional.empty()
            toLookupRequest() >> lookupRequest
            isItemCanBeCreated() >> false
        }
        and: 'item does not exist'
        itemSearchService.findUniqueItem(lookupRequest) >> Optional.empty()

        when:
        def item = modelEntityService.createOrUpdateItem storageRequest, createItemStrategy

        then:
        item == null
        0 * createItemStrategy._
        0 * entityModelPopulator._
        0 * itemModelPopulator._
    }

    @Test
    def 'item is not created when a validator throws exception'() {
        given: 'item does not exit'
        itemSearchService.findUniqueItem(_ as ItemLookupRequest) >> Optional.empty()
        and: 'create validator fails'
        modelEntityService.createItemValidators = [failingCreateValidator(), passingValidator()]

        when:
        modelEntityService.createOrUpdateItem storageRequest(), createItemStrategy

        then:
        thrown EdmException
        0 * createItemStrategy._
        0 * modelEntityService.createItemValidators[1].beforeCreateItem(_, _)
    }

    @Test
    def 'item is not looked up when validator throws exception'() {
        given: 'lookup validator fails'
        modelEntityService.createItemValidators = [failingLookupValidator(), passingValidator()]

        when:
        modelEntityService.createOrUpdateItem storageRequest(), createItemStrategy

        then:
        thrown EdmException
        0 * itemSearchService._
        0 * modelEntityService.createItemValidators[1].beforeItemLookup(_, _)
    }

    @Test
    def 'item is not populated when validator throws exception'() {
        given: 'populate validator fails'
        modelEntityService.createItemValidators = [failingPopulateValidator(), passingValidator()]

        when:
        modelEntityService.createOrUpdateItem storageRequest(), createItemStrategy

        then:
        thrown InternalProcessingException
        0 * entityModelPopulator._
        0 * itemModelPopulator._
        0 * modelEntityService.createItemValidators[1].beforePopulateItem(_, _)
    }

    @Test
    def 'injects integration key into an ODataEntry'() {
        given:
        def entry = Stub(ODataEntry) {
            getProperties() >> new HashMap()
        }

        when:
        def integrationKey = modelEntityService.addIntegrationKeyToODataEntry Stub(EdmEntitySet), entry

        then:
        integrationKey == INTERNAL_INTEGRATION_KEY
        entry.properties['integrationKey'] == INTERNAL_INTEGRATION_KEY
    }

    @Test
    def 'item found in storage request context takes precedence over an existing item'() {
        given: 'item exists'
        def existingItem = Stub ItemModel
        itemSearchService.findUniqueItem(_ as ItemLookupRequest) >> Optional.of(existingItem)
        and: 'request contains some other item'
        def request = storageRequest(Stub(ItemModel))

        when:
        def itemReturned = modelEntityService.createOrUpdateItem request, createItemStrategy

        then:
        itemReturned != existingItem
        0 * request.putItem(_)
    }

    @Test
    def 'delegates lookup of multiple items to the item search service'() {
        given: 'look up strategy finds items'
        def item = Stub(ItemModel)
        itemSearchService.findItems(_ as ItemLookupRequest) >> ItemSearchResult.createWith([item])

        when:
        def result = modelEntityService.lookupItems Stub(ItemLookupRequest)

        then:
        result.entries == [item]
    }

    @Test
    def 'delegates count lookup to the item search service'() {
        given:
        def expectedCount = 5
        itemSearchService.countItems(_ as ItemSearchRequest) >> expectedCount

        expect:
        modelEntityService.count(Stub(ItemLookupRequest)) == expectedCount
    }

    @Test
    def 'findOrCreateItem delegates to the itemModelService when it is set'() {
        given:
        def itemModelService = Mock ContextItemModelService
        def request = storageRequest()
        modelEntityService.setContextItemModelService(itemModelService)

        when:
        modelEntityService.findOrCreateItem request

        then:
        1 * itemModelService.findOrCreateItem(request)
    }

    @Test
    def 'findOrCreateItem prefers item found in the context to an item existing in the platform'() {
        given: 'context contains an item'
        def contextItem = Stub ItemModel
        def request = storageRequest(contextItem)
        and: 'a matching item exists in the platform'
        def existingItem = Stub ItemModel
        itemSearchService.findUniqueItem(_ as ItemLookupRequest) >> Optional.of(existingItem)
        and: 'the service has a validator configured'
        def validator = passingValidator()
        modelEntityService.createItemValidators = [validator]

        when:
        def item = modelEntityService.findOrCreateItem request

        then:
        item.is contextItem
        0 * entityModelPopulator.populateItem(contextItem, request)
        0 * itemModelPopulator.populate(contextItem, request)
        0 * request.putItem(contextItem)
        0 * validator.beforeItemLookup(_, _)
        0 * validator.beforeCreateItem(_, _)
        0 * validator.beforePopulateItem(_, _)
    }

    @Test
    def 'findOrCreateItem returns an item existing in the platform when there is no item in the context'() {
        given: 'context does not contain an item'
        def request = storageRequest(null)
        and: 'a matching item exists in the platform'
        def existingItem = Stub ItemModel
        itemSearchService.findUniqueItem(_ as ItemLookupRequest) >> Optional.of(existingItem)
        and: 'the service has a validator configured'
        def validator = passingValidator()
        modelEntityService.createItemValidators = [validator]

        when:
        def item = modelEntityService.findOrCreateItem request

        then:
        item.is existingItem
        1 * entityModelPopulator.populateItem(existingItem, request)
        1 * itemModelPopulator.populate(existingItem, request)
        1 * request.putItem(existingItem)
        1 * validator.beforeItemLookup(_, _)
        0 * validator.beforeCreateItem(_, _)
        1 * validator.beforePopulateItem(_, _)
    }

    @Test
    def 'findOrCreateItem creates new item when item is not found'() {
        given: 'item does not exist'
        def request = storageRequest(null)
        itemSearchService.findUniqueItem(_ as ItemLookupRequest) >> Optional.empty()
        and: 'create strategy creates an item'
        def item = Stub ItemModel
        createItemStrategy.createItem(request) >> item
        and: 'the service has a validator configured'
        def validator = passingValidator()
        modelEntityService.createItemValidators = [validator]

        when:
        modelEntityService.findOrCreateItem request

        then:
        1 * entityModelPopulator.populateItem(item, request)
        1 * itemModelPopulator.populate(item, request)
        1 * request.putItem(item)
        1 * validator.beforeItemLookup(_, _)
        1 * validator.beforeCreateItem(_, _)
        1 * validator.beforePopulateItem(_, _)
    }

    @Test
    def 'findOrCreateItem throws exception when lookup validator throws exception'() {
        given: 'lookup validator fails'
        modelEntityService.createItemValidators = [failingLookupValidator(), passingValidator()]

        when:
        modelEntityService.findOrCreateItem storageRequest()

        then:
        thrown InternalProcessingException
        0 * itemSearchService._
        0 * modelEntityService.createItemValidators[1].beforeItemLookup(_, _)
    }


    @Test
    def 'findOrCreateItem throws exception when a create validator throws exception'() {
        given: 'item does not exit'
        itemSearchService.findUniqueItem(_ as ItemLookupRequest) >> Optional.empty()
        and: 'create validator fails'
        modelEntityService.createItemValidators = [failingCreateValidator(), passingValidator()]

        when:
        modelEntityService.findOrCreateItem storageRequest()

        then:
        thrown InternalProcessingException
        0 * createItemStrategy._
        0 * modelEntityService.createItemValidators[1].beforeCreateItem(_, _)
    }

    @Test
    def 'findOrCreateItem throws exception when populate validator throws exception'() {
        given: 'populate validator fails'
        modelEntityService.createItemValidators = [failingPopulateValidator(), passingValidator()]

        when:
        modelEntityService.findOrCreateItem storageRequest()

        then:
        thrown InternalProcessingException
        0 * entityModelPopulator._
        0 * itemModelPopulator._
        0 * modelEntityService.createItemValidators[1].beforePopulateItem(_, _)
    }

    @Test
    def 'findOrCreateItem throws exception when context item retrieval throws exception'() {
        given:
        def request = Stub(StorageRequest) {
            getContextItem() >> { throw new EdmException(EdmException.COMMON)}
        }

        when:
        modelEntityService.findOrCreateItem request

        then:
        thrown InternalProcessingException
    }

    @Test
    def 'findOrCreateItem throws exception when found item cannot be set in the context'() {
        given:
        def request = storageRequest()
        request.putItem(_ as ItemModel) >> { throw new EdmException(EdmException.COMMON) }

        when:
        modelEntityService.findOrCreateItem request

        then:
        thrown InternalProcessingException
    }

    @Test
    def 'getODataEntry populates entity with integrationKey attribute'() {
        given:
        def request = Stub(ItemConversionRequest) {
            getIntegrationObjectCode() >> EXISTING_OBJECT_CODE
            getEntityType() >> Stub(EdmEntityType) {
                getName() >> EXISTING_ITEM_TYPE_CODE
            }
        }

        and:
        typeDescriptorService.getTypeDescriptor(EXISTING_OBJECT_CODE, EXISTING_ITEM_TYPE_CODE) >> Optional.of(itemType)

        when:
        def entry = modelEntityService.getODataEntry request

        then:
        1 * entityModelPopulator.populateEntity(_, request)
        entry.properties.get('integrationKey') == INTERNAL_INTEGRATION_KEY
    }

    @Test
    def "IntegrationObjectItemNotFoundException is thrown when typeDescriptor is not found"() {
        given:
        def request = Stub(ItemConversionRequest) {
            getIntegrationObjectCode() >> EXISTING_OBJECT_CODE
            getEntityType() >> Stub(EdmEntityType) {
                getName() >> EXISTING_ITEM_TYPE_CODE
            }
        }

        and:
        typeDescriptorService.getTypeDescriptor(EXISTING_OBJECT_CODE, EXISTING_ITEM_TYPE_CODE) >> Optional.empty()

        when:
        modelEntityService.getODataEntry request

        then:
        def e = thrown IntegrationObjectItemNotFoundException
        with(e) {
            integrationObjectCode == EXISTING_OBJECT_CODE
            integrationItemType == EXISTING_ITEM_TYPE_CODE
            message == "Integration object $EXISTING_OBJECT_CODE does not contain item type $EXISTING_ITEM_TYPE_CODE"
        }
    }

    private StorageRequest storageRequest(final ItemModel itemModel = null) {
        Mock(StorageRequest) {
            getContextItem() >> Optional.ofNullable(itemModel)
            toLookupRequest() >> Stub(ItemLookupRequest)
            isItemCanBeCreated() >> true
        }
    }

    private CreateItemValidator passingValidator() {
        Mock(CreateItemValidator)
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
}
