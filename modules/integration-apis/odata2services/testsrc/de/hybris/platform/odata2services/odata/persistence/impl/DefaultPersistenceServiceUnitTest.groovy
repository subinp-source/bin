/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.locking.ItemLockedForProcessingException
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.suspend.SystemIsSuspendedException
import de.hybris.platform.inboundservices.persistence.ContextItemModelService
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.ItemSearchResult
import de.hybris.platform.integrationservices.search.ItemSearchService
import de.hybris.platform.integrationservices.search.validation.ItemSearchRequestValidator
import de.hybris.platform.odata2services.odata.persistence.ConversionOptions
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest
import de.hybris.platform.odata2services.odata.persistence.ModelEntityService
import de.hybris.platform.odata2services.odata.persistence.PersistenceRuntimeApplicationException
import de.hybris.platform.odata2services.odata.persistence.StorageRequest
import de.hybris.platform.odata2services.odata.persistence.exception.ItemNotFoundException
import de.hybris.platform.odata2services.odata.persistence.hook.PersistHookExecutor
import de.hybris.platform.odata2services.odata.persistence.hook.PersistHookNotFoundException
import de.hybris.platform.odata2services.odata.persistence.hook.PostPersistHookException
import de.hybris.platform.odata2services.odata.persistence.hook.PrePersistHookException
import de.hybris.platform.odata2services.odata.persistence.lookup.ItemLookupResult
import de.hybris.platform.odata2services.odata.processor.RetrievalErrorRuntimeException
import de.hybris.platform.servicelayer.exceptions.ModelSavingException
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.session.SessionExecutionBody
import de.hybris.platform.servicelayer.session.SessionService
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.junit.Test
import org.mockito.Mockito
import org.springframework.transaction.support.SimpleTransactionStatus
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import org.springframework.transaction.support.TransactionTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.mock

@UnitTest
class DefaultPersistenceServiceUnitTest extends Specification {
    private static final def PRE_PERSIST_HOOK_NAME = 'BEFORE_SAVE'
    private static final def POST_PERSIST_HOOK_NAME = 'AFTER_SAVE'
    private static final def INTEGRATION_KEY = 'integrationKey|Value'
    private static final def ENTITY_TYPE = 'SomeType'

    def ITEM = Stub ItemModel
    def ITEM_CHANGED_BY_HOOK = Stub ItemModel

    def modelService = Mock ModelService
    def modelEntityService = Stub ModelEntityService
    def itemSearchService = Stub ItemSearchService
    def hookRegistry = Stub(PersistHookExecutor) {
        runPrePersistHook(PRE_PERSIST_HOOK_NAME, ITEM, INTEGRATION_KEY) >> Optional.of(ITEM_CHANGED_BY_HOOK)
    }
    def itemModelService = Stub(ContextItemModelService) {
        findOrCreateItem(_ as StorageRequest) >> ITEM
    }
    private ItemSearchRequestValidator deleteItemPermissionValidator = Mock(ItemSearchRequestValidator)
    def persistenceService = new DefaultPersistenceService(
            modelService: modelService,
            modelEntityService: modelEntityService,
            itemSearchService: itemSearchService,
            persistHookRegistry: hookRegistry,
            itemModelService: itemModelService,
            deleteItemValidators: [deleteItemPermissionValidator]
    )

    def setup() {
        persistenceService.sessionService = Stub(SessionService) {
            executeInLocalView(_ as SessionExecutionBody) >> { args ->
                args[0].executeWithoutResult()
            }
        }
        persistenceService.transactionTemplate = Stub(TransactionTemplate) {
            execute(_ as TransactionCallbackWithoutResult) >> { args ->
                args[0].doInTransaction(new SimpleTransactionStatus())
            }
        }
    }

    @Test
    def 'setDeleteItemValidators ignores null values'() {
        when:
        def service = new DefaultPersistenceService(deleteItemValidators: null)

        then:
        service.getDeleteItemValidators().isEmpty()
    }

