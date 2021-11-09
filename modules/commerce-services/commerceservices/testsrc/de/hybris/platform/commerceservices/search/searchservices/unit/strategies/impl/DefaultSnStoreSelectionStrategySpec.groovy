/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.unit.strategies.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.basecommerce.model.site.BaseSiteModel
import de.hybris.platform.commerceservices.search.searchservices.strategies.impl.DefaultSnStoreSelectionStrategy
import de.hybris.platform.searchservices.admin.dao.SnIndexTypeDao
import de.hybris.platform.searchservices.model.SnIndexTypeModel
import de.hybris.platform.store.BaseStoreModel
import org.junit.Test
import spock.lang.Specification

@UnitTest
public class DefaultSnStoreSelectionStrategySpec extends Specification {

	static final String INDEX_TYPE_ID = "indexType"

	static final String STORE_UID1 = "store1"
	static final String STORE_UID2 = "store2"

	SnIndexTypeDao snIndexTypeDao = Mock()

	DefaultSnStoreSelectionStrategy storeSelectionStrategy

	def setup() {
		storeSelectionStrategy = new DefaultSnStoreSelectionStrategy()
		storeSelectionStrategy.setSnIndexTypeDao(snIndexTypeDao)
	}

	@Test
	def "Cannot get default store by index type id"() {
		given:
		SnIndexTypeModel indexType = Mock()

		snIndexTypeDao.findIndexTypeById(INDEX_TYPE_ID) >> Optional.of(indexType)

		when:
		Optional<BaseStoreModel> result = storeSelectionStrategy.getDefaultStore(INDEX_TYPE_ID)

		then:
		result.isEmpty()
	}

	@Test
	def "Get default store by index type id"() {
		given:
		SnIndexTypeModel indexType = Mock()
		BaseStoreModel store1 = Mock()
		BaseStoreModel store2 = Mock()

		snIndexTypeDao.findIndexTypeById(INDEX_TYPE_ID) >> Optional.of(indexType)
		indexType.getStores() >> List.of(store1, store2)

		when:
		Optional<BaseStoreModel> result = storeSelectionStrategy.getDefaultStore(INDEX_TYPE_ID)

		then:
		result.isPresent()
		result.get() == store1
	}

	@Test
	def "Cannot get default store by index type model"() {
		given:
		SnIndexTypeModel indexType = Mock()

		when:
		Optional<BaseStoreModel> result = storeSelectionStrategy.getDefaultStore(indexType)

		then:
		result.isEmpty()
	}

	@Test
	def "Get default store by index type model"() {
		given:
		SnIndexTypeModel indexType = Mock()
		BaseStoreModel store1 = Mock()
		BaseStoreModel store2 = Mock()

		indexType.getStores() >> List.of(store1, store2)

		when:
		Optional<BaseStoreModel> result = storeSelectionStrategy.getDefaultStore(indexType)

		then:
		result.isPresent()
		result.get() == store1
	}

	@Test
	def "Get default store by index type model (from site1)"() {
		given:
		SnIndexTypeModel indexType = Mock()
		BaseSiteModel site1 = Mock()
		BaseSiteModel site2 = Mock()
		BaseStoreModel store1 = Mock()
		BaseStoreModel store2 = Mock()

		indexType.getSites() >> List.of(site1, site2)
		site1.getStores() >> List.of(store1, store2)

		when:
		Optional<BaseStoreModel> result = storeSelectionStrategy.getDefaultStore(indexType)

		then:
		result.isPresent()
		result.get() == store1
	}

	@Test
	def "Get default store by index type model (from site2)"() {
		given:
		SnIndexTypeModel indexType = Mock()
		BaseSiteModel site1 = Mock()
		BaseSiteModel site2 = Mock()
		BaseStoreModel store1 = Mock()
		BaseStoreModel store2 = Mock()

		indexType.getSites() >> List.of(site1, site2)
		site2.getStores() >> List.of(store1, store2)

		when:
		Optional<BaseStoreModel> result = storeSelectionStrategy.getDefaultStore(indexType)

		then:
		result.isPresent()
		result.get() == store1
	}

	@Test
	def "Cannot get stores by index type id"() {
		given:
		SnIndexTypeModel indexType = Mock()

		snIndexTypeDao.findIndexTypeById(INDEX_TYPE_ID) >> Optional.of(indexType)

		when:
		List<BaseStoreModel> result = storeSelectionStrategy.getStores(INDEX_TYPE_ID)

		then:
		result != null
		result.isEmpty()
	}

	@Test
	def "Get stores by index type id"() {
		given:
		SnIndexTypeModel indexType = Mock()
		BaseStoreModel store1 = Mock()
		BaseStoreModel store2 = Mock()

		snIndexTypeDao.findIndexTypeById(INDEX_TYPE_ID) >> Optional.of(indexType)
		indexType.getStores() >> List.of(store1, store2)

		when:
		List<BaseStoreModel> result = storeSelectionStrategy.getStores(INDEX_TYPE_ID)

		then:
		result != null
		result.size() == 2
		result[0] == store1
		result[1] == store2
	}

	@Test
	def "Cannot get stores by index type model"() {
		given:
		SnIndexTypeModel indexType = Mock()

		when:
		List<BaseStoreModel> result = storeSelectionStrategy.getStores(indexType)

		then:
		result != null
		result.isEmpty()
	}

	@Test
	def "Get stores by index type model"() {
		given:
		SnIndexTypeModel indexType = Mock()
		BaseStoreModel store1 = Mock()
		BaseStoreModel store2 = Mock()

		indexType.getStores() >> List.of(store1, store2)

		when:
		List<BaseStoreModel> result = storeSelectionStrategy.getStores(indexType)

		then:
		result != null
		result.size() == 2
		result[0] == store1
		result[1] == store2
	}

	@Test
	def "Get stores by index type model (from site1)"() {
		given:
		SnIndexTypeModel indexType = Mock()
		BaseSiteModel site1 = Mock()
		BaseSiteModel site2 = Mock()
		BaseStoreModel store1 = Mock()
		BaseStoreModel store2 = Mock()

		indexType.getSites() >> List.of(site1, site2)
		site1.getStores() >> List.of(store1, store2)

		when:
		List<BaseStoreModel> result = storeSelectionStrategy.getStores(indexType)

		then:
		result != null
		result.size() == 2
		result[0] == store1
		result[1] == store2
	}

	@Test
	def "Get stores by index type model (from site2)"() {
		given:
		SnIndexTypeModel indexType = Mock()
		BaseSiteModel site1 = Mock()
		BaseSiteModel site2 = Mock()
		BaseStoreModel store1 = Mock()
		BaseStoreModel store2 = Mock()

		indexType.getSites() >> List.of(site1, site2)
		site2.getStores() >> List.of(store1, store2)

		when:
		List<BaseStoreModel> result = storeSelectionStrategy.getStores(indexType)

		then:
		result != null
		result.size() == 2
		result[0] == store1
		result[1] == store2
	}
}
