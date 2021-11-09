/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.unit.provider.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.commerceservices.search.searchservices.provider.impl.ProductAttributeSnIndexerValueProvider
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator
import de.hybris.platform.searchservices.core.service.SnQualifier
import de.hybris.platform.searchservices.document.data.SnDocument
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerFieldWrapper
import de.hybris.platform.variants.model.VariantProductModel
import org.junit.Test
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

@UnitTest
public class ProductAttributeSnIndexerValueProviderSpec extends Specification {

	static final String FIELD_1_ID = "field1"
	static final String FIELD_2_ID = "field2"

	static final PK PRODUCT_PK = PK.fromLong(1)
	static final PK VARIANT_PRODUCT_PK = PK.fromLong(2)

	SnIndexerContext indexerContext = Mock()
	ProductModel product = Mock()
	SnDocument document = new SnDocument()

	SnExpressionEvaluator snExpressionEvaluator = Mock()

	ProductAttributeSnIndexerValueProvider valueProvider

	def setup() {
		product.getPk() >> PRODUCT_PK

		valueProvider = new ProductAttributeSnIndexerValueProvider()
		valueProvider.setSnExpressionEvaluator(snExpressionEvaluator)
	}

	@Test
	def "Return supported qualifier classes"() {
		when:
		Set<Class<?>> qualifierClasses = valueProvider.getSupportedQualifierClasses()

		then:
		assertThat(qualifierClasses).contains(Locale)
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

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(product), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide null value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ProductAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == null
	}

	@Test
	def "Provide localized value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID, localized: true)
		Map<String, String> valueProviderParameters = Map.of(ProductAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID)
		SnQualifier qualifier1 = Mock()
		SnQualifier qualifier2 = Mock()
		List<SnQualifier> qualifiers = List.of(qualifier1, qualifier2)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters, qualifiers: qualifiers)
		]

		qualifier1.getAs(Locale) >> Locale.ENGLISH
		qualifier2.getAs(Locale) >> Locale.GERMAN
		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(product), FIELD_1_ID, List.of(Locale.ENGLISH, Locale.GERMAN)) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide values for multiple fields"() {
		given:
		SnField field1 = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters1 = Map.of(ProductAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID)

		SnField field2 = new SnField(id: FIELD_2_ID)
		Map<String, String> valueProviderParameters2 = Map.of(ProductAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_2_ID)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field1, valueProviderParameters: valueProviderParameters1),
				new DefaultSnIndexerFieldWrapper(field: field2, valueProviderParameters: valueProviderParameters2)
		]

		Object value1 = Mock()
		Object value2 = Mock()
		snExpressionEvaluator.evaluate(Set.of(product), FIELD_1_ID) >> value1
		snExpressionEvaluator.evaluate(Set.of(product), FIELD_2_ID) >> value2

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value1)
		document.getFields().get(FIELD_2_ID).is(value2)
	}

	@Test
	def "Provide value using expression"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ProductAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(product), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product with variants with default product selector"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ProductAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		VariantProductModel variantProduct = Mock()
		variantProduct.getPk() >> VARIANT_PRODUCT_PK
		variantProduct.getBaseProduct() >> product

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(variantProduct), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, variantProduct, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product with variants with CURRENT product selector"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(
				ProductAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID,
				ProductAttributeSnIndexerValueProvider.PRODUCT_SELECTOR_PARAM, ProductAttributeSnIndexerValueProvider.PRODUCT_SELECTOR_VALUE_CURRENT)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		VariantProductModel variantProduct = Mock()
		variantProduct.getPk() >> VARIANT_PRODUCT_PK
		variantProduct.getBaseProduct() >> product

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(variantProduct), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, variantProduct, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product with variants with CURRENT_PARENT product selector"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(
				ProductAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID,
				ProductAttributeSnIndexerValueProvider.PRODUCT_SELECTOR_PARAM, ProductAttributeSnIndexerValueProvider.PRODUCT_SELECTOR_VALUE_CURRENT_PARENT)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		VariantProductModel variantProduct = Mock()
		variantProduct.getPk() >> VARIANT_PRODUCT_PK
		variantProduct.getBaseProduct() >> product

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(variantProduct, product), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, variantProduct, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product with variants with BASE product selector"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(
				ProductAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID,
				ProductAttributeSnIndexerValueProvider.PRODUCT_SELECTOR_PARAM, ProductAttributeSnIndexerValueProvider.PRODUCT_SELECTOR_VALUE_BASE)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		VariantProductModel variantProduct = Mock()
		variantProduct.getPk() >> VARIANT_PRODUCT_PK
		variantProduct.getBaseProduct() >> product

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(product), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, variantProduct, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}
}
