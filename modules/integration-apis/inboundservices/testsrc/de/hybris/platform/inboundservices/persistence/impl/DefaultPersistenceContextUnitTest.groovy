/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.inboundservices.persistence.impl.DefaultPersistenceContext.persistenceContextBuilder

@UnitTest
class DefaultPersistenceContextUnitTest extends Specification {
    private static final def CONTENT_LOCALE = Locale.GERMAN
    private static final def ACCEPT_LOCALE = Locale.ENGLISH
    private static final def INTEGRATION_KEY = "theItemsKey"
    private static final def ITEM_CODE = "SomeType"
    def ITEM = Stub(IntegrationItem) {
        getItemType() >> Stub(TypeDescriptor) {
            getItemCode() >> ITEM_CODE
        }
        getIntegrationKey() >> INTEGRATION_KEY
    }

    @Test
    @Unroll
    def "IllegalArgumentException is thrown when request is built with null #condition"() {
        when:
        persistenceContextBuilder()
                .withContentLocale(content)
                .withAcceptLocale(accept)
                .withIntegrationItem(item)
                .build()

        then:
        thrown(IllegalArgumentException)

        where:
        condition                 | content        | accept         | item
        'Accept-Language locale'  | Locale.ENGLISH | null           | Stub(IntegrationItem)
        'Content-Language locale' | null           | Locale.ENGLISH | Stub(IntegrationItem)
        'IntegrationItem'         | Locale.ENGLISH | Locale.ENGLISH | null
    }

    @Test
    def "request can be built with all attributes explicitly specified"() {
        given:
        def request = persistenceContextBuilder()
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withIntegrationItem(ITEM)
                .withReplaceAttributes(true)
                .withItemCanBeCreated(true)
                .build()

        expect:
        request != null
        with(request) {
            contentLocale == CONTENT_LOCALE
            acceptLocale == ACCEPT_LOCALE
            integrationItem == ITEM
            replaceAttributes
            itemCanBeCreated
        }
    }

    @Test
    def "new request instance can be built from another request using a different builder"() {
        given:
        def original = persistenceContextBuilder()
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withIntegrationItem(ITEM)
                .withReplaceAttributes(true)
                .withItemCanBeCreated(true)
                .build()

        when:
        def copy = persistenceContextBuilder().from(original).build()

        then:
        copy != null
        !copy.is(original)
        with(copy) {
            contentLocale == original.contentLocale
            acceptLocale == original.acceptLocale
            integrationItem == original.integrationItem
            replaceAttributes == original.replaceAttributes
            itemCanBeCreated == original.itemCanBeCreated
        }
    }

    @Test
    def "new request instance can be built from another request using the same builder"() {
        given:
        def builder = persistenceContextBuilder()
        def original = builder
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withIntegrationItem(ITEM)
                .withReplaceAttributes(true)
                .withItemCanBeCreated(true)
                .build()

        when:
        def copy = builder.from(original).build()

        then:
        copy != null
        !copy.is(original)
        with(copy) {
            contentLocale == original.contentLocale
            acceptLocale == original.acceptLocale
            integrationItem == original.integrationItem
            replaceAttributes == original.replaceAttributes
            itemCanBeCreated == original.itemCanBeCreated
        }
    }

    @Test
    def "2 different persistenceContext instances can be created using the same persistence context builder"() {
        given:
        def item1 = Stub IntegrationItem
        def item2 = Stub IntegrationItem
        def builder = persistenceContextBuilder()
                .withAcceptLocale(Locale.GERMAN)
                .withContentLocale(Locale.ENGLISH)

        when:
        def context1 = builder.withIntegrationItem(item1).build()
        def context2 = builder.withIntegrationItem(item2).build()

        then:
        !context1.is(context2)
        context1.integrationItem == item1
    }

    @Test
    def "created request does not contain context items"() {
        expect:
        persistenceContext().contextItem == Optional.empty()
    }

    @Test
    def "context item placed into the request can be retrieved back"() {
        final PersistenceContext request = persistenceContext()
        final ItemModel itemModel = Stub(ItemModel)

        given:
        request.putItem(itemModel)

        expect:
        request.getContextItem().orElse(null) == itemModel
    }

    @Test
    def "handles null item passed into the context"() {
        final PersistenceContext request = persistenceContext()

        given:
        request.putItem null

        expect:
        request.contextItem.empty
    }

    @Test
    def "a copy of the request contains context items()"() {
        given:
        def original = persistenceContext()
        original.putItem(Stub(ItemModel))

        when:
        def copy = persistenceContextBuilder().from(original).build()

        then:
        copy.getContextItem() == original.getContextItem()
    }

    @Test
    def "insertItem creates a new map entry when itemType does not exist in items Map"() {
        given:
        def context = persistenceContext()
        def newItem = Stub(ItemModel)

        when:
        context.putItem(newItem)
        
        then:
        context.items[ITEM_CODE].contains INTEGRATION_KEY, newItem
    }

