/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.admin.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration
import de.hybris.platform.searchservices.admin.service.SnIndexConfigurationService
import de.hybris.platform.searchservices.integration.admin.AbstractSnAdminSpec
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel
import de.hybris.platform.servicelayer.model.ModelService

import javax.annotation.Resource

import org.junit.Test


@IntegrationTest
public class SnIndexConfigurationServiceSpec extends AbstractSnAdminSpec {

	@Resource
	private ModelService modelService

	@Resource
	SnIndexConfigurationService snIndexConfigurationService

	@Test
	def "Get empty index configurations"() {
		when:
		List<SnIndexConfiguration> resultIndexConfigurations = snIndexConfigurationService.getAllIndexConfigurations()

		then:
		resultIndexConfigurations != null
		resultIndexConfigurations.size() == 0
	}

	@Test
	def "Get index configurations with single item"() {
		given:
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_1_ID)
		modelService.saveAll(indexConfiguration)

		when:
		List<SnIndexConfiguration> resultIndexConfigurations = snIndexConfigurationService.getAllIndexConfigurations()

		then:
		resultIndexConfigurations != null
		resultIndexConfigurations.size() == 1

		resultIndexConfigurations[0].id == INDEX_CONFIGURATION_1_ID
	}

	@Test
	def "Get index configurations with multiple items"() {
		given:
		SnIndexConfigurationModel indexConfiguration1 = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_1_ID)
		SnIndexConfigurationModel indexConfiguration2 = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_2_ID)
		modelService.saveAll(indexConfiguration1, indexConfiguration2)

		when:
		List<SnIndexConfiguration> resultIndexConfigurations = snIndexConfigurationService.getAllIndexConfigurations()

		then:
		resultIndexConfigurations != null
		resultIndexConfigurations.size() == 2

		resultIndexConfigurations[0].id == INDEX_CONFIGURATION_1_ID
		resultIndexConfigurations[1].id == INDEX_CONFIGURATION_2_ID
	}

	@Test
	def "Get non existing index configuration for id"() {
		when:
		Optional<SnIndexConfiguration> resultIndexConfigurationOptional = snIndexConfigurationService.getIndexConfigurationForId(INDEX_CONFIGURATION_1_ID)

		then:
		resultIndexConfigurationOptional.isEmpty()
	}

	@Test
	def "Get index configuration for id"() {
		given:
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_1_ID)
		modelService.saveAll(indexConfiguration)

		when:
		Optional<SnIndexConfiguration> resultIndexConfigurationOptional = snIndexConfigurationService.getIndexConfigurationForId(INDEX_CONFIGURATION_1_ID)

		then:
		resultIndexConfigurationOptional.isPresent()

		resultIndexConfigurationOptional.get().id == INDEX_CONFIGURATION_1_ID
	}
}
