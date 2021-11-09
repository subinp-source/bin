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
import de.hybris.platform.searchservices.indexer.service.impl.OptionalParamSnIndexerValueProcessor

import org.junit.Test

import spock.lang.Specification
import spock.lang.Unroll


@UnitTest
@Unroll
public class OptionalParamSnIndexerValueProcessorSpec extends Specification {

	static final String FIELD_ID = "field"

	static final String LANGUAGE_ID = "en"

	static final String QUALIFIER_TYPE_ID = "qualifierType"
	static final String QUALIFIER_ID = "qualifier"

	SnIndexerContext indexerContext = Mock()

	OptionalParamSnIndexerValueProcessor valueProcessor

	def setup() {
		valueProcessor = new OptionalParamSnIndexerValueProcessor()
	}

	@Test
	def "Value is optional by default #testId: fieldType=#fieldType, value=#value"(testId, fieldType, value) {
		given:
		SnField field = new SnField(id: FIELD_ID, fieldType: fieldType, multiValued: false, localized: false)
		Map<String, String> valueProviderParameters = Map.of()
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == value

		where:
		testId | fieldType             | value
		1      | SnFieldType.STRING    | null
		2      | SnFieldType.STRING    | "a"
		3      | SnFieldType.TEXT      | null
		4      | SnFieldType.TEXT      | "a"
		5      | SnFieldType.BOOLEAN   | null
		6      | SnFieldType.BOOLEAN   | Boolean.TRUE
		7      | SnFieldType.INTEGER   | null
		8      | SnFieldType.INTEGER   | 2 as Integer
		9      | SnFieldType.LONG      | null
		10     | SnFieldType.LONG      | 2 as Long
		11     | SnFieldType.FLOAT     | null
		12     | SnFieldType.FLOAT     | 2.2f as Float
		13     | SnFieldType.DOUBLE    | null
		14     | SnFieldType.DOUBLE    | 2.2d as Double
		15     | SnFieldType.DATE_TIME | null
		16     | SnFieldType.DATE_TIME | new Date()
	}

