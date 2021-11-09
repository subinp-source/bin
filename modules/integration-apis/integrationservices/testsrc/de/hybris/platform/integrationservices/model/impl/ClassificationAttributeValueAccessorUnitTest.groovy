/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.classification.ClassificationService
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Locale.ENGLISH
import static java.util.Locale.JAPANESE

@UnitTest
class ClassificationAttributeValueAccessorUnitTest extends Specification {
	def classAttributeAssignment = Stub(ClassAttributeAssignmentModel)
	def typeAttributeDescriptor = Stub(TypeAttributeDescriptor)
	def service = Stub(ClassificationService)

	@Test
	@Unroll
	def "create classification attribute value accessor when class attribute assignment getLocalized() is #value"() {
		given: "class attribute assignment getLocalized() is #value"
		classAttributeAssignment.getLocalized() >> value
		def valueAccessor = new ClassificationAttributeValueAccessor(
				typeAttributeDescriptor,
				classAttributeAssignment,
				service)

		expect:
		with(valueAccessor) {
			attribute == typeAttributeDescriptor
			classAttributeAssignmentModel == classAttributeAssignment
			classificationService == service
			localized == expected
		}

		where:
		value | expected
		true  | true
		false | false
		null  | false
	}

	@Test
	@Unroll
	def "call to #method () with non-product args #args, #dynamicArgs returns #result"() {
		expect:
		localizedValueAccessor()."$method"(args, *dynamicArgs) == result

		where:
		args            | method      | dynamicArgs         | result
		null            | 'getValue'  | []                  | null
		Stub(ItemModel) | 'getValue'  | []                  | null
		null            | 'getValue'  | [ENGLISH]           | null
		Stub(ItemModel) | 'getValue'  | [ENGLISH]           | null
		null            | 'getValues' | []                  | [:]
		Stub(ItemModel) | 'getValues' | []                  | [:]
		null            | 'getValues' | [ENGLISH, JAPANESE] | [:]
		Stub(ItemModel) | 'getValues' | [ENGLISH, JAPANESE] | [:]
	}

	private ClassificationAttributeValueAccessor localizedValueAccessor() {
		classAttributeAssignment.getLocalized() >> true
		new ClassificationAttributeValueAccessor(
				typeAttributeDescriptor,
				classAttributeAssignment,
				service)
	}
}