    @Test
    def "insertItem replaces item in map when item for the same itemType and integrationKey exist in items Map"() {
        given:
        def context = persistenceContext()
        def oldItem = Stub(ItemModel)
        def newItem = Stub(ItemModel)
        context.putItem(oldItem)

        when:
        context.putItem(newItem)

        then:
        context.items[ITEM_CODE].contains INTEGRATION_KEY, newItem
    }

    @Test
    def "getContextItem returns an optional empty when no items are empty"() {
        given:
        def context = persistenceContext()

        expect:
        context.getContextItem() == Optional.empty()
    }
    
    @Test
    def "getContextItem returns matching item based off its integrationKey and itemType"() {
        given:
        def context = persistenceContext()
        def newItem = Stub(ItemModel)
        and: "items has 1 entry for itemType and integrationKey"
        context.putItem(newItem)

        expect:
        context.getContextItem() == Optional.of(newItem)
    }

    @Test
    def "PersistenceContext can be converted to ItemSearchRequest"() {
        given:
        def persistenceContext = persistenceContext()

        when:
        def itemSearchRequest = persistenceContext.toItemSearchRequest()

        then:
        with(itemSearchRequest) {
            paginationParameters == Optional.empty()
            integrationItem == persistenceContext.integrationItem
            filter == null
            !includeTotalCount()
            !countOnly
            orderBy == []
            acceptLocale == persistenceContext.acceptLocale
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
        def nestedIntegrationItem = Stub(IntegrationItem)
        ITEM.getReferencedItem(typeAttributeDescriptor) >> nestedIntegrationItem

        and:
        def persistenceContext = persistenceContext()

        when:
        def nestedPersistenceContext = persistenceContext.getReferencedContext(typeAttributeDescriptor) as PersistenceContext

        then:
        with(nestedPersistenceContext) {
            integrationItem.is nestedIntegrationItem
            sourceContext.get().is persistenceContext
            rootContext.is persistenceContext
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
        def referencedContext = persistenceContext().getReferencedContext attribute

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
        def nestedItems = [Stub(IntegrationItem), Stub(IntegrationItem)]
        ITEM.getReferencedItems(typeAttributeDescriptor) >> nestedItems
        def persistenceContext = persistenceContext()

        when:
        def nestedContexts = persistenceContext.getReferencedContexts(typeAttributeDescriptor)

        then:
        nestedContexts.size() == 2
        with(nestedContexts[0]) {
            integrationItem.is nestedItems[0]
            sourceContext.get().is persistenceContext
            rootContext.is persistenceContext
        }
        with(nestedContexts[1]) {
            integrationItem.is nestedItems[1]
            sourceContext.get().is persistenceContext
            rootContext.is persistenceContext
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
        ITEM.getReferencedItems(typeAttributeDescriptor) >> [Stub(IntegrationItem), Stub(IntegrationItem)]

        when:
        def nestedContexts = persistenceContext().getReferencedContexts(typeAttributeDescriptor)

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
        persistenceContext().sourceContext.empty
    }

    @Test
    def "root context is itself when current context is not a referenced context"() {
        given:
        def persistenceContext = persistenceContext()

        expect:
        persistenceContext.rootContext.is persistenceContext
    }

    @Test
    def "multiple levels of nesting returns the correct source and root contexts"() {
        given: 'lowest level attribute descriptor'
        def lowestLevelAttributeName = "lowestLevelAttrName"
        def lowestLevelTypeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> lowestLevelAttributeName
        }
        and: 'lowest level integration item'
        def lowestLevelIntegrationItem = Stub(IntegrationItem)
        and: 'middle level attribute descriptor'
        def middleLevelAttributeName = "middleLevelAttrName"
        def middleLevelTypeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> middleLevelAttributeName
        }
        and: 'middle level integration item'
        def middleLevelIntegrationItem = Stub(IntegrationItem) {
            getReferencedItem(lowestLevelTypeAttributeDescriptor) >> lowestLevelIntegrationItem
        }
        ITEM.getReferencedItem(middleLevelTypeAttributeDescriptor) >> middleLevelIntegrationItem
        def persistenceContext = persistenceContext()

        when:
        def middlePersistenceContext = persistenceContext.getReferencedContext(middleLevelTypeAttributeDescriptor) as PersistenceContext
        def lowestPersistenceContext = middlePersistenceContext.getReferencedContext(lowestLevelTypeAttributeDescriptor) as PersistenceContext

        then:
        with(lowestPersistenceContext) {
            sourceContext.get().is middlePersistenceContext
            rootContext.is persistenceContext
            integrationItem.is lowestLevelIntegrationItem
        }
    }

    def persistenceContext(final IntegrationItem item = ITEM) {
        return persistenceContextBuilder()
                .withContentLocale(CONTENT_LOCALE)
                .withAcceptLocale(ACCEPT_LOCALE)
                .withIntegrationItem(item)
                .build()
    }
}
