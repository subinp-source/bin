/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.indexer.service.impl

import static org.assertj.core.api.Assertions.assertThat

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator
import de.hybris.platform.searchservices.core.service.SnQualifier
import de.hybris.platform.searchservices.document.data.SnDocument
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerFieldWrapper
import de.hybris.platform.searchservices.indexer.service.impl.ModelAttributeSnIndexerValueProvider

import org.junit.Test

import spock.lang.Specification


@UnitTest
public class ModelAttributeSnIndexerValueProviderSpec extends Specification {

	static final String FIELD_1_ID = "field1"
	static final String FIELD_2_ID = "field2"

	static final String EXPRESSION = "expression"

	SnIndexerContext indexerContext = Mock()
	ItemModel model = Mock()
	SnDocument document = new SnDocument()

	SnExpressionEvaluator snExpressionEvaluator = Mock()

	ModelAttributeSnIndexerValueProvider valueProvider

	def setup() {
		valueProvider = new ModelAttributeSnIndexerValueProvider()
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
		snExpressionEvaluator.evaluate(model, FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, model, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide null value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of()

		List<SnIndexerFieldWrapper> fieldWrappers = [
			new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		Object value = null
		snExpressionEvaluator.evaluate(model, FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, model, document)

		then:
		document.getFields().get(FIELD_1_ID) == null
	}

	@Test
	def "Provide localized value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID, localized: true)
		Map<String, String> valueProviderParameters = Map.of()
		SnQualifier qualifier1 = Mock()
		SnQualifier qualifier2 = Mock()
		List<SnQualifier> qualifiers = List.of(qualifier1, qualifier2)

		List<SnIndexerFieldWrapper> fieldWrappers = [
			new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters, qualifiers: qualifiers)
		]

		qualifier1.getAs(Locale) >> Locale.ENGLISH
		qualifier2.getAs(Locale) >> Locale.GERMAN
		Object value = Mock()
		snExpressionEvaluator.evaluate(model, FIELD_1_ID, List.of(Locale.ENGLISH, Locale.GERMAN)) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, model, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
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

		Object value1 = Mock()
		Object value2 = Mock()
		snExpressionEvaluator.evaluate(model, FIELD_1_ID) >> value1
		snExpressionEvaluator.evaluate(model, FIELD_2_ID) >> value2

		when:
		valueProvider.provide(indexerContext, fieldWrappers, model, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value1)
		document.getFields().get(FIELD_2_ID).is(value2)
	}

	@Test
	def "Provide value using expression"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ModelAttributeSnIndexerValueProvider.EXPRESSION_PARAM, EXPRESSION)

		List<SnIndexerFieldWrapper> fieldWrappers = [
			new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		Object value = Mock()
		snExpressionEvaluator.evaluate(model, EXPRESSION) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, model, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}
}
