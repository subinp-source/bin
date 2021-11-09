/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.integration.searchservices

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchDataDrivenSpec
import de.hybris.platform.searchservices.search.data.SnSearchResult
import org.junit.Test

import javax.annotation.Resource
import java.nio.charset.StandardCharsets

@IntegrationTest
class SnAsSearchWithCatalogVersionSpec extends AbstractSnSearchDataDrivenSpec {

	static final String CATALOG_ID = "hwcatalog"
	static final String CATALOG_VERSION_STAGED = "Staged"
	static final String CATALOG_VERSION_ONLINE = "Online"

	@Resource
	CatalogVersionService catalogVersionService

	def setup() {
		catalogVersionService.setSessionCatalogVersions(Collections.emptyList())
	}

	def cleanup() {
		catalogVersionService.setSessionCatalogVersions(Collections.emptyList())
	}

	@Override
	public Object loadData() {
		loadDefaultData()
		importCsv("/searchservices/test/integration/snCatalogCategories.impex", StandardCharsets.UTF_8.name())
		importCsv("/searchservices/test/integration/snIndexTypeAddCatalogs.impex", StandardCharsets.UTF_8.name())
		importCsv("/adaptivesearch/test/integration/searchservices/asCategoryAwareSearchProfile.impex", StandardCharsets.UTF_8.name())
		importCsv("/adaptivesearch/test/integration/searchservices/asSearchProfileActivation.impex", StandardCharsets.UTF_8.name())
	}

	@Test
	def "Search profile is not activated without catalog version"() {
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
	def "Search profile is activated for Staged catalog version"() {
		given:
		catalogVersionService.setSessionCatalogVersion(CATALOG_ID, CATALOG_VERSION_STAGED)

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort)

		then:
		with(searchResult) {
			searchHits.size() == 4
			searchHits[0].id == DOC4_ID
			searchHits[1].id == DOC1_ID
			searchHits[2].id == DOC2_ID
			searchHits[3].id == DOC3_ID
		}
	}

	@Test
	def "Search profile is not activated for Online catalog version"() {
		given:
		catalogVersionService.setSessionCatalogVersion(CATALOG_ID, CATALOG_VERSION_ONLINE)

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
}
