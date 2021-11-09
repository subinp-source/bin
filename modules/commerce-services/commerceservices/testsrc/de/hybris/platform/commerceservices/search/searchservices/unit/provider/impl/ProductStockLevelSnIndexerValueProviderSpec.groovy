/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.unit.provider.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.commerceservices.search.searchservices.provider.impl.ProductStockLevelSnIndexerValueProvider
import de.hybris.platform.commerceservices.search.searchservices.strategies.SnStoreSelectionStrategy
import de.hybris.platform.commerceservices.stock.CommerceStockService
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.admin.data.SnIndexType
import de.hybris.platform.searchservices.core.service.SnQualifier
import de.hybris.platform.searchservices.document.data.SnDocument
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerFieldWrapper
import de.hybris.platform.store.BaseStoreModel
import org.junit.Test
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

@UnitTest
public class ProductStockLevelSnIndexerValueProviderSpec extends Specification {

	static final String INDEX_TYPE_ID = "indexType"

	static final String FIELD_1_ID = "field1"
	static final String FIELD_2_ID = "field2"

	static final String QUALIFIER_TYPE_ID = "qualifierType"

	static final String STORE_1_UID = "store1"
	static final String STORE_2_UID = "store2"

	static final Long STOCK_LEVEL_10 = Long.valueOf(10)
	static final Long STOCK_LEVEL_20 = Long.valueOf(20)

	SnIndexerContext indexerContext = Mock()
	ProductModel product = Mock()
	SnDocument document = new SnDocument()

	CommerceStockService commerceStockService = Mock()
	SnStoreSelectionStrategy snStoreSelectionStrategy = Mock()

	ProductStockLevelSnIndexerValueProvider valueProvider

	def setup() {
		indexerContext.getIndexType() >> new SnIndexType(id: INDEX_TYPE_ID)

		valueProvider = new ProductStockLevelSnIndexerValueProvider()
		valueProvider.setCommerceStockService(commerceStockService)
		valueProvider.setSnStoreSelectionStrategy(snStoreSelectionStrategy)
	}

	@Test
	def "Return supported qualifier classes"() {
		when:
		Set<Class<?>> qualifierClasses = valueProvider.getSupportedQualifierClasses()

		then:
		assertThat(qualifierClasses).contains(BaseStoreModel)
	}

	@Test
	def "Fail to modify supported qualifier classes"() {
		given:
		Set<Class<?>> qualifierClasses = valueProvider.getSupportedQualifierClasses()

		when:
		qualifierClasses.add(this.getClass())

		then:
		thrown(UnsupportedOperationException)
	}

	@Test
	def "Provide value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of()

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		BaseStoreModel store = new BaseStoreModel(uid: STORE_1_UID)
		snStoreSelectionStrategy.getDefaultStore(INDEX_TYPE_ID) >> Optional.of(store)
		commerceStockService.getStockLevelForProductAndBaseStore(product, store) >> STOCK_LEVEL_10

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == STOCK_LEVEL_10
	}

	@Test
	def "Provide null value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of()

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		BaseStoreModel store = new BaseStoreModel(uid: STORE_1_UID)
		snStoreSelectionStrategy.getDefaultStore(INDEX_TYPE_ID) >> Optional.of(store)
		commerceStockService.getStockLevelForProductAndBaseStore(product, store) >> null

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == null
	}

	@Test
	def "Provide qualified value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID, localized: true, qualifierTypeId: QUALIFIER_TYPE_ID)
		Map<String, String> valueProviderParameters = Map.of()
		SnQualifier qualifier1 = Mock()
		SnQualifier qualifier2 = Mock()
		List<SnQualifier> qualifiers = List.of(qualifier1, qualifier2)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters, qualifiers: qualifiers)
		]

		BaseStoreModel store1 = new BaseStoreModel(uid: STORE_1_UID)
		qualifier1.getId() >> STORE_1_UID
		qualifier1.getAs(BaseStoreModel) >> store1

		BaseStoreModel store2 = new BaseStoreModel(uid: STORE_2_UID)
		qualifier2.getId() >> STORE_2_UID
		qualifier2.getAs(BaseStoreModel) >> store2

		commerceStockService.getStockLevelForProductAndBaseStore(product, store1) >> STOCK_LEVEL_10
		commerceStockService.getStockLevelForProductAndBaseStore(product, store2) >> STOCK_LEVEL_20

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == [
				(STORE_1_UID): STOCK_LEVEL_10,
				(STORE_2_UID): STOCK_LEVEL_20
		]
	}

	@Test
	def "Provide values for multiple fields"() {
		given:
		SnField field1 = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters1 = Map.of()

		SnField field2 = new SnField(id: FIELD_2_ID)
		Map<String, String> valueProviderParameters2 = Map.of()

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field1, valueProviderParameters: valueProviderParameters1),
				new DefaultSnIndexerFieldWrapper(field: field2, valueProviderParameters: valueProviderParameters2)
		]

		BaseStoreModel store = new BaseStoreModel(uid: STORE_1_UID)
		snStoreSelectionStrategy.getDefaultStore(INDEX_TYPE_ID) >> Optional.of(store)
		commerceStockService.getStockLevelForProductAndBaseStore(product, store) >> STOCK_LEVEL_10

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == STOCK_LEVEL_10
		document.getFields().get(FIELD_2_ID) == STOCK_LEVEL_10
	}
}