	def "Value is optional #testId: fieldType=#fieldType, value=#value"(testId, fieldType, value) {
		given:
		SnField field = new SnField(id: FIELD_ID, fieldType: fieldType, multiValued: false, localized: false)
		Map<String, String> valueProviderParameters = Map.of(OptionalParamSnIndexerValueProcessor.OPTIONAL_PARAM, Boolean.TRUE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == value

		where:
		testId | fieldType             | value
		1      | SnFieldType.STRING    | null
		2      | SnFieldType.STRING    | "a"
		3      | SnFieldType.TEXT      | null
		4      | SnFieldType.TEXT      | "a"
		5      | SnFieldType.BOOLEAN   | null
		6      | SnFieldType.BOOLEAN   | Boolean.TRUE
		7      | SnFieldType.INTEGER   | null
		8      | SnFieldType.INTEGER   | 2 as Integer
		9      | SnFieldType.LONG      | null
		10     | SnFieldType.LONG      | 2 as Long
		11     | SnFieldType.FLOAT     | null
		12     | SnFieldType.FLOAT     | 2.2f as Float
		13     | SnFieldType.DOUBLE    | null
		14     | SnFieldType.DOUBLE    | 2.2d as Double
		15     | SnFieldType.DATE_TIME | null
		16     | SnFieldType.DATE_TIME | new Date()
	}

	@Test
	def "Value is not optional #testId: fieldType=#fieldType, value=#value"(testId, fieldType, value) {
		given:
		SnField field = new SnField(id: FIELD_ID, fieldType: fieldType, multiValued: false, localized: false)
		Map<String, String> valueProviderParameters = Map.of(OptionalParamSnIndexerValueProcessor.OPTIONAL_PARAM, Boolean.FALSE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		thrown(SnIndexerException)

		where:
		testId | fieldType             | value
		1      | SnFieldType.STRING    | null
		2      | SnFieldType.TEXT      | null
		3      | SnFieldType.BOOLEAN   | null
		4      | SnFieldType.INTEGER   | null
		5      | SnFieldType.LONG      | null
		6      | SnFieldType.FLOAT     | null
		7      | SnFieldType.DOUBLE    | null
		8      | SnFieldType.DATE_TIME | null
	}

	def "Value is optional on multi-valued field #testId: fieldType=#fieldType, value=#value"(testId, fieldType, value) {
		given:
		SnField field = new SnField(id: FIELD_ID, fieldType: fieldType, multiValued: true, localized: false)
		Map<String, String> valueProviderParameters = Map.of(OptionalParamSnIndexerValueProcessor.OPTIONAL_PARAM, Boolean.TRUE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == value

		where:
		testId | fieldType             | value
		1      | SnFieldType.STRING    | null
		2      | SnFieldType.STRING    | List.of()
		3      | SnFieldType.STRING    | List.of("a")
		4      | SnFieldType.TEXT      | null
		5      | SnFieldType.TEXT      | List.of()
		6      | SnFieldType.TEXT      | List.of("a")
		7      | SnFieldType.BOOLEAN   | null
		8      | SnFieldType.BOOLEAN   | List.of()
		9      | SnFieldType.BOOLEAN   | List.of(Boolean.TRUE)
		10     | SnFieldType.INTEGER   | null
		11     | SnFieldType.INTEGER   | List.of()
		12     | SnFieldType.INTEGER   | List.of(2 as Integer)
		13     | SnFieldType.LONG      | null
		14     | SnFieldType.LONG      | List.of()
		15     | SnFieldType.LONG      | List.of(2 as Long)
		16     | SnFieldType.FLOAT     | null
		17     | SnFieldType.FLOAT     | List.of()
		18     | SnFieldType.FLOAT     | List.of(2.2f as Float)
		19     | SnFieldType.DOUBLE    | null
		20     | SnFieldType.DOUBLE    | List.of()
		21     | SnFieldType.DOUBLE    | List.of(2.2d as Double)
		22     | SnFieldType.DATE_TIME | null
		23     | SnFieldType.DATE_TIME | List.of()
		24     | SnFieldType.DATE_TIME | List.of(new Date())
	}

	@Test
	def "Value is not optional on multi-valued field #testId: fieldType=#fieldType, value=#value"(testId, fieldType, value) {
		given:
		SnField field = new SnField(id: FIELD_ID, fieldType: fieldType, multiValued: true, localized: false)
		Map<String, String> valueProviderParameters = Map.of(OptionalParamSnIndexerValueProcessor.OPTIONAL_PARAM, Boolean.FALSE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		thrown(SnIndexerException)

		where:
		testId | fieldType             | value
		1      | SnFieldType.STRING    | null
		2      | SnFieldType.STRING    | List.of()
		3      | SnFieldType.TEXT      | null
		4      | SnFieldType.TEXT      | List.of()
		5      | SnFieldType.BOOLEAN   | null
		6      | SnFieldType.BOOLEAN   | List.of()
		7      | SnFieldType.INTEGER   | null
		8      | SnFieldType.INTEGER   | List.of()
		9      | SnFieldType.LONG      | null
		10     | SnFieldType.LONG      | List.of()
		11     | SnFieldType.FLOAT     | null
		12     | SnFieldType.FLOAT     | List.of()
		13     | SnFieldType.DOUBLE    | null
		14     | SnFieldType.DOUBLE    | List.of()
		15     | SnFieldType.DATE_TIME | null
		16     | SnFieldType.DATE_TIME | List.of()
	}

	def "Value is optional on localized field #testId: fieldType=#fieldType, value=#value"(testId, fieldType, value) {
		given:
		SnField field = new SnField(id: FIELD_ID, fieldType: fieldType, multiValued: false, localized: true)
		Map<String, String> valueProviderParameters = Map.of(OptionalParamSnIndexerValueProcessor.OPTIONAL_PARAM, Boolean.TRUE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == value

		where:
		testId | fieldType             | value
		1      | SnFieldType.STRING    | null
		2      | SnFieldType.STRING    | Map.of()
		3      | SnFieldType.STRING    | Map.of(LANGUAGE_ID, "a")
		4      | SnFieldType.TEXT      | null
		5      | SnFieldType.TEXT      | Map.of()
		6      | SnFieldType.TEXT      | Map.of(LANGUAGE_ID, "a")
		7      | SnFieldType.BOOLEAN   | null
		8      | SnFieldType.BOOLEAN   | Map.of()
		9      | SnFieldType.BOOLEAN   | Map.of(LANGUAGE_ID, Boolean.TRUE)
		10     | SnFieldType.INTEGER   | null
		11     | SnFieldType.INTEGER   | Map.of()
		12     | SnFieldType.INTEGER   | Map.of(LANGUAGE_ID, 2 as Integer)
		13     | SnFieldType.LONG      | null
		14     | SnFieldType.LONG      | Map.of()
		15     | SnFieldType.LONG      | Map.of(LANGUAGE_ID, 2 as Long)
		16     | SnFieldType.FLOAT     | null
		17     | SnFieldType.FLOAT     | Map.of()
		18     | SnFieldType.FLOAT     | Map.of(LANGUAGE_ID, 2.2f as Float)
		19     | SnFieldType.DOUBLE    | null
		20     | SnFieldType.DOUBLE    | Map.of()
		21     | SnFieldType.DOUBLE    | Map.of(LANGUAGE_ID, 2.2d as Double)
		22     | SnFieldType.DATE_TIME | null
		23     | SnFieldType.DATE_TIME | Map.of()
		24     | SnFieldType.DATE_TIME | Map.of(LANGUAGE_ID, new Date())
	}

	@Test
	def "Value is not optional on localized field #testId: fieldType=#fieldType, value=#value"(testId, fieldType, value) {
		given:
		SnField field = new SnField(id: FIELD_ID, fieldType: fieldType, multiValued: false, localized: true)
		Map<String, String> valueProviderParameters = Map.of(OptionalParamSnIndexerValueProcessor.OPTIONAL_PARAM, Boolean.FALSE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		thrown(SnIndexerException)

		where:
		testId | fieldType             | value
		1      | SnFieldType.STRING    | null
		2      | SnFieldType.STRING    | Map.of()
		3      | SnFieldType.TEXT      | null
		4      | SnFieldType.TEXT      | Map.of()
		5      | SnFieldType.BOOLEAN   | null
		6      | SnFieldType.BOOLEAN   | Map.of()
		7      | SnFieldType.INTEGER   | null
		8      | SnFieldType.INTEGER   | Map.of()
		9      | SnFieldType.LONG      | null
		10     | SnFieldType.LONG      | Map.of()
		11     | SnFieldType.FLOAT     | null
		12     | SnFieldType.FLOAT     | Map.of()
		13     | SnFieldType.DOUBLE    | null
		14     | SnFieldType.DOUBLE    | Map.of()
		15     | SnFieldType.DATE_TIME | null
		16     | SnFieldType.DATE_TIME | Map.of()
	}

	def "Value is optional on qualified field #testId: fieldType=#fieldType, value=#value"(testId, fieldType, value) {
		given:
		SnField field = new SnField(id: FIELD_ID, fieldType: fieldType, multiValued: false, localized: false, qualifierTypeId: QUALIFIER_TYPE_ID)
		Map<String, String> valueProviderParameters = Map.of(OptionalParamSnIndexerValueProcessor.OPTIONAL_PARAM, Boolean.TRUE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == value

		where:
		testId | fieldType             | value
		1      | SnFieldType.STRING    | null
		2      | SnFieldType.STRING    | Map.of()
		3      | SnFieldType.STRING    | Map.of(QUALIFIER_ID, "a")
		4      | SnFieldType.TEXT      | null
		5      | SnFieldType.TEXT      | Map.of()
		6      | SnFieldType.TEXT      | Map.of(QUALIFIER_ID, "a")
		7      | SnFieldType.BOOLEAN   | null
		8      | SnFieldType.BOOLEAN   | Map.of()
		9      | SnFieldType.BOOLEAN   | Map.of(QUALIFIER_ID, Boolean.TRUE)
		10     | SnFieldType.INTEGER   | null
		11     | SnFieldType.INTEGER   | Map.of()
		12     | SnFieldType.INTEGER   | Map.of(QUALIFIER_ID, 2 as Integer)
		13     | SnFieldType.LONG      | null
		14     | SnFieldType.LONG      | Map.of()
		15     | SnFieldType.LONG      | Map.of(QUALIFIER_ID, 2 as Long)
		16     | SnFieldType.FLOAT     | null
		17     | SnFieldType.FLOAT     | Map.of()
		18     | SnFieldType.FLOAT     | Map.of(QUALIFIER_ID, 2.2f as Float)
		19     | SnFieldType.DOUBLE    | null
		20     | SnFieldType.DOUBLE    | Map.of()
		21     | SnFieldType.DOUBLE    | Map.of(QUALIFIER_ID, 2.2d as Double)
		22     | SnFieldType.DATE_TIME | null
		23     | SnFieldType.DATE_TIME | Map.of()
		24     | SnFieldType.DATE_TIME | Map.of(QUALIFIER_ID, new Date())
	}

	@Test
	def "Value is not optional on qualified field #testId: fieldType=#fieldType, value=#value"(testId, fieldType, value) {
		given:
		SnField field = new SnField(id: FIELD_ID, fieldType: fieldType, multiValued: false, localized: false, qualifierTypeId: QUALIFIER_TYPE_ID)
		Map<String, String> valueProviderParameters = Map.of(OptionalParamSnIndexerValueProcessor.OPTIONAL_PARAM, Boolean.FALSE.toString())
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)

		when:
		valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		thrown(SnIndexerException)

		where:
		testId | fieldType             | value
		1      | SnFieldType.STRING    | null
		2      | SnFieldType.STRING    | Map.of()
		3      | SnFieldType.TEXT      | null
		4      | SnFieldType.TEXT      | Map.of()
		5      | SnFieldType.BOOLEAN   | null
		6      | SnFieldType.BOOLEAN   | Map.of()
		7      | SnFieldType.INTEGER   | null
		8      | SnFieldType.INTEGER   | Map.of()
		9      | SnFieldType.LONG      | null
		10     | SnFieldType.LONG      | Map.of()
		11     | SnFieldType.FLOAT     | null
		12     | SnFieldType.FLOAT     | Map.of()
		13     | SnFieldType.DOUBLE    | null
		14     | SnFieldType.DOUBLE    | Map.of()
		15     | SnFieldType.DATE_TIME | null
		16     | SnFieldType.DATE_TIME | Map.of()
	}
}
