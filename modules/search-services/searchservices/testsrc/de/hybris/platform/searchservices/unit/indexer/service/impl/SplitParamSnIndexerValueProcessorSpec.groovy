/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.indexer.service.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.enums.SnFieldType
import de.hybris.platform.searchservices.indexer.SnIndexerException
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerFieldWrapper
import de.hybris.platform.searchservices.indexer.service.impl.SplitParamSnIndexerValueProcessor

import org.junit.Test

import spock.lang.Specification
import spock.lang.Unroll


@UnitTest
@Unroll
public class SplitParamSnIndexerValueProcessorSpec extends Specification {

	static final String FIELD_ID = "field"

	static final String LANGUAGE_ID1 = "en"
	static final String LANGUAGE_ID2 = "de"

	static final String QUALIFIER_TYPE_ID = "qualifierType"
	static final String QUALIFIER_ID1 = "qualifier1"
	static final String QUALIFIER_ID2 = "qualifier2"

	SnIndexerContext indexerContext = Mock()

	SplitParamSnIndexerValueProcessor valueProcessor

	def setup() {
		valueProcessor = new SplitParamSnIndexerValueProcessor()
	}

	@Test
	def "Don't split value"() {
		given:
		def value = "a b"

		SnField field = new SnField(id: FIELD_ID, fieldType: SnFieldType.STRING, multiValued: false, localized: false)
		Map<String, String> valueProviderParameters = Map.of()
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == value
	}

	@Test
	def "Cannot split value on not supported field type #testId: fieldType=#fieldType"(testId, fieldType) {
		given:
		def value = "a b"

		SnField field = new SnField(id: FIELD_ID, fieldType: fieldType, multiValued: false, localized: false)
		Map<String, String> valueProviderParameters = Map.of(SplitParamSnIndexerValueProcessor.SPLIT_PARAM, Boolean.TRUE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		thrown(SnIndexerException)

		where:
		testId | fieldType
		1      | SnFieldType.BOOLEAN
		2      | SnFieldType.INTEGER
		3      | SnFieldType.LONG
		4      | SnFieldType.FLOAT
		5      | SnFieldType.DOUBLE
		6      | SnFieldType.DATE_TIME
	}

	@Test
	def "Split value #testId: fieldType='#fieldType'"(testId, fieldType) {
		given:
		def value = "a b"

		SnField field = new SnField(id: FIELD_ID, fieldType: fieldType, multiValued: false, localized: false)
		Map<String, String> valueProviderParameters = Map.of(SplitParamSnIndexerValueProcessor.SPLIT_PARAM, Boolean.TRUE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == List.of("a", "b")

		where:
		testId | fieldType
		1      | SnFieldType.STRING
		2      | SnFieldType.TEXT
	}

	@Test
	def "Split value on multi-valued field"() {
		given:
		def value = List.of("a b", "c d")

		SnField field = new SnField(id: FIELD_ID, fieldType: SnFieldType.STRING, multiValued: true, localized: false)
		Map<String, String> valueProviderParameters = Map.of(SplitParamSnIndexerValueProcessor.SPLIT_PARAM, Boolean.TRUE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == List.of("a", "b", "c", "d")
	}

	@Test
	def "Split value on localized field"() {
		given:
		def value = [
			LANGUAGE_ID1: "a b",
			LANGUAGE_ID2: "c d"
		]

		SnField field = new SnField(id: FIELD_ID, fieldType: SnFieldType.STRING, multiValued: false, localized: true)
		Map<String, String> valueProviderParameters = Map.of(SplitParamSnIndexerValueProcessor.SPLIT_PARAM, Boolean.TRUE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == [
			LANGUAGE_ID1: List.of("a", "b"),
			LANGUAGE_ID2: List.of("c", "d")
		]
	}

	@Test
	def "Split value on qualified field"(testId, fieldType) {
		given:
		def value = [
			QUALIFIER_ID1: "a b",
			QUALIFIER_ID2: "c d"
		]

		SnField field = new SnField(id: FIELD_ID, fieldType: SnFieldType.STRING, multiValued: false, localized: false, qualifierTypeId: QUALIFIER_TYPE_ID)
		Map<String, String> valueProviderParameters = Map.of(SplitParamSnIndexerValueProcessor.SPLIT_PARAM, Boolean.TRUE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == [
			QUALIFIER_ID1: List.of("a", "b"),
			QUALIFIER_ID2: List.of("c", "d")
		]

		where:
		testId | fieldType
		1      | SnFieldType.STRING
		2      | SnFieldType.TEXT
	}


	@Test
	def "Split value with regex #testId: #regex -> #expectedResult"(testId, value, splitRegex, expectedResult) {
		given:
		SnField field = new SnField(id: FIELD_ID, fieldType: SnFieldType.STRING, multiValued: false, localized: false)
		Map<String, String> valueProviderParameters = Map.of(SplitParamSnIndexerValueProcessor.SPLIT_PARAM, Boolean.TRUE.toString(), SplitParamSnIndexerValueProcessor.SPLIT_REGEX_PARAM, splitRegex)
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == expectedResult

		where:
		testId | value   | splitRegex || expectedResult
		1      | "a/b"   | "\\/"      || List.of("a", "b")
		2      | "a/b|c" | "\\/|\\|"  || List.of("a", "b", "c")
		3      | "a.b.c" | "\\."      || List.of("a", "b", "c")
	}
}
