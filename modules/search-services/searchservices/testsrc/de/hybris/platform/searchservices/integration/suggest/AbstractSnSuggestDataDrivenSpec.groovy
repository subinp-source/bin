/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.suggest

import static de.hybris.platform.searchservices.support.ItemListenerMode.CLASS

import de.hybris.platform.searchservices.support.ItemListener

@ItemListener(mode = CLASS)
abstract class AbstractSnSuggestDataDrivenSpec extends AbstractSnSuggestSpec {

	@Override
	def setupSpecWithSpring() {
		super.setupSpecWithSpring()
		createTestData()
		loadCoreData()
		loadData()
		executeFullIndexerOperation(INDEX_TYPE_ID)
	}

	@Override
	def cleanupSpecWithSpring() {
		deleteTestData()
		super.cleanupSpecWithSpring()
	}

	def loadData() {
		// empty
	}
}
