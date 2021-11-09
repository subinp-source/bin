/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.search.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchDataDrivenSpec
import de.hybris.platform.searchservices.search.SnSearchException
import de.hybris.platform.searchservices.search.data.SnMatchQuery
import de.hybris.platform.searchservices.search.data.SnQueryFunctionRankRule
import de.hybris.platform.searchservices.search.data.SnSearchQuery
import de.hybris.platform.searchservices.search.data.SnSearchResult

import org.junit.Test


@IntegrationTest
class SnSearchQueryFunctionRankRuleSpec extends AbstractSnSearchDataDrivenSpec {

	static final Float DEFAULT_SCORE = 1f

	@Override
	public Object loadData() {
		loadDefaultData()
	}

	@Test
	def "Documents have default score"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort)

		then:
		with(searchResult) {
			searchHits.size() == 4

			searchHits[0].id == DOC1_ID
			searchHits[0].score == 1

			searchHits[1].id == DOC2_ID
			searchHits[1].score == 1

			searchHits[2].id == DOC3_ID
			searchHits[2].score == 1

			searchHits[3].id == DOC4_ID
			searchHits[3].score == 1
		}
	}

	@Test
	def "Fail on null rank rule"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			searchQuery.getRankRules().add(null)
		})

		then:
		thrown(SnSearchException)
	}

	@Test
	def "Fail on query function rank rule without query"() {
		given:
		float weight = 2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)
	}

	@Test
	def "Fail on query function rank rule with negative weight"() {
		given:
		float weight = -2f

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnMatchQuery query = new SnMatchQuery()
			query.setExpression("code")
			query.setValue("doc2")

			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(query)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		thrown(SnSearchException)
	}

	@Test
	def "Add single promote query function rank rule matching single document"() {
		given:
		float weight = 2f
		float expectedScore = DEFAULT_SCORE * weight

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnMatchQuery query = new SnMatchQuery()
			query.setExpression("code")
			query.setValue("doc2")

			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(query)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		with(searchResult) {
			searchHits.size() == 4

			searchHits[0].id == DOC2_ID
			searchHits[0].score == expectedScore

			searchHits[1].id == DOC1_ID
			searchHits[1].score == DEFAULT_SCORE

			searchHits[2].id == DOC3_ID
			searchHits[2].score == DEFAULT_SCORE

			searchHits[3].id == DOC4_ID
			searchHits[3].score == DEFAULT_SCORE
		}
	}

	@Test
	def "Add single promote query function rank rule matching multiple documents"() {
		given:
		float weight = 2f
		float expectedScore = DEFAULT_SCORE * weight

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnMatchQuery query = new SnMatchQuery()
			query.setExpression("color")
			query.setValue("GREEN")

			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(query)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		with(searchResult) {
			searchHits.size() == 4

			searchHits[0].id == DOC2_ID
			searchHits[0].score == expectedScore

			searchHits[1].id == DOC3_ID
			searchHits[1].score == expectedScore

			searchHits[2].id == DOC1_ID
			searchHits[2].score == DEFAULT_SCORE

			searchHits[3].id == DOC4_ID
			searchHits[3].score == 1
		}
	}

	@Test
	def "Add single demote query function rank rule matching single document"() {
		given:
		float weight = 0.5f
		float expectedScore = DEFAULT_SCORE * weight

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnMatchQuery query = new SnMatchQuery()
			query.setExpression("code")
			query.setValue("doc2")

			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(query)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		with(searchResult) {
			searchHits.size() == 4

			searchHits[0].id == DOC1_ID
			searchHits[0].score == DEFAULT_SCORE

			searchHits[1].id == DOC3_ID
			searchHits[1].score == DEFAULT_SCORE

			searchHits[2].id == DOC4_ID
			searchHits[2].score == 1

			searchHits[3].id == DOC2_ID
			searchHits[3].score == expectedScore
		}
	}

	@Test
	def "Add single demote query function rank rule matching multiple documents"() {
		given:
		float weight = 0.5f
		float expectedScore = DEFAULT_SCORE * weight

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnMatchQuery query = new SnMatchQuery()
			query.setExpression("size")
			query.setValue("M")

			SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule()
			queryFunctionRankRule.setQuery(query)
			queryFunctionRankRule.setWeight(weight)

			searchQuery.getRankRules().add(queryFunctionRankRule)
		})

		then:
		with(searchResult) {
			searchHits.size() == 4

			searchHits[0].id == DOC3_ID
			searchHits[0].score == DEFAULT_SCORE

			searchHits[1].id == DOC4_ID
			searchHits[1].score == 1

			searchHits[2].id == DOC1_ID
			searchHits[2].score == expectedScore

			searchHits[3].id == DOC2_ID
			searchHits[3].score == expectedScore
		}
	}

	@Test
	def "Add multiple query function rank rules with common document matching"() {
		given:
		float weight1 = 3f
		float weight2 = 2f
		float expectedScore1 = DEFAULT_SCORE * weight1 * weight2
		float expectedScore2 = DEFAULT_SCORE * weight1
		float expectedScore3 = DEFAULT_SCORE * weight2

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnMatchQuery query1 = new SnMatchQuery()
			query1.setExpression("color")
			query1.setValue("GREEN")

			SnQueryFunctionRankRule queryFunctionRankRule1 = new SnQueryFunctionRankRule()
			queryFunctionRankRule1.setQuery(query1)
			queryFunctionRankRule1.setWeight(weight1)

			SnMatchQuery query2 = new SnMatchQuery()
			query2.setExpression("size")
			query2.setValue("M")

			SnQueryFunctionRankRule queryFunctionRankRule2 = new SnQueryFunctionRankRule()
			queryFunctionRankRule2.setQuery(query2)
			queryFunctionRankRule2.setWeight(weight2)

			searchQuery.getRankRules().add(queryFunctionRankRule1)
			searchQuery.getRankRules().add(queryFunctionRankRule2)
		})

		then:
		with(searchResult) {
			searchHits.size() == 4

			searchHits[0].score == expectedScore1
			searchHits[0].id == DOC2_ID

			searchHits[1].score == expectedScore2
			searchHits[1].id == DOC3_ID

			searchHits[2].score == expectedScore3
			searchHits[2].id == DOC1_ID

			searchHits[3].score == 1
			searchHits[3].id == DOC4_ID
		}
	}

	@Test
	def "Add multiple query function rank rules without common document matching"() {
		given:
		float weight1 = 3f
		float weight2 = 2f
		float expectedScore1 = DEFAULT_SCORE * weight1
		float expectedScore2 = DEFAULT_SCORE * weight2

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			SnMatchQuery query1 = new SnMatchQuery()
			query1.setExpression("code")
			query1.setValue("doc3")

			SnQueryFunctionRankRule queryFunctionRankRule1 = new SnQueryFunctionRankRule()
			queryFunctionRankRule1.setQuery(query1)
			queryFunctionRankRule1.setWeight(weight1)

			SnMatchQuery query2 = new SnMatchQuery()
			query2.setExpression("code")
			query2.setValue("doc1")

			SnQueryFunctionRankRule queryFunctionRankRule2 = new SnQueryFunctionRankRule()
			queryFunctionRankRule2.setQuery(query2)
			queryFunctionRankRule2.setWeight(weight2)

			searchQuery.getRankRules().add(queryFunctionRankRule1)
			searchQuery.getRankRules().add(queryFunctionRankRule2)
		})

		then:
		with(searchResult) {
			searchHits.size() == 4

			searchHits[0].score == expectedScore1
			searchHits[0].id == DOC3_ID

			searchHits[1].score == expectedScore2
			searchHits[1].id == DOC1_ID

			searchHits[2].score == DEFAULT_SCORE
			searchHits[2].id == DOC2_ID

			searchHits[3].score == 1
			searchHits[3].id == DOC4_ID
		}
	}
}
