/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.search.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchSpec
import de.hybris.platform.searchservices.search.data.SnSearchQuery
import de.hybris.platform.searchservices.search.data.SnSearchResult
import de.hybris.platform.servicelayer.i18n.CommonI18NService

import java.nio.charset.StandardCharsets

import javax.annotation.Resource

import org.junit.Test

@IntegrationTest
class SnSearchSynonymSpec extends AbstractSnSearchSpec {

	static final String PRODUCT_CODE_1 = "doc1"
	static final String PRODUCT_CODE_2 = "doc2"
	static final String PRODUCT_CODE_3 = "doc3"

	@Resource
	CommonI18NService commonI18NService

	def setup() {
		createTestData()
		loadCoreData()
		loadDefaultData()
		importData("/searchservices/test/integration/snSynonymDictionary.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexConfigurationAddSynonymDictionary.impex", StandardCharsets.UTF_8.name())
	}

	def cleanup() {
		deleteTestData()
	}

	@Test
	def "Search one-way synonym"() {
		given:
		LanguageModel enLanguage = commonI18NService.getLanguage(LANGUAGE_EN_ISOCODE)
		executeFullIndexerOperation(INDEX_TYPE_ID)

		when:
		commonI18NService.setCurrentLanguage(enLanguage)

		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			searchQuery.setQuery("camise")
		})

		then:
		with(searchResult) {
			size == 1
			searchHits.size() == 1
			searchHits[0].fields.code == PRODUCT_CODE_1
		}
	}

	@Test
	def "Search two-way synonym"() {
		given:
		LanguageModel enLanguage = commonI18NService.getLanguage(LANGUAGE_EN_ISOCODE)
		executeFullIndexerOperation(INDEX_TYPE_ID)

		when:
		commonI18NService.setCurrentLanguage(enLanguage)

		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			searchQuery.setQuery("sweather")
		})

		then:
		with(searchResult) {
			size == 1
			searchHits.size() == 1
			searchHits[0].fields.code == PRODUCT_CODE_3
		}
	}
}
