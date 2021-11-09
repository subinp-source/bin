/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.entity

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.odata2services.odata.persistence.exception.MissingKeyException
import de.hybris.platform.odata2services.odata.schema.KeyGenerator
import de.hybris.platform.odata2services.odata.schema.navigation.NavigationPropertyListGeneratorRegistry
import de.hybris.platform.odata2services.odata.schema.property.AbstractPropertyListGenerator
import org.apache.olingo.odata2.api.edm.provider.*
import org.junit.Test
import spock.lang.Specification

@UnitTest
class ComposedEntityTypeGeneratorUnitTest extends Specification {

	private static final String CODE_1 = "code1"

	def propertyListGenerator = Stub(AbstractPropertyListGenerator)
	def navPropertyListGeneratorRegistry = Stub(NavigationPropertyListGeneratorRegistry)
	def descriptorFactory = Stub(DescriptorFactory)
	def keyGenerator = Stub(KeyGenerator)


	private ComposedEntityTypeGenerator composedEntityTypeGenerator = new ComposedEntityTypeGenerator(
			registry: navPropertyListGeneratorRegistry,
			descriptorFactory: descriptorFactory,
			propertiesGenerator: propertyListGenerator,
			keyGenerator: keyGenerator
	)

	@Test
	def "generate combines navigation properties as expected"() {
		given: "integration object item with code"
		def ioi = integrationObjectItem()

		and: "registry generates a list of navigation properties"
		def navigationPropertyName1 = "navigationProperty1"
		def navigationPropertyName2 = "navigationProperty2"
		def navigationPropertyName3 = "navigationProperty3"
		def navigationProperty1A = navigationProperty(navigationPropertyName1)
		def navigationProperty2 = navigationProperty(navigationPropertyName2)

		navPropertyListGeneratorRegistry.generate(_ as Collection) >> [navigationProperty1A, navigationProperty2]

		and: "property list generator generates a list of properties"
		def propertyName1 = "property1"
		def property1 = simpleProperty(propertyName1)
		final List<Property> expectedPropertyList = [property1]
		propertyListGenerator.generate(ioi) >> expectedPropertyList

		and: "key generator generates a key"
		final Key expectedKey = createKeyWithPropertyRefNames("propertyRef1")
		keyGenerator.generate(expectedPropertyList) >> Optional.of(expectedKey)

		and: "item type descriptor is found"
		def typeDescriptor = Stub(TypeDescriptor)
		descriptorFactory.createItemTypeDescriptor(ioi) >> typeDescriptor

		def navigationProperty1B = navigationProperty(navigationPropertyName1)
		def navigationProperty3 = navigationProperty(navigationPropertyName3)

		navPropertyListGeneratorRegistry.generate(typeDescriptor) >> [navigationProperty1B, navigationProperty3]

		when:
		final List<EntityType> entityTypes = composedEntityTypeGenerator.generate(ioi)

		then:
		entityTypes.size() == 1
		def entityType = entityTypes.get(0)
		with(entityType) {
			name == CODE_1
			properties == expectedPropertyList
			navigationProperties == [navigationProperty1A, navigationProperty2, navigationProperty3]
		}
	}

	@Test
	def "generate when typeDescriptor is null populates the expected entityType"() {
		given: "integration object item with code"
		def ioi = integrationObjectItem()

		and: "registry generates a list of navigation properties"
		def name1 = "navigationProperty1"
		def navigationPropertyName2 = "navigationProperty2"
		def navigationProperty1A = navigationProperty(name1)
		def navigationProperty2A = navigationProperty(navigationPropertyName2)
		
		navPropertyListGeneratorRegistry.generate(_ as Collection) >> [navigationProperty1A, navigationProperty2A]

		and: "property list generator generates a list of properties"
		def propertyName1 = "property1"
		def property1 = simpleProperty(propertyName1)
		final List<Property> expectedPropertyList = [property1]
		propertyListGenerator.generate(ioi) >> expectedPropertyList

		and: "key generator generates a key"
		final Key expectedKey = createKeyWithPropertyRefNames("propertyRef1")
		keyGenerator.generate(expectedPropertyList) >> Optional.of(expectedKey)

		when:
		final List<EntityType> entityTypes = composedEntityTypeGenerator.generate(ioi)
		
		then:
		entityTypes.size() == 1
		def entityType = entityTypes.get(0)
		with(entityType) {
			name == CODE_1
			properties == expectedPropertyList
			navigationProperties == [navigationProperty1A, navigationProperty2A]
		}
	}

	@Test
	def "generate key generation throws exception"()
	{
		given: "integration object item with code"
		def ioi = integrationObjectItem()

		and: "registry generates a empty list"
		navPropertyListGeneratorRegistry.generate(_ as Collection) >> []

		and: "property list generator generates a empty list"
		propertyListGenerator.generate(ioi) >> []

		and: "key generator throws exception"
		keyGenerator.generate(_ as List) >> { throw Stub(IllegalArgumentException) }

		when:
		composedEntityTypeGenerator.generate(ioi)

		then:
		thrown(IllegalArgumentException)
	}

	@Test
	def "generate without key property throws MissingKeyException"()
	{
		given: "integration object item with code"
		def ioi = integrationObjectItem()

		and: "registry generates a empty list"
		navPropertyListGeneratorRegistry.generate(_ as Collection) >> []

		and: "property list generator generates a empty list"
		propertyListGenerator.generate(ioi) >> []

		and: "key generator throws exception"
		keyGenerator.generate(_ as List) >> Optional.empty()

		when:
		composedEntityTypeGenerator.generate(ioi)

		then:
		def e = thrown(MissingKeyException)
		e.message.contains(CODE_1)
	}

	@Test
	def "generate with null IntegrationObjectItemModel throws exception"()
	{
		when:
		composedEntityTypeGenerator.generate(null)

		then:
		thrown(IllegalArgumentException)
	}

	private Key createKeyWithPropertyRefNames(final String propertyRefName)
	{
		Stub(Key) {
			getKeys() >> [propertyRef(propertyRefName)]
		}
	}

	private PropertyRef propertyRef(final String propertyRefName)
	{
		Stub(PropertyRef) {
			getName() >> propertyRefName
		}
	}

	private Property simpleProperty(final String propertyName)
	{
		Stub(SimpleProperty) {
			getName() >> propertyName
		}
	}

	private NavigationProperty navigationProperty(final String propertyName)
	{
		Stub(NavigationProperty) {
			getName() >> propertyName
		}
	}

	private IntegrationObjectItemModel integrationObjectItem() {
		Stub(IntegrationObjectItemModel) {
			getCode() >> CODE_1
		}
	}
}
