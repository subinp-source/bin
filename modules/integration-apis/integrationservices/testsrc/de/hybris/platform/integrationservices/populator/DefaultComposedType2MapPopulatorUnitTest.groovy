/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.service.IntegrationObjectConversionService
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultComposedType2MapPopulatorUnitTest extends Specification {

	private static final def ATTR_NAME = "testAttribute"
	private static final def QUALIFIER = "qualifier"

	def populator = new DefaultComposedType2MapPopulator()

	def targetMap = [:]
	private def attributeDescriptor = Stub(TypeAttributeDescriptor) {
		getAttributeName() >> ATTR_NAME
		getQualifier() >> QUALIFIER
	}
	private def itemModel = Stub(ItemModel)
	private def conversionService = Mock(IntegrationObjectConversionService)

	def setup() {
		populator.setConversionService(conversionService)
	}

	@Test
	def "populates to map when attribute is of composite type"() {
		given:
		attributeHasValue(Stub(ItemModel))
		and:
		def convertedValue = ['convertedAttributeName': 'convertedValue']
		conversionServiceReturnsConvertedItem(convertedValue)

		when:
		populator.populate(conversionContext(attributeDescriptor), targetMap)

		then:
		targetMap == [(ATTR_NAME): convertedValue]
	}

	@Test
	def "does not populate to map when item is null"() {
		given:
		attributeHasValue(null)

		when:
		populator.populate(conversionContext(attributeDescriptor), targetMap)

		then:
		0 * conversionService.convert(_)
		targetMap.isEmpty()
	}

	@Test
	def "does not populate to map when item is primitive"() {
		given:
		attributeIsPrimitive()

		when:
		populator.populate(conversionContext(attributeDescriptor), targetMap)

		then:
		0 * conversionService.convert(_)
		targetMap.isEmpty()
	}

	@Test
	def "does not populate to map when item is enum"() {
		given:
		attributeIsEnum()

		when:
		populator.populate(conversionContext(attributeDescriptor), targetMap)

		then:
		0 * conversionService.convert(_)
		targetMap.isEmpty()
	}

	@Test
	def "does not populate to map when item is collection"() {
		given:
		attributeIsCollection()

		when:
		populator.populate(conversionContext(attributeDescriptor), targetMap)

		then:
		0 * conversionService.convert(_)
		targetMap.isEmpty()
	}

	@Test
	def "does not populate to map when item is map"() {
		given:
		attributeIsMap()

		when:
		populator.populate(conversionContext(attributeDescriptor), targetMap)

		then:
		0 * conversionService.convert(_)
		targetMap.isEmpty()
	}

	private ItemToMapConversionContext conversionContext(TypeAttributeDescriptor attribute) {
		Stub(ItemToMapConversionContext) {
			getItemModel() >> itemModel
			getTypeDescriptor() >>
					Stub(TypeDescriptor) {
						getAttributes() >> [attribute]
					}
		}
	}

	def attributeIsPrimitive() {
		attributeDescriptor.getAttributeType() >> Stub(TypeDescriptor) {
			isPrimitive() >> true
		}
	}

	def attributeIsEnum() {
		attributeDescriptor.getAttributeType() >> Stub(TypeDescriptor) {
			isEnumeration() >> true
		}
	}

	def attributeIsCollection() {
		attributeDescriptor.isCollection() >> true
	}

	def attributeIsMap() {
		attributeDescriptor.isMap() >> true
	}

	def attributeHasValue(Object value) {
		attributeDescriptor.accessor() >> Stub(AttributeValueAccessor) {
			getValue(itemModel) >> value
		}
	}

	private Map<String, Object> conversionServiceReturnsConvertedItem(Map<String, Object> convertedValue) {
		conversionService.convert(_ as ItemToMapConversionContext) >> convertedValue
	}
}