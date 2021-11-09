/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.validator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Specification

@UnitTest
class SingleRootItemValidatorUnitTest extends Specification {
	def rootItemValidator = new SingleRootItemValidator()

	@Test
	def "new root item throws exception when a different root already exists for the same IO"() {
		given:
		def io = Stub(IntegrationObjectModel) {
			getCode() >> "TestProduct"
		}

		and:
		io.getRootItem() >> rootItem(io, "Product")

		when:
		rootItemValidator.onValidate(rootItem(io, "Catalog"), null)

		then:
		def e = thrown InterceptorException
		e.message.contains("Catalog")
		e.message.contains("Product")
	}

	@Test
	def "Successfully validates the same root item that already exists"() {
		given:
		def itemCode = "Product"

		and:
		def io = Stub(IntegrationObjectModel) {
			getCode() >> "TestProduct"
		}

		and:
		io.getRootItem() >> rootItem(io, itemCode)

		when:
		rootItemValidator.onValidate(rootItem(io, itemCode), null)

		then:
		noExceptionThrown()
	}

	@Test
	def "validates a new root item when no other items exist"() {
		given:
		def io = Stub(IntegrationObjectModel) {
			getCode() >> "TestProduct"
		}

		and:
		io.getRootItem() >> null

		when:
		rootItemValidator.onValidate(rootItem(io, "Product"), null)

		then:
		noExceptionThrown()
	}

	@Test
	def "validates a new non-root item when no other items exist"() {
		given:
		final IntegrationObjectItemModel newItem =  item("Category")

		when:
		rootItemValidator.onValidate(newItem, null)

		then:
		noExceptionThrown()
	}

	private IntegrationObjectItemModel item(final String itemCode) {
		def nonRootItem = itemStubWithCode(itemCode)
		nonRootItem.getRoot() >> false
		nonRootItem
	}

	private IntegrationObjectItemModel rootItem(final IntegrationObjectModel integrationObject, final String itemCode) {
		def rootItem = itemStubWithCode(itemCode)
		rootItem.getRoot() >> true
		rootItem.getIntegrationObject() >> integrationObject
		rootItem
	}

	private IntegrationObjectItemModel itemStubWithCode(code) {
		Stub(IntegrationObjectItemModel) {
			getCode() >> code
		}
	}
}
