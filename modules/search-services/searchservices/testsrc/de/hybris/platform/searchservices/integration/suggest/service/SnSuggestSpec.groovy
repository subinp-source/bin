/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.suggest.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.integration.suggest.AbstractSnSuggestDataDrivenSpec
import de.hybris.platform.searchservices.suggest.data.SnSuggestResult
import org.junit.Test
import spock.lang.Unroll

import java.nio.charset.StandardCharsets

import static de.hybris.platform.searchservices.support.util.ObjectUtils.matchContains

@Unroll
@IntegrationTest
class SnSuggestSpec extends AbstractSnSuggestDataDrivenSpec {

	@Override
	public Object loadData() {
		loadDefaultData()
	}

	def setup() {
		importData("/searchservices/test/integration/snIndexTypeAddFields.impex", StandardCharsets.UTF_8.name())
	}

	@Test
	def "Search with query (useForSuggesting) #row.testId: description=#row.description, data=#row.data"(row) {
		given:
		resetFields({ field ->
			field.setUseForSuggesting(false)
		})

		if (row.fields) {
			for (def field : row.fields) {
				patchField(field.id, field)
			}
		}

		executeFullIndexerOperation(INDEX_TYPE_ID)

		when:
		SnSuggestResult suggestResult = executeSuggestQuery(INDEX_TYPE_ID, {
			row.data
		})

		then:
		matchContains(suggestResult, row.expectedData)

		where:
		row << loadSuggestSpecJson("/searchservices/test/integration/suggest/validQueriesUseForSuggesting.json")
	}
}
