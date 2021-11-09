/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ClassificationCollectionDescriptorUnitTest extends Specification {

	@Test
	@Unroll
	def "classification collection descriptor for #scenario is #result"() {
		given:
		def classAttributeAssignment = Stub(ClassAttributeAssignmentModel) {
			getMultiValued() >> isMultiValued
		}

		when:
		def descriptor = ClassificationCollectionDescriptor.create(classAttributeAssignment)

		then:
		descriptor.newCollection() == result

		where:
		scenario        | isMultiValued | result
		'multivalued'   | true          | []
		'single valued' | false         | null
	}
}
