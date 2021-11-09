/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.search.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchDataDrivenSpec
import de.hybris.platform.searchservices.search.SnSearchException
import de.hybris.platform.searchservices.search.data.SnQueryFunctionRankRule
import de.hybris.platform.searchservices.search.data.SnSearchQuery
import de.hybris.platform.searchservices.search.data.SnSearchResult

import org.junit.Test

import spock.lang.Unroll


@Unroll
@IntegrationTest
class SnSearchQueryFunctionRankRuleQuerySpec extends AbstractSnSearchDataDrivenSpec {

	@Override
	public Object loadData() {
		loadDefaultData()
	}

	@Test
	def "Add query function rank rule with and query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({
			it.score == weight
		}).collect({
			it.id
		}) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validAndQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid and query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidAndQueries.json")
	}

	@Test
	def "Add query function rank rule with or query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({ it.score == weight }).collect({ it.id }) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validOrQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid or query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidOrQueries.json")
	}

	@Test
	def "Add query function rank rule with not query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({ it.score == weight }).collect({ it.id }) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validNotQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid not query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidNotQueries.json")
	}

	@Test
	def "Add query function rank rule with matchTerm query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({ it.score == weight }).collect({ it.id }) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validMatchTermQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid matchTerm query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidMatchTermQueries.json")
	}

	@Test
	def "Add query function rank rule with matchTerms query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({ it.score == weight }).collect({ it.id }) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validMatchTermsQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid matchTerms query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidMatchTermsQueries.json")
	}

	@Test
	def "Add query function rank rule with match query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({ it.score == weight }).collect({ it.id }) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validMatchQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid match query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidMatchQueries.json")
	}

	@Test
	def "Add query function rank rule with equal query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({ it.score == weight }).collect({ it.id }) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validEqualQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid equal query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidEqualQueries.json")
	}

	@Test
	def "Add query function rank rule with notEqual query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({ it.score == weight }).collect({ it.id }) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validNotEqualQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid notEqual query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidNotEqualQueries.json")
	}

	@Test
	def "Add query function rank rule with greaterThanOrEqual query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({ it.score == weight }).collect({ it.id }) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validGreaterThanOrEqualQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid greaterThanOrEqual query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidGreaterThanOrEqualQueries.json")
	}

	@Test
	def "Add query function rank rule with greaterThan query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({ it.score == weight }).collect({ it.id }) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validGreaterThanQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid greaterThan query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidGreaterThanQueries.json")
	}

	@Test
	def "Add query function rank rule with lessThanOrEqual query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({ it.score == weight }).collect({ it.id }) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validLessThanOrEqualQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid lessThanOrEqual query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidLessThanOrEqualQueries.json")
	}

	@Test
	def "Add query function rank rule with lessThan query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({ it.score == weight }).collect({ it.id }) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validLessThanQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid lessThan query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidLessThanQueries.json")
	}

	@Test
	def "Add query function rank rule with range query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		searchResult.searchHits.findAll({ it.score == weight }).collect({ it.id }) == row.matchingDocuments

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/validRangeQueries.json")
	}

	@Test
	def "Add query function rank rule with invalid range query #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(row.data)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)

		where:
		row << loadQuerySpecJson("/searchservices/test/integration/search/queries/invalidRangeQueries.json")
	}
}
