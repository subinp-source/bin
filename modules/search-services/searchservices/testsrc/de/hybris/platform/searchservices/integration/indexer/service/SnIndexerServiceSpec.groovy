/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.indexer.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.enums.SnDocumentOperationType
import de.hybris.platform.searchservices.indexer.SnIndexerException
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation
import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest
import de.hybris.platform.searchservices.indexer.service.SnIndexerResponse
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerItemSourceOperation
import de.hybris.platform.searchservices.indexer.service.impl.FlexibleSearchSnIndexerItemSource
import de.hybris.platform.searchservices.integration.indexer.AbstractSnIndexerSpec

import java.nio.charset.StandardCharsets

import org.junit.Test


@IntegrationTest
public class SnIndexerServiceSpec extends AbstractSnIndexerSpec {

	private static final String FULL_QUERY = "SELECT {p:pk} FROM {Product AS p}"
	private static final String INCREMENTAL_QUERY = "SELECT {p:pk} FROM {Product AS p} WHERE {p.modifiedtime} > ?startTime"

	private static final String START_TIME_PARAM = "startTime"

	private static final long PRODUCTS_COUNT = 6
	private static final long PRODUCTS_CREATED_COUNT = 2
	private static final long PRODUCTS_UPDATED_COUNT = 2

	def loadData() {
		loadDefaultData()
	}

	@Test
	public void runFullOperation() {
		when:
		final SnIndexerItemSource itemSource = new FlexibleSearchSnIndexerItemSource(FULL_QUERY, Collections.emptyMap())
		final SnIndexerRequest indexerRequest = snIndexerService.createFullIndexerRequest(INDEX_TYPE_ID, itemSource)
		final SnIndexerResponse indexerResponse = snIndexerService.index(indexerRequest)

		then:
		with (indexerResponse) {
			getTotalItems() == PRODUCTS_COUNT
			getProcessedItems() == PRODUCTS_COUNT
		}
	}

	@Test
	public void failToRunIncrementalOperationBeforeFullOperation() {
		when:
		final SnIndexerItemSource itemSource = new FlexibleSearchSnIndexerItemSource(INCREMENTAL_QUERY, Collections.emptyMap())
		final SnIndexerItemSourceOperation itemSourceOperation = new DefaultSnIndexerItemSourceOperation(SnDocumentOperationType.CREATE_UPDATE, itemSource)
		List<SnIndexerItemSourceOperation> itemSourceOperations = List.of(itemSourceOperation)
		final SnIndexerRequest indexerRequest = snIndexerService.createIncrementalIndexerRequest(INDEX_TYPE_ID, itemSourceOperations)
		snIndexerService.index(indexerRequest)

		then:
		thrown(SnIndexerException)
	}

	@Test
	public void runIncrementalOperation() {
		when:
		final SnIndexerItemSource fullItemSource = new FlexibleSearchSnIndexerItemSource(FULL_QUERY, Collections.emptyMap())
		final SnIndexerRequest fullIndexerRequest = snIndexerService.createFullIndexerRequest(INDEX_TYPE_ID, fullItemSource)
		snIndexerService.index(fullIndexerRequest)

		final Date startDate = new Date()
		importData("/searchservices/test/integration/snCatalogAddProduct1.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snCatalogUpdateProduct1.impex", StandardCharsets.UTF_8.name())

		final SnIndexerItemSource incrementalItemSource = new FlexibleSearchSnIndexerItemSource(INCREMENTAL_QUERY,
			Collections.singletonMap(START_TIME_PARAM, startDate))
		final SnIndexerItemSourceOperation incrementalItemSourceOperation = new DefaultSnIndexerItemSourceOperation(SnDocumentOperationType.CREATE_UPDATE, incrementalItemSource)
		List<SnIndexerItemSourceOperation> incrementalItemSourceOperations = List.of(incrementalItemSourceOperation)
		final SnIndexerRequest incrementalIndexerRequest = snIndexerService.createIncrementalIndexerRequest(INDEX_TYPE_ID, incrementalItemSourceOperations)
		final SnIndexerResponse incrementalIndexerResponse = snIndexerService.index(incrementalIndexerRequest)

		then:
		with (incrementalIndexerResponse) {
			getTotalItems() == PRODUCTS_CREATED_COUNT + PRODUCTS_UPDATED_COUNT
			getProcessedItems() == PRODUCTS_CREATED_COUNT + PRODUCTS_UPDATED_COUNT
		}
	}
}
