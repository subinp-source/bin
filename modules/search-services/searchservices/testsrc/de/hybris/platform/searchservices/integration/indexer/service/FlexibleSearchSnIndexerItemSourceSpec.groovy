/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.indexer.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.PK
import de.hybris.platform.searchservices.enums.SnDocumentOperationType
import de.hybris.platform.searchservices.enums.SnIndexerOperationType
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext
import de.hybris.platform.searchservices.indexer.service.SnIndexerContextFactory
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation
import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerItemSourceOperation
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerRequest
import de.hybris.platform.searchservices.indexer.service.impl.FlexibleSearchSnIndexerItemSource
import de.hybris.platform.searchservices.integration.indexer.AbstractSnIndexerSpec

import javax.annotation.Resource

import org.junit.Test


@IntegrationTest
public class FlexibleSearchSnIndexerItemSourceSpec extends AbstractSnIndexerSpec {

	@Resource
	private SnIndexerContextFactory snIndexerContextFactory

	def loadData() {
		loadDefaultData()
	}

	@Test
	public void createItemSource() {
		given:
		final String query = "SELECT {pk} FROM {Product}"
		final Map<String, Object> queryParameters = new HashMap<>()

		when:
		final SnIndexerItemSource itemSource = new FlexibleSearchSnIndexerItemSource(query, queryParameters)
		final SnIndexerItemSourceOperation itemSourceOperation = new DefaultSnIndexerItemSourceOperation(SnDocumentOperationType.CREATE, itemSource)
		List<SnIndexerItemSourceOperation> itemSourceOperations = List.of(itemSourceOperation)
		final SnIndexerRequest indexerRequest = new DefaultSnIndexerRequest(INDEX_TYPE_ID, SnIndexerOperationType.FULL, itemSourceOperations)
		final SnIndexerContext indexerContext = snIndexerContextFactory.createIndexerContext(indexerRequest)
		final List<PK> pks = itemSource.getPks(indexerContext)

		then:
		pks != null
		pks.size() == 6
	}
}
