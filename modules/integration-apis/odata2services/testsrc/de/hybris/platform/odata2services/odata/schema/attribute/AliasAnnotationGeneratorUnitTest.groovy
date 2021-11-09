/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.odata2services.odata.schema.attribute

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyMetadataGenerator
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.integrationservices.model.MockIntegrationObjectItemModelBuilder.itemModelBuilder

@UnitTest
class AliasAnnotationGeneratorUnitTest extends Specification {

	def aliasGenerator = new AliasAnnotationGenerator()

	def integrationKeyMetadataGenerator = Mock(IntegrationKeyMetadataGenerator)
	@Shared
	def item = itemModelBuilder().build()


	def setup() {
		aliasGenerator.setIntegrationKeyMetadataGenerator(integrationKeyMetadataGenerator)
	}

	@Test
	@Unroll
	def "generate null"() {
		given:
		integrationKeyMetadataGenerator.generateKeyMetadata(itemModel) >> keyMetadata

		expect:
		aliasGenerator.generate(itemModel) == null

		where:
		itemModel | keyMetadata
		null      | null
		item      | ""
	}

	@Test
	def "generate when item has key properties"()
	{
		given:
		integrationKeyMetadataGenerator.generateKeyMetadata(item) >> "SomeType_uniqueAttribute"

		when:
		def generatedAlias = aliasGenerator.generate(item)

		then:
		generatedAlias.name == "s:Alias"
		generatedAlias.text == "SomeType_uniqueAttribute"
	}
}