/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.persistence

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.odata.persistence.StorageRequest.storageRequestBuilder

@UnitTest
class StorageRequestUnitTest extends Specification {
    private static final String POST_HOOK = "postHook"
    private static final String PRE_HOOK = "preHook"
    private static final String OBJECT_CODE = "IntegrationObjectCode"
    private static final Locale CONTENT_LOCALE = Locale.GERMAN
    private static final Locale ACCEPT_LOCALE = Locale.ENGLISH
    private static final String ENTITY_TYPE_NAME = "EntityTypeName"
    private static final String INTEGRATION_KEY = "testIntegrationKey"

    @Shared
    private EdmEntityType entityType = Stub(EdmEntityType) {
        getName() >> ENTITY_TYPE_NAME
    }
    @Shared
    private EdmEntitySet entitySet = Stub(EdmEntitySet) {
        getEntityType() >> entityType
    }
    private TypeDescriptor typeDescriptor = Stub(TypeDescriptor)
    private ODataEntry oDataEntry = Stub(ODataEntry)

    private IntegrationItem item = Stub(IntegrationItem) {
        getItemType() >> typeDescriptor
        getIntegrationKey() >> INTEGRATION_KEY
    }

    @Test
    @Unroll
    def "IllegalArgumentException is thrown when request is built with null #condition"() {
        given:
        entitySet.getEntityType() >> type

        when:
        storageRequestBuilder()
                .withEntitySet(null)
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withODataEntry(oDataEntry)
                .withIntegrationObject(OBJECT_CODE)
                .withIntegrationItem(intItem)
                .build()

        then:
        thrown IllegalArgumentException

        where:
        condition                 | set       | type       | content        | accept        | objectCode  | intItem
        'entity set'              | null      | entityType | CONTENT_LOCALE | ACCEPT_LOCALE | OBJECT_CODE | integrationItem()
        'entity type'             | entitySet | null       | CONTENT_LOCALE | ACCEPT_LOCALE | OBJECT_CODE | integrationItem()
        'integration object code' | entitySet | entityType | CONTENT_LOCALE | ACCEPT_LOCALE | null        | integrationItem()
        'Accept-Language locale'  | entitySet | entityType | CONTENT_LOCALE | null          | OBJECT_CODE | integrationItem()
        'Content-Language locale' | entitySet | entityType | null           | ACCEPT_LOCALE | OBJECT_CODE | integrationItem()
        'integration item'        | entitySet | entityType | CONTENT_LOCALE | ACCEPT_LOCALE | OBJECT_CODE | null
    }

    @Test
    def "request can be built with all attributes explicitly specified"() {
        given:
        def request = storageRequestBuilder()
                .withEntitySet(entitySet)
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withODataEntry(oDataEntry)
                .withPostPersistHook(POST_HOOK)
                .withPrePersistHook(PRE_HOOK)
                .withIntegrationObject(OBJECT_CODE)
                .withIntegrationItem(item)
                .withIntegrationKey(INTEGRATION_KEY)
                .withReplaceAttributes(true)
                .withItemCanBeCreated(true)
                .build()

        expect:
        request != null
        with(request) {
            entitySet == entitySet
            entityType == entityType
            contentLocale == CONTENT_LOCALE
            acceptLocale == ACCEPT_LOCALE
            oDataEntry == oDataEntry
            postPersistHook == POST_HOOK
            prePersistHook == PRE_HOOK
            integrationObjectCode == OBJECT_CODE
            integrationItem == item
            integrationKey == INTEGRATION_KEY
            replaceAttributes
            itemCanBeCreated
        }
    }

    @Test
    def "request can be built with null OData entry"() {
        given:
        def request = storageRequestBuilder()
                .withEntitySet(entitySet)
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withIntegrationObject(OBJECT_CODE)
                .withIntegrationItem(item)
                .withODataEntry(null)
                .build()

        expect:
        request?.ODataEntry == null
    }

    @Test
    def "request can be built without persistence hooks specification"() {
        given:
        def request = storageRequestBuilder()
                .withEntitySet(entitySet)
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withIntegrationObject(OBJECT_CODE)
                .withIntegrationItem(item)
                .build()

        expect:
        request != null
        request.postPersistHook == ""
        request.prePersistHook == ""
    }

