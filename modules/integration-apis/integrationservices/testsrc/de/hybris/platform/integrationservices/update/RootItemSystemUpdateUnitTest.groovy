/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.update

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.search.SearchResult
import org.junit.Test
import spock.lang.Specification

@UnitTest
class RootItemSystemUpdateUnitTest extends Specification {

	def systemUpdate = new RootItemSystemUpdate()
	def flexSearch = Mock(FlexibleSearchService)
	def modelService = Mock(ModelService)

	def setup() {
		systemUpdate.setFlexibleSearchService(flexSearch)
		systemUpdate.setModelService(modelService)
	}

	@Test
	def "no items to update"() {
		given:
		def searchResult = Mock(SearchResult) {
			getResult() >> []
		}
		and:
		flexSearch.search(_ as FlexibleSearchQuery) >> searchResult

		when:
		systemUpdate.updateNullRootItems()

		then:
		1 * modelService.saveAll([])
	}

	@Test
	def "integration object items without a set root item should have root item set to false"() {
		given:
		def item = Mock(IntegrationObjectItemModel) {
			getRootItem() >> null
		}
		and:
		def searchResult = Mock(SearchResult) {
			getResult() >> [item]
		}
		and:
		flexSearch.search(_ as FlexibleSearchQuery) >> searchResult

		when:
		systemUpdate.updateNullRootItems()

		then:
		1 * item.setRoot(false)
		1 * modelService.saveAll([item])
	}
}
