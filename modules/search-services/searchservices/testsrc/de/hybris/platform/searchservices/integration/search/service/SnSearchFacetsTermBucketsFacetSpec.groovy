/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.search.service

import static de.hybris.platform.searchservices.support.util.ObjectUtils.matchContains

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchDataDrivenSpec
import de.hybris.platform.searchservices.search.SnSearchException
import de.hybris.platform.searchservices.search.data.SnSearchResult

import org.junit.Test

import spock.lang.Unroll


@Unroll
@IntegrationTest
public class SnSearchFacetsTermBucketsFacetSpec extends AbstractSnSearchDataDrivenSpec {

	@Override
	public Object loadData() {
		loadDefaultData()
	}

	@Test
	def "Search with term buckets facets #row.testId: description=#row.description, data=#row.data"(row) {
		when:
		if (row.languages) {
			setCurrentLanguage(row.languages[0])
		}

		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, {
			row.data
		})

		then:
		matchContains(searchResult, row.expectedData)

		where:
		row << loadSearchSpecJson("/searchservices/test/integration/search/facets/validTermBucketsFacets.json")
	}

	@Test
	def "Fail to search with term buckets facets #row.testId: description=#row.description, data=#row.data"(row) {
		when:
		if (row.languages) {
			setCurrentLanguage(row.languages[0])
		}

		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, { row.data })

		then:
		thrown(SnSearchException)

		where:
		row << loadSearchSpecJson("/searchservices/test/integration/search/facets/invalidTermBucketsFacets.json")
	}
}
