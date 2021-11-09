/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.schema.navigation

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class NavigationPropertyListGeneratorRegistryUnitTest extends Specification {
	def registry = new NavigationPropertyListGeneratorRegistry()

	@Test
	def "resulting list contains 0 navigation properties from null generator list"() {
		given:
		registry.setGenerators(null)

		expect:
		registry.generate(attributesDoNotMatter() as Collection<IntegrationObjectItemAttributeModel>).isEmpty()
	}

	@Test
	@Unroll
	def "resulting list contains #resultSize navigation properties from #generators.size generators"() {
		given:
		registry.setGenerators(generators)

		when:
		def result = registry.generate(attributesDoNotMatter() as Collection<IntegrationObjectItemAttributeModel>)

		then:
		result.size() == resultSize

		where:
		generators                                                                 | resultSize
		[mockGeneratorWithNumberOfResults(2), mockGeneratorWithNumberOfResults(1)] | 3
		[mockGeneratorWithNumberOfResults(1), mockGeneratorWithNumberOfResults(0)] | 1
		[mockGeneratorWithNumberOfResults(5)]                                      | 5
		[]                                                                         | 0
	}

	@Test
	@Unroll
	def "resulting list contains #resultSize navigation properties from #generators.size creators"() {
		given:
		registry.setSchemaElementGenerators(generators)

		when:
		def result = registry.generate(Stub(TypeDescriptor))

		then:
		result.size() == resultSize

		where:
		generators                                                                                   | resultSize
		[schemaElementGeneratorWithNumberOfResults(2), schemaElementGeneratorWithNumberOfResults(1)] | 3
		[schemaElementGeneratorWithNumberOfResults(1), schemaElementGeneratorWithNumberOfResults(0)] | 1
		[schemaElementGeneratorWithNumberOfResults(5)]                                               | 5
		[]                                                                                           | 0
	}

	def attributesDoNotMatter() {
		[]
	}

	def mockGeneratorWithNumberOfResults(def count) {
		Stub(SchemaElementGenerator) {
			generate(_ as Collection) >> [Stub(NavigationProperty)] * count
		}
	}

	def schemaElementGeneratorWithNumberOfResults(def count) {
		Stub(SchemaElementGenerator) {
			generate(_ as TypeDescriptor) >> [Stub(NavigationProperty)] * count
		}
	}
}
