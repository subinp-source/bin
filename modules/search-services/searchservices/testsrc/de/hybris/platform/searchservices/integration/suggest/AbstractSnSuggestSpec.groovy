/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.suggest

import de.hybris.platform.searchservices.integration.AbstractSnIntegrationSpec
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery
import de.hybris.platform.searchservices.suggest.data.SnSuggestResult
import de.hybris.platform.searchservices.suggest.service.SnSuggestRequest
import de.hybris.platform.searchservices.suggest.service.SnSuggestResponse
import de.hybris.platform.searchservices.suggest.service.SnSuggestService

import java.nio.charset.StandardCharsets

import javax.annotation.Resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.TypeFactory

import spock.lang.Shared

abstract class AbstractSnSuggestSpec extends AbstractSnIntegrationSpec {

	@Resource
	SnSuggestService snSuggestService

	@Shared
	ObjectMapper objectMapper

	def setupSpec() {
		objectMapper = new ObjectMapper()
	}

	def loadCoreData() {
		importData("/searchservices/test/integration/snLanguages.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snCurrencies.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexConfiguration.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexConfigurationAddLanguages.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexConfigurationAddCurrencies.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexType.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexTypeAddFields.impex", StandardCharsets.UTF_8.name())
	}

	def loadDefaultData() {
		importData("/searchservices/test/integration/snCatalogBase.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snCatalogSampleProducts.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/suggest/snIndexTypeUpdatesForSuggest.impex", StandardCharsets.UTF_8.name())
	}

	SnSuggestResult executeSuggestQuery(String indexTypeId, Closure creator, Closure... decorators) {
		final SnSuggestQuery suggestQuery = creator()

		for (Closure decorator : decorators) {
			decorator(suggestQuery)
		}

		final SnSuggestRequest suggestRequest = snSuggestService.createSuggestRequest(indexTypeId, suggestQuery)
		final SnSuggestResponse suggestResponse = snSuggestService.suggest(suggestRequest)

		return suggestResponse.getSuggestResult()
	}

	SnSuggestQuery createSuggestQuery() {
		return new SnSuggestQuery()
	}

	List<SnSuggestSpec> loadSuggestSpecJson(String resource) {
		URL url = AbstractSnSuggestSpec.class.getResource(resource)
		final TypeFactory typeFactory = objectMapper.getTypeFactory()
		final CollectionType type = typeFactory.constructCollectionType(List.class, SnSuggestSpec.class)

		return objectMapper.readValue(url, type)
	}

	static class SnSuggestSpec {
		String testId
		String description
		List<Object> fields
		SnSuggestQuery data
		Object expectedData
	}
}
