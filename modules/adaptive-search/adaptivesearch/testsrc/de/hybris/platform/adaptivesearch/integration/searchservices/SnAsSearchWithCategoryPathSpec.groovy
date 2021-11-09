/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.integration.searchservices

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.adaptivesearch.constants.AdaptivesearchConstants
import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchDataDrivenSpec
import de.hybris.platform.searchservices.search.data.SnFilter
import de.hybris.platform.searchservices.search.data.SnMatchTermQuery
import de.hybris.platform.searchservices.search.data.SnSearchQuery
import de.hybris.platform.searchservices.search.data.SnSearchResult
import org.junit.Test

import javax.annotation.Resource
import java.nio.charset.StandardCharsets

@IntegrationTest
class SnAsSearchWithCategoryPathSpec extends AbstractSnSearchDataDrivenSpec {

	static final String CATALOG_ID = "hwcatalog"
	static final String CATALOG_VERSION_STAGED = "Staged"

	static final String CATEGORY_CAT10_CODE = "cat10"
	static final String CATEGORY_CAT12_CODE = "cat12"

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
		importCsv("/adaptivesearch/test/integration/searchservices/asCatalogProductCategories.impex", StandardCharsets.UTF_8.name())
		importCsv("/adaptivesearch/test/integration/searchservices/asIndexTypeAddAllCategoriesProperty.impex", StandardCharsets.UTF_8.name())
		importCsv("/adaptivesearch/test/integration/searchservices/asCategoryAwareSearchProfile.impex", StandardCharsets.UTF_8.name())
		importCsv("/adaptivesearch/test/integration/searchservices/asSearchProfileActivation.impex", StandardCharsets.UTF_8.name())
	}

	@Test
	def "Search profile is activated for Staged catalog version, search for category global"() {
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
	def "Search profile is activated for Staged catalog version, search for category cat10"() {
		given:
		catalogVersionService.setSessionCatalogVersion(CATALOG_ID, CATALOG_VERSION_STAGED)

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			searchQuery.getFilters().add(
				new SnFilter(query: new SnMatchTermQuery(expression: AdaptivesearchConstants.ALL_CATEGORIES_PROPERTY, value: CATEGORY_CAT10_CODE)))
		})

		then:
		with(searchResult) {
			searchHits.size() == 4
			searchHits[0].id == DOC4_ID
			searchHits[1].id == DOC3_ID
			searchHits[2].id == DOC1_ID
			searchHits[3].id == DOC2_ID
		}
	}

	@Test
	def "Search profile is activated for Staged catalog version, search for category cat12"() {
		given:
		catalogVersionService.setSessionCatalogVersion(CATALOG_ID, CATALOG_VERSION_STAGED)

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort, { SnSearchQuery searchQuery ->
			searchQuery.getFilters().add(
				new SnFilter(query: new SnMatchTermQuery(expression: AdaptivesearchConstants.ALL_CATEGORIES_PROPERTY, value: CATEGORY_CAT12_CODE)))
		})

		then:
		with(searchResult) {
			searchHits.size() == 4
			searchHits[0].id == DOC4_ID
			searchHits[1].id == DOC3_ID
			searchHits[2].id == DOC2_ID
			searchHits[3].id == DOC1_ID
		}
	}
}
