/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.indexer

import de.hybris.platform.searchservices.integration.AbstractSnIntegrationSpec

import java.nio.charset.StandardCharsets


abstract class AbstractSnIndexerSpec extends AbstractSnIntegrationSpec {

	def setup() {
		createTestData()
		loadCoreData()
		loadData()
	}

	def cleanup() {
		deleteTestData()
	}

	def loadData() {
		// empty
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
		importData("/searchservices/test/integration/snCatalogProducts.impex", StandardCharsets.UTF_8.name())
	}
}