    @Test
    def "new request instance can be built from another request"() {
        given:
        def original = storageRequestBuilder()
                .withEntitySet(entitySet)
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withODataEntry(oDataEntry)
                .withPostPersistHook(POST_HOOK)
                .withPrePersistHook(PRE_HOOK)
                .withIntegrationObject(OBJECT_CODE)
                .withIntegrationItem(item)
                .withReplaceAttributes(true)
                .withItemCanBeCreated(true)
                .build()
        def copy = storageRequestBuilder().from(original).build()

        expect:
        copy != null
        ! copy.is(original)
        with(copy) {
            entitySet  == entitySet
            entityType  == entityType
            contentLocale  == CONTENT_LOCALE
            acceptLocale  == ACCEPT_LOCALE
            oDataEntry  == oDataEntry
            postPersistHook  == POST_HOOK
            prePersistHook  == PRE_HOOK
            integrationObjectCode  == OBJECT_CODE
            integrationItem == item
            replaceAttributes
            itemCanBeCreated
        }
    }

    @Test
    def "created request does not contain context items"() {
        expect:
        storageRequest().contextItem == Optional.empty()
    }

    @Test
    def "request returns empty context item when it's not found by the integration key"() {
        given: 'item is placed into the context with the default integration key'
        final StorageRequest request = storageRequest()
        request.putItem Stub(ItemModel)
        and: 'a context is created with different key'
        def copy = storageRequestBuilder().from(request)
                .withIntegrationItem(integrationItem("differentKey"))
                .build()

        expect:
        copy.contextItem.empty
    }

    @Test
    def "request returns empty context item when it's not found by the entity type"() {
        given: 'given item of default type is placed into the context'
        final StorageRequest request = storageRequest()
        request.putItem Stub(ItemModel)
        and: 'a context is created with different item type'
        def copy = storageRequestBuilder().from(request)
                .withIntegrationItem(integrationItem(INTEGRATION_KEY, 'DifferentType'))
                .build()

        expect:
        copy.contextItem.empty
    }

    @Test
    def "context item placed into the request can be retrieved back"() {
        final StorageRequest request = storageRequest()
        final ItemModel itemModel = Stub(ItemModel)

        given:
        request.putItem(itemModel)

        expect:
        request.getContextItem().orElse(null) == itemModel
    }

    @Test
    def "handles null item passed into the context"() {
        final StorageRequest request = storageRequest()

        given:
        request.putItem null

        expect:
        request.contextItem.empty
    }

    @Test
    def "a copy of the request contains context items()"() {
        given:
        def original = storageRequest()
        original.putItem Stub(ItemModel)

        when:
        def copy = storageRequestBuilder().from(original).build()

        then:
        copy.contextItem == original.contextItem
    }

    @Test
    def "getContextItem returns an optional empty when no items are empty"() {
        given:
        def context = storageRequest()

        expect:
        context.getContextItem() == Optional.empty()
    }

    @Test
    def "getContextItem returns matching item based off its integrationKey and itemType"() {
        given:
        def context = storageRequest()
        def newItem = Stub(ItemModel)
        and: "items has 1 entry for itemType and integrationKey"
        context.putItem(newItem)

        expect:
        context.getContextItem() == Optional.of(newItem)
    }

    @Test
    def "integration key can be derived from the context integration item"() {
        given:
        def request = storageRequestBuilder()
                .withEntitySet(entitySet)
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withIntegrationObject(OBJECT_CODE)
                .withIntegrationItem(item)
                .build()

        expect:
        request.integrationKey == item.integrationKey
    }

    @Test
    def "integration key specified for the request overrides the key value in the integration item"() {
        given:
        def request = storageRequestBuilder()
                .withEntitySet(entitySet)
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withIntegrationObject(OBJECT_CODE)
                .withIntegrationKey('item|key')
                .withIntegrationItem(item)
                .build()

        expect:
        request.integrationKey == 'item|key'
    }