    @Test
    def 'createEntityData() invokes persistence hooks around save'() {
        setup: 'Mockito mocks because Spock does not verify order correctly for some reason'
        persistenceService.modelService = mock ModelService
        persistenceService.persistHookRegistry = mock PersistHookExecutor
        doReturn(Optional.of(ITEM_CHANGED_BY_HOOK)).when(persistenceService.persistHookRegistry)
                .runPrePersistHook(PRE_PERSIST_HOOK_NAME, ITEM, INTEGRATION_KEY)

        when:
        persistenceService.createEntityData storageRequest()

        then:
        persistenceHooksCalledInCorrectOrder()
    }

    def persistenceHooksCalledInCorrectOrder() {
        def mockModelService = persistenceService.modelService
        def mockHookExecutor = persistenceService.persistHookRegistry
        def callOrder = Mockito.inOrder mockModelService, mockHookExecutor
        callOrder.verify(mockHookExecutor).runPrePersistHook(PRE_PERSIST_HOOK_NAME, ITEM, INTEGRATION_KEY)
        callOrder.verify(mockModelService).saveAll()
        callOrder.verify(mockHookExecutor).runPostPersistHook(POST_PERSIST_HOOK_NAME, ITEM_CHANGED_BY_HOOK, INTEGRATION_KEY)
        true
    }

    @Test
    def 'createEntityData() does not save an item if Pre-Persist-Hook filtered the item out'() {
        given: 'Pre-Persist-Hook returns Optional.empty() for the item to be persisted'
        persistenceService.persistHookRegistry = Mock(PersistHookExecutor) {
            runPrePersistHook(PRE_PERSIST_HOOK_NAME, ITEM, INTEGRATION_KEY) >> Optional.empty()
        }
        and: 'a storage request'
        def request = storageRequest()

        when:
        def response = persistenceService.createEntityData request

        then:
        0 * modelService._
        0 * persistenceService.persistHookRegistry.runPostPersistHook(_, _, _)
        response == request.ODataEntry
    }

    @Test
    def 'createEntityData() returns ODataEntry for the persisted item'() {
        given: 'a request with the persisted item in the context'
        def request = storageRequest()
        request.getContextItem() >> Optional.of(Stub(ItemModel))
        and: 'an entry corresponding to the item'
        def savedEntry = Stub ODataEntry
        modelEntityService.getODataEntry(_ as ItemConversionRequest) >> savedEntry

        when:
        def responseODataEntry = persistenceService.createEntityData request

        then:
        responseODataEntry == savedEntry
    }

    @Test
    def 'createEntityData() returns request ODataEntry when request does not have an item in the context'() {
        given: 'a request without an item in the context'
        def request = storageRequest()
        request.getContextItem() >> Optional.empty()

        when:
        def responseODataEntry = persistenceService.createEntityData request

        then:
        responseODataEntry == request.ODataEntry
    }

    @Test
    def 'createEntityData() handles ItemLockedForProcessingException'() {
        given:
        def exception = new ItemLockedForProcessingException('IGNORE - Testing exception')
        persistenceService.itemModelService = Stub(ContextItemModelService) {
            findOrCreateItem(_ as StorageRequest) >> { throw exception }
        }

        when:
        persistenceService.createEntityData storageRequest()

        then:
        0 * modelService._
        0 * hookRegistry._
        def e = thrown PersistenceRuntimeApplicationException
        e.code == 'internal_error'
        e.cause == exception
    }

    @Test
    @Unroll
    def "createEntityData() handles #exception.class.simpleName"() {
        given:
        modelService.saveAll() >> { throw exception }

        when:
        persistenceService.createEntityData storageRequest()

        then:
        def e = thrown PersistenceRuntimeApplicationException
        e.code == 'internal_error'
        e.cause == exception

        where:
        exception << [new SystemIsSuspendedException('IGNORE - Testing Exception'), new ModelSavingException('IGNORE - Testing Exception')]
    }

