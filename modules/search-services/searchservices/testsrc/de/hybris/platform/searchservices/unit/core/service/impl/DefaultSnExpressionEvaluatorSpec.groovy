/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.core.service.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.searchservices.core.service.impl.DefaultSnExpressionEvaluator
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.type.TypeService

import org.junit.Test

import spock.lang.Specification


@UnitTest
public class DefaultSnExpressionEvaluatorSpec extends Specification {

	static final EN_LANGUAGE_ID = "en"
	static final DE_LANGUAGE_ID = "de"

	TypeService typeService = Mock()
	ModelService modelService = Mock()

	DefaultSnExpressionEvaluator expressionEvaluator

	def setup() {
		expressionEvaluator = new DefaultSnExpressionEvaluator()
		expressionEvaluator.setTypeService(typeService)
		expressionEvaluator.setModelService(modelService)
	}

	@Test
	def "Evaluate on null root returns null"() {
		when:
		def result = expressionEvaluator.evaluate(null, "code")

		then:
		result == null
	}

	@Test
	def "Fail to evaluate with null expression"() {
		when:
		expressionEvaluator.evaluate(new Object(), null)

		then:
		thrown(IllegalArgumentException)
	}

	@Test
	def "Evaluate non existing attribute from item returns null"() {
		given:
		ItemModel root = Mock()
		String expression = "nonExisting"

		ComposedTypeModel composedType = Mock()
		AttributeDescriptorModel attributeDescriptor = Mock()

		typeService.getComposedTypeForClass(root.getClass()) >> composedType
		typeService.hasAttribute(composedType, expression) >> false

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == null
	}

	@Test
	def "Evaluate pk attribute from item"() {
		given:
		PK pk = PK.fromLong(2)
		ItemModel root = Mock()
		String expression = "pk"

		root.getPk() >> pk

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == pk
	}

	@Test
	def "Evaluate attribute from item"() {
		given:
		String value = "testValue"
		ItemModel root = Mock()
		String expression = "code"

		ComposedTypeModel composedType = Mock()
		AttributeDescriptorModel attributeDescriptor = Mock()

		typeService.getComposedTypeForClass(root.getClass()) >> composedType
		typeService.hasAttribute(composedType, expression) >> true
		typeService.getAttributeDescriptor(composedType, expression) >> attributeDescriptor
		modelService.getAttributeValue(root, expression) >> value

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == value
	}

	@Test
	def "Evaluate collection attribute from item"() {
		given:
		List<String> value = List.of("tag1", "tag2")
		ItemModel root = Mock()
		String expression = "tags"

		ComposedTypeModel composedType = Mock()
		AttributeDescriptorModel attributeDescriptor = Mock()

		typeService.getComposedTypeForClass(root.getClass()) >> composedType
		typeService.hasAttribute(composedType, expression) >> true
		typeService.getAttributeDescriptor(composedType, expression) >> attributeDescriptor
		modelService.getAttributeValue(root, expression) >> value

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == value
	}

	@Test
	def "Evaluate map attribute from item"() {
		given:
		String key = "key1"
		String value = "value1"
		ItemModel root = Mock()
		String baseExpression = "attributes"
		String expression = baseExpression + "." + key

		ComposedTypeModel composedType = Mock()
		AttributeDescriptorModel attributeDescriptor = Mock()

		typeService.getComposedTypeForClass(root.getClass()) >> composedType
		typeService.hasAttribute(composedType, baseExpression) >> true
		typeService.getAttributeDescriptor(composedType, baseExpression) >> attributeDescriptor
		modelService.getAttributeValue(root, baseExpression) >> Map.of(key, value)

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == value
	}


	@Test
	def "Evaluate non localized attribute from item using locales"() {
		given:
		String value = "testValue"
		ItemModel root = Mock()
		String expression = "code"

		ComposedTypeModel composedType = Mock()
		AttributeDescriptorModel attributeDescriptor = Mock()

		typeService.getComposedTypeForClass(root.getClass()) >> composedType
		typeService.hasAttribute(composedType, expression) >> true
		typeService.getAttributeDescriptor(composedType, expression) >> attributeDescriptor
		attributeDescriptor.getLocalized() >> false
		modelService.getAttributeValue(root, expression) >> value

		when:
		def result = expressionEvaluator.evaluate(root, expression, List.of(Locale.ENGLISH, Locale.GERMAN))

		then:
		result == value
	}

