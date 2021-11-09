/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.indexer.service.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.document.data.SnDocument
import de.hybris.platform.searchservices.indexer.SnIndexerException
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProcessor
import de.hybris.platform.searchservices.indexer.service.impl.AbstractSnIndexerValueProvider

import org.junit.Test

import spock.lang.Specification


@UnitTest
public class SnIndexerValueProviderSpec extends Specification {

	SnIndexerContext indexerContext = Mock()
	SnField field = Mock()
	SnIndexerFieldWrapper fieldWrapper = Mock()
	ItemModel source = Mock()
	SnDocument document = Mock()
	Object value = Mock()
	Object processedValue1 = Mock()
	Object processedValue2 = Mock()

	SnIndexerValueProcessor valueProcessor1 = Mock()
	SnIndexerValueProcessor valueProcessor2 = Mock()

	@Test
	def "Value processors are called on the correct order 1"() {
		given:
		AbstractSnIndexerValueProvider valueProvider = new SnIndexerValueProvider()
		valueProvider.setValueProcessors(List.of(valueProcessor1, valueProcessor2))

		List<SnIndexerFieldWrapper> fieldWrappers = List.of(fieldWrapper)

		fieldWrapper.getField() >> field
		valueProcessor1.process(indexerContext, fieldWrapper, value) >> processedValue1
		valueProcessor2.process(indexerContext, fieldWrapper, processedValue1) >> processedValue2

		when:
		valueProvider.provide(indexerContext, fieldWrappers, source, document)

		then:
		1 * document.setFieldValue(field, processedValue2)
	}

	@Test
	def "Value processors are called on the correct order 2"() {
		given:
		AbstractSnIndexerValueProvider valueProvider = new SnIndexerValueProvider()
		valueProvider.setValueProcessors(List.of(valueProcessor2, valueProcessor1))

		List<SnIndexerFieldWrapper> fieldWrappers = List.of(fieldWrapper)

		fieldWrapper.getField() >> field
		valueProcessor2.process(indexerContext, fieldWrapper, value) >> processedValue1
		valueProcessor1.process(indexerContext, fieldWrapper, processedValue1) >> processedValue2

		when:
		valueProvider.provide(indexerContext, fieldWrappers, source, document)

		then:
		1 * document.setFieldValue(field, processedValue2)
	}

	class SnIndexerValueProvider extends AbstractSnIndexerValueProvider<ItemModel, Void> {
		@Override
		public Set getSupportedQualifierClasses() throws SnIndexerException {
			return null
		}

		@Override
		protected Object getFieldValue(SnIndexerContext indexerContext, SnIndexerFieldWrapper fieldWrapper,
			ItemModel source, Void data) throws SnIndexerException {
			return value
		}
	}
}