    @Test
    def "storage request can be converted to item lookup request"() {
        given:
        def storageRequest = storageRequest()

        when:
        def lookupRequest = storageRequest.toLookupRequest()

        then:
        with(lookupRequest) {
            integrationObjectCode == storageRequest.integrationObjectCode
            contentType == storageRequest.contentType
            acceptLocale == storageRequest.acceptLocale
            entitySet == storageRequest.entitySet
            entityType == storageRequest.entityType
            oDataEntry == storageRequest.ODataEntry
            integrationItem == storageRequest.integrationItem
            integrationKey == storageRequest.integrationKey
            serviceRoot == storageRequest.serviceRoot
            requestUri == storageRequest.requestUri
        }
    }

    @Test
    def "StorageRequest can be converted to ItemSearchRequest"() {
        given:
        def storageRequest = storageRequest()

        when:
        def itemSearchRequest = storageRequest.toItemSearchRequest()

        then:
        with(itemSearchRequest) {
            paginationParameters == Optional.empty()
            typeDescriptor == storageRequest.integrationItem.itemType
            filter == null
            !includeTotalCount()
            !countOnly
            orderBy == []
            acceptLocale == storageRequest.acceptLocale
        }
    }

    @Test
    def "getReferenceContext creates a PersistenceContext for the attribute"() {
        given: 'attribute descriptor'
        def attributeName = "innerAttributeName"
        def typeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> attributeName
        }
        and: 'there is a nested integration item'
        def nestedItemIntegrationKey = 'nestedKey'
        def nestedIntegrationItem = Stub(IntegrationItem) {
            getIntegrationKey() >> nestedItemIntegrationKey
        }
        item.getReferencedItem(typeAttributeDescriptor) >> nestedIntegrationItem
        and:
        def storageRequest = storageRequest()

        when:
        def nestedPersistenceContext = storageRequest.getReferencedContext(typeAttributeDescriptor)

