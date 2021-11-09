/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.suggest.service.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration
import de.hybris.platform.searchservices.admin.data.SnIndexType
import de.hybris.platform.searchservices.admin.service.SnIndexConfigurationService
import de.hybris.platform.searchservices.admin.service.SnIndexTypeService
import de.hybris.platform.searchservices.core.service.SnQualifier
import de.hybris.platform.searchservices.core.service.SnQualifierProvider
import de.hybris.platform.searchservices.core.service.SnQualifierType
import de.hybris.platform.searchservices.core.service.SnQualifierTypeFactory
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery
import de.hybris.platform.searchservices.suggest.service.SnSuggestContext
import de.hybris.platform.searchservices.suggest.service.SnSuggestRequest
import de.hybris.platform.searchservices.suggest.service.SnSuggestResponse
import de.hybris.platform.searchservices.suggest.service.impl.DefaultSnSuggestContextFactory
import de.hybris.platform.searchservices.suggest.service.impl.DefaultSnSuggestRequest

import org.junit.Test

import spock.lang.Specification


@UnitTest
public class DefaultSnSuggestContextFactorySpec extends Specification {

	static final String INDEX_TYPE_ID = "indexType"
	static final String INDEX_CONFIGURATION_ID = "indexConfiguration"
	static final String QUALIFIER_TYPE_ID = "qualifierType"
	static final String INDEX_ID = "index"

	SnIndexConfigurationService snIndexConfigurationService = Mock()
	SnIndexTypeService snIndexTypeService = Mock()
	SnQualifierTypeFactory snQualifierTypeFactory = Mock()

	DefaultSnSuggestContextFactory snSuggestContextFactory

	def setup() {
		snSuggestContextFactory = new DefaultSnSuggestContextFactory()
		snSuggestContextFactory.setSnIndexConfigurationService(snIndexConfigurationService)
		snSuggestContextFactory.setSnIndexTypeService(snIndexTypeService)
		snSuggestContextFactory.setSnQualifierTypeFactory(snQualifierTypeFactory)
	}

	@Test
	def "Fail to create context without request"() {
		when:
		snSuggestContextFactory.createSuggestContext(null)

		then:
		thrown(NullPointerException)
	}

	@Test
	def "Create context with index type"() {
		given:
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_ID)

		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_ID) >> Optional.of(indexType)

		final SnSuggestQuery suggestQuery = new SnSuggestQuery()
		final SnSuggestRequest suggestRequest = new DefaultSnSuggestRequest(INDEX_TYPE_ID, suggestQuery)

		when:
		SnSuggestContext context = snSuggestContextFactory.createSuggestContext(suggestRequest)

		then:
		context != null
		context.getIndexType() == indexType
		context.getIndexConfiguration() == null
		context.getQualifiers() == [:]
		context.getIndexId() == INDEX_TYPE_ID
		context.getSuggestRequest() == suggestRequest
		context.getSuggestResponse() == null
	}

	@Test
	def "Create context with index type and index configuration"() {
		given:
		SnIndexConfiguration indexConfiguration = new SnIndexConfiguration(id: INDEX_CONFIGURATION_ID)
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_ID, indexConfigurationId: INDEX_CONFIGURATION_ID)

		snIndexConfigurationService.getIndexConfigurationForId(INDEX_CONFIGURATION_ID) >> Optional.of(indexConfiguration)
		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_ID) >> Optional.of(indexType)

		final SnSuggestQuery suggestQuery = new SnSuggestQuery()
		final SnSuggestRequest suggestRequest = new DefaultSnSuggestRequest(INDEX_TYPE_ID, suggestQuery)

		when:
		SnSuggestContext context = snSuggestContextFactory.createSuggestContext(suggestRequest)

		then:
		context != null
		context.getIndexType() == indexType
		context.getIndexConfiguration() == indexConfiguration
		context.getQualifiers() == [:]
		context.getIndexId() == INDEX_TYPE_ID
		context.getSuggestRequest() == suggestRequest
		context.getSuggestResponse() == null
	}

	@Test
	def "Create context with qualifiers"() {
		given:
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_ID)

		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_ID) >> Optional.of(indexType)

		final SnSuggestQuery suggestQuery = new SnSuggestQuery()
		final SnSuggestRequest suggestRequest = new DefaultSnSuggestRequest(INDEX_TYPE_ID, suggestQuery)

		SnQualifierType qualifierType = Mock()
		SnQualifierProvider qualifierProvider = Mock()
		SnQualifier qualifier = Mock()

		snQualifierTypeFactory.getAllQualifierTypes() >> List.of(qualifierType)
		qualifierType.getId() >> QUALIFIER_TYPE_ID
		qualifierType.getQualifierProvider() >> qualifierProvider
		qualifierProvider.getCurrentQualifiers(_) >> List.of(qualifier)

		when:
		SnSuggestContext context = snSuggestContextFactory.createSuggestContext(suggestRequest)

		then:
		context != null
		context.getIndexType() == indexType
		context.getIndexConfiguration() == null
		context.getQualifiers() == [
			(QUALIFIER_TYPE_ID): List.of(qualifier)
		]
		context.getIndexId() == INDEX_TYPE_ID
		context.getSuggestRequest() == suggestRequest
		context.getSuggestResponse() == null
	}

	@Test
	def "Can update context index id"() {
		given:
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_ID)

		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_ID) >> Optional.of(indexType)

		final SnSuggestQuery suggestQuery = new SnSuggestQuery()
		final SnSuggestRequest suggestRequest = new DefaultSnSuggestRequest(INDEX_TYPE_ID, suggestQuery)

		SnSuggestContext context = snSuggestContextFactory.createSuggestContext(suggestRequest)

		when:
		context.setIndexId(INDEX_ID)

		then:
		context != null
		context.getIndexType() == indexType
		context.getIndexConfiguration() == null
		context.getQualifiers() == [:]
		context.getIndexId() == INDEX_ID
		context.getSuggestRequest() == suggestRequest
		context.getSuggestResponse() == null
	}

	@Test
	def "Can update context suggest response"() {
		given:
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_ID)

		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_ID) >> Optional.of(indexType)

		final SnSuggestQuery suggestQuery = new SnSuggestQuery()
		final SnSuggestRequest suggestRequest = new DefaultSnSuggestRequest(INDEX_TYPE_ID, suggestQuery)
		SnSuggestResponse suggestResponse = Mock()

		SnSuggestContext context = snSuggestContextFactory.createSuggestContext(suggestRequest)

		when:
		context.setSuggestResponse(suggestResponse)

		then:
		context != null
		context.getIndexType() == indexType
		context.getIndexConfiguration() == null
		context.getQualifiers() == [:]
		context.getIndexId() == INDEX_TYPE_ID
		context.getSuggestRequest() == suggestRequest
		context.getSuggestResponse() == suggestResponse
	}
}
