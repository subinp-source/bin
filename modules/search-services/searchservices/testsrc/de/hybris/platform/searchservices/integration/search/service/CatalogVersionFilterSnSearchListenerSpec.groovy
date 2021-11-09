/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.search.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.searchservices.constants.SearchservicesConstants
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchSpec
import de.hybris.platform.searchservices.search.data.SnSearchResult

import java.nio.charset.StandardCharsets

import javax.annotation.Resource

import org.junit.Test


@IntegrationTest
class CatalogVersionFilterSnSearchListenerSpec extends AbstractSnSearchSpec {

	private static final int DOCS_SIZE = 6
	private static final int DOCS_STAGED_SIZE = 3
	private static final int DOCS_ONLINE_SIZE = 3

	@Resource
	CatalogVersionService catalogVersionService

	def setup() {
		createTestData()
		loadCoreData()
		loadData()
	}

	def cleanup() {
		deleteTestData()
	}

	def loadData() {
		importData("/searchservices/test/integration/snCatalogBase.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snCatalogProducts.impex", StandardCharsets.UTF_8.name())
	}

	def loadCatalogVersionFilterListener() {
		importData("/searchservices/test/integration/snIndexTypeAddCatalogVersionFilterListener.impex",
			StandardCharsets.UTF_8.name())
	}

	@Test
	def "Products from all catalog versions are returned"() {
		when:
		executeFullIndexerOperation(INDEX_TYPE_ID)
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery)

		then:
		with(searchResult) {
			size == DOCS_SIZE
			searchHits.size() == DOCS_SIZE
		}
	}

	@Test
	def "Products from all catalog versions are returned: catalog version filter listener not configured"() {
		given:
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_ONLINE)

		when:
		executeFullIndexerOperation(INDEX_TYPE_ID)
		catalogVersionService.setSessionCatalogVersions(List.of(onlineCatalogVersion))
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery)

		then:
		with(searchResult) {
			size == DOCS_SIZE
			searchHits.size() == DOCS_SIZE

			searchHits.collect({
				it.fields[SearchservicesConstants.CATALOG_VERSION_FIELD]
			}).every({
				it == "${CATALOG_ID}:${CATALOG_VERSION_STAGED}" || it == "${CATALOG_ID}:${CATALOG_VERSION_ONLINE}"
			})
		}
	}

	@Test
	def "Products from all catalog versions are returned: session catalog versions not set"() {
		given:
		loadCatalogVersionFilterListener()

		when:
		executeFullIndexerOperation(INDEX_TYPE_ID)
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery)

		then:
		with(searchResult) {
			size == DOCS_SIZE
			searchHits.size() == DOCS_SIZE

			searchHits.collect({
				it.fields[SearchservicesConstants.CATALOG_VERSION_FIELD]
			}).every({
				it == "${CATALOG_ID}:${CATALOG_VERSION_STAGED}" || it == "${CATALOG_ID}:${CATALOG_VERSION_ONLINE}"
			})
		}
	}

	@Test
	def "Only products from staged catalog version are returned"() {
		given:
		loadCatalogVersionFilterListener()
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_STAGED)

		when:
		executeFullIndexerOperation(INDEX_TYPE_ID)
		catalogVersionService.setSessionCatalogVersions(List.of(stagedCatalogVersion))
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery)

		then:
		with(searchResult) {
			size == DOCS_STAGED_SIZE
			searchHits.size() == DOCS_STAGED_SIZE

			searchHits.collect({
				it.fields[SearchservicesConstants.CATALOG_VERSION_FIELD]
			}).every({
				it == "${CATALOG_ID}:${CATALOG_VERSION_STAGED}"
			})
		}
	}

	@Test
	def "Only products from online catalog version are returned"() {
		given:
		loadCatalogVersionFilterListener()
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_ONLINE)

		when:
		executeFullIndexerOperation(INDEX_TYPE_ID)
		catalogVersionService.setSessionCatalogVersions(List.of(onlineCatalogVersion))
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery)

		then:
		with(searchResult) {
			size == DOCS_ONLINE_SIZE
			searchHits.size() == DOCS_ONLINE_SIZE

			searchHits.collect({
				it.fields[SearchservicesConstants.CATALOG_VERSION_FIELD]
			}).every({
				it == "${CATALOG_ID}:${CATALOG_VERSION_ONLINE}"
			})
		}
	}

	@Test
	def "Products from staged and online catalog version are returned"() {
		given:
		loadCatalogVersionFilterListener()
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_STAGED)
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_ONLINE)

		when:
		executeFullIndexerOperation(INDEX_TYPE_ID)
		catalogVersionService.setSessionCatalogVersions(List.of(stagedCatalogVersion, onlineCatalogVersion))
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery)

		then:
		with(searchResult) {
			size == DOCS_STAGED_SIZE + DOCS_ONLINE_SIZE
			searchHits.size() == DOCS_STAGED_SIZE + DOCS_ONLINE_SIZE

			searchHits.collect({
				it.fields[SearchservicesConstants.CATALOG_VERSION_FIELD]
			}).every({
				it == "${CATALOG_ID}:${CATALOG_VERSION_STAGED}" || it == "${CATALOG_ID}:${CATALOG_VERSION_ONLINE}"
			})
		}
	}
}
