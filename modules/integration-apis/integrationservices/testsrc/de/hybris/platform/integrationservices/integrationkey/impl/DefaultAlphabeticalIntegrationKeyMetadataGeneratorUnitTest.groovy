/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.integrationservices.integrationkey.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import org.junit.Test
import spock.lang.Specification

import static de.hybris.platform.integrationservices.model.BaseMockItemAttributeModelBuilder.oneToOneRelationAttributeBuilder
import static de.hybris.platform.integrationservices.model.BaseMockItemAttributeModelBuilder.simpleAttributeBuilder
import static de.hybris.platform.integrationservices.model.MockIntegrationObjectItemModelBuilder.itemModelBuilder

@UnitTest
class DefaultAlphabeticalIntegrationKeyMetadataGeneratorUnitTest extends Specification {

	def integrationKeyGenerator = new DefaultAlphabeticalIntegrationKeyMetadataGenerator()

	@Test
	def "generate null throws IllegalArgumentException"() {
		when:
		integrationKeyGenerator.generateKeyMetadata(null)

		then:
		thrown(IllegalArgumentException)
	}

	@Test
	def "non unique attributes"() {
		given:
		final IntegrationObjectItemModel item = itemModelBuilder()
				.withCode("ItemWithNoUniqueProperties")
				.withAttribute(simpleKeyAttributeBuilder("name"))
				.build()

		expect:
		integrationKeyGenerator.generateKeyMetadata(item) == ""
	}

	@Test
	def "simple key attribute"() {
		given:
		final IntegrationObjectItemModel item = itemModelBuilder()
				.withCode("MyItem")
				.withIntegrationObject("IO")
				.withKeyAttribute(simpleKeyAttributeBuilder("code"))
				.build()

		expect:
		integrationKeyGenerator.generateKeyMetadata(item) == "MyItem_code"
	}

	@Test
	def "two key attributes for same type"() {
		given:
		final IntegrationObjectItemModel item = itemModelBuilder()
				.withCode("Type")
				.withIntegrationObject("IO")
				.withKeyAttribute(simpleKeyAttributeBuilder("attr1"))
				.withKeyAttribute(simpleKeyAttributeBuilder("attr2"))
				.build()

		expect:
		integrationKeyGenerator.generateKeyMetadata(item) == "Type_attr1|Type_attr2"
	}

	@Test
	def "three key attributes for different types"() {
		given:
		final String itemObjectCode = "ItemObjectCode"
		final String itemACode = "ItemACode"
		final String itemBCode = "ItemBCode"
		final String itemCCode = "ItemCCode"

		final IntegrationObjectItemModel itemB = itemModelBuilder()
				.withCode(itemBCode)
				.withIntegrationObject(itemObjectCode)
				.withKeyAttribute(simpleKeyAttributeBuilder("attrB"))
				.build()

		final IntegrationObjectItemModel itemC = itemModelBuilder()
				.withCode(itemCCode)
				.withIntegrationObject(itemObjectCode)
				.withKeyAttribute(simpleKeyAttributeBuilder("attrC"))
				.build()

		final IntegrationObjectItemModel itemA = itemModelBuilder()
				.withCode(itemACode)
				.withIntegrationObject(itemObjectCode)
				.withKeyAttribute(simpleKeyAttributeBuilder("attrA"))
				.withKeyAttribute(oneToOneRelationAttributeBuilder().withName("attrB").withReturnIntegrationObjectItem(itemB).withUnique(true).withLocalized(false))
				.withKeyAttribute(oneToOneRelationAttributeBuilder().withName("attrC").withReturnIntegrationObjectItem(itemC).withUnique(true).withLocalized(false))
				.build()

		expect:
		integrationKeyGenerator.generateKeyMetadata(itemA) == "ItemACode_attrA|ItemBCode_attrB|ItemCCode_attrC"
	}

	@Test
	def "duplicate unique attributes appear only once"() {
		given:
		final String itemObjectCode = "ItemObjectCode"
		final String itemACode = "ItemACode"
		final String itemBCode = "ItemBCode"
		final String itemCCode = "ItemCCode"

		final IntegrationObjectItemModel itemA = itemModelBuilder()
				.withCode(itemACode)
				.withIntegrationObject(itemObjectCode)
				.withKeyAttribute(simpleKeyAttributeBuilder("attrA"))
				.build()

		final IntegrationObjectItemModel itemB = itemModelBuilder()
				.withCode(itemBCode)
				.withIntegrationObject(itemObjectCode)
				.withKeyAttribute(simpleKeyAttributeBuilder("attrB"))
				.withKeyAttribute(oneToOneRelationAttributeBuilder().withName("attrA").withReturnIntegrationObjectItem(itemA).withUnique(true).withLocalized(false))
				.build()

		final IntegrationObjectItemModel itemC = itemModelBuilder()
				.withCode(itemCCode)
				.withIntegrationObject(itemObjectCode)
				.withKeyAttribute(simpleKeyAttributeBuilder("attrC"))
				.withKeyAttribute(oneToOneRelationAttributeBuilder().withName("attrA").withReturnIntegrationObjectItem(itemA).withUnique(true).withLocalized(false))
				.withKeyAttribute(oneToOneRelationAttributeBuilder().withName("attrB").withReturnIntegrationObjectItem(itemB).withUnique(true).withLocalized(false))
				.build()

		expect:
		integrationKeyGenerator.generateKeyMetadata(itemC) == "ItemACode_attrA|ItemBCode_attrB|ItemCCode_attrC"
	}
	
	def simpleKeyAttributeBuilder(final String name) {
		simpleAttributeBuilder()
				.withName(name)
				.withUnique(true)
				.withLocalized(false)
	}
}
