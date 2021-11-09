/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.admin.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.searchservices.admin.data.SnIndexType
import de.hybris.platform.searchservices.admin.service.SnIndexTypeService
import de.hybris.platform.searchservices.integration.admin.AbstractSnAdminSpec
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel
import de.hybris.platform.searchservices.model.SnIndexTypeModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.type.TypeService

import javax.annotation.Resource

import org.junit.Test


@IntegrationTest
public class SnIndexTypeServiceSpec extends AbstractSnAdminSpec {

	@Resource
	private TypeService typeService

	@Resource
	private ModelService modelService

	@Resource
	SnIndexTypeService snIndexTypeService

	private ComposedTypeModel itemComposedType

	def setup() {
		itemComposedType = typeService.getComposedTypeForClass(ProductModel.class)
	}

	@Test
	def "Get empty index types"() {
		when:
		List<SnIndexType> resultIndexTypes = snIndexTypeService.getAllIndexTypes()

		then:
		resultIndexTypes != null
		resultIndexTypes.size() == 0
	}

	@Test
	def "Get index types with single item"() {
		given:
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType)
		modelService.saveAll(indexType)

		when:
		List<SnIndexType> resultIndexTypes = snIndexTypeService.getAllIndexTypes()

		then:
		resultIndexTypes != null
		resultIndexTypes.size() == 1

		resultIndexTypes[0].id == INDEX_TYPE_1_ID
	}

	@Test
	def "Get index types with multiple items"() {
		given:
		SnIndexTypeModel indexType1 = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType)
		SnIndexTypeModel indexType2 = new SnIndexTypeModel(id: INDEX_TYPE_2_ID, itemComposedType: itemComposedType)
		modelService.saveAll(indexType1, indexType2)

		when:
		List<SnIndexType> resultIndexTypes = snIndexTypeService.getAllIndexTypes()

		then:
		resultIndexTypes != null
		resultIndexTypes.size() == 2

		resultIndexTypes[0].id == INDEX_TYPE_1_ID
		resultIndexTypes[1].id == INDEX_TYPE_2_ID
	}

	@Test
	def "Get empty index types for index configuration"() {
		when:
		List<SnIndexType> resultIndexTypes = snIndexTypeService.getIndexTypesForIndexConfiguration(INDEX_CONFIGURATION_ID)

		then:
		resultIndexTypes != null
		resultIndexTypes.size() == 0
	}

	@Test
	def "Get index types for index configuration with single item"() {
		given:
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		List<SnIndexType> resultIndexTypes = snIndexTypeService.getIndexTypesForIndexConfiguration(INDEX_CONFIGURATION_ID)

		then:
		resultIndexTypes != null
		resultIndexTypes.size() == 1

		resultIndexTypes[0].id == INDEX_TYPE_1_ID
	}

	@Test
	def "Get index types for index configuration with multiple items"() {
		given:
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType1 = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		SnIndexTypeModel indexType2 = new SnIndexTypeModel(id: INDEX_TYPE_2_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType1, indexType2)

		when:
		List<SnIndexType> resultIndexTypes = snIndexTypeService.getIndexTypesForIndexConfiguration(INDEX_CONFIGURATION_ID)

		then:
		resultIndexTypes != null
		resultIndexTypes.size() == 2

		resultIndexTypes[0].id == INDEX_TYPE_1_ID
		resultIndexTypes[1].id == INDEX_TYPE_2_ID
	}

	@Test
	def "Get index types for index configuration, filter by index configuration"() {
		given:
		SnIndexConfigurationModel indexConfiguration1 = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_1_ID)
		SnIndexConfigurationModel indexConfiguration2 = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_2_ID)
		SnIndexTypeModel indexType1 = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration1)
		SnIndexTypeModel indexType2 = new SnIndexTypeModel(id: INDEX_TYPE_2_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration2)
		modelService.saveAll(indexConfiguration1, indexConfiguration2, indexType1, indexType2)

		when:
		List<SnIndexType> resultIndexTypes = snIndexTypeService.getIndexTypesForIndexConfiguration(INDEX_CONFIGURATION_1_ID)

		then:
		resultIndexTypes != null
		resultIndexTypes.size() == 1

		resultIndexTypes[0].id == INDEX_TYPE_1_ID
	}

	@Test
	def "Get non existing index type for id"() {
		when:
		Optional<SnIndexType> resultIndexTypeOptional = snIndexTypeService.getIndexTypeForId(INDEX_TYPE_1_ID)

		then:
		resultIndexTypeOptional.isEmpty()
	}

	@Test
	def "Get index type for id"() {
		given:
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType)
		modelService.saveAll(indexType)

		when:
		Optional<SnIndexType> indexTypeOptional = snIndexTypeService.getIndexTypeForId(INDEX_TYPE_1_ID)

		then:
		indexTypeOptional.isPresent()

		indexTypeOptional.get().id == INDEX_TYPE_1_ID
	}
}
