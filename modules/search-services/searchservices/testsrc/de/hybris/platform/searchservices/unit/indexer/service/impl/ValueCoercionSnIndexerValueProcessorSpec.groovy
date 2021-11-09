/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.indexer.service.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerFieldWrapper
import de.hybris.platform.searchservices.indexer.service.impl.ValueCoercionSnIndexerValueProcessor

import org.junit.Test

import spock.lang.Specification


@UnitTest
public class ValueCoercionSnIndexerValueProcessorSpec extends Specification {

	static final String FIELD_ID = "field"

	SnIndexerContext indexerContext = Mock()

	ValueCoercionSnIndexerValueProcessor valueProcessor

	def setup() {
		valueProcessor = new ValueCoercionSnIndexerValueProcessor()
	}

	@Test
	def "Coalesce value on single-valued field"() {
		given:
		def value = "value"

		SnField field = new SnField(id: FIELD_ID, multiValued: false)
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == value
	}

	@Test
	def "Coalesce collection value on single-valued field"() {
		given:
		def value1 = "value1"
		def value2 = "value2"
		def value = List.of(value1, value2)

		SnField field = new SnField(id: FIELD_ID, multiValued: false)
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == value1
	}

	@Test
	def "Coalesce value on multi-valued field"() {
		given:
		def value = "value"

		SnField field = new SnField(id: FIELD_ID, multiValued: true)
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == List.of(value)
	}

	@Test
	def "Coalesce collection value on multi-valued field"() {
		given:
		def value1 = "value1"
		def value2 = "value2"
		def value = List.of(value1, value2)

		SnField field = new SnField(id: FIELD_ID, multiValued: true)
		SnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper(field: field)

		when:
		Object processedValue = valueProcessor.process(indexerContext, fieldWrapper, value)

		then:
		processedValue == value
	}
}
