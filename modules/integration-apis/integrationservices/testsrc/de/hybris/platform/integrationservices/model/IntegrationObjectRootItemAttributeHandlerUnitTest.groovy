/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.validator.SingleRootItemConstraintViolationException
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class IntegrationObjectRootItemAttributeHandlerUnitTest extends Specification {
	private final IntegrationObjectRootItemAttributeHandler handler = new IntegrationObjectRootItemAttributeHandler()

	@Shared
	def root = rootItem()
	
	@Unroll
	@Test
	def "get returns #condition"()
	{
		expect:
		handler.get(integrationObjectWithItems(items)) == expected

		where:
		items          | expected | condition
		[]             | null     | "null when no items are defined"
		[item()]       | null     | "null when no rootItem is defined"
		[root]         | root     | "root item when 1 root item is defined on the integration object"
		[item(), root] | root     | "root item when 1 root and other non root items exist for the same integration object"
	}

	@Test
	def "throws exception when more than 1 root item is defined"()
	{
		given:
		final IntegrationObjectModel io = integrationObjectWithItems([rootItem(), root])

		when:
		handler.get(io)

		then:
		thrown(SingleRootItemConstraintViolationException)
	}

	private IntegrationObjectModel integrationObjectWithItems(final List<IntegrationObjectItemModel> items) {
		Stub(IntegrationObjectModel) {
			getItems() >> items
		}
	}

	private IntegrationObjectItemModel item() {
		Stub(IntegrationObjectItemModel) {
			getRoot() >> false
		}
	}

	private IntegrationObjectItemModel rootItem() {
		Stub(IntegrationObjectItemModel) {
			getRoot() >> true
		}
	}
}
