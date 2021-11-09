/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.odata2services.odata.schema.navigation

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.odata2services.odata.NestedAbstractItemTypeCannotBeCreatedException
import de.hybris.platform.odata2services.odata.ODataNavigationProperty
import de.hybris.platform.odata2services.odata.UniqueCollectionNotAllowedException
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator
import de.hybris.platform.odata2services.odata.schema.association.AssociationGenerator
import de.hybris.platform.odata2services.odata.schema.association.AssociationGeneratorRegistry
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty
import org.junit.Test
import spock.lang.Specification

import static de.hybris.platform.integrationservices.model.BaseMockItemAttributeModelBuilder.*
import static de.hybris.platform.integrationservices.model.MockIntegrationObjectItemModelBuilder.itemModelBuilder
import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.mock

@UnitTest
class NavigationPropertyGeneratorUnitTest extends Specification {
	private static final String PRODUCT = "Product"
	private static final String UNIT = "Unit"
	private static final String KEYWORD = "Keyword"
	private static final String ATTRIBUTE_NAME = "attrName"
	private static final String CATEGORY = "Category"

	def annotation = Stub(AnnotationAttribute)

	def associationGeneratorRegistry = Stub(AssociationGeneratorRegistry)
	def associationGenerator = Stub(AssociationGenerator)
	def attributeListGenerator = Stub(SchemaElementGenerator)

	def generator = new NavigationPropertyGenerator()

	def setup() {
		generator.setAttributeListGenerator(attributeListGenerator)
		generator.setAssociationGeneratorRegistry(associationGeneratorRegistry)
		attributeListGenerator.generate(_) >> [annotation]
	}

	@Test
	def "generate association"() {
		given:
		final IntegrationObjectItemAttributeModel attributeModel =
				oneToOneRelationAttributeBuilder()
						.withSource(PRODUCT)
						.withTarget(UNIT)
						.withName(ATTRIBUTE_NAME)
						.withIntegrationObjectItem(itemModelBuilder().build())
						.build()
		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorGeneratesSourceAndTargetRoles(PRODUCT, UNIT)

		when:
		final Optional<NavigationProperty> navigationPropertyOptional = generator.generate(attributeModel)

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
		final IntegrationObjectItemAttributeModel attribute =
				collectionAttributeBuilder()
						.withName(ATTRIBUTE_NAME)
						.withSource(PRODUCT)
						.withTarget(CATEGORY)
						.withIntegrationObjectItem(itemModelBuilder().build())
						.build()
		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorGeneratesSourceAndTargetRoles(PRODUCT, CATEGORY)

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
		final IntegrationObjectItemModel returnItem = itemModelBuilder()
				.withCode(CATEGORY)
				.withKeyAttribute(simpleKeyAttributeBuilder("simpleKey"))
				.build()

		final IntegrationObjectItemModel itemA = itemModelBuilder()
				.withCode(PRODUCT)
				.withKeyAttribute(collectionAttributeBuilder()
						.withName(ATTRIBUTE_NAME)
						.withSource(PRODUCT)
						.withTarget(CATEGORY)
						.withReturnIntegrationObjectItem(returnItem))
				.build()

		final IntegrationObjectItemAttributeModel attribute = itemA.getKeyAttributes()[0]

		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorGeneratesSourceAndTargetRoles(PRODUCT, KEYWORD)

		when:
		generator.generate(attribute)

		then:
		UniqueCollectionNotAllowedException e = thrown(UniqueCollectionNotAllowedException)
		e.getPropertyName() == ATTRIBUTE_NAME
		e.getEntityType() == PRODUCT
	}

	def simpleKeyAttributeBuilder(final String name) {
		simpleAttributeBuilder()
				.withName(name)
				.withUnique(true)
				.withLocalized(false)
	}

