/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.navigation

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.odata2services.odata.NestedAbstractItemTypeCannotBeCreatedException
import de.hybris.platform.odata2services.odata.UniqueCollectionNotAllowedException
import de.hybris.platform.odata2services.odata.schema.association.AssociationGenerator
import de.hybris.platform.odata2services.odata.schema.association.AssociationGeneratorRegistry
import de.hybris.platform.odata2services.odata.schema.attribute.PropertyAnnotationListGenerator
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute
import org.apache.olingo.odata2.api.edm.provider.Association
import org.apache.olingo.odata2.api.edm.provider.AssociationEnd
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty
import org.junit.Test
import spock.lang.Specification

import static de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModelUtils.falseIfNull

@UnitTest
class NavigationPropertySchemaElementGeneratorUnitTest extends Specification {
	private static final String PRODUCT = "Product"
	private static final String UNIT = "Unit"
	private static final String KEYWORD = "Keyword"
	private static final String ATTRIBUTE_NAME = "attrName"
	private static final String CATEGORY = "Category"

	def annotation = Stub(AnnotationAttribute)
	def associationGeneratorRegistry = Stub(AssociationGeneratorRegistry)
	def associationGenerator = Stub(AssociationGenerator)
	def propertyAnnotationListGenerator = Stub(PropertyAnnotationListGenerator)

	def generator = new NavigationPropertySchemaElementGenerator(
			propertyAnnotationListGenerator: propertyAnnotationListGenerator,
			associationGeneratorRegistry: associationGeneratorRegistry
	)

	def setup() {
		propertyAnnotationListGenerator.generate(_) >> [annotation]
	}

	@Test
	def "generate association"() {
		given:
		def attribute = typeAttributeDescriptor(ATTRIBUTE_NAME, [autocreate:false])

		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorRegistry.getAssociationGenerator(_ as TypeAttributeDescriptor) >> Optional.of(associationGenerator)
		associationGenerator.generate(_ as TypeAttributeDescriptor) >> association([source: PRODUCT, target: UNIT])

		when:
		final Optional<NavigationProperty> navigationPropertyOptional = generator.generate(attribute)

		then:
		navigationPropertyOptional.isPresent()
		def navigationProperty = navigationPropertyOptional.get()
		with(navigationProperty) {
			name == ATTRIBUTE_NAME
			fromRole == PRODUCT
			toRole == UNIT
			annotationAttributes == [annotation]
			with(relationship) {
				name == "${PRODUCT}_${UNIT}"
			}
		}
	}

	@Test
	def "generate for collection"() {
		given:
		def attribute = typeAttributeDescriptor(ATTRIBUTE_NAME, [collection: true])

		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorRegistry.getAssociationGenerator(_ as TypeAttributeDescriptor) >> Optional.of(associationGenerator)
		associationGenerator.generate(_ as TypeAttributeDescriptor) >> association([source: PRODUCT, target: CATEGORY])

		when:
		final Optional navigationPropertyOptional = generator.generate(attribute)

		then:
		navigationPropertyOptional.isPresent()
		def navigationProperty = navigationPropertyOptional.get()
		with(navigationProperty) {
			name == ATTRIBUTE_NAME
			fromRole == PRODUCT
			toRole == CATEGORY
			annotationAttributes == [annotation]
			with(relationship) {
				name == "${PRODUCT}_${CATEGORY}"
			}
		}
	}

	@Test
	def "generate for unique collection"() {
		given:
		def attribute = typeAttributeDescriptor(ATTRIBUTE_NAME, [key: true, collection: true])
		attribute.getTypeDescriptor() >> typeDescriptor(PRODUCT)

		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorRegistry.getAssociationGenerator(_ as TypeAttributeDescriptor) >> Optional.of(associationGenerator)
		associationGenerator.generate(_ as TypeAttributeDescriptor) >> association([source: PRODUCT, target: KEYWORD])

		when:
		generator.generate(attribute)

		then:
		UniqueCollectionNotAllowedException e = thrown(UniqueCollectionNotAllowedException)
		e.getPropertyName() == ATTRIBUTE_NAME
		e.getEntityType() == PRODUCT
	}

