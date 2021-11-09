/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.validator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import org.junit.Test
import spock.lang.Specification

@UnitTest
class SingleRootItemConstraintViolationExceptionUnitTest extends Specification {

	@Test
	def "exception takes multiple integration object items"() {
		when:
		def e = new SingleRootItemConstraintViolationException("TestIO", itemModel("Unit"), itemModel("Catalog"))

		then:
		e.message.contains("TestIO")
		e.message.contains("Unit")
		e.message.contains("Catalog")
	}

	@Test
	def "exception takes list of integration object items"() {
		when:
		def e = new SingleRootItemConstraintViolationException("TestIO", Arrays.asList(itemModel("Product"), itemModel("Category")))

		then:
		e.message.contains("TestIO")
		e.message.contains("Product")
		e.message.contains("Category")
	}

	private IntegrationObjectItemModel itemModel(code) {
		Stub(IntegrationObjectItemModel) {
			getCode() >> code
		}
	}
}
