/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.search.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchDataDrivenSpec
import de.hybris.platform.searchservices.search.data.SnSearchResult
import org.junit.Test
import spock.lang.Unroll

import java.nio.charset.StandardCharsets

import static de.hybris.platform.searchservices.support.util.ObjectUtils.matchContains

@Unroll
@IntegrationTest
class SnSearchSpec extends AbstractSnSearchDataDrivenSpec {

	@Override
	public Object loadData() {
		loadDefaultData()
	}

	def setup() {
		importData("/searchservices/test/integration/snIndexTypeAddFields.impex", StandardCharsets.UTF_8.name())
	}

	@Test
	def "Search with query (retrievable) #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		resetFields({ field ->
			field.setRetrievable(false)
		})

		if (row.fields) {
			for (def field : row.fields) {
				patchField(field.id, field)
			}
		}

		exportConfiguration()

		List<String> nonRetrievableFieldIds = getFields().findAll({ !it.retrievable }).collect({ it.id })

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, {
			row.data
		})

		then:
		matchContains(searchResult, row.expectedData)
		searchResult.searchHits.every({ searchHit ->
			nonRetrievableFieldIds.every({ nonRetrievableFieldId ->
				!searchHit.fields.containsKey(nonRetrievableFieldId)
			})
		})

		where:
		row << loadSearchSpecJson("/searchservices/test/integration/search/validQueriesRetrievable.json")
	}

	@Test
	def "Search with query (searchable) #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		resetFields({ field ->
			field.setSearchable(false)
			field.setWeight(1.0f)
		})

		if (row.fields) {
			for (def field : row.fields) {
				patchField(field.id, field)
			}
		}

		exportConfiguration()

		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, {
			row.data
		})

		then:
		matchContains(searchResult, row.expectedData)

		where:
		row << loadSearchSpecJson("/searchservices/test/integration/search/validQueriesSearchable.json")
	}

	@Test
	def "Pk field is returned by default"() {
		when:
		SnSearchResult searchResult = executeSearchQuery(INDEX_TYPE_ID, this.&createSearchQuery, this.&addDefaultSearchQuerySort)

		then:
		with(searchResult) {
			searchHits.size() == 4
			searchHits[0].fields.pk != null
			searchHits[1].fields.pk != null
			searchHits[2].fields.pk != null
			searchHits[3].fields.pk != null
		}
	}
}
