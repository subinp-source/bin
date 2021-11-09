/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.admin

import de.hybris.platform.searchservices.integration.AbstractSnIntegrationSpec


abstract class AbstractSnAdminSpec extends AbstractSnIntegrationSpec {

	def setup() {
		loadData()
	}

	def loadData() {
		// empty
	}
}
