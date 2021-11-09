/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.search.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchSpec
import de.hybris.platform.searchservices.search.SnSearchException
import de.hybris.platform.searchservices.search.data.SnSearchQuery
import de.hybris.platform.searchservices.search.data.SnSearchResult

import org.junit.Test


@IntegrationTest
class SnSearchCoreSpec extends AbstractSnSearchSpec {

	def setup() {
		createTestData()
		loadCoreData()
	}

	def cleanup() {
		deleteTestData()
	}

	@Test
	def "Fail to search on not existing index"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, { SnSearchQuery searchQuery ->
			searchQuery.setQuery("red")
		})

		then:
		thrown(SnSearchException)
	}

	@Test
	def "Search on empty index"() {
		given:
		executeFullIndexerOperation(INDEX_TYPE_ID)

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, { SnSearchQuery  searchQuery ->
			searchQuery.setQuery("red")
		})

		then:
		with(searchResult) {
			size == 0
			searchHits.isEmpty()
		}
	}

	@Test
	def "Search"() {
		given:
		loadDefaultData()
		executeFullIndexerOperation(INDEX_TYPE_ID)

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, { SnSearchQuery searchQuery ->
			searchQuery.setQuery("red")
		})

		then:
		with(searchResult) {
			size == 1
			searchHits.size() == 1
			searchHits[0].id == DOC1_ID
		}
	}
}
