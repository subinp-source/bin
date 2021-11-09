/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.ItemModelFactory
import de.hybris.platform.inboundservices.persistence.ItemModelPopulator
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.inboundservices.persistence.populator.ItemCreationException
import de.hybris.platform.inboundservices.persistence.validation.ItemPersistRequestValidator
import de.hybris.platform.inboundservices.persistence.validation.PersistenceContextValidator
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import de.hybris.platform.integrationservices.search.ItemSearchService
import de.hybris.platform.servicelayer.exceptions.ModelCreationException
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultContextItemModelServiceUnitTest extends Specification {

    def itemSearchService = Stub ItemSearchService
    def itemFactory = Stub ItemModelFactory
    def itemModelPopulator = Mock ItemModelPopulator
    def persistenceContextValidator = Mock PersistenceContextValidator
    def itemPersistReqValidator = Mock ItemPersistRequestValidator
    def contextItemModelService = new DefaultContextItemModelService(searchService: itemSearchService, itemFactory: itemFactory, itemModelPopulator: itemModelPopulator,
            itemPersistRequestValidators: [itemPersistReqValidator], persistenceContextValidators: [persistenceContextValidator])

    @Test
    def 'findOrCreateItem prefers item found in the context to an item existing in the platform'() {
        given: 'context contains an item'
        def contextItem = Stub ItemModel
        def request = persistenceRequest(contextItem)
        and: 'a matching item exists in the platform'
        def existingItem = Stub ItemModel
        itemSearchService.findUniqueItem(_ as ItemSearchRequest) >> Optional.of(existingItem)

        when:
        def item = contextItemModelService.findOrCreateItem request

        then:
        item.is contextItem
        1 * itemModelPopulator.populate(contextItem, request)
        0 * request.putItem(contextItem)
        0 * itemPersistReqValidator.validate(request, existingItem)
        0 * persistenceContextValidator.validate(request)
    }

    @Test
    def 'findOrCreateItem returns an item existing in the platform when there is no item in the context'() {
        given: 'context does not contain an item'
        def request = persistenceRequest(null)
        and: 'a matching item exists in the platform'
        def existingItem = Stub ItemModel
        itemSearchService.findUniqueItem(_ as ItemSearchRequest) >> Optional.of(existingItem)

        when:
        def item = contextItemModelService.findOrCreateItem request

        then:
        item.is existingItem
        1 * itemModelPopulator.populate(existingItem, request)
        1 * request.putItem(existingItem)
        1 * itemPersistReqValidator.validate(request, existingItem)
        0 * persistenceContextValidator.validate(request)
    }

    @Test
    def 'findOrCreateItem creates new item when item is not found'() {
        given: 'item does not exist'
        def request = persistenceRequest(null)
        itemSearchService.findUniqueItem(_ as ItemSearchRequest) >> Optional.empty()
        and:
        def newItem = Stub ItemModel
        itemFactory.createItem(request) >> newItem

        when:
        contextItemModelService.findOrCreateItem request

        then:
        1 * itemModelPopulator.populate(newItem, request)
        1 * request.putItem(newItem)
        1 * itemPersistReqValidator.validate(request, newItem)
        1 * persistenceContextValidator.validate(request)
    }

    @Test
    def 'findOrCreateItem throws exception when item factory throws exception'() {
        given: 'item does not exist'
        def request = persistenceRequest(null)
        itemSearchService.findUniqueItem(_ as ItemSearchRequest) >> Optional.empty()
        and:
        itemFactory.createItem(request) >> { throw new ModelCreationException("", Stub(Throwable)) }

        when:
        contextItemModelService.findOrCreateItem request

        then:
        thrown ItemCreationException
    }

    private PersistenceContext persistenceRequest(final ItemModel itemModel) {
        Mock(PersistenceContext) {
            getContextItem() >> Optional.ofNullable(itemModel)
            toItemSearchRequest() >> Stub(ItemSearchRequest)
            isItemCanBeCreated() >> true
            getIntegrationItem() >> Stub(IntegrationItem) {
                getItemType() >> Stub(TypeDescriptor) {
                    getItemCode() >> "code"
                }
            }
        }
    }
}
