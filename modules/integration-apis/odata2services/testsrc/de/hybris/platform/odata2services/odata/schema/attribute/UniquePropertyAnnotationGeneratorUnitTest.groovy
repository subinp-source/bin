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
class UniquePropertyAnnotationGeneratorUnitTest extends Specification {

	def generator = new UniquePropertyAnnotationGenerator()

	@Test
	@Unroll
	def "isApplicable() is #applicable when #scenario"() {
		expect:
		generator.isApplicable(descriptor) == applicable

		where:
		applicable | scenario                               | descriptor
		false      | 'descriptor is null'                   | null
		false      | "isKeyAttribute() returns $applicable" | Stub(TypeAttributeDescriptor) { isKeyAttribute() >> false }
		true       | "isKeyAttribute() returns $applicable" | Stub(TypeAttributeDescriptor) { isKeyAttribute() >> true }
	}

	@Test
	def "test IsUnique annotation generated correctly"() {
		when:
		def annotation = generator.generate Stub(TypeAttributeDescriptor)

		then:
		with(annotation) {
			name == 's:IsUnique'
			text == 'true'
		}
	}
}