	@Test
	def "Evaluate localized attribute from item using locales"() {
		given:
		Locale key1 = Locale.ENGLISH
		String value1 = "testValue1"
		Locale key2 = Locale.GERMAN
		String value2 = "testValue2"
		Map<String, String> value = Map.of(key1, value1, key2, value2)
		ItemModel root = Mock()
		String expression = "code"

		ComposedTypeModel composedType = Mock()
		AttributeDescriptorModel attributeDescriptor = Mock()

		typeService.getComposedTypeForClass(root.getClass()) >> composedType
		typeService.hasAttribute(composedType, expression) >> true
		typeService.getAttributeDescriptor(composedType, expression) >> attributeDescriptor
		attributeDescriptor.getLocalized() >> true
		modelService.getAttributeValue(root, expression, Locale.ENGLISH) >> value1
		modelService.getAttributeValue(root, expression, Locale.GERMAN) >> value2

		when:
		def result = expressionEvaluator.evaluate(root, expression, List.of(Locale.ENGLISH, Locale.GERMAN))

		then:
		result == value
	}

	@Test
	def "Evaluate non existing attribute from object returns null"() {
		given:
		String value = "testValue"
		Object root = new TestType(code: value)
		String expression = "nonExisting"

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == null
	}

	@Test
	def "Evaluate attribute from object"() {
		given:
		String value = "testValue"
		Object root = new TestType(code: value)
		String expression = "code"

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == value
	}

	@Test
	def "Evaluate collection attribute from object"() {
		given:
		List<String> value = List.of("tag1, tag2")
		Object root = new TestType(tags: value)
		String expression = "tags"

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == value
	}

	@Test
	def "Evaluate map attribute from object"() {
		given:
		String key = "key1"
		String value = "value1"
		Object root = new TestType(attributes: Map.of(key, value))
		String attribute = "attributes"
		String expression = attribute + "." + key

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == value
	}

	@Test
	def "Evaluate attribute from object using locales"() {
		given:
		String value = "testValue"
		Object root = new TestType(code: value)
		String expression = "code"

		when:
		def result = expressionEvaluator.evaluate(root, expression, List.of(Locale.ENGLISH, Locale.GERMAN))

		then:
		result == value
	}

	@Test
	def "Evaluate nested attribute"() {
		given:
		String value = "testValue"
		Object root = new TestType(child: new TestType(code: value))
		String attribute1 = "child"
		String attribute2 = "code"
		String expression = attribute1 + "." + attribute2

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == value
	}

	@Test
	def "Evaluate nested collection attribute"() {
		given:
		List<String> value = List.of("tag1, tag2")
		Object root = new TestType(child: new TestType(tags: value))
		String attribute1 = "child"
		String attribute2 = "tags"
		String expression = attribute1 + "." + attribute2

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == value
	}

	@Test
	def "Evaluate nested map attribute"() {
		given:
		String key = "key1"
		String value = "value1"
		Object root = new TestType(child: new TestType(attributes: Map.of(key, value)))
		String attribute1 = "child"
		String attribute2 = "attributes"
		String expression = attribute1 + "." + attribute2 + "." + key

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == value
	}

	@Test
	def "Evaluate nested attribute, parent attribute is collection"() {
		given:
		String value1 = "testValue1"
		String value2 = "testValue2"
		List<String> value = List.of(value1, value2)
		Object root = new TestType(children: List.of(new TestType(code: value1), new TestType(code: value2)))
		String attribute1 = "children"
		String attribute2 = "code"
		String expression = attribute1 + "." + attribute2

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == value
	}

	@Test
	def "Evaluate nested collection attribute, parent attribute is collection"() {
		given:
		List<String> value1 = List.of("tag1, tag2")
		List<String> value2 = List.of("tag3, tag4")
		List<String> value = value1 + value2
		Object root = new TestType(children: List.of(new TestType(tags: value1), new TestType(tags: value2)))
		String attribute1 = "children"
		String attribute2 = "tags"
		String expression = attribute1 + "." + attribute2

		when:
		def result = expressionEvaluator.evaluate(root, expression)

		then:
		result == value
	}

	static class TestType {
		String code
		List<String> tags
		Map<String, String> attributes
		TestType child
		List<TestType> children
	}
}
