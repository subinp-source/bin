/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.index.service.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration
import de.hybris.platform.searchservices.admin.data.SnIndexType
import de.hybris.platform.searchservices.admin.service.SnIndexConfigurationService
import de.hybris.platform.searchservices.admin.service.SnIndexTypeService
import de.hybris.platform.searchservices.core.service.SnQualifier
import de.hybris.platform.searchservices.core.service.SnQualifierProvider
import de.hybris.platform.searchservices.core.service.SnQualifierType
import de.hybris.platform.searchservices.core.service.SnQualifierTypeFactory
import de.hybris.platform.searchservices.index.service.SnIndexContext
import de.hybris.platform.searchservices.index.service.impl.DefaultSnIndexContextFactory

import org.junit.Test

import spock.lang.Specification


@UnitTest
public class DefaultSnIndexContextFactorySpec extends Specification {

	static final String INDEX_TYPE_ID = "indexType"
	static final String INDEX_CONFIGURATION_ID = "indexConfiguration"
	static final String QUALIFIER_TYPE_ID = "qualifierType"
	static final String INDEX_ID = "index"

	SnIndexConfigurationService snIndexConfigurationService = Mock()
	SnIndexTypeService snIndexTypeService = Mock()
	SnQualifierTypeFactory snQualifierTypeFactory = Mock()

	DefaultSnIndexContextFactory snIndexContextFactory

	def setup() {
		snIndexContextFactory = new DefaultSnIndexContextFactory()
		snIndexContextFactory.setSnIndexConfigurationService(snIndexConfigurationService)
		snIndexContextFactory.setSnIndexTypeService(snIndexTypeService)
		snIndexContextFactory.setSnQualifierTypeFactory(snQualifierTypeFactory)
	}

	@Test
	def "Fail to create context without index type id"() {
		when:
		snIndexContextFactory.createIndexContext(null)

		then:
		thrown(NullPointerException)
	}

	@Test
	def "Create context with index type"() {
		given:
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_ID)

		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_ID) >> Optional.of(indexType)

		when:
		SnIndexContext context = snIndexContextFactory.createIndexContext(INDEX_TYPE_ID)

		then:
		context != null
		context.getIndexType() == indexType
		context.getIndexConfiguration() == null
		context.getQualifiers() == [:]
		context.getIndexId() == INDEX_TYPE_ID
	}

	@Test
	def "Create context with index type and index configuration"() {
		given:
		SnIndexConfiguration indexConfiguration = new SnIndexConfiguration(id: INDEX_CONFIGURATION_ID)
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_ID, indexConfigurationId: INDEX_CONFIGURATION_ID)

		snIndexConfigurationService.getIndexConfigurationForId(INDEX_CONFIGURATION_ID) >> Optional.of(indexConfiguration)
		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_ID) >> Optional.of(indexType)

		when:
		SnIndexContext context = snIndexContextFactory.createIndexContext(INDEX_TYPE_ID)

		then:
		context != null
		context.getIndexType() == indexType
		context.getIndexConfiguration() == indexConfiguration
		context.getQualifiers() == [:]
		context.getIndexId() == INDEX_TYPE_ID
	}

	@Test
	def "Create context with qualifiers"() {
		given:
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_ID)

		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_ID) >> Optional.of(indexType)

		SnQualifierType qualifierType = Mock()
		SnQualifierProvider qualifierProvider = Mock()
		SnQualifier qualifier = Mock()

		snQualifierTypeFactory.getAllQualifierTypes() >> List.of(qualifierType)
		qualifierType.getId() >> QUALIFIER_TYPE_ID
		qualifierType.getQualifierProvider() >> qualifierProvider
		qualifierProvider.getCurrentQualifiers(_) >> List.of(qualifier)

		when:
		SnIndexContext context = snIndexContextFactory.createIndexContext(INDEX_TYPE_ID)

		then:
		context != null
		context.getIndexType() == indexType
		context.getIndexConfiguration() == null
		context.getQualifiers() == [
			(QUALIFIER_TYPE_ID): List.of(qualifier)
		]
		context.getIndexId() == INDEX_TYPE_ID
	}

	@Test
	def "Can update context index id"() {
		given:
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_ID)

		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_ID) >> Optional.of(indexType)

		SnIndexContext context = snIndexContextFactory.createIndexContext(INDEX_TYPE_ID)

		when:
		context.setIndexId(INDEX_ID)

		then:
		context != null
		context.getIndexType() == indexType
		context.getIndexConfiguration() == null
		context.getQualifiers() == [:]
		context.getIndexId() == INDEX_ID
	}
}