	@Test
	def "generate for map"() {
		given:
		def attribute = typeAttributeDescriptor(ATTRIBUTE_NAME, [map: true])

		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorRegistry.getAssociationGenerator(_ as TypeAttributeDescriptor) >> Optional.of(associationGenerator)
		associationGenerator.generate(_ as TypeAttributeDescriptor) >> association([source: PRODUCT, target: KEYWORD])

		when:
		final Optional navigationPropertyOptional = generator.generate(attribute)


		then:
		navigationPropertyOptional.isPresent()
		def navigationProperty = navigationPropertyOptional.get()
		with(navigationProperty) {
			name == ATTRIBUTE_NAME
			fromRole == PRODUCT
			toRole == KEYWORD
			annotationAttributes == [annotation]
			with(relationship) {
				name == "${PRODUCT}_${KEYWORD}"
			}
		}
	}

	@Test
	def "generate for unique map"() {
		given:
		def attribute = typeAttributeDescriptor(ATTRIBUTE_NAME, [map: true, key: true])
		attribute.getTypeDescriptor() >> typeDescriptor(PRODUCT)

		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorRegistry.getAssociationGenerator(_ as TypeAttributeDescriptor) >> Optional.of(associationGenerator)
		associationGenerator.generate(_ as TypeAttributeDescriptor) >> association([source: PRODUCT, target: KEYWORD])

		when:
		generator.generate(attribute)

		then:
		UniqueCollectionNotAllowedException e = thrown(UniqueCollectionNotAllowedException)
		e.getPropertyName() == ATTRIBUTE_NAME
		e.getEntityType() == PRODUCT
	}

	@Test
	def "generate for autocreate abstract type"() {
		given: "autocreate = true && abstract = true"
		def attribute = typeAttributeDescriptor(ATTRIBUTE_NAME, [autocreate: true, abstract: true])
		attribute.getTypeDescriptor() >> typeDescriptor(PRODUCT)

		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorRegistry.getAssociationGenerator(_ as TypeAttributeDescriptor) >> Optional.of(associationGenerator)
		associationGenerator.generate(_ as TypeAttributeDescriptor) >> association([source: PRODUCT, target: KEYWORD])

		when:
		generator.generate(attribute)

		then:
		NestedAbstractItemTypeCannotBeCreatedException e = thrown(NestedAbstractItemTypeCannotBeCreatedException)
		e.getPropertyName() == ATTRIBUTE_NAME
		e.getEntityType() == PRODUCT
	}

	@Test
	def "generate for autocreate non abstract type"() {
		given: "autocreate = true && abstract = false"
		def attribute = typeAttributeDescriptor(ATTRIBUTE_NAME, [autocreate: true])

		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorRegistry.getAssociationGenerator(_ as TypeAttributeDescriptor) >> Optional.of(associationGenerator)
		associationGenerator.generate(_ as TypeAttributeDescriptor) >> association([source: PRODUCT, target: KEYWORD])

		when:
		final Optional navigationPropertyOptional = generator.generate(attribute)

		then:
		navigationPropertyOptional.isPresent()
		def navigationProperty = navigationPropertyOptional.get()
		with(navigationProperty) {
			name == ATTRIBUTE_NAME
			fromRole == PRODUCT
			toRole == KEYWORD
			annotationAttributes == [annotation]
			with(relationship) {
				name == "${PRODUCT}_${KEYWORD}"
			}
		}
	}

	@Test
	def "generate for simple property"() {
		given:
		associationGeneratorRegistry.getAssociationGenerator(_ as TypeAttributeDescriptor) >> Optional.empty()

		when:
		final Optional optionalProperty = generator.generate(Stub(TypeAttributeDescriptor))

		then:
		optionalProperty.isEmpty()
	}

	@Test
	def "generate for null property"() {
		when:
		final Optional optionalProperty = generator.generate(null)

		then:
		optionalProperty.isEmpty()
	}

	def typeAttributeDescriptor(String attributeName, Map<String, Boolean> params) {
		Stub(TypeAttributeDescriptor) {
			isAutoCreate() >> falseIfNull(params['autocreate'])
			isKeyAttribute() >> falseIfNull(params['key'])
			isCollection() >> falseIfNull(params['collection'])
			isMap() >> falseIfNull(params['map'])
			getAttributeName() >> attributeName
			getAttributeType() >> Stub(TypeDescriptor) {
				isAbstract() >> falseIfNull(params['abstract'])
			}
		}
	}

	private Association association(Map<String, String> params) {
		Stub(Association) {
			getName() >> "${params['source']}_${params['target']}"
			getEnd1() >> Stub(AssociationEnd) {
				getRole() >> params['source']
			}
			getEnd2() >> Stub(AssociationEnd) {
				getRole() >> params['target']
			}
		}
	}

	def typeDescriptor(String code) {
		Stub(TypeDescriptor) {
			getTypeCode() >> code
			getItemCode() >> code
		}
	}
}
