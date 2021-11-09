/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.search.FlexSearchQueryGenerator
import de.hybris.platform.integrationservices.search.ItemNotFoundForKeyReferencedItemPropertyException
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import de.hybris.platform.integrationservices.search.NonUniqueItemFoundException
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.search.SearchResult
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification

@UnitTest
class DefaultItemSearchServiceUnitTest extends Specification {
    private static final def FLEXIBLE_SEARCH_QUERY = new FlexibleSearchQuery('query does not matter')

    @Shared
    def requestWithIntegrationItem = searchRequest Stub(IntegrationItem)
    @Shared
    def requestWithoutIntegrationItem = searchRequest(null)

    def queryGenerator = Stub(FlexSearchQueryGenerator) {
        generate(requestWithIntegrationItem) >> FLEXIBLE_SEARCH_QUERY
        generate(requestWithoutIntegrationItem) >> FLEXIBLE_SEARCH_QUERY
    }
    def flexibleSearchService = Stub(FlexibleSearchService)

    def itemSearchService = new DefaultItemSearchService(
            flexSearchQueryGenerator: queryGenerator,
            flexibleSearchService: flexibleSearchService
    )

    @Test
    def 'findUniqueItem returns no results for an item not found'() {
        given: 'flexible search returns a result with no items'
        flexibleSearchService.search(FLEXIBLE_SEARCH_QUERY) >> Stub(SearchResult)

        expect:
        itemSearchService.findUniqueItem(requestWithIntegrationItem).empty
    }

    @Test
    def 'findUniqueItem returns 1 results for an existing item'() {
        given: 'flexible search returns a result with 1 item'
        def item = Stub ItemModel
        flexibleSearchService.search(FLEXIBLE_SEARCH_QUERY) >> Stub(SearchResult) {
            getCount() >> 1
            getResult() >> [item]
        }

        when:
        def result = itemSearchService.findUniqueItem requestWithIntegrationItem

        then:
        result == Optional.of(item)
    }

    @Test
    def 'findUniqueItem throws an exception when multiple items are found'() {
        given: 'flexible search returns a result with 2 items'
        flexibleSearchService.search(FLEXIBLE_SEARCH_QUERY) >> Stub(SearchResult) {
            getCount() >> 2
        }

        when:
        itemSearchService.findUniqueItem requestWithIntegrationItem

        then:
        def e = thrown NonUniqueItemFoundException
        e.request == requestWithIntegrationItem
    }

    @Test
    def 'findUniqueItem returns empty result when generator throws ItemNotFoundForKeyReferencedItemPropertyException'() {
        given:
        def request = Stub ItemSearchRequest
        queryGenerator.generate(request) >> { throw new ItemNotFoundForKeyReferencedItemPropertyException('', '') }

        expect:
        itemSearchService.findUniqueItem(request).empty
    }

    @Test
    def 'findItems returns a list of existing items'() {
        given: 'flexible search returns a list of items'
        def item1 = Stub(ItemModel)
        def item2 = Stub(ItemModel)
        flexibleSearchService.search(FLEXIBLE_SEARCH_QUERY) >> Stub(SearchResult) {
            getTotalCount() >> 2
            getResult() >> [item1, item2]
        }

        when:
        def result = itemSearchService.findItems requestWithoutIntegrationItem

        then:
        with(result) {
            totalCount == Optional.of(2)
            items == [item1, item2]
        }
    }

    @Test
    def 'findItems returns an empty list when no items exist'() {
        given: 'query execution returns an empty list'
        flexibleSearchService.search(FLEXIBLE_SEARCH_QUERY) >> Stub(SearchResult) {
            getTotalCount() >> 0
            getResult() >> []
        }

        when:
        def result = itemSearchService.findItems requestWithoutIntegrationItem

        then:
        with(result) {
            totalCount == Optional.of(0)
            items.empty
        }
    }

    @Test
    def 'count returns total count for existing items'() {
        given: 'query execution returns count of matching items'
        def totalCount = 3
        flexibleSearchService.search(FLEXIBLE_SEARCH_QUERY) >> Stub(SearchResult) {
            getTotalCount() >> totalCount
        }

        when:
        def totalItemsCount = itemSearchService.countItems requestWithoutIntegrationItem

        then:
        totalItemsCount == totalCount
    }

    ItemSearchRequest searchRequest(IntegrationItem requestedItem) {
        Stub(ItemSearchRequest) {
            getRequestedItem() >> Optional.ofNullable(requestedItem)
        }
    }
}
