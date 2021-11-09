/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.search.service

import static de.hybris.platform.searchservices.support.util.ObjectUtils.matchContains

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchDataDrivenSpec
import de.hybris.platform.searchservices.search.SnSearchException
import de.hybris.platform.searchservices.search.data.SnSearchResult

import java.nio.charset.StandardCharsets

import org.junit.Test

import spock.lang.Unroll


@Unroll
@IntegrationTest
public class SnSearchGroupSpec extends AbstractSnSearchDataDrivenSpec {

	@Override
	public Object loadData() {
		loadDefaultData()
		importData("/searchservices/test/integration/search/groups/snIndexTypeUpdatesForSearchGroup.impex", StandardCharsets.UTF_8.name())
	}

	@Test
	def "Search with group #row.testId: description=#row.description, data=#row.data"(row) {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, { row.data })

		then:
		matchContains(searchResult, row.expectedData)

		where:
		row << loadSearchSpecJson("/searchservices/test/integration/search/groups/validGroups.json")
	}

	@Test
	def "Fail to search with group #row.testId: description=#row.description, data=#row.data"(row) {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, { row.data })

		then:
		thrown(SnSearchException)

		where:
		row << loadSearchSpecJson("/searchservices/test/integration/search/groups/invalidGroups.json")
	}
}