    @Test
    @Unroll
    def "createEntityData() handles #exception.class.simpleName for Pre-Persist-Hook"() {
        given:
        persistenceService.persistHookRegistry = Stub(PersistHookExecutor) {
            runPrePersistHook(PRE_PERSIST_HOOK_NAME, ITEM, INTEGRATION_KEY) >> { throw exception }
        }

        when:
        persistenceService.createEntityData storageRequest()

        then:
        def e = thrown PersistenceRuntimeApplicationException
        e.code == errCode
        e.message == exception.message
        e.integrationKey == INTEGRATION_KEY

        where:
        exception                                                                       | errCode
        new PersistHookNotFoundException('IGNORE - Testing Exception', INTEGRATION_KEY) | 'hook_not_found'
        new PrePersistHookException(PRE_PERSIST_HOOK_NAME, null, INTEGRATION_KEY)       | 'pre_persist_error'
    }

    @Test
    @Unroll
    def "createEntityData() handles #exception.class.simpleName for Post-Persist-Hook"() {
        given:
        hookRegistry.runPostPersistHook(POST_PERSIST_HOOK_NAME, _ as ItemModel, INTEGRATION_KEY) >> { throw exception }

        when:
        persistenceService.createEntityData storageRequest()

        then:
        def e = thrown PersistenceRuntimeApplicationException
        e.code == errCode
        e.message == exception.message
        e.integrationKey == INTEGRATION_KEY

        where:
        exception                                                                       | errCode
        new PersistHookNotFoundException('IGNORE - Testing Exception', INTEGRATION_KEY) | 'hook_not_found'
        new PostPersistHookException(POST_PERSIST_HOOK_NAME, null, INTEGRATION_KEY)     | 'post_persist_error'
    }

    @Test
    def 'createEntryData() throws exception if item not found and cannot be created'() {
        given: 'a persistence context'
        def request = storageRequest()
        and: 'item is not found and not created'
        persistenceService.itemModelService = Stub(ContextItemModelService) {
            findOrCreateItem(request) >> null
        }

        when:
        persistenceService.createEntityData request

        then:
        thrown ItemNotFoundException
    }

    @Test
    def 'getEntityData() returns entry for the found item'() {
        given: 'item is found'
        def oDataEntry = Stub ODataEntry
        def request = lookupRequest()
        itemSearchService.findUniqueItem(request) >> Optional.of(ITEM)

        and: 'the found item converts into the ODataEntry'
        def conversionRequest = Stub ItemConversionRequest
        def options = Stub ConversionOptions
        request.toConversionRequest(ITEM, options) >> conversionRequest
        modelEntityService.getODataEntry(conversionRequest) >> oDataEntry

        when:
        def entityData = persistenceService.getEntityData request, options

        then:
        entityData.is oDataEntry
    }

    @Test
    def 'getEntityData() throws exception if item is not found'() {
        given: 'item not found'
        def lookupRequest = lookupRequest()
        itemSearchService.findUniqueItem(lookupRequest) >> Optional.empty()

        when:
        persistenceService.getEntityData lookupRequest, Stub(ConversionOptions)

        then:
        def e = thrown ItemNotFoundException
        e.message.contains ENTITY_TYPE
        e.message.contains INTEGRATION_KEY
    }

    @Test
    def 'deleteItem() removes the item when it is found'() {
        given: 'the item is found'
        def request = lookupRequest()
        itemSearchService.findUniqueItem(request) >> Optional.of(ITEM)

        when:
        persistenceService.deleteItem request

        then:
        1 * modelService.remove(ITEM)
        1 * deleteItemPermissionValidator.validate(request)
    }

