/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.unit.strategies.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.basecommerce.model.site.BaseSiteModel
import de.hybris.platform.commerceservices.search.searchservices.strategies.impl.DefaultSnProductIndexTypeSelectionStrategy
import de.hybris.platform.searchservices.admin.data.SnIndexType
import de.hybris.platform.searchservices.model.SnIndexTypeModel
import de.hybris.platform.site.BaseSiteService
import de.hybris.platform.store.BaseStoreModel
import de.hybris.platform.store.services.BaseStoreService
import org.junit.Test
import spock.lang.Specification

@UnitTest
public class DefaultSnProductIndexTypeSelectionStrategySpec extends Specification {

	static final String INDEX_TYPE_ID = "indexType"

	BaseSiteService baseSiteService = Mock()
	BaseStoreService baseStoreService = Mock()

	DefaultSnProductIndexTypeSelectionStrategy productIndexTypeSelectionStrategy

	def setup() {
		productIndexTypeSelectionStrategy = new DefaultSnProductIndexTypeSelectionStrategy()
		productIndexTypeSelectionStrategy.setBaseSiteService(baseSiteService)
		productIndexTypeSelectionStrategy.setBaseStoreService(baseStoreService)
	}

	@Test
	def "Cannot get index type"() {
		when:
		Optional<String> result = productIndexTypeSelectionStrategy.getProductIndexTypeId()

		then:
		result.isEmpty()
	}

	@Test
	def "Get index type from base store"() {
		given:
		BaseStoreModel store = Mock()
		SnIndexTypeModel indexType = Mock()

		baseStoreService.getCurrentBaseStore() >> store
		store.getProductIndexType() >> indexType
		indexType.getId() >> INDEX_TYPE_ID

		when:
		Optional<SnIndexType> result = productIndexTypeSelectionStrategy.getProductIndexTypeId()

		then:
		result.isPresent()
		result.get() == INDEX_TYPE_ID
	}

	@Test
	def "Get index type from base site"() {
		given:
		BaseSiteModel site = Mock()
		SnIndexTypeModel indexType = Mock()

		baseSiteService.getCurrentBaseSite() >> site
		site.getProductIndexType() >> indexType
		indexType.getId() >> INDEX_TYPE_ID

		when:
		Optional<String> result = productIndexTypeSelectionStrategy.getProductIndexTypeId()

		then:
		result.isPresent()
		result.get() == INDEX_TYPE_ID
	}
}
