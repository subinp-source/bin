/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.suggest.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.integration.suggest.AbstractSnSuggestSpec
import de.hybris.platform.searchservices.suggest.SnSuggestException
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery
import de.hybris.platform.searchservices.suggest.data.SnSuggestResult

import org.junit.Test


@IntegrationTest
class SnSuggestCoreSpec extends AbstractSnSuggestSpec {

	def setup() {
		createTestData()
		loadCoreData()
	}

	def cleanup() {
		deleteTestData()
	}

	@Test
	def "Fail to suggest on not existing index"() {
		when:
		SnSuggestResult suggestResult = executeSuggestQuery(INDEX_TYPE_ID, this.&createSuggestQuery, { SnSuggestQuery suggestQuery ->
			suggestQuery.setQuery("GREE")
		})

		then:
		thrown(SnSuggestException)
	}

	@Test
	def "Suggest on empty index"() {
		given:
		executeFullIndexerOperation(INDEX_TYPE_ID)

		when:
		SnSuggestResult suggestResult = executeSuggestQuery(INDEX_TYPE_ID, this.&createSuggestQuery, { SnSuggestQuery suggestQuery ->
			suggestQuery.setQuery("GREE")
		})

		then:
		with(suggestResult) {
			size == 0
			suggestHits.isEmpty()
		}
	}

	@Test
	def "Suggest"() {
		given:
		loadDefaultData()
		executeFullIndexerOperation(INDEX_TYPE_ID)

		when:
		SnSuggestResult suggestResult = executeSuggestQuery(INDEX_TYPE_ID, this.&createSuggestQuery, { SnSuggestQuery suggestQuery ->
			suggestQuery.setQuery("RE")
		})

		then:
		with(suggestResult) {
			size == 1
			suggestHits.size() == 1
			suggestHits[0].query == "RED"
		}
	}
}
