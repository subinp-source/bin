/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.servicelayer.search.SearchResult
import org.junit.Test
import spock.lang.Specification

@UnitTest
class ItemSearchResultUnitTest extends Specification {
    @Test
    def "creates instance from flexible search result"() {
        given:
        def searchResult = Stub(SearchResult) {
            getTotalCount() >> 2
            getResult() >> ['entry one', 'entry two']
        }

        expect:
        with(ItemSearchResult.createFrom(searchResult)) {
            totalCount.get() == searchResult.totalCount
            items == searchResult.result
        }
    }

    @Test
    def "creates instance with items and count"() {
        given:
        def count = 30
        def items = [1, 2, 3]

        expect:
        with(ItemSearchResult.createWith(items, count)) {
            totalCount.get() == count
            items == items
        }
    }

    @Test
    def "creates instance with items and no count"() {
        given:
        def items = [Stub(ItemModel)]

        expect:
        with(ItemSearchResult.createWith(items)) {
            totalCount.empty
            items == items
        }
    }

    @Test
    def "entries collection is immutable"() {
        given:
        def result = ItemSearchResult.createWith(["one", "two"])

        when:
        result.items.clear()

        then:
        thrown UnsupportedOperationException
    }

    @Test
    def "can transform to a different type of entries"() {
        given:
        def result = ItemSearchResult.createWith([1, 2, 3, 5, 8], 13)

        expect:
        with(result.map({ String.valueOf(it) })) {
            items == ["1", "2", "3", "5", "8"]
            totalCount == result.totalCount
        }
    }
}
