/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.unit.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.commerceservices.search.searchservices.impl.StoreSnQualifierProvider
import de.hybris.platform.commerceservices.search.searchservices.strategies.SnStoreSelectionStrategy
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration
import de.hybris.platform.searchservices.admin.data.SnIndexType
import de.hybris.platform.searchservices.core.service.SnContext
import de.hybris.platform.searchservices.core.service.SnQualifier
import de.hybris.platform.store.BaseStoreModel
import de.hybris.platform.store.services.BaseStoreService
import org.junit.Test
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

@UnitTest
public class StoreSnQualifierProviderSpec extends Specification {

	static final String INDEX_CONFIGURATION_ID = "indexConfiguration"
	static final String INDEX_TYPE_ID = "indexType"

	static final String STORE_UID1 = "store1"
	static final String STORE_UID2 = "store2"

	SnContext context = Mock()
	SnIndexConfiguration indexConfiguration = Mock()
	SnIndexType indexType = Mock()
	SnField field = Mock()
	ItemModel item = Mock()

	BaseStoreService baseStoreService = Mock()
	SnStoreSelectionStrategy snStoreSelectionStrategy = Mock()

	StoreSnQualifierProvider qualifierProvider

	def setup() {
		indexConfiguration.getId() >> INDEX_CONFIGURATION_ID
		indexType.getId() >> INDEX_TYPE_ID

		Map<String, Object> attributes = new HashMap<>()
		context.getAttributes() >> attributes
		context.getIndexConfiguration() >> indexConfiguration
		context.getIndexType() >> indexType

		qualifierProvider = new StoreSnQualifierProvider()
		qualifierProvider.setBaseStoreService(baseStoreService)
		qualifierProvider.setSnStoreSelectionStrategy(snStoreSelectionStrategy)
	}

	@Test
	def "Return supported qualifier classes"() {
		when:
		Set<Class<?>> qualifierClasses = qualifierProvider.getSupportedQualifierClasses()

		then:
		assertThat(qualifierClasses).contains(BaseStoreModel)
	}

	@Test
	def "Fail to modify supported qualifier classes"() {
		given:
		Set<Class<?>> qualifierClasses = qualifierProvider.getSupportedQualifierClasses()

		when:
		qualifierClasses.add(this.getClass())

		then:
		thrown(UnsupportedOperationException)
	}

	@Test
	def "Available qualifiers is empty"() {
		when:
		List<SnQualifier> qualifiers = qualifierProvider.getAvailableQualifiers(context)

		then:
		qualifiers != null
		qualifiers.isEmpty()
	}

	@Test
	def "Available qualifiers has single qualifier"() {
		given:
		BaseStoreModel store = new BaseStoreModel(uid: STORE_UID1)

		snStoreSelectionStrategy.getStores(INDEX_TYPE_ID) >> List.of(store)

		when:
		List<SnQualifier> qualifiers = qualifierProvider.getAvailableQualifiers(context)

		then:
		qualifiers != null
		qualifiers.size() == 1

		with(qualifiers[0]) {
			id == STORE_UID1
			getAs(BaseStoreModel) == store
		}
	}

	@Test
	def "Available qualifiers has multiple qualifiers"() {
		given:
		BaseStoreModel store1 = new BaseStoreModel(uid: STORE_UID1)
		BaseStoreModel store2 = new BaseStoreModel(uid: STORE_UID2)

		snStoreSelectionStrategy.getStores(INDEX_TYPE_ID) >> List.of(store1, store2)

		when:
		List<SnQualifier> qualifiers = qualifierProvider.getAvailableQualifiers(context)

		then:
		qualifiers != null
		qualifiers.size() == 2

		with(qualifiers[0]) {
			id == STORE_UID1
			getAs(BaseStoreModel) == store1
		}

		with(qualifiers[1]) {
			id == STORE_UID2
			getAs(BaseStoreModel) == store2
		}
	}

	@Test
	def "Current qualifiers is empty"() {
		when:
		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		then:
		qualifiers != null
		qualifiers.isEmpty()
	}

	@Test
	def "Current qualifiers has single qualifier"() {
		given:
		BaseStoreModel store = new BaseStoreModel(uid: STORE_UID1)

		baseStoreService.getCurrentBaseStore() >> store

		when:
		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		then:
		qualifiers != null
		qualifiers.size() == 1

		with(qualifiers[0]) {
			id == STORE_UID1
			getAs(BaseStoreModel) == store
		}
	}

	@Test
	def "Fail to get qualifier as not supported class"() {
		given:
		BaseStoreModel store = new BaseStoreModel(uid: STORE_UID1)

		baseStoreService.getCurrentBaseStore() >> store

		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		when:
		qualifiers.get(0).getAs(LanguageModel)

		then:
		thrown(IllegalArgumentException)
	}


	@Test
	def "Can get qualifier as BaseStoreModel class"() {
		given:
		BaseStoreModel store = new BaseStoreModel(uid: STORE_UID1)

		baseStoreService.getCurrentBaseStore() >> store

		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		when:
		boolean result = qualifiers.get(0).canGetAs(BaseStoreModel)

		then:
		result == true
	}

	@Test
	def "Cannot get qualifier as BaseStoreModel subclass"() {
		given:
		BaseStoreModel store = new BaseStoreModel(uid: STORE_UID1)

		baseStoreService.getCurrentBaseStore() >> store

		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		when:
		boolean result = qualifiers.get(0).canGetAs(ExtendedBaseStoreModel)

		then:
		result == false
	}

	@Test
	def "Cannot get qualifier as not supported class"() {
		given:
		BaseStoreModel store = new BaseStoreModel(uid: STORE_UID1)

		baseStoreService.getCurrentBaseStore() >> store

		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		when:
		boolean result = qualifiers.get(0).canGetAs(LanguageModel)

		then:
		result == false
	}

	static class ExtendedBaseStoreModel extends BaseStoreModel {
	}
}