	@Test
	def "generate for map"() {
		given:
		final IntegrationObjectItemAttributeModel attribute =
				mapAttributeBuilder()
						.withName(ATTRIBUTE_NAME)
						.withSource(PRODUCT)
						.withTarget(KEYWORD)
						.withIntegrationObjectItem(itemModelBuilder().build())
						.build()
		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorGeneratesSourceAndTargetRoles(PRODUCT, KEYWORD)

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
		final IntegrationObjectItemModel returnItem = itemModelBuilder()
				.withCode(KEYWORD)
				.withKeyAttribute(simpleKeyAttributeBuilder("simpleKey"))
				.build()

		final IntegrationObjectItemModel itemA = itemModelBuilder()
				.withCode(PRODUCT)
				.withKeyAttribute(mapAttributeBuilder()
						.withName(ATTRIBUTE_NAME)
						.withSource(PRODUCT)
						.withTarget(KEYWORD)
						.withReturnIntegrationObjectItem(returnItem))
				.build()

		final IntegrationObjectItemAttributeModel attribute = itemA.getKeyAttributes()[0]


		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorGeneratesSourceAndTargetRoles(PRODUCT, KEYWORD)

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
		final IntegrationObjectItemAttributeModel attribute =
				oneToOneRelationAttributeBuilder()
						.withName(ATTRIBUTE_NAME)
						.withAutoCreate(true)
						.withIntegrationObjectItem(itemModelBuilder().withCode(PRODUCT).withItemModel(nonAbstractComposedTypeModel(PRODUCT)).build())
						.withReturnIntegrationObjectItem(itemModelBuilder().withCode(UNIT).withItemModel(abstractComposedTypeModel(UNIT)).build())
						.build()
		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorGeneratesSourceAndTargetRoles(PRODUCT, UNIT)

		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorGeneratesSourceAndTargetRoles(PRODUCT, KEYWORD)

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
		final IntegrationObjectItemAttributeModel attribute =
				oneToOneRelationAttributeBuilder()
						.withAutoCreate(true)
						.withName(ATTRIBUTE_NAME)
						.withIntegrationObjectItem(itemModelBuilder().withCode(PRODUCT).withItemModel(nonAbstractComposedTypeModel(PRODUCT)).build())
						.withReturnIntegrationObjectItem(itemModelBuilder().withCode(UNIT).withItemModel(nonAbstractComposedTypeModel(UNIT)).build())
						.build()

		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorGeneratesSourceAndTargetRoles(PRODUCT, KEYWORD)

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
	def "generate for partof non abstract type"() {
		given: "autocreate = true && abstract = false"
		final IntegrationObjectItemAttributeModel attribute =
				oneToOneRelationAttributeBuilder()
						.withName(ATTRIBUTE_NAME)
						.withPartOf(true)
						.withIntegrationObjectItem(itemModelBuilder().withCode(PRODUCT).withItemModel(nonAbstractComposedTypeModel(PRODUCT)).build())
						.withReturnIntegrationObjectItem(itemModelBuilder().withCode(UNIT).withItemModel(nonAbstractComposedTypeModel(UNIT)).build())
						.build()

		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorGeneratesSourceAndTargetRoles(PRODUCT, KEYWORD)

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
	def "generate for partof abstract type"() {
		given: "autocreate = true && abstract = true"
		final IntegrationObjectItemAttributeModel attribute =
				oneToOneRelationAttributeBuilder()
						.withName(ATTRIBUTE_NAME)
						.withPartOf(true)
						.withIntegrationObjectItem(itemModelBuilder().withCode(PRODUCT).withItemModel(nonAbstractComposedTypeModel(PRODUCT)).build())
						.withReturnIntegrationObjectItem(itemModelBuilder().withCode(UNIT).withItemModel(abstractComposedTypeModel(UNIT)).build())
						.build()
		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorGeneratesSourceAndTargetRoles(PRODUCT, UNIT)

		and: "associationGenerator method calls are stubbed with source and target types"
		associationGeneratorGeneratesSourceAndTargetRoles(PRODUCT, KEYWORD)

		when:
		generator.generate(attribute)

		then:
		NestedAbstractItemTypeCannotBeCreatedException e = thrown(NestedAbstractItemTypeCannotBeCreatedException)
		e.getPropertyName() == ATTRIBUTE_NAME
		e.getEntityType() == PRODUCT
	}

	@Test
	def "generate for simple property"() {
		given:
		associationGeneratorRegistry.getAssociationGenerator(_) >> Optional.empty()

		when:
		final Optional optionalProperty = generator.generate(Stub(IntegrationObjectItemAttributeModel))

		then:
		optionalProperty.isEmpty()
	}

	@Test
	def "generate for null property"() {
		when:
		generator.generate(null)

		then:
		thrown(IllegalArgumentException)
	}

	private static ComposedTypeModel abstractComposedTypeModel(final String itemCode) {
		final ComposedTypeModel model = mock(ComposedTypeModel.class)
		doReturn(itemCode).when(model).getCode()
		doReturn(true).when(model).getAbstract()
		return model;
	}

	private static ComposedTypeModel nonAbstractComposedTypeModel(final String itemCode) {
		final ComposedTypeModel model = mock(ComposedTypeModel.class)
		doReturn(itemCode).when(model).getCode()
		doReturn(false).when(model).getAbstract()
		return model;
	}

	private void associationGeneratorGeneratesSourceAndTargetRoles(final String source, final String target) {
		associationGeneratorRegistry.getAssociationGenerator(_) >> Optional.of(associationGenerator)
		associationGenerator.getAssociationName(_) >> "${source}_${target}"
		associationGenerator.getSourceRole(_) >> source
		associationGenerator.getTargetRole(_) >> target
	}

	def validNavigationPropertyDescriptor() {
		attributeDescriptor(autocreate: false, key: false, collection: false, map: false)
	}

	def attributeDescriptor(Map<String, Boolean> params) {
		def descriptor = Stub(TypeAttributeDescriptor)
		descriptor.isAutoCreate() >> params['autocreate']
		descriptor.isKeyAttribute() >> params['key']
		descriptor.isCollection() >> params['collection']
		descriptor.isMap() >> params['map']
		descriptor
	}

	def typeDescriptor(Map<String, Boolean> params) {
		Stub(TypeDescriptor) {
			isAbstract() >> params['abstract']
		}
	}
}
