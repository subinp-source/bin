/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.integration.searchservices

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchDataDrivenSpec
import de.hybris.platform.searchservices.search.data.*
import org.junit.Test

import javax.annotation.Resource
import java.nio.charset.StandardCharsets

@IntegrationTest
class SnAsSearchSpec extends AbstractSnSearchDataDrivenSpec {

	static final String CATALOG_ID = "hwcatalog"
	static final String CATALOG_VERSION_STAGED = "Staged"

	@Resource
	CatalogVersionService catalogVersionService

	@Override
	public Object loadData() {
		loadDefaultData()
		importCsv("/searchservices/test/integration/snIndexTypeAddCatalogs.impex", StandardCharsets.UTF_8.name())
		importCsv("/adaptivesearch/test/integration/searchservices/asSimpleSearchProfile.impex", StandardCharsets.UTF_8.name())
		importCsv("/adaptivesearch/test/integration/searchservices/asSearchProfileActivation.impex", StandardCharsets.UTF_8.name())
	}

	def setup() {
		catalogVersionService.setSessionCatalogVersions(Collections.emptyList())
	}

	def cleanup() {
		catalogVersionService.setSessionCatalogVersions(Collections.emptyList())
	}

	@Test
	def "Search"() {
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
	def "Search with promoted item"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			searchQuery.getRankRules().add(new SnPromotedHitsRankRule(hits: [
				new SnPromotedHit(id: DOC2_CODE)
			]))
		})

		then:
		with(searchResult) {
			searchHits.size() == 4
			searchHits[0].id == DOC2_ID
			searchHits[1].id == DOC1_ID
			searchHits[2].id == DOC3_ID
			searchHits[3].id == DOC4_ID
		}
	}

	@Test
	def "Search with excluded item"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			searchQuery.getFilters().add(new SnFilter(query: new SnNotQuery(query: new SnMatchTermsQuery(expression: "code", values: ["doc2"]))))
		})

		then:
		with(searchResult) {
			searchHits.size() == 3
			searchHits[0].id == DOC1_ID
			searchHits[1].id == DOC3_ID
			searchHits[2].id == DOC4_ID
		}
	}

	@Test
	def "Apply search profile"() {
		given:
		catalogVersionService.setSessionCatalogVersion(CATALOG_ID, CATALOG_VERSION_STAGED)

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort)

		then:
		with(searchResult) {
			searchHits.size() == 3
			searchHits[0].id == DOC2_ID
			searchHits[1].id == DOC1_ID
			searchHits[2].id == DOC4_ID
		}
	}

	@Test
	def "Apply search profile, promote a document if it matches the search query"() {
		given:
		catalogVersionService.setSessionCatalogVersion(CATALOG_ID, CATALOG_VERSION_STAGED)

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			searchQuery.getFilters().add(new SnFilter(query: new SnMatchTermsQuery(expression: "code", values: ["doc1", "doc2"])))
		})

		then:
		with(searchResult) {
			searchHits.size() == 2
			searchHits[0].id == DOC2_ID
			searchHits[1].id == DOC1_ID
		}
	}

	@Test
	def "Apply search profile, don't promote a document if it doesn't match the search query"() {
		given:
		catalogVersionService.setSessionCatalogVersion(CATALOG_ID, CATALOG_VERSION_STAGED)

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			searchQuery.getFilters().add(new SnFilter(query: new SnMatchTermsQuery(expression: "code", values: ["doc1"])))
		})

		then:
		with(searchResult) {
			searchHits.size() == 1
			searchHits[0].id == DOC1_ID
		}
	}
}
