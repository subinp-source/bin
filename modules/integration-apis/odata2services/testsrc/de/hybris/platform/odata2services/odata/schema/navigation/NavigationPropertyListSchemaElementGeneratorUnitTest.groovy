/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.navigation

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class NavigationPropertyListSchemaElementGeneratorUnitTest extends Specification {
	private static final String ITEM_CODE = "Product"

	def propertyGenerator = Stub(NavigationPropertySchemaElementGenerator)
	def generator = new NavigationPropertyListSchemaElementGenerator(
			navigationPropertyGenerator: propertyGenerator
	)

	@Shared
	def navigationProperty = Stub(NavigationProperty)
	
	@Test
	@Unroll
	def "generate when type descriptor has a single attribute that generates #msg"()
	{
		given:
		def attribute1 = Stub(TypeAttributeDescriptor)
		def itemTypeDescriptor = typeDescriptor(ITEM_CODE, [attribute1])

		and: "property generator returns navigation property"
		propertyGenerator.generate(attribute1) >> navPropertyResult

		when:
		def navPropertyList = generator.generate(itemTypeDescriptor)

		then:
		navPropertyList == expectedResult

		where:
		navPropertyResult               | msg                    | expectedResult
		Optional.of(navigationProperty) | "a navigationProperty" | [navigationProperty]
		Optional.empty()                | "optional empty"       | []
	}

	@Test
	@Unroll
	def "generate when type descriptor #msg"()
	{
		when:
		def navPropertyList = generator.generate(descriptor)

		then:
		navPropertyList.size() == 0

		where:
		descriptor                | msg
		typeDescriptor(ITEM_CODE) | "has no attributes"
		null                      | "is null"
	}

	def typeDescriptor(String code, List attributes = []) {
		Stub(TypeDescriptor) {
			getTypeCode() >> code
			getItemCode() >> code
			getAttributes() >> attributes
		}
	}
}