    @Test
    def 'deleteItem() throws exception when the item is not found'() {
        given: 'item not found'
        def lookupRequest = lookupRequest()
        itemSearchService.findUniqueItem(lookupRequest) >> Optional.empty()

        when:
        persistenceService.deleteItem lookupRequest

        then:
        0 * modelService._
        def e = thrown ItemNotFoundException
        e.message.contains ENTITY_TYPE
        e.message.contains INTEGRATION_KEY
    }

    @Test
    def 'deleteItem() throws exception when deleteItemValidator throws an exception'() {
        given: 'the item is found'
        def request = lookupRequest()
        itemSearchService.findUniqueItem(request) >> Optional.of(ITEM)
        and: "deleteItemPermissionValidator throws a RuntimeException"
        def e = new RuntimeException()
        deleteItemPermissionValidator.validate(request) >> { throw e }

        when:
        persistenceService.deleteItem request

        then:
        def actualE = thrown(RuntimeException)
        actualE.is e
        0 * modelService.remove(ITEM)
    }

    @Test
    def 'getEntities() returns entries for the found items'() {
        def totalCount = 20
        given: 'items found'
        def lookupRequest = lookupRequest()
        itemSearchService.findItems(lookupRequest) >> searchResult(totalCount, [Stub(ItemModel), Stub(ItemModel)])

        when:
        def result = persistenceService.getEntities lookupRequest, Stub(ConversionOptions)

        then:
        result.entries.size() == 2
        result.totalCount == totalCount
    }

    @Test
    def 'getEntities() returns empty result when items not found'() {
        given: 'items not found'
        def lookupRequest = lookupRequest()
        itemSearchService.findItems(lookupRequest) >> notFoundResult()

        when:
        def result = persistenceService.getEntities lookupRequest, Stub(ConversionOptions)

        then:
        result.entries.empty
    }

    @Test
    def 'getEntities() handles EdmException'() {
        def itemType = 'TestItem'

        given: 'items found'
        def lookupRequest = lookupRequest()
        itemSearchService.findItems(lookupRequest) >> searchResult([item(itemType), item(itemType)])
        and: 'item conversion fails'
        modelEntityService.getODataEntry(_ as ItemConversionRequest) >> { throw Stub(EdmException) }

        when:
        persistenceService.getEntities lookupRequest, Stub(ConversionOptions)

        then:
        def e = thrown RetrievalErrorRuntimeException
        e.code == 'runtime_error'
        e.entityType == itemType
        e.message.contains itemType
    }

    private StorageRequest storageRequest() {
        Stub(StorageRequest) {
            getIntegrationKey() >> INTEGRATION_KEY
            getODataEntry() >> Stub(ODataEntry)
            getPrePersistHook() >> PRE_PERSIST_HOOK_NAME
            getPostPersistHook() >> POST_PERSIST_HOOK_NAME
        }
    }

    private ItemLookupRequest lookupRequest() {
        Stub(ItemLookupRequest) {
            getTypeDescriptor() >> Stub(TypeDescriptor) {
                getItemCode() >> ENTITY_TYPE
            }
            getAcceptLocale() >> Locale.ENGLISH
            getRequestedItem() >> Optional.of(
                    Stub(IntegrationItem) {
            getIntegrationKey() >> INTEGRATION_KEY
        }
            )
        }
    }

    private ItemModel item(String type = ENTITY_TYPE) {
        Stub(ItemModel) {
            getItemtype() >> type
        }
    }

    private static ItemSearchResult<ItemModel> notFoundResult() {
        searchResult(0)
    }

    private static ItemSearchResult<ItemModel> searchResult(List<ItemModel> items = []) {
        searchResult(items.size(), items)
    }

    private static ItemSearchResult<ItemModel> searchResult(int count, List<ItemModel> items = []) {
        ItemSearchResult.createWith(items, count)
    }

    private static ItemLookupResult<ItemModel> itemLookupResult(int count, List<ItemModel> items = []) {
        ItemLookupResult.createFrom(items, count)
    }
}
