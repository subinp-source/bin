/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.search.service


import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchDataDrivenSpec
import de.hybris.platform.searchservices.search.SnSearchException
import de.hybris.platform.searchservices.search.data.SnFilter
import de.hybris.platform.searchservices.search.data.SnMatchQuery
import de.hybris.platform.searchservices.search.data.SnSearchQuery
import de.hybris.platform.searchservices.search.data.SnSearchResult

import org.junit.Test


@IntegrationTest
class SnSearchFilterSpec extends AbstractSnSearchDataDrivenSpec {

	@Override
	public Object loadData() {
		loadDefaultData()
	}

	@Test
	def "All documents are included in the search result"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort)

		then:
		with(searchResult) {
			searchHits.size() == 4
			searchHits[0].id == DOC1_ID
			searchHits[1].id == DOC2_ID
			searchHits[2].id == DOC3_ID
			searchHits[3].id == DOC4_ID
		}
	}

	@Test
	def "Fail on null filter"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			searchQuery.getFilters().add(null)
		})

		then:
		thrown(SnSearchException)
	}

	@Test
	def "Fail on filter without query"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnFilter filter = new SnFilter()

			searchQuery.getFilters().add(filter)
		})

		then:
		thrown(SnSearchException)
	}

	@Test
	def "Add single filter matching single document"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnMatchQuery query = new SnMatchQuery()
			query.setExpression("code")
			query.setValue("doc2")

			SnFilter filter = new SnFilter()
			filter.setQuery(query)

			searchQuery.getFilters().add(filter)
		})

		then:
		with(searchResult) {
			searchHits.size() == 1
			searchHits[0].id == DOC2_ID
		}
	}

	@Test
	def "Add single filter matching multiple documents"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnMatchQuery query = new SnMatchQuery()
			query.setExpression("color")
			query.setValue("GREEN")

			SnFilter filter = new SnFilter()
			filter.setQuery(query)

			searchQuery.getFilters().add(filter)
		})

		then:
		with(searchResult) {
			searchHits.size() == 2
			searchHits[0].id == DOC2_ID
			searchHits[1].id == DOC3_ID
		}
	}

	@Test
	def "Add multiple filters with common document matching"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnMatchQuery query1 = new SnMatchQuery()
			query1.setExpression("color")
			query1.setValue("GREEN")

			SnFilter filter1 = new SnFilter()
			filter1.setQuery(query1)

			SnMatchQuery query2 = new SnMatchQuery()
			query2.setExpression("size")
			query2.setValue("M")

			SnFilter filter2 = new SnFilter()
			filter2.setQuery(query2)

			searchQuery.getFilters().add(filter1)
			searchQuery.getFilters().add(filter2)
		})

		then:
		with(searchResult) {
			searchHits.size() == 1
			searchHits[0].id == DOC2_ID
		}
	}

	@Test
	def "Add multiple filters without common document matching"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnMatchQuery query1 = new SnMatchQuery()
			query1.setExpression("code")
			query1.setValue("doc3")

			SnFilter filter1 = new SnFilter()
			filter1.setQuery(query1)

			SnMatchQuery query2 = new SnMatchQuery()
			query2.setExpression("code")
			query2.setValue("doc1")

			SnFilter filter2 = new SnFilter()
			filter2.setQuery(query2)

			searchQuery.getFilters().add(filter1)
			searchQuery.getFilters().add(filter2)
		})

		then:
		with(searchResult) {
			searchHits.size() == 0
		}
	}
}