        then:
        with(nestedPersistenceContext) {
            integrationItem.is nestedIntegrationItem
            sourceContext.get().integrationItem.is storageRequest.integrationItem
            rootContext.integrationItem.is storageRequest.integrationItem
        }
    }

    @Test
    @Unroll
    def "referenced PersistenceContext has isItemCanBeCreated()=#res when the attribute has autoCreate=#create and partOf=#part"() {
        given:
        def attribute = Stub(TypeAttributeDescriptor) {
            isAutoCreate() >> create
            isPartOf() >> part
        }

        when:
        def referencedContext = storageRequest().getReferencedContext attribute

        then:
        referencedContext.itemCanBeCreated == res

        where:
        create | part  | res
        false  | false | false
        true   | false | true
        false  | true  | true
        true   | true  | true
    }

    @Test
    def "getReferencedContexts creates multiple PersistenceContexts for a collection attribute"() {
        given: 'attribute descriptor'
        def attributeName = "innerAttributeName"
        def typeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> attributeName
        }
        and: 'there are two nested integration items'
        def nestedItems = [integrationItem('key1'), integrationItem('key2')]
        item.getReferencedItems(typeAttributeDescriptor) >> nestedItems
        and:
        def storageRequest = storageRequest()

        when:
        def nestedContexts = storageRequest.getReferencedContexts(typeAttributeDescriptor)

        then:
        nestedContexts.size() == 2
        with(nestedContexts[0]) {
            integrationItem.is nestedItems[0]
            sourceContext.get().integrationItem.is storageRequest.integrationItem
            rootContext.integrationItem.is storageRequest.integrationItem
        }
        with(nestedContexts[1]) {
            integrationItem.is nestedItems[1]
            sourceContext.get().integrationItem.is storageRequest.integrationItem
            rootContext.integrationItem.is storageRequest.integrationItem
        }
    }

    @Test
    @Unroll
    def "referenced contexts have isItemCanBeCreated()=#res when the collection attribute has autoCreate=#create and partOf=#part"() {
        given: 'attribute descriptor'
        def attributeName = 'items'
        def typeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> attributeName
            isAutoCreate() >> create
            isPartOf() >> part
        }
        and: 'there are two nested integration items'
        item.getReferencedItems(typeAttributeDescriptor) >> [integrationItem('one'), integrationItem('two')]

        when:
        def nestedContexts = storageRequest().getReferencedContexts(typeAttributeDescriptor)

        then:
        nestedContexts.collect { it.itemCanBeCreated } == [res, res]

        where:
        create | part  | res
        false  | false | false
        true   | false | true
        false  | true  | true
        true   | true  | true
    }

    @Test
    def "source context is empty when context is not a referenced context"() {
        expect:
        storageRequest().sourceContext.empty
    }

    @Test
    def "root context is itself when current context is not a referenced context"() {
        given:
        def storageRequest = storageRequest()

        expect:
        storageRequest.rootContext.integrationItem.is storageRequest.integrationItem
    }

    @Test
    def "multiple levels of nesting returns the correct source and root contexts"() {
        given: 'lowest level attribute descriptor'
        def lowestLevelAttributeName = "lowestLevelAttrName"
        def lowestLevelTypeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> lowestLevelAttributeName
        }
        and: 'lowest level integration item'
        def lowestLevelIntegrationItem = Stub IntegrationItem
        and: 'middle level attribute descriptor'
        def middleLevelAttributeName = "middleLevelAttrName"
        def middleLevelTypeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> middleLevelAttributeName
        }
        and: 'middle level integration item'
        def middleLevelIntegrationItem = Stub(IntegrationItem) {
            getReferencedItem(lowestLevelTypeAttributeDescriptor) >> lowestLevelIntegrationItem
        }
        item.getReferencedItem(middleLevelTypeAttributeDescriptor) >> middleLevelIntegrationItem
        and:
        def storageRequest = storageRequest()

        when:
        def middlePersistenceContext = storageRequest.getReferencedContext(middleLevelTypeAttributeDescriptor)
        def lowestPersistenceContext = middlePersistenceContext.getReferencedContext(lowestLevelTypeAttributeDescriptor)

        then:
        with(lowestPersistenceContext) {
            integrationItem.is lowestLevelIntegrationItem
            sourceContext.get().integrationItem.is middlePersistenceContext.integrationItem
            rootContext.integrationItem.is storageRequest.integrationItem
        }
    }

    @Test
    def "request builder does not mutate previously created request instances"() {
        given:
        def builder = storageRequestBuilder()
                .withEntitySet(entitySet)
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withODataEntry(oDataEntry)
                .withPostPersistHook(POST_HOOK)
                .withPrePersistHook(PRE_HOOK)
                .withIntegrationObject(OBJECT_CODE)
                .withIntegrationItem(item)
                .withIntegrationKey(INTEGRATION_KEY)
                .withReplaceAttributes(true)
                .withItemCanBeCreated(true)
        and:
        def firstReq = builder.build()
        def secondReq = builder
                .withContentLocale(Locale.FRENCH)
                .withAcceptLocale(Locale.FRENCH)
                .withODataEntry(Stub(ODataEntry))
                .withPostPersistHook('otherHook')
                .withPrePersistHook('otherHook')
                .withIntegrationObject('OtherObject')
                .withIntegrationItem(Stub(IntegrationItem))
                .withIntegrationKey('otherKey')
                .withReplaceAttributes(false)
                .withItemCanBeCreated(false)
                .build()

        expect:
        !firstReq.is(secondReq)
        with(firstReq) {
            contentLocale != secondReq.contentLocale
            acceptLocale != secondReq.acceptLocale
            getODataEntry() != secondReq.getODataEntry()
            postPersistHook != secondReq.postPersistHook
            prePersistHook != secondReq.prePersistHook
            integrationObjectCode != secondReq.integrationObjectCode
            integrationItem != secondReq.integrationItem
            integrationKey != secondReq.integrationKey
            replaceAttributes != secondReq.replaceAttributes
            itemCanBeCreated != secondReq.itemCanBeCreated
        }
    }

    private StorageRequest storageRequest() {
        return storageRequestBuilder()
                .withEntitySet(entitySet)
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withODataEntry(oDataEntry)
                .withPostPersistHook(POST_HOOK)
                .withPrePersistHook(PRE_HOOK)
                .withIntegrationObject(OBJECT_CODE)
                .withIntegrationKey(INTEGRATION_KEY)
                .withIntegrationItem(item)
                .build()
    }

    private IntegrationItem integrationItem(String key=INTEGRATION_KEY, String type=ENTITY_TYPE_NAME) {
        Stub(IntegrationItem) {
            getIntegrationKey() >> key
            getItemType() >> Stub(TypeDescriptor) {
                getItemCode() >> type
            }
        }
    }
}
