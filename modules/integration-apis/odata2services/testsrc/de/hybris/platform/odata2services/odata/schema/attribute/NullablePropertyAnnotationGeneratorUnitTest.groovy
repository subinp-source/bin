/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.schema.attribute

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class NullablePropertyAnnotationGeneratorUnitTest extends Specification {

	def generator = new NullablePropertyAnnotationGenerator()

	@Test
	@Unroll
	def "isApplicable() is #applicable"() {
		expect:
		generator.isApplicable(descriptor) == applicable

		where:
		applicable | descriptor
		true       | Stub(TypeAttributeDescriptor)
		false      | null
	}

	@Test
	@Unroll
	def "generate annotation for Nullable='#nullable'"() {
		given:
		def descriptor = Stub(TypeAttributeDescriptor) {
			isNullable() >> nullable
		}

		expect:
		generator.generate(descriptor).text == "$nullable"

		where:
		nullable << [true, false]
	}

	@Test
	def "annotation name is Nullable"() {
		expect:
		generator.generate(Stub(TypeAttributeDescriptor)).name == "Nullable"
	}
}